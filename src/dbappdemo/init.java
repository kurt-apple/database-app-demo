package dbappdemo;

import java.sql.SQLException;
import java.util.Locale;
//import java.util.Random;
import java.util.TimeZone;

public class init {
    //set vars.
    public static void init() {
        int i = -1;
        /*Random nonSequitor = new Random();
        i = nonSequitor.nextInt(TimeZone.getAvailableIDs().length) - 1;
        while(TimeZone.getDefault().getID().equalsIgnoreCase(TimeZone.getAvailableIDs()[i]))
            i = nonSequitor.nextInt() % TimeZone.getAvailableIDs().length;*/

        TimeZone.setDefault(i < 0 ? TimeZone.getDefault() 
            : TimeZone.getTimeZone(TimeZone.getAvailableIDs()[i]));
        try { new dbuser("test", "test"); }
            catch(ClassNotFoundException e) { System.exit(969); }
            catch(SQLException e) { System.exit(254); }
        utils.setFrenchUIvar(Locale.getDefault().equals(Locale.FRENCH));
    }
}
