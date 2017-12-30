package dbappdemo;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Properties;

public class dbuser {
    private static Connection conn;           
    private static final String DRIVER = "com.mysql.jdbc.Driver";         
    private static final String DB     = "XXXXXXXXXXXXXXXXXXXXX";         
    private static final String URL    = "jdbc:mysql://X.X.X.X/" + DB;         
    private static final String DBUSERNAME = "XXXXXXXXXXXXXXXXX";
    private static final String DBPASSWORD = "XXXXXXXXXXXXXXXXX";
    private static String user;
    private static String pass;
    private static final Timestamp XTS = new Timestamp(System.currentTimeMillis());
    public dbuser(String usern, String passw) throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        Properties devprop = new Properties();
        conn = DriverManager.getConnection(URL,DBUSERNAME,DBPASSWORD);
        String checkUNPW = "SELECT userId FROM user WHERE userName = '" + usern 
                         + "' AND password = '" + passw + "';";
        Statement s = getConnection().createStatement();
        ResultSet userresult = s.executeQuery(checkUNPW);
        if(!userresult.first()) throw new SQLException();
        user = usern; pass = passw;
    }
    public static String getPass() { return pass; }
    public static String getUser() { return user; }
    public static Connection getConnection() { return conn; }
}
