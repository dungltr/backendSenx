package draft.query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static draft.utils.number.isInteger;

public class formQuery {
    String Query;
    String Area;
    String Day;
    String Date0;
    String Date1;
    String Date2;
    static String dateForm = "yyyy_MM_dd_HH_mm_ss";

    public formQuery (String Query, String Area, String Date1, String Date2) {
        this.Query = Query;
        this.Area = Area;
        this.Date0 = Date2;
        this.Date1 = Date1;
        this.Date2 = Date2;

        switch (Query){
            case "1":
                if (Date1.contains("_")){
                    this.Day = subDate(Date1, Date2, TimeUnit.DAYS);
                }
                else this.Day = Date1;
                break;
            case "2":
                if (isInteger(Date1)){
                    this.Day = Date1;
                }
                else {
                    this.Day = subDate(Date1, Date2, TimeUnit.DAYS);
                }
                break;
            default:
                break;
        }
    }
    public String get_Area () {return Area;}
    public String get_Query () {return Query;}
    public String get_Day () {return Day;}
    public String get_Date () {return Date0;}

    public String get_Date1 () {return Date1;}
    public String get_Date2 () {return Date2;}

    public void set_Area (String Area) {this.Area = Area;}
    public void set_Query (String Query) {this.Query = Query;}
    public void set_Day (String Day) {this.Day = Day;}
    public void set_Date (String Date) {this.Date0 = Date;}

    public void set_Date1 (String Day) {this.Date1 = Day;}
    public void set_Date2 (String Date) {this.Date2 = Date;}

    public static String subDate(String Date1, String Date2, TimeUnit timeUnit) {
        DateFormat dateFormat = new SimpleDateFormat(dateForm);//Dung edit, old is HH:mm:ss
        Date dateStart = null;
        Date dateEnd = null;
        try {
            dateStart = new SimpleDateFormat(dateForm).parse(Date1);
            dateEnd = new SimpleDateFormat(dateForm).parse(Date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int day = (int) getDateDiff(dateStart,dateEnd, TimeUnit.DAYS);
        //int a = (int) timeUnit.convert(diffInMillies,TimeUnit.DAYS);
        return  Integer.toString(day);
    }
    public static String subDay(String Day, String Date2) {
        DateFormat dateFormat = new SimpleDateFormat(dateForm);//Dung edit, old is HH:mm:ss
        Date dateStart = null;
        Date dateEnd = null;

        try {
            dateEnd = new SimpleDateFormat(dateForm).parse(Date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Instant end = dateEnd.toInstant();
        Instant start = end.minus(Duration.ofDays(Integer.parseInt(Day)));
        dateStart = Date.from(start);
        return dateStart.toString();
    }
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
    /*
    public static Date getDate1(Day day, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        Date date1 = new SimpleDateFormat()
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }
    */
}
