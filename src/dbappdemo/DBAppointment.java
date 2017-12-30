package dbappdemo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DBAppointment {
    private static int newID;
    public static int handleAddAppointment(
            int customerID, 
            String title, 
            String description, 
            String location, 
            String contact, 
            Timestamp start, 
            Timestamp endts) throws SQLException {
        String checkAppointment = "SELECT appointmentId FROM U048lL.appointment WHERE customerId = " 
                                + customerID + " AND start = '" + start + "';";
        ResultSet appointmentSet = utils.SQLRUN(checkAppointment, x -> utils.query(x));
        if(!appointmentSet.first()) {
            String genNewIDQuery = "SELECT MAX(appointmentID) FROM U048lL.appointment";
            ResultSet IDSet = utils.SQLRUN(genNewIDQuery, x -> utils.query(x));
            newID = IDSet.first() ? IDSet.getInt(1) + 1 : 1;
            
            String addNewAppointment = "INSERT INTO U048lL.appointment "
            + "(appointmentId, customerId, title, description, location, contact, "
            + "start, end, createDate, createdBy, lastUpdateBy) VALUES ( "
            + newID            + ", "     + customerID         + ", '"
            + title            + "', '"   + description        + "', '"
                    + location + "', '"
            + contact          + "', '"   + start              + "', '"
            + endts            + "', "    + "CURRENT_TIMESTAMP, '"
            + dbuser.getUser() + "', '"   + dbuser.getUser()   + "' );";
            
            utils.SQLRUN(addNewAppointment, x -> utils.update(x));
            return newID;
        }
        return appointmentSet.getInt(1);
    }
    
    public static int handleAddAppointmentCLI(int customerID, 
                                            String title, 
                                            String description, 
                                            String location, 
                                            String contact, 
                                            Timestamp start, 
                                            Timestamp endts) throws SQLException {
        String checkAppointment = "SELECT appointmentId FROM U048lL.appointment WHERE customerId = " 
                + customerID + " AND start = '" + start + "';";
        ResultSet appointmentSet = utils.SQLRUN(checkAppointment, x -> utils.query(x));
        if(!appointmentSet.first()) {
            String genNewIDQuery = "SELECT MAX(appointmentID) FROM U048lL.appointment";
            ResultSet IDSet = utils.SQLRUN(genNewIDQuery, x -> utils.query(x));
            newID = IDSet.first() ? IDSet.getInt(1) + 1 : 1;
            
            String addNewAppointment = "INSERT INTO U048lL.appointment "
            + "(appointmentId, customerId, title, description, location, url, " 
            + "contact, start, end, createDate, createdBy, lastUpdateBy) VALUES ( "
            + newID            + ", "     + customerID         + ", '"
            + title            + "', '"   + description        + "', '"
                    + location + "', 'test', '"
            + contact          + "', '"   + start              + "', '"
            + endts            + "', "    + "CURRENT_TIMESTAMP, '"
            + dbuser.getUser() + "', '"   + dbuser.getUser()   + "' );";
            
            utils.SQLRUN(addNewAppointment, x -> utils.updateCLI(x));
            return newID;
        }
        return appointmentSet.getInt(1);
    }
    
    public static boolean handleModifyAppointment(int appointmentID, 
                                                String title, 
                                                String description, 
                                                String location, 
                                                String contact, 
                                                Timestamp startUTC, 
                                                Timestamp endtsUTC) throws SQLException {
        String rQuery = "SELECT * FROM U048lL.appointment WHERE appointmentId = " + appointmentID + ";";
        if(utils.spawn(rQuery, x -> utils.noResults(x))) return false;
        String updateApptQuery   
        = "UPDATE U048lL.appointment SET "
        + "title                    = ?, "
        + "description              = ?, "
        + "location                 = ?, "
        + "contact                  = ?, "
        + "start                    = ?, "
        + "end                      = ?, "
        + "lastUpdateBy             = ?  "
        + "WHERE appointmentId      = ?; ";
        
        PreparedStatement updateAppointment = dbuser.getConnection().prepareStatement(updateApptQuery);
        
        updateAppointment.setString   (1, title           );
        updateAppointment.setString   (2, description     );
        updateAppointment.setString   (3, location        );
        updateAppointment.setString   (4, contact         );
        updateAppointment.setTimestamp(5, startUTC        );
        updateAppointment.setTimestamp(6, endtsUTC        );
        updateAppointment.setString   (7, dbuser.getUser());
        updateAppointment.setInt      (8, appointmentID   );
        
        updateAppointment.executeUpdate();
        return true;
    }
    
    public static void handleDeleteAppointment(int appointmentID) throws SQLException {
        String ds = "DELETE FROM U048lL.appointment WHERE appointmentId = " + appointmentID + ";";
        utils.SQLRUN(ds, x -> utils.update(x));
    }
}
