package dbappdemo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;
import java.util.function.Predicate;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class utils {
    public static void closeCurrentWindow(Button b) {
        Scene s = b.getScene();
        Stage r = (Stage) s.getWindow();
        r.close();
    }
    
    public static TidierFXML loadFX(String URL, Modality mode) {
        FXMLLoader xloader = new FXMLLoader(utils.class.getResource(URL));
        Stage xstage = new Stage();
        xstage.initModality(mode);
        try {
            xstage.setScene(new Scene(xloader.load()));
        } catch (IOException e) {
            e.printStackTrace(System.out);
            System.exit(699);
        }
        return new TidierFXML(xloader, xstage);
    }
    
    private static boolean choice = false;
    public static void setAnswer(boolean b) { choice = b; }
    public static boolean choicebox(String newMessage) {
        TidierFXML cbLoader = loadFX("FXMLChoiceBox.fxml", Modality.APPLICATION_MODAL);
        cbLoader.<FXMLChoiceBoxController>getController().passMessage(newMessage);
        cbLoader.getStage().showAndWait();
        return choice;
    }
    
    public static boolean errorbox(String newMessage) {
        TidierFXML ebLoader = loadFX("FXMLErrorBox.fxml", Modality.APPLICATION_MODAL);
        ebLoader.<FXMLErrorBoxController>getController().passMessage(newMessage);
        ebLoader.getStage().showAndWait();
        return true;
    }

    public static boolean spawn(String message, Predicate<String> action) {
        return action.test(message);
    }

    public static ResultSet SQLRUN(String message, Function<String, ResultSet> action) {
        return action.apply(message);
    }

    public static boolean noResults(String x) {
        try {
            return !utils.SQLRUN(x, y -> utils.query(y)).first();
        } catch (SQLException e) {
            return true;
        }
    }

    public static ResultSet query(String x) {
        try {
            return dbuser.getConnection().createStatement().executeQuery(x);
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            utils.spawn("Error querying database.", y -> utils.errorbox(y));
            return null;
        }
    }

    public static ResultSet queryCLI(String x) {
        try {
            return dbuser.getConnection().createStatement().executeQuery(x);
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            return null;
        }
    }

    public static ResultSet update(String x) {
        try {
            dbuser.getConnection().createStatement().executeUpdate(x);
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            utils.spawn("Failed to update the database.", y -> utils.errorbox(y));
        }
        return null;
    }

    public static ResultSet updateCLI(String x) {
        try {
            dbuser.getConnection().createStatement().executeUpdate(x);
        } catch (SQLException e) {
            e.printStackTrace(System.out);
            return null;
        }
        return null;
    }

    public static String getStamp() throws SQLException {
        Statement timestampgetter = dbuser.getConnection().createStatement();
        ResultSet timestampset = timestampgetter.executeQuery("SELECT NOW()");
        timestampset.first();
        return timestampset.getTimestamp(1).toString();
    }
    public static boolean FrenchUI;
    public static void setFrenchUIvar(boolean x) { FrenchUI = x; }
    
    public static boolean appendlog(boolean isSuccess) {
        String message = Instant.now() + " - " + dbuser.getUser() 
                       + (isSuccess ? " SUCCESSFUL LOGIN" 
                       : " FAILED AUTHENTICATION") + "\n";
        try {
            Files.write(Paths.get("userlog.txt"), message.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    
    private static final String MSGF = "Upcoming appointment: ";
    private static String apptname = "";
    private static final TimerTask APPTPOP = new TimerTask() {
        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                public void run() {
                    utils.spawn(MSGF + apptname, x -> utils.errorbox(x));
                    utils.setAlarm();
                }
            });
        }
    };

    public static void setAlarm() {
        String a = "SELECT title, start FROM appointment " 
                 + "WHERE start >= NOW() AND title != '" 
                 + apptname + "' ORDER BY start ASC;";
        ResultSet b = utils.SQLRUN(a, x -> utils.query(x));
        try {
            if (b.first()) {
                Timer c = new Timer();
                OffsetDateTime ldt = time.toODT(b.getTimestamp(2));
                apptname = b.getString(1);
                ldt = ldt.minusMinutes(15);
                try {
                    c.schedule(APPTPOP, Date.from(ldt.toInstant()));
                } catch (IllegalStateException e) {/* ignore */
                }
            }
        } catch (SQLException e) { /*ignore*/ }
    }
}
