package dbappdemo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCustomer {
    private static int countryID;
    private static int cityID;
    private static int addressID;
    private static int customerID;
    private static int active;
    
    public static int handleAddCustomer(String CustomerName, 
                                        String CountryName, 
                                        String CityName, 
                                        String address1, 
                                        String address2, 
                                        String postalCode, 
                                        String phoneNumber) throws SQLException {
        countryID  = DBCountry.handleCountry(CountryName);
        cityID     = DBCity.handleCity(CityName, countryID);
        addressID  = DBAddress.handleAddress(address1, address2, postalCode, phoneNumber, cityID);
        active     = 1;
        String newIDString = "SELECT MAX(customerID) FROM U048lL.customer";
        ResultSet IDSet = utils.SQLRUN(newIDString, x -> utils.query(x));
        customerID = IDSet.first() ? IDSet.getInt(1) + 1 : 1;
        
        String addCustomerQuery = "INSERT INTO U048lL.customer (customerId, customerName, "
                                + "addressId, active, createDate, createdBy, lastUpdateBy) VALUES ( "
                                + customerID       + ",  '"
                                + CustomerName     + "',  "
                                + addressID        + ",   "
                                + active     + ", NOW(), '"
                                + dbuser.getUser() + "', '"
                                + dbuser.getUser() + "' );";
        
        utils.SQLRUN(addCustomerQuery, x -> utils.update(x));
        return customerID;
    }
    
    public static boolean handleModifyCustomer( int customerID, 
                                                String CustomerName, 
                                                String CountryName, 
                                                String CityName, 
                                                String address1, 
                                                String address2, 
                                                String postalCode, 
                                                String phoneNumber) throws SQLException {
        String cQuery = "SELECT customerId FROM U048lL.customer WHERE customerId = " + customerID + ";";
        if(utils.spawn(cQuery, x -> utils.noResults(x))) return false;
        countryID = DBCountry.handleCountry(CountryName);
        cityID = DBCity.handleCity(CityName, countryID);
        //kma 8/02: customers have a many-to-one relationship. old addresses best shouldn't be modified.
        //handleAddress already checks if an address already exists in the database and 
        //will return the ID if found.
        addressID = DBAddress.handleAddress(address1, address2, postalCode, phoneNumber, cityID);
        
        String updateCustomerQuery = "UPDATE customer SET "
        + "customerName     = '" + CustomerName     + "', "
        + "addressId        =  " + addressID        + ",  "
        + "active           =  " + 1                + ",  "
        + "lastUpdateBy     = '" + dbuser.getUser() + "'  "
        + "WHERE customerId =  " + customerID       + ";  ";
        
        utils.SQLRUN(updateCustomerQuery, x -> utils.update(x));
        return true;
    }
    
    public static void deactivateCustomer(int customerID) {
        String deactivateCustomerQuery  = "UPDATE U048lL.customer SET "
                                        + "active = " + 0 + " WHERE customerId = " + customerID + ";";
    }
}
