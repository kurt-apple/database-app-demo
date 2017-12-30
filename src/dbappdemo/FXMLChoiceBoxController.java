package dbappdemo;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class FXMLChoiceBoxController implements Initializable {
    @Override public void initialize(URL url, ResourceBundle rb) {}
    @FXML private Label msg;
    @FXML private Button yesButton, noButton;
    
    public void passMessage(String s) { msg.setText(s); }
    
    @FXML public void handleNoButton() {
        utils.setAnswer(false);
        utils.closeCurrentWindow(noButton);
    }
    
    @FXML public void handleYesButton() {
        utils.setAnswer(true);
        utils.closeCurrentWindow(yesButton);
}   }
