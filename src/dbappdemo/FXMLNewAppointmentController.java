package dbappdemo;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class FXMLNewAppointmentController implements Initializable {
    
    private int custIdint;
    private int apptIdint = 0;
    
    @FXML TextField title, custname, custid, descr, contact, starttime, endtime;
    @FXML DatePicker startdate, enddate;
    @FXML ChoiceBox<String> AMPMstart, AMPMend, location;
    @FXML Button findButton, saveButton, cancelButton, deleteButton, monthSum;
    
    //It's my intent to use friendly names for IDs
    //Daylight Savings is handled that way
    Instant n = Instant.now();
    final String AZt = "US/Arizona";
    final String NYt = "America/New_York";
    final String UKt = "Europe/London";
    final ZoneId inAZ = TimeZone.getTimeZone(AZt).toZoneId();
    final ZoneId inNY = TimeZone.getTimeZone(NYt).toZoneId();
    final ZoneId inUK = TimeZone.getTimeZone(UKt).toZoneId();
    //Instant i should be the start ODT or something. Offset changes over time.
    ZoneOffset inAZo(Instant i) { return inAZ.getRules().getOffset(i); }
    ZoneOffset inNYo(Instant i) { return inNY.getRules().getOffset(i); }
    ZoneOffset inUKo(Instant i) { return inUK.getRules().getOffset(i); }
    ZoneOffset locationBasedOffset;
    ZoneId locationBasedId;
            
    boolean isModify = false;
    OffsetDateTime startodt;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        AMPMstart.getItems().add("AM");
        AMPMstart.getItems().add("PM");
        AMPMend  .getItems().add("AM");
        AMPMend  .getItems().add("PM");
        location.getItems().add("Phoenix AZ");
        location.getItems().add("New York NY");
        location.getItems().add("London UK");
        location.getSelectionModel().selectFirst();
        startodt = OffsetDateTime.now();
        deleteButton.disableProperty().set(true);
    }
    DateTimeFormatter timestandard = DateTimeFormatter.ofPattern("h:mm a");
    
    public void take_existing_appointment(String name, OffsetDateTime X) {
        isModify = true;
        //enable and show delete button
        deleteButton.disableProperty().set(false);
        //
        String getAppointmentQuery =
        "SELECT appointment.appointmentId, appointment.customerId, appointment.title, \n"
        + "appointment.description, appointment.location, appointment.contact, appointment.end,\n" 
        + "customer.customerName FROM appointment INNER JOIN customer \n" 
        + "ON customer.customerId = appointment.customerId WHERE customer.customerName = ? \n" 
        + "AND appointment.start = ? ORDER BY start ASC;";
        try {
            //PreparedStatement because timestamps weren't working consistently here with original Statement implementation
            PreparedStatement getApptPS = dbuser.getConnection().prepareStatement(getAppointmentQuery);
            Timestamp s = time.UTCTS(X);
            getApptPS.setString(1, name);
            getApptPS.setTimestamp(2, s);
            ResultSet gotAppt = getApptPS.executeQuery();
            if(!gotAppt.first()) {
                utils.spawn("error fetching appointment. " + 565, x -> utils.errorbox(x));
                isModify = false; 
                deleteButton.disableProperty().set(true);
            }
            else {
                //location
                location.getSelectionModel().select(gotAppt.getString(5));
                refreshTimeZoneInfo();
                apptIdint = gotAppt.getInt(1);
                //customerId
                custIdint = gotAppt.getInt(2);
                custid.setText("ID: " + gotAppt.getInt(2));
                //title
                title.setText(gotAppt.getString(3));
                //description
                descr.setText(gotAppt.getString(4));
                //contact
                contact.setText(gotAppt.getString(6));
                //start
                starttime.setText(X.format(timestandard).split(" ")[0]);
                startdate.setValue(X.toLocalDate());
                AMPMstart.getSelectionModel().select(X.withOffsetSameInstant(locationBasedOffset).getHour() >= 12 ? 1 : 0);
                //end
                OffsetDateTime edODT = time.toODT(gotAppt.getTimestamp(7));
                endtime.setText(edODT.format(timestandard).split(" ")[0]);
                enddate.setValue(edODT.toLocalDate());
                AMPMend.getSelectionModel().select(edODT.withOffsetSameInstant(locationBasedOffset).getHour() >= 12 ? 1 : 0);
                //customername
                custname.setText(gotAppt.getString(8));
        }   }
        catch(SQLException e) { e.printStackTrace(System.out); }
    }
    
    @FXML void handleFind() throws SQLException {
        String custQuery= "SELECT customerName, customerId"
                        + " FROM customer "
                        + "WHERE customerName = '" + custname.getText() + "'";
        ResultSet custSet = utils.SQLRUN(custQuery, x -> utils.query(x));
        if(!custSet.first()) {
            custid.setText("not found");
            custIdint = 0;
        }
        else {
            custIdint = custSet.getInt(2);
            custid.setText("ID: " + custIdint);
    }   }
    
    @FXML void handleSave() throws SQLException {
        refreshTimeZoneInfo();
        //validate all filled
        if((title.getText().isEmpty()
            || custIdint == 0 
            || descr.getText().isEmpty()
            || contact.getText().isEmpty()
            || startdate.getValue() == null
            || enddate.getValue() == null
            || starttime.getText().isEmpty()
            || endtime.getText().isEmpty()
            || AMPMstart.getValue() == null
            || AMPMend.getValue() == null))
        {   utils.spawn("All appointment data must be filled in.", x -> utils.errorbox(x));
            return;
        }
        switch(validTimes()) {
            case 1: //out of hours
                utils.spawn("The appointment must be scheduled for business hours 9AM to 5PM", x -> utils.errorbox(x));
                return;
            case 2: //start is at or after end
                utils.spawn("The appointment must have a duration greater than zero.", x -> utils.errorbox(x));
                return;
            case 3:
                utils.spawn("The appointment must be scheduled for a weekday.", x -> utils.errorbox(x));
                return;
        }
        Timestamp stTS = time.UTCTS(toApptODT(true));
        Timestamp enTS = time.UTCTS(toApptODT(false));
        //look for appointments that either involve the customer or the 
        //consultant at the new times entered for the appointment.
        String validateConflictQuery = "SELECT title FROM appointment WHERE \n"
                                     + "(((start <= '" + stTS + "') AND (end > '" + stTS + "')) \n" 
                                     + "OR ((start > '" + stTS + "') AND (start < '" + enTS + "'))) \n" 
                                     + "AND location = '" + location.getSelectionModel().getSelectedItem() 
                                     + "' AND appointmentId != " + apptIdint + " AND (createdBy = '" 
                                     + dbuser.getUser() + "' OR customerId = " + custIdint + ");";
        ResultSet conflictSet = utils.SQLRUN(validateConflictQuery, x -> utils.query(x));
        if(conflictSet.first())
            utils.spawn("this appointment conflicts with another at that time or place: "
                    + conflictSet.getString(1), x -> utils.errorbox(x));
        //save appointment
        else {
            if(isModify) { 
                DBAppointment.handleModifyAppointment(
                        apptIdint, 
                        title.getText(), 
                        descr.getText(), 
                        location.getSelectionModel().getSelectedItem(), 
                        contact.getText(), 
                        stTS, 
                        enTS);
            }
            else {
                ResultSet maxIDResultSet = utils.SQLRUN(
                        "SELECT MAX(appointmentId) FROM appointment", x -> utils.query(x));
                int newId = maxIDResultSet.first() ? maxIDResultSet.getInt(1) + 1 : 1;
                String addAppointmentQuery 
                    = "INSERT INTO appointment (appointmentId, customerId, title, description, "
                    + "location, contact, url, start, end, createDate, createdBy, lastUpdateBy) VALUES ( "
                    + newId + ", "
                    + custIdint + ", '"
                    + title.getText() + "', '"
                    + descr.getText() + "', '"
                    + location.getSelectionModel().getSelectedItem() + "', '"
                    + contact.getText() + "', '"
                    + "test url', '"
                    + stTS + "', '"
                    + enTS + "', "
                    + "NOW(), '"
                    + dbuser.getUser() + "', '"
                    + dbuser.getUser() + "');";
                utils.SQLRUN(addAppointmentQuery, x -> utils.update(x));
            }
            startodt = toApptODT(true);
            utils.spawn("SUCCESS: Set for " + startodt.format(timestandard) + "; in your time zone that's " 
                    + startodt.format(timestandard.withZone(ZoneId.systemDefault())), 
                    x -> utils.errorbox(x));
            utils.setAlarm();
            utils.closeCurrentWindow(saveButton);
    }   }
    
    @FXML void handleCancel() { utils.closeCurrentWindow(cancelButton); }
    
    @FXML void handleDelete()
    {   if(isModify && !deleteButton.disabledProperty().get())
        {   if(utils.spawn("really delete this appointment?", x -> utils.choicebox(x)))
            {   String t = title.getText();
                String dQ = "DELETE FROM appointment WHERE"
                + "\n title = '"         + t + "'"
                + "\n AND customerId = " + custIdint
                + "\n AND start = '"     
                + time.UTCTS(toApptODT(true)) + "'";
                utils.SQLRUN(dQ, x -> utils.update(x));
                utils.closeCurrentWindow(saveButton);
    }   }   }
    
    //KMA 09 07 2017 ~~~ true: get start time // else: get end time from fields.
    //important: must use appointment timezone, not user timezone.
    private OffsetDateTime toApptODT(boolean isStart) {
        refreshTimeZoneInfo();
        OffsetDateTime x;
        if(isStart)
        {   x = OffsetDateTime.of(startdate.getValue(), LocalTime.of(
                Integer.parseInt(starttime.getText().split(":")[0]), 
                Integer.parseInt(starttime.getText().split(":")[1])), locationBasedOffset);
            if(AMPMstart.getSelectionModel().getSelectedItem().equalsIgnoreCase("PM"))
                x = x.plusHours(12);
        }
        else
        {   x = OffsetDateTime.of(startdate.getValue(), LocalTime.of(
                Integer.parseInt(endtime.getText().split(":")[0]), 
                Integer.parseInt(endtime.getText().split(":")[1])), locationBasedOffset);
            if(AMPMend.getSelectionModel().getSelectedItem().equalsIgnoreCase("PM"))
                x = x.plusHours(12);
        }
        return x;
    }
    
    private int validTimes() {
        refreshTimeZoneInfo();
        //check for time outside business hours, or appointment attempted to be set with non-real duration
        OffsetDateTime startodt = toApptODT(true);
        OffsetDateTime endodt = toApptODT(false);
        if(startodt.getHour() < 9 || startodt.getHour() >= 17)
            return 1;
        if((endodt.getHour() < 9 || (endodt.getHour() == 9 && endodt.getMinute() == 0)) 
        || (endodt.getHour() > 17 || (endodt.getHour() == 17 && endodt.getMinute() != 0)))
            return 1;
        if(startodt.toEpochSecond() >= endodt.toEpochSecond())
            return 2;
        if(time.WKDAY(startodt) == 0 || time.WKDAY(startodt) == 6)
            return 3;
        if(time.WKDAY(endodt) == 0 ||   time.WKDAY(endodt) == 6)
            return 3;
            return 0;
    }
    private void refreshTimeZoneInfo() {
        String s = location.getSelectionModel().getSelectedItem();
        Instant ti = startodt.toInstant();
        if(s.equalsIgnoreCase("Phoenix AZ")) {
            locationBasedId = inAZ;
            locationBasedOffset = inAZo(ti);
        }
        else if(s.equalsIgnoreCase("New York NY")) {
            locationBasedId = inNY;
            locationBasedOffset = inNYo(ti);
        }
        else if(s.equalsIgnoreCase("London UK")) {
            locationBasedId = inUK;
            locationBasedOffset = inUKo(ti);
        }
        timestandard = timestandard.withZone(locationBasedId);
    }
}
