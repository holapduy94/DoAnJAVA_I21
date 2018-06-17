package nhom7.uit.com.moviereview.utils;

import java.util.Calendar;

/**
 * Created by phuocthang on 10/20/2017.
 */

public class FormatStringUrl {
    public static String SpaceTo(String query){
        String result = query.replace(" ", "%20");
        return result;
    }

    public static String getCurrentTime(){
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH) + 1;
        String result = "Last update: " + hour + ":" + minute + " " + day + "/" + month;
        return result;

    }

    public static  String getCurrentDateV2(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        String date = day + "-" + month + "-" + year;
        return date;
    }
}
