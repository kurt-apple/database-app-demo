package dbappdemo;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class FXMLErrorBoxController implements Initializable {
    @Override public void initialize(URL url, ResourceBundle rb) { }
    @FXML private Label boxMessage;
    @FXML private Button okButton;
    public void passMessage(String newMessage) { boxMessage.setText(newMessage);     }
    @FXML public void handleOkButtonAction()   { utils.closeCurrentWindow(okButton); }
}
