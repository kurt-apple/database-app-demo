package dbappdemo;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class FXMLTipsPanelController implements Initializable {
    @FXML TextArea tf;
    @Override public void initialize(URL url, ResourceBundle rb) {
        Calendar x = Calendar.getInstance();
        String tips = "TIPS.\n"
                    + "There are 3 REPORTS this application generates on demand for you:\n"
                    + "- Consultant Calendar: Select a consultant from the main view's dropdown,\n"
                    + "  or select All.\n"
                    + "- Number of appointment types by month (see Month Summary)\n"
                    + "- Customer 'Agenda'; Find/Select a customer, then 'View Cust. Agenda'\n\n"
                    + "timezone: " + Calendar.getInstance().getTimeZone().getDisplayName() 
                    + " (" + time.tzLOC().getId() + ")";
        tf.setEditable(false);
        tf.setText(tips);
}   }
