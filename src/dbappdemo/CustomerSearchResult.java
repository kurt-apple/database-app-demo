package dbappdemo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CustomerSearchResult {
    SimpleStringProperty  customerName     = new SimpleStringProperty ();
    SimpleStringProperty  customerAddress1 = new SimpleStringProperty ();
    SimpleIntegerProperty customerId       = new SimpleIntegerProperty();
    SimpleIntegerProperty customerActive   = new SimpleIntegerProperty();
    
    public CustomerSearchResult(int IdNumber, String name, String address1, int activebit) {
        customerName    .set(name     );
        customerAddress1.set(address1 );
        customerId      .set(IdNumber );
        customerActive  .set(activebit);
    }
    
    public int    getcustomerId()       { return        customerId.get(); }
    public String getcustomerName()     { return      customerName.get(); }
    public int    getcustomerActive()   { return    customerActive.get(); }
    public String getcustomerAddress1() { return  customerAddress1.get(); }
    public void   setcustomerId      (int    x){       customerId.set(x); }
    public void   setcustomerName    (String x){     customerName.set(x); }
    public void   setcustomerActive  (int    x){   customerActive.set(x); }
    public void   setcustomerAddress1(String x){ customerAddress1.set(x); }
    public SimpleStringProperty  customerNameProperty    () { return     customerName; }
    public SimpleStringProperty  customerAddress1Property() { return customerAddress1; }
    public SimpleIntegerProperty customerIdProperty      () { return       customerId; }
    public SimpleIntegerProperty customerActiveProperty  () { return   customerActive; }
}
