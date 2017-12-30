package dbappdemo;

import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class dbappdemo extends Application {
    @Override public void start(Stage stage)
    {   TidierFXML SWIIFXML = utils.loadFX("FXMLLogin.fxml", Modality.NONE);
        SWIIFXML.getStage().setTitle(utils.FrenchUI ? "S'identifier" : "Login");
        SWIIFXML.getStage().show();
    }
    public static void main(String[] args) 
    {   init.init();
        launch(args);
        System.exit(0);
}   }
