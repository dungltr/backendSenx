package draft.query;

public class formQuery2 {
    String Query;
    String Area;
    String Date1;
    String Date2;


    public formQuery2(String Query, String Area, String Date1, String Date2) {
        this.Query = Query;
        this.Area = Area;
        this.Date1 = Date1;
        this.Date2 = Date2;
    }
    public String get_Area () {return Area;}
    public String get_Query () {return Query;}
    public String get_Date1 () {return Date1;}
    public String get_Date2 () {return Date2;}

    public void set_Area (String Area) {this.Area = Area;}
    public void set_Query (String Query) {this.Query = Query;}
    public void set_Date1 (String Date1) {this.Date1 = Date1;}
    public void set_Date2 (String Date2) {this.Date2 = Date2;}
}
