package dbappdemo;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLCustomerAgendaBulletinController implements Initializable {
    @FXML TableView<AppointmentSearchResult> appointmentBulletin;
    @FXML TableColumn<AppointmentSearchResult, String> apptTitle, apptDesc, apptStart, apptEnd;
    LinkedList<AppointmentSearchResult> agendaList = new LinkedList<>();
    private static final String qText = "SELECT title, description, start, end FROM appointment " 
            + "WHERE customerId = ? AND start > NOW() ORDER BY start ASC;";
    @Override public void initialize(URL url, ResourceBundle rb) {}
    
    DateTimeFormatter timestandard = DateTimeFormatter.ofPattern("h:mm a");
    
    public void passCustomerID(int ID) throws SQLException {
        PreparedStatement apptsQuery = dbuser.getConnection().prepareStatement(qText);
        apptsQuery.setInt(1, ID);
        ResultSet results = apptsQuery.executeQuery();
        appointmentBulletin.getItems().clear();
        agendaList.clear();
        
        timestandard.withZone(ZoneId.systemDefault());
        
        OffsetDateTime t1, t2;
        
        if(results.first()) {
            do {  t1 = time.toODT(results.getTimestamp(3));
                  t2 = time.toODT(results.getTimestamp(4));
                  agendaList.add( new AppointmentSearchResult(
                  results.getString(1), 
                  results.getString(2), 
                  t1.format(timestandard),
                  t2.format(timestandard)));
            } while(results.next()); }
        
        appointmentBulletin.setItems(FXCollections.observableList(agendaList));
        apptTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDesc .setCellValueFactory(new PropertyValueFactory<>( "desc"));
        apptStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        apptEnd  .setCellValueFactory(new PropertyValueFactory<>(  "end"));
}   }
