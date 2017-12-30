package dbappdemo;

import javafx.beans.property.SimpleStringProperty;

public class AppointmentSearchResult {
    SimpleStringProperty title, desc, start, end;
    public AppointmentSearchResult(String t, String d, String s, String e)
    {   title = new SimpleStringProperty(t);
        desc  = new SimpleStringProperty(d);
        start = new SimpleStringProperty(s);
        end   = new SimpleStringProperty(e);
    }
    public String gettitle() { return title.get(); }
    public String getdesc()  { return desc .get(); }
    public String getstart() { return start.get(); }
    public String getend()   { return end  .get(); }

    public void settitle(String x) { title.set(x); }
    public void setdesc (String x) { desc .set(x); }
    public void setstart(String x) { start.set(x); }
    public void setend  (String x) { end  .set(x); }

    public SimpleStringProperty titleProperty() { return title; }
    public SimpleStringProperty descProperty () { return desc;  }
    public SimpleStringProperty startProperty() { return start; }
    public SimpleStringProperty endProperty  () { return end;   }
}
