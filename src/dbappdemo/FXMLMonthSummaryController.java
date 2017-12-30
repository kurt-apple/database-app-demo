package dbappdemo;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class FXMLMonthSummaryController implements Initializable {
    @FXML ListView<String> summaryList = new ListView<>();
    LinkedList<String> summaryLL = new LinkedList<>();
    @Override public void initialize(URL url, ResourceBundle rb) { }
    
    public void passMonth(Timestamp st, Timestamp en) {
        String querymonth = "SELECT title, COUNT(*) AS count FROM appointment WHERE start > '" + st 
                + "' AND end < '" + en + "' GROUP BY title ORDER BY count DESC;";
        ResultSet monthappt = utils.SQLRUN(querymonth, x -> utils.query(x));
        try { if(monthappt.first()) {
            do { summaryList.getItems().add(monthappt.getInt(2) + ": " + monthappt.getString(1)); }
            while (monthappt.next());
        }   }
        catch (Exception e) { e.printStackTrace(System.out); }
}   }
