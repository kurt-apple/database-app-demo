package dbappdemo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCity {
    public static int handleCity(String cityToCheck, int countryID) throws SQLException {
        String checkCityQuery = "SELECT cityID FROM U048lL.city WHERE city = '" + cityToCheck 
                              + "' AND countryID = " + countryID + ";";
        ResultSet citySet = utils.SQLRUN(checkCityQuery, x -> utils.query(x));
        if(!citySet.first()) { 
            String genNewID = "SELECT MAX(cityID) FROM U048lL.city";
            ResultSet IDSet = utils.SQLRUN(genNewID, x -> utils.query(x));
            int newID = IDSet.first() ? IDSet.getInt(1) + 1 : 1;
            
            String addCityQuery = "INSERT INTO U048lL.city (cityID, city, countryID, createDate, "
                                + "createdBy, lastUpdateBy) VALUES ( "
                                + newID             + ",  '"
                                + cityToCheck       + "',  "
                                + countryID         + ",   "
                                + "CURRENT_TIMESTAMP,     '"
                                + dbuser.getUser()  + "', '"
                                + dbuser.getPass()  + "'); ";
            
            utils.SQLRUN(addCityQuery, x -> utils.update(x));
            return newID;
        }
        else return citySet.getInt(1);
    }
    
    public static int getCountryID(int cityID) throws SQLException {
        String retrieveCountryIDQuery = "SELECT countryID FROM U048lL.city WHERE cityId = " + cityID + ";";
        ResultSet countrySet = utils.SQLRUN(retrieveCountryIDQuery, x -> utils.query(x));
        return countrySet.first() ? countrySet.getInt(1) : -1;
}   }
