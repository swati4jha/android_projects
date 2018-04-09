package com.mad.group24_hw07;

/**
 * Created by mihai on 2/27/17.
 */

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.ocpsoft.prettytime.PrettyTime;

/*
Usage:
dm.saveNote(new Note("Note 1", "Note 1 text", (new Date()).toString(), "Completed"));
    dm.saveNote(new Note("Note 2", "Note 2 text", "Mon Feb 27 19:17:13 EST 2017", "Pending"));
    dm.saveNote(new Note("Note 3", "Note 3 text", "Mon Feb 27 19:17:11 EST 2017", "Completed"));

    List<Note> notes = dm.getAllNotes();
    Log.d("demo", notes.toString());
    List<Note> notesCompleted = dm.getAllCompleted();
    Log.d("demo", notesCompleted.toString());
    Log.d("demo", "time from now: "+Ptime.PtimeFormatter.getPtimeFrom(dm.getNote(Long.parseLong("1")).getTime()));
    Log.d("demo", "time from now millis: "+Ptime.PtimeFormatter.getPTimeMillis(dm.getNote(Long.parseLong("2")).getTime()));
 */

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

        //"EEE, d MMM yyyy HH:mm:ss Z"	          is for:            Wed, 4 Jul 2001 12:08:56 -0700

        public static String getPtimeFrom(String t){
            PrettyTime p = new PrettyTime();
            DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
            try {
                Date date = (Date)formatter.parse(t);
                Log.d("demo", "time from now: "+p.format(date));
                return p.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //Log.d("demo", "time now: "+p.format(date));
            return null;
        }

        public static String formatDate(String dateParser){

            SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
            Date date = null;
            try {
                date = format.parse(dateParser.trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat df = new SimpleDateFormat("EEE, d, MMM yyyy");
            String newDate = df.format(date);
            return newDate;
        }

        public static String getPTimeMillis(String t){
            PrettyTime p = new PrettyTime();
            String currMilis = String.valueOf(System.currentTimeMillis());

            DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
            try {
                Date aTime  = formatter.parse(t);
                Log.d("demo", "time millis: "+aTime.getTime());
                return String.valueOf(Long.parseLong(currMilis) - aTime.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
