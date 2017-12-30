package dbappdemo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBAddress {
    public static int handleAddress(String address1, 
                                    String address2, 
                                    String postalCode, 
                                    String phone, 
                                    int cityID) throws SQLException {
        String checkAddressQuery = "SELECT addressID FROM U048lL.address WHERE address = '" 
                            + address1 + "' AND address2 = '" + address2 + "' AND cityId = " + cityID + ";";
        ResultSet addressSet = utils.SQLRUN(checkAddressQuery, x -> utils.query(x));
        if(!addressSet.first()) {
            String genNewID = "SELECT MAX(addressID) FROM U048lL.address";
            ResultSet IDSet = utils.SQLRUN(genNewID, x -> utils.query(x));
            int newID = IDSet.first() ? IDSet.getInt(1) + 1 : 1;
            
            String addAddressQuery = "INSERT INTO U048lL.address (addressId, address, "
            + "address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy) VALUES ( " 
            + newID            + ",  '"     + address1         + "', '"     + address2         + "',  "
            + cityID           + ",  '"     + postalCode       + "', '"     + phone            + "',  " 
            + "CURRENT_TIMESTAMP,    '"     + dbuser.getUser() + "', '"     + dbuser.getUser() + "' );";
            
            utils.SQLRUN(addAddressQuery, x -> utils.update(x));
            return newID;
        }
        else return addressSet.getInt(1);
}   }
