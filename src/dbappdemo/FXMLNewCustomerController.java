package dbappdemo;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class FXMLNewCustomerController implements Initializable {
    @FXML Button saveButton, cancelButton;
    @FXML TextField name, address1, address2, city, country, postal, phone;
    String cxname, addr, addr2, cxcity, cxcountry, cxpostal, cxphone;
    int cxID, addressId, cityId, countryId;
    boolean isModify = false;
    
    @Override public void initialize(URL url, ResourceBundle rb) { }
    
    @FXML void saveCustomer() throws SQLException {
        //validate form completion
        if(name.getText().isEmpty()
        || address1.getText().isEmpty() 
        || city.getText().isEmpty() 
        || country.getText().isEmpty() 
        || postal.getText().isEmpty() 
        || phone.getText().isEmpty()) {
            utils.spawn("Form must be complete before customer can be added.", x -> utils.errorbox(x));
            return;
        }
        if(!isModify) {
            //check for conflicts
            int newAddressID = DBAddress.handleAddress(
                    address1.getText(), 
                    address2.getText(), 
                    postal.getText(), 
                    phone.getText(), 
                    DBCity.handleCity(
                            city.getText(), 
                            DBCountry.handleCountry(country.getText())));
            String conflictQuery = "SELECT customerId FROM customer"
                                 + " WHERE (customerName = '" + name.getText() + "' "
                                 + " AND addressId = " + newAddressID + ");";
            if(!utils.spawn(conflictQuery, x -> utils.noResults(x))) {
                utils.spawn("A customer already exists with this name and contact information.", x -> utils.errorbox(x));
                return;
            }
            //add
            DBCustomer.handleAddCustomer(
                    name.getText(), 
                    country.getText(), 
                    city.getText(), 
                    address1.getText(), 
                    address2.getText(), 
                    postal.getText(), 
                    phone.getText());
        }
        else DBCustomer.handleModifyCustomer(
                cxID, 
                name.getText(), 
                country.getText(), 
                city.getText(), 
                address1.getText(), 
                address2.getText(), 
                postal.getText(), 
                phone.getText());
        utils.closeCurrentWindow(saveButton);
    }
    
    @FXML void handleCancel() { utils.closeCurrentWindow(cancelButton); }

    void modify(int inputcustid) throws SQLException {
        String retrieveCXInfo = "SELECT c.customerName, c.addressId, a.address, a.address2,"
        + " ci.cityId, ci.city, cou.countryId, cou.country, a.postalCode, a.phone FROM customer AS c"
        + " INNER JOIN address AS a ON a.addressId = c.addressId"
        + " INNER JOIN city AS ci ON ci.cityId = a.cityId AND a.addressId = c.addressId" 
        + " INNER JOIN country AS cou ON cou.countryId = ci.countryId AND ci.cityId = a.cityId"
        + " WHERE c.customerId = " + inputcustid + ";";
        ResultSet x = utils.SQLRUN(retrieveCXInfo, y -> utils.query(y));
        if(x.first()) {
            isModify = true;
            cxID = inputcustid;
            cxname    = x.getString(1);
            addr      = x.getString(3);
            addr2     = x.getString(4);
            cityId    = x.getInt(5);
            cxcity    = x.getString(6);
            countryId = x.getInt(7);
            cxcountry = x.getString(8);
            cxpostal  = x.getString(9);
            cxphone   = x.getString(10);
            name.setText(cxname);
            address1.setText(addr);
            address2.setText(addr2);
            city.setText(cxcity);
            country.setText(cxcountry);
            postal.setText(cxpostal);
            phone.setText(cxphone);
        }
        else throw new SQLException();
    }
}
