package dbappdemo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCountry {
    public static int handleCountry(String countryToCheck) throws SQLException {
        String cQuery = "SELECT countryID FROM U048lL.country WHERE country = '" + countryToCheck + "';";
        ResultSet countrySet = utils.SQLRUN(cQuery, x -> utils.query(x));
        if(!countrySet.first()) { //creating new country row
            String genNewID = "SELECT MAX(countryID) FROM U048lL.country";
            ResultSet IDSet = utils.SQLRUN(genNewID, x -> utils.query(x));
            int newID = IDSet.first() ? IDSet.getInt(1) + 1 : 1;
            
            String addCountryQuery = "INSERT INTO U048lL.country (countryID, country, createDate, "
                                   + "createdBy, lastUpdateBy) VALUES ( "
                                   + newID            + ",  '"
                                   + countryToCheck   + "', '"
                                   + utils.getStamp() + "', '"
                                   + dbuser.getUser() + "', '"
                                   + dbuser.getPass() + "'); ";
            
            utils.SQLRUN(addCountryQuery, x -> utils.update(x));
            return newID;
        }
        else return countrySet.getInt(1);
}   }
