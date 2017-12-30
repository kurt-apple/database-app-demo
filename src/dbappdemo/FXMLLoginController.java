package dbappdemo;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class FXMLLoginController implements Initializable {
    //login form gets locale, and translates into French
    @FXML TextField     username;
    @FXML PasswordField password;
    @FXML Label         logintitle;
    @FXML Button        loginB, cancelB;
    
    String cancelDialog;
    
    private final boolean nonEng = utils.FrenchUI;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logintitle.setText(nonEng ? "S'identifier" : "Log In");
    }
    
    @FXML
    public void handleLogin() {
        try { 
            new dbuser(username.getText(), password.getText());
            init.init();
        }
        catch(ClassNotFoundException e) {
            utils.spawn(nonEng ? "Erreur. Veuillez réessayer." 
                : "Error. Please retry.", x -> utils.errorbox(x));
            utils.appendlog(false);
            return; }
        catch(SQLException f) {
            utils.spawn(nonEng ? "Nom d'utilisateur et mot de passe ne correspondent pas." 
                : "Username and password do not match.", x -> utils.errorbox(x));
            utils.appendlog(false);
            return; }
        utils.appendlog(true);
        //utils.spawn(nonEng ? "Connecté." : "Logged in.", x -> utils.errorbox(x));
        
        utils.setAlarm();
        
        TidierFXML mainWindow = utils.loadFX("FXMLMainView.fxml", Modality.NONE);
        mainWindow.getStage().show();
        utils.closeCurrentWindow(loginB);
    }
    
    @FXML public void handleCancelLogin() {
        utils.closeCurrentWindow(cancelB);
        System.exit(0);
}   }
