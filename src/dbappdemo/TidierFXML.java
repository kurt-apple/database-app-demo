package dbappdemo;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

// :)
public class TidierFXML {
    public FXMLLoader a;
    public Stage      b;
    public TidierFXML(FXMLLoader xLoader, Stage xStage) {
        a = xLoader;
        b = xStage;}
    public FXMLLoader getLoader() { return a; }
    public Stage       getStage() { return b; }
    public <T>T   getController() { return a.getController(); }
}
