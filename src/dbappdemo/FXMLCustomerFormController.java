package dbappdemo;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;

public class FXMLCustomerFormController implements Initializable {

    @FXML TextField searchBox;
    @FXML Button    searchButton, 
                    addCustomerButton, 
                    viewCustomerButton, 
                    deactivateCustomerButton, 
                    backButton, 
                    viewAgenda;
    @FXML TableView<CustomerSearchResult> customers;
    @FXML TableColumn<CustomerSearchResult, String> name, address1;
    @FXML TableColumn<CustomerSearchResult, Integer> Id, Active;
    LinkedList<CustomerSearchResult> searchResultsList = new LinkedList<>();
    
    @Override public void initialize(URL url, ResourceBundle rb) 
    { try{handleSearch(); } catch(Exception e) {/*ignore*/} }
    
    @FXML void handleSearch() throws SQLException {
        String searchCustomer = 
            "SELECT customer.customerId, customer.customerName, address.address," 
            + " customer.active FROM customer INNER JOIN address ON "
            + "customer.addressId = address.addressId" 
            + (searchBox.getText().isEmpty() ? ";" : " WHERE customerName = '" + searchBox.getText() + "';");
        ResultSet customerSearchResults = utils.SQLRUN(searchCustomer, x -> utils.query(x));
        searchResultsList.clear();
        
        if(customerSearchResults.first()) 
            { do {searchResultsList.add(
              new CustomerSearchResult (
                  customerSearchResults.getInt(1), 
                  customerSearchResults.getString(2), 
                  customerSearchResults.getString(3), 
                  customerSearchResults.getInt(4))); }
            while(customerSearchResults.next()); }
        
        customers.setItems(FXCollections.observableList(searchResultsList));
        Id      .setCellValueFactory(new PropertyValueFactory<>("customerId"));
        name    .setCellValueFactory(new PropertyValueFactory<>("customerName"));
        address1.setCellValueFactory(new PropertyValueFactory<>("customerAddress1"));
        Active  .setCellValueFactory(new PropertyValueFactory<>("customerActive"));
    }
    
    @FXML void handleAdd() {
        TidierFXML addCust = utils.loadFX("FXMLNewCustomer.fxml", Modality.APPLICATION_MODAL);
        addCust.getStage().showAndWait();
        try { handleSearch(); } catch(Exception e) {/*ignore*/}
    }
    
    @FXML void handleView() throws SQLException {
        int x = customers.getSelectionModel().getSelectedItem().getcustomerId();
        TidierFXML viewCust = utils.loadFX("FXMLNewCustomer.fxml", Modality.APPLICATION_MODAL);
        viewCust.<FXMLNewCustomerController>getController().modify(x);
        viewCust.getStage().showAndWait();
    }
    
    @FXML void handleViewAgenda() throws SQLException {
        int x = customers.getSelectionModel().getSelectedItem().getcustomerId();
        TidierFXML viewAgenda = utils.loadFX("FXMLCustomerAgendaBulletin.fxml", Modality.APPLICATION_MODAL);
        viewAgenda.<FXMLCustomerAgendaBulletinController>getController().passCustomerID(x);
        viewAgenda.getStage().showAndWait();
    }
    
    @FXML void handleDelete() throws SQLException
    {   if(utils.spawn("Really deactivate this customer?", x -> utils.choicebox(x)))
        {   int y = customers.getSelectionModel().getSelectedItem().getcustomerId();
            //consider checking times of appointments too
            String checkx = "SELECT appointment.appointmentId FROM appointment " 
                          + "WHERE appointment.customerId = " + y + " AND appointment.start > NOW();";
            if(!utils.spawn(checkx, x -> utils.noResults(x))) 
                utils.spawn("This customer still has appointments scheduled!", x -> utils.errorbox(x));
            else { String delx = "UPDATE customer SET active = 0 WHERE customerId = " + y + ";";
                   utils.SQLRUN(delx, x -> utils.update(x));
        }   }   
        try{handleSearch(); } catch(Exception e) {/*ignore*/}
    }
    
    @FXML void handleBack() { utils.closeCurrentWindow(backButton); }
}
