package swati.example.com.inclass10;

/**
 * Created by mihai on 2/27/17.
 */

import android.util.Log;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ptime {
    static public class PtimeFormatter {
        public static String getPtime(){
        PrettyTime p = new PrettyTime();
        Log.d("demo", "time now: "+new Date());
            //getPtimeFrom("Mon Feb 27 19:17:13 EST 2017");
        return p.format(new Date());
        //prints: “moments from now”

        //System.out.println(p.format(new Date(System.currentTimeMillis() + 1000 * 60 * 10)));
        //prints: “10 minutes from now”
        }

        public static String getPtimeFrom(String t){
            PrettyTime p = new PrettyTime();
            DateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
            try {
                Date date = (Date)formatter.parse(t);
                //Log.d("demo", "time from now: "+p.format(date));
                return p.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //Log.d("demo", "time now: "+p.format(date));
            return null;
        }

        public static String getPTimeMillis(String t){
            PrettyTime p = new PrettyTime();
            String currMilis = String.valueOf(System.currentTimeMillis());

            DateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
            try {
                Date aTime  = formatter.parse(t);
                //Log.d("demo", "time millis: "+aTime.getTime());
                return String.valueOf(Long.parseLong(currMilis) - aTime.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
