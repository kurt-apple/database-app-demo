package dbappdemo;

import java.net.URL;
import javafx.fxml.FXML;
import java.sql.ResultSet;
import javafx.fxml.Initializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.Label;
import java.util.LinkedList;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import java.util.ResourceBundle;
import javafx.scene.input.MouseButton;
import javafx.beans.value.ObservableValue;
import javafx.stage.Modality;

public class FXMLMainViewController implements Initializable {
    OffsetDateTime onscreen;
    OffsetDateTime currentt;

    public FXMLMainViewController() {
    }

    //Declaration of FXML and Data Structures
    @FXML Button  back1m,
                  next1m,
                  back1w,
                  next1w,
                  newAppointment,
                  customerInfoViewButton,
                  apptViewButton,
                  exitButton,
                  tips;

    @FXML Label   monthLabel,
                  weekLabel,
                  sunweekLabel,
                  monweekLabel,
                  tueweekLabel,
                  wedweekLabel,
                  thuweekLabel,
                  friweekLabel,
                  satweekLabel;
    @FXML ChoiceBox<String> userfilterchoicebox = new ChoiceBox<>();
    @FXML Tab     weeklyTab, monthlyTab;

    @FXML ListView<String>
    su0,  su1,  su2,  su3,  su4,  su5,
    mo0,  mo1,  mo2,  mo3,  mo4,  mo5,
    tu0,  tu1,  tu2,  tu3,  tu4,  tu5,
    we0,  we1,  we2,  we3,  we4,  we5,
    th0,  th1,  th2,  th3,  th4,  th5,
    fr0,  fr1,  fr2,  fr3,  fr4,  fr5,
    sa0,  sa1,  sa2,  sa3,  sa4,  sa5;

    @FXML Label
    sun0, sun1, sun2, sun3, sun4, sun5,
    mon0, mon1, mon2, mon3, mon4, mon5,
    tue0, tue1, tue2, tue3, tue4, tue5,
    wed0, wed1, wed2, wed3, wed4, wed5,
    thu0, thu1, thu2, thu3, thu4, thu5,
    fri0, fri1, fri2, fri3, fri4, fri5,
    sat0, sat1, sat2, sat3, sat4, sat5;

    LinkedList<ListView<String>> SundaysLV    = new LinkedList<>();
    LinkedList<ListView<String>> MondaysLV    = new LinkedList<>();
    LinkedList<ListView<String>> TuesdaysLV   = new LinkedList<>();
    LinkedList<ListView<String>> WednesdaysLV = new LinkedList<>();
    LinkedList<ListView<String>> ThursdaysLV  = new LinkedList<>();
    LinkedList<ListView<String>> FridaysLV    = new LinkedList<>();
    LinkedList<ListView<String>> SaturdaysLV  = new LinkedList<>();

    @FXML ListView<String> SundayLV    = new ListView<>();
    @FXML ListView<String> MondayLV    = new ListView<>();
    @FXML ListView<String> TuesdayLV   = new ListView<>();
    @FXML ListView<String> WednesdayLV = new ListView<>();
    @FXML ListView<String> ThursdayLV  = new ListView<>();
    @FXML ListView<String> FridayLV    = new ListView<>();
    @FXML ListView<String> SaturdayLV  = new ListView<>();

    LinkedList<Label> SundaysL         = new LinkedList<>();
    LinkedList<Label> MondaysL         = new LinkedList<>();
    LinkedList<Label> TuesdaysL        = new LinkedList<>();
    LinkedList<Label> WednesdaysL      = new LinkedList<>();
    LinkedList<Label> ThursdaysL       = new LinkedList<>();
    LinkedList<Label> FridaysL         = new LinkedList<>();
    LinkedList<Label> SaturdaysL       = new LinkedList<>();

    LinkedList<LinkedList<Label>> CalL = new LinkedList<>();
    LinkedList<ListView<String>>  WkLV = new LinkedList<>();
    LinkedList<LinkedList<ListView<String>>> CalLV = new LinkedList<>();
    LinkedList<Label> WeekL = new LinkedList<>();

    //

    private void formatWeekCalendar() {
        OffsetDateTime w = onscreen.minusDays(time.WKDAY(onscreen));
        String ws1 = w.getMonthValue() + "/" + w.getDayOfMonth();
        sunweekLabel.setText("Sunday: "    + w.getDayOfMonth());
        monweekLabel.setText("Monday: "    + w.plusDays(1).getDayOfMonth());
        tueweekLabel.setText("Tuesday: "   + w.plusDays(2).getDayOfMonth());
        wedweekLabel.setText("Wednesday: " + w.plusDays(3).getDayOfMonth());
        thuweekLabel.setText("Thursday: "  + w.plusDays(4).getDayOfMonth());
        friweekLabel.setText("Friday: "    + w.plusDays(5).getDayOfMonth());
        satweekLabel.setText("Saturday: "  + w.plusDays(6).getDayOfMonth());
        String ws2 = w.plusDays(6).getMonth().getValue() + "/" + w.plusDays(6).getDayOfMonth();
        weekLabel.setText("Week of " + ws1 + " to " + ws2);
        try { populate_weekly_calendar(); }
        catch(SQLException e)
        {   utils.spawn("error populating weekly calendar\n" + e.getMessage(), x -> utils.errorbox(x));
    }   }

    DateTimeFormatter MonthTitle = DateTimeFormatter.ofPattern("MMMM u");

    private void formatMonthCalendar() {
        //month
        int m = 1;
        int lom = time.lengthOfMonth(onscreen);
        int ony = onscreen.getYear();
        int cury = currentt.getYear();
        int onm = onscreen.getMonthValue();
        int curm = currentt.getMonthValue();
        String gray = " -fx-background-color: gray;";
        String orange = " -fx-background-color: orange;";
        String cadet = " -fx-background-color: cadetblue;";
        String ccc = " -fx-background-color: #CCCCCC;";
        monthLabel.setText(onscreen.format(MonthTitle));
        if(cury > ony || (cury == ony && curm > onm)) monthLabel.setStyle(gray);
        else if(curm == onm && cury == ony) monthLabel.setStyle(orange);
        else monthLabel.setStyle(cadet);
        //day, top row
        for(int a = 0; a < 7; a++) {
            CalL.get(a).get(0).setText(""+(a < time.WKDAY(onscreen) ? "" : m++));
            if(a < time.WKDAY(onscreen)) CalL.get(a).get(0).setStyle(gray);
            else if(cury > ony) CalL.get(a).get(0).setStyle(gray);
            else if(cury == ony && curm > onm) CalL.get(a).get(0).setStyle(gray);
            else CalL.get(a).get(0).setStyle(ccc);
        }
        //day, 2nd row and down
        for(int b = 1; b < 6; b++)
        {   for(int c = 0; c < 7; c++)
            {   CalL.get(c).get(b).setText(""+((m > lom) ? "" : m));
                //any prior years or months: no special highlighting, just gray
                //on-screen days after on-screen month, present or future, blue highlight no text
                //on-screen future years or months: light gray with blue after end of month.
                //today orange, else light gray
                if((cury > ony) || ((cury == ony)&&(curm > onm))) CalL.get(c).get(b).setStyle(gray);
                else if(m > lom) CalL.get(c).get(b).setStyle(cadet);
                else if(cury < ony) CalL.get(c).get(b).setStyle(ccc);
                else if(curm < onm) CalL.get(c).get(b).setStyle(ccc);
                else if(m == currentt.getDayOfMonth()) CalL.get(c).get(b).setStyle(orange);
                else CalL.get(c).get(b).setStyle(ccc);
                m++;
        }   }
        try { populate_monthly_calendar(); }
        catch(SQLException e) { utils.spawn("Error populating monthly calendar.", x -> utils.errorbox(x)); }
    }

    DateTimeFormatter timestandard = DateTimeFormatter.ofPattern("h:mm a");
    
    public void populate_weekly_calendar() throws SQLException {
        OffsetDateTime week_start = onscreen.minusDays(time.WKDAY(onscreen)).withHour(0).withMinute(0);
        OffsetDateTime week_end = week_start.plusDays(6).withHour(23).withMinute(59);
        System.out.println("getting week agenda for " + week_start.getDayOfMonth() 
                            + " to " + week_end.getDayOfMonth());
        String wAgQuery = "SELECT customer.customerName, appointment.start"
                        + " FROM appointment INNER JOIN customer"
                        + " ON customer.customerId = appointment.customerId "
                        + "WHERE (appointment.start BETWEEN '" + time.UTCTS(week_start) + "'"
                        + " AND '" + time.UTCTS(week_end) + "') "
                        + (pollUserFilter() ? "" : "AND appointment.createdBy = '" + getUserFilter() + "' ")
                        + "ORDER BY appointment.start ASC;";
        ResultSet wAgSet = utils.SQLRUN(wAgQuery, x -> utils.query(x));
        for (int i = 0; i < 7; i++) WkLV.get(i).getItems().clear();
        String s;
        if (wAgSet.first()) {
            do {
                OffsetDateTime a = time.toODT(wAgSet.getTimestamp(2));
                s = timestandard.format(a) + " " + wAgSet.getString(1);
                WkLV.get(time.WKDAY(a)).getItems().add(s);
            }   while (wAgSet.next());
    }   }

    //

    OffsetDateTime month_start, month_end;
    public void populate_monthly_calendar() throws SQLException {
        month_start = OffsetDateTime.of(
                onscreen.getYear(), onscreen.getMonthValue(), 1, 0, 0, 0, 0, time.tzLOC());
        month_end = month_start.plusMonths(1).minusDays(1); //Inigo: "I am not left handed!"
        String mAgQuery
            = "SELECT customer.customerName, appointment.start"
            + " FROM appointment INNER JOIN customer"
            + " ON customer.customerId = appointment.customerId "
            + "WHERE (appointment.start BETWEEN '" + time.UTCTS(month_start) 
            + "' AND '" + time.UTCTS(month_end) + "' ) "
            + (pollUserFilter() ? "" : "AND appointment.createdBy = '" + getUserFilter() + "' ")
            + "ORDER BY appointment.start ASC;";
        ResultSet mAgSet = utils.SQLRUN(mAgQuery, x -> utils.query(x));
        int ti, tj;

        //clear lists
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                CalLV.get(i).get(j).getItems().clear();
            }
        }

        String s, s0;
        Timestamp tmp;
        //System.out.println("------------------------------");
        //System.out.println("Populating Calendar Boxes.");
        if (mAgSet.first()) {
            timestandard = timestandard.withZone(ZoneId.systemDefault());
            //System.out.println("Getting next timestamp from server results.");
            OffsetDateTime a;
            do {tmp = mAgSet.getTimestamp(2);
                a = time.toODT(tmp);
                ti = a.getDayOfMonth()-1;
                tj = time.WKDAY(a);
                s0 = timestandard.format(a);
                //System.out.println(s0);
                s = s0 + " " + mAgSet.getString(1);
                CalLV.get(tj).get((ti + tj) / 7).getItems().add(s);
            }   while (mAgSet.next());
    }   }

    public boolean pollUserFilter() {
        return userfilterchoicebox.getItems().isEmpty()
        || getUserFilter().equalsIgnoreCase("ALL");
    }

    @Override public void initialize(URL url, ResourceBundle rb) {
        refreshUserFilter();
        userfilterchoicebox.getSelectionModel().selectedItemProperty().addListener(
        (ObservableValue<? extends String> o, String oV, String nV) -> { formatBothCalendars(); });
        currentt = OffsetDateTime.now();
        System.out.println("currentt date: " + currentt.getMonthValue() + "/" + currentt.getDayOfMonth());
        onscreen = currentt.withDayOfMonth(1);
        System.out.println("onscreen date: " + onscreen.getMonthValue() + "/" + onscreen.getDayOfMonth());

        //

        SundaysLV     .add(su0);    SundaysLV     .add(su1);    SundaysLV     .add(su2);    SundaysLV     .add(su3);
        MondaysLV     .add(mo0);    MondaysLV     .add(mo1);    MondaysLV     .add(mo2);    MondaysLV     .add(mo3);
        TuesdaysLV    .add(tu0);    TuesdaysLV    .add(tu1);    TuesdaysLV    .add(tu2);    TuesdaysLV    .add(tu3);
        WednesdaysLV  .add(we0);    WednesdaysLV  .add(we1);    WednesdaysLV  .add(we2);    WednesdaysLV  .add(we3);
        ThursdaysLV   .add(th0);    ThursdaysLV   .add(th1);    ThursdaysLV   .add(th2);    ThursdaysLV   .add(th3);
        FridaysLV     .add(fr0);    FridaysLV     .add(fr1);    FridaysLV     .add(fr2);    FridaysLV     .add(fr3);
        SaturdaysLV   .add(sa0);    SaturdaysLV   .add(sa1);    SaturdaysLV   .add(sa2);    SaturdaysLV   .add(sa3);

        SundaysLV     .add(su4);    SundaysLV     .add(su5);    CalLV.add(   SundaysLV);    WeekL.add(sunweekLabel);
        MondaysLV     .add(mo4);    MondaysLV     .add(mo5);    CalLV.add(   MondaysLV);    WeekL.add(monweekLabel);
        TuesdaysLV    .add(tu4);    TuesdaysLV    .add(tu5);    CalLV.add(  TuesdaysLV);    WeekL.add(tueweekLabel);
        WednesdaysLV  .add(we4);    WednesdaysLV  .add(we5);    CalLV.add(WednesdaysLV);    WeekL.add(wedweekLabel);
        ThursdaysLV   .add(th4);    ThursdaysLV   .add(th5);    CalLV.add( ThursdaysLV);    WeekL.add(thuweekLabel);
        FridaysLV     .add(fr4);    FridaysLV     .add(fr5);    CalLV.add(   FridaysLV);    WeekL.add(friweekLabel);
        SaturdaysLV   .add(sa4);    SaturdaysLV   .add(sa5);    CalLV.add( SaturdaysLV);    WeekL.add(satweekLabel);

        SundaysL     .add(sun0);    SundaysL     .add(sun1);    SundaysL     .add(sun2);    SundaysL     .add(sun3);
        MondaysL     .add(mon0);    MondaysL     .add(mon1);    MondaysL     .add(mon2);    MondaysL     .add(mon3);
        TuesdaysL    .add(tue0);    TuesdaysL    .add(tue1);    TuesdaysL    .add(tue2);    TuesdaysL    .add(tue3);
        WednesdaysL  .add(wed0);    WednesdaysL  .add(wed1);    WednesdaysL  .add(wed2);    WednesdaysL  .add(wed3);
        ThursdaysL   .add(thu0);    ThursdaysL   .add(thu1);    ThursdaysL   .add(thu2);    ThursdaysL   .add(thu3);
        FridaysL     .add(fri0);    FridaysL     .add(fri1);    FridaysL     .add(fri2);    FridaysL     .add(fri3);
        SaturdaysL   .add(sat0);    SaturdaysL   .add(sat1);    SaturdaysL   .add(sat2);    SaturdaysL   .add(sat3);

        SundaysL     .add(sun4);    SundaysL     .add(sun5);    CalL.add(     SundaysL);    WkLV.add(     SundayLV);
        MondaysL     .add(mon4);    MondaysL     .add(mon5);    CalL.add(     MondaysL);    WkLV.add(     MondayLV);
        TuesdaysL    .add(tue4);    TuesdaysL    .add(tue5);    CalL.add(    TuesdaysL);    WkLV.add(    TuesdayLV);
        WednesdaysL  .add(wed4);    WednesdaysL  .add(wed5);    CalL.add(  WednesdaysL);    WkLV.add(  WednesdayLV);
        ThursdaysL   .add(thu4);    ThursdaysL   .add(thu5);    CalL.add(   ThursdaysL);    WkLV.add(   ThursdayLV);
        FridaysL     .add(fri4);    FridaysL     .add(fri5);    CalL.add(     FridaysL);    WkLV.add(     FridayLV);
        SaturdaysL   .add(sat4);    SaturdaysL   .add(sat5);    CalL.add(   SaturdaysL);    WkLV.add(   SaturdayLV);
        formatBothCalendars();

        //KMA: iterate over the collection of listviews to set a bunch of event handlers
        //KMA: got lambda?
        CalLV.forEach((x) -> { //LinkedList of LinkedList
            x.forEach((y) -> { //LinkedList of ListView
                y.setOnMouseClicked((z) -> {
                    //only left-click (or alternate primary mouse click input)
                    if(z.getButton().equals(MouseButton.PRIMARY)) {
                        //set double click action in particular.
                        if(z.getClickCount() == 2) {
                            //format: HH:MM AM [customerName]
                            String[] apptstr = y.getSelectionModel().getSelectedItem().split("M ");
                            int selectedHour = Integer.parseInt(apptstr[0].split(":")[0]);
                            int selectedMins = Integer.parseInt(apptstr[0].split(":")[1].substring(0, 2));
                            //will either be A or P
                            if(apptstr[0].endsWith("A") && selectedHour == 12) selectedHour = 0;
                            else if(apptstr[0].endsWith("P")) selectedHour += 12;
                            int selectedDayOfMonth = Integer.parseInt(
                                    CalL.get(CalLV.indexOf(x)).get(x.indexOf(y)).getText());
                            OffsetDateTime seODT = OffsetDateTime.of(
                                    onscreen.getYear(), 
                                    onscreen.getMonthValue(), 
                                    selectedDayOfMonth, 
                                    selectedHour, 
                                    selectedMins, 0, 0, time.tzLOC());
                            TidierFXML xnac = utils.loadFX(
                                    "FXMLNewAppointment.fxml", 
                                    Modality.APPLICATION_MODAL);
                            xnac.<FXMLNewAppointmentController>getController()
                                    .take_existing_appointment(apptstr[1], seODT);
                            xnac.getStage().showAndWait();
                            formatBothCalendars();
        }   }   }); }); });

        //now for weekly agenda view event handlers.
        WkLV.forEach((x) -> {
            x.setOnMouseClicked((y) -> {
                if(y.getButton().equals(MouseButton.PRIMARY)) {
                    if(y.getClickCount() == 2) {
                        String[] selectedAppt = x.getSelectionModel().getSelectedItem().split("M ");
                        int selectedHour = Integer.parseInt(selectedAppt[0].split(":")[0]);
                        int selectedMins = Integer.parseInt(selectedAppt[0].split(":")[1].substring(0, 2));
                        if(selectedAppt[0].endsWith("P")) selectedHour += 12;
                        int selectedDayOfMonth = Integer.parseInt(
                                WeekL.get(WkLV.indexOf(x)).getText().split(": ")[1]);
                        OffsetDateTime seODT = OffsetDateTime.of(
                                onscreen.getYear(), 
                                onscreen.getMonthValue(), 
                                selectedDayOfMonth, 
                                selectedHour, 
                                selectedMins, 0, 0, time.tzLOC());
                        TidierFXML xnac = utils.loadFX(
                                "FXMLNewAppointment.fxml", 
                                Modality.APPLICATION_MODAL);
                        xnac.<FXMLNewAppointmentController>getController()
                                .take_existing_appointment(selectedAppt[1], seODT);
                        xnac.getStage().showAndWait();
                        formatWeekCalendar();
    }   }   }); }); }

    @FXML private void handleBack1Month() {
        System.out.println("orig: " + onscreen.getMonthValue() + "/" + onscreen.getDayOfMonth());
        onscreen = onscreen.minusMonths(1).withDayOfMonth(1);
        System.out.println("now: " + onscreen.getMonthValue() + "/" + onscreen.getDayOfMonth());
        formatMonthCalendar();
    }

    @FXML private void handleBack1Week() {
        onscreen = onscreen.minusDays(7);
        formatBothCalendars();
    }

    @FXML private void handleNext1Month() {
        System.out.println("orig: " + onscreen.getMonthValue() + "/" + onscreen.getDayOfMonth());
        onscreen = onscreen.plusMonths(1).withDayOfMonth(1);
        System.out.println("now: " + onscreen.getMonthValue() + "/" + onscreen.getDayOfMonth());
        formatMonthCalendar();
    }

    @FXML private void handleNext1Week() {
        onscreen = onscreen.plusDays(7);
        formatBothCalendars();
    }

    @FXML void handleNewAppointment() {
        utils.loadFX("FXMLNewAppointment.fxml", Modality.APPLICATION_MODAL).getStage().showAndWait();
        formatBothCalendars();
    }

    @FXML void handleCustomerInfo() {
        utils.loadFX("FXMLCustomerForm.fxml", Modality.APPLICATION_MODAL).getStage().showAndWait();
        formatBothCalendars();
    }

    void refreshUserFilter() {
        String usersquery = "SELECT DISTINCT createdBy FROM appointment;";
        ResultSet userSet = utils.SQLRUN(usersquery, x -> utils.query(x));
        userfilterchoicebox.getItems().clear();
        userfilterchoicebox.getItems().add("ALL");
        try { if(userSet.first()) {
            do { userfilterchoicebox.getItems().add(userSet.getString(1)); }
            while (userSet.next());
        }   }
        catch (SQLException e) { e.printStackTrace(System.out); }
        userfilterchoicebox.getSelectionModel().select("ALL");
    }

    void formatBothCalendars() { formatMonthCalendar(); formatWeekCalendar(); }

    @FXML void handleExit() {
        utils.closeCurrentWindow(exitButton);
        System.exit(0);
    }

    @FXML void handleMonthSum() {
        TidierFXML monthSum = utils.loadFX("FXMLMonthSummary.fxml", Modality.APPLICATION_MODAL);
        monthSum.<FXMLMonthSummaryController>getController()
                .passMonth(time.UTCTS(month_start), time.UTCTS(month_end));
        monthSum.getStage().showAndWait();
    }

    @FXML void getTips() { utils.loadFX("FXMLTipsPanel.fxml", Modality.NONE).getStage().showAndWait(); }

    private String getUserFilter() { return userfilterchoicebox.getSelectionModel().getSelectedItem(); }
}
