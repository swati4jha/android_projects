package com.example.mihai.group24_hw05;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mihai on 2/15/17.
 */
/**
 * authors: Mihai Mehedint, Swati Jha
 * assignment: HW4
 * file name: MainActivity.java
 */



public class GameUtil {
    static public class GamePullParser{

        static ArrayList<Game> parseGames(InputStream in) throws XmlPullParserException, IOException {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            //Log.d("demo", "input stream: "+in);
            parser.setInput(in, "UTF-8");
            ArrayList<Game> list = new ArrayList<>();
            Game game=null;
            int event= parser.getEventType();

            while(event!=XmlPullParser.END_DOCUMENT){
                switch (event){
                    case XmlPullParser.START_TAG:
                        //Log.d("demo", "Parser getName is: "+ parser.getName());
                        if (parser.getName()!=null) {

                            if (parser.getName().equals("Game")) {
                                game = new Game();
                            } else if (parser.getName().equals("id")) {
                                game.setId(parser.nextText().trim());
                            } else if (parser.getName().equals("GameTitle")) {
                                game.setTitle(parser.nextText().trim());
                            } else if (parser.getName().equals("ReleaseDate")) {
                                SimpleDateFormat format = new SimpleDateFormat("mm/dd/yyyy");
                                Date date = null;
                                try {
                                    date = format.parse(parser.nextText().trim());
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy");
                                    String year = df.format(date);
                                    game.setDate(year);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            } else if (parser.getName().equals("Platform")) {
                                game.setPlatform(parser.nextText().trim());
                            }
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("Game")){
                            list.add(game);
                            game=null;
                        }
                        break;
                    default:
                        break;
                }
                event=parser.next();
            }


            return list;

        }
    }

}
