package com.mad.inclass06;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

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

            while(event!=XmlPullParser.END_DOCUMENT && list.size()<2){
                switch (event){
                    case XmlPullParser.START_TAG:
                        //Log.d("demo", "Parser getName is: "+ parser.getName());
                        if (parser.getName()!=null) {

                            if (parser.getName().equals("Game")) {
                                game = new Game();
                            } else if (parser.getName().equals("id")) {
                                String id = parser.nextText().trim();
                                game.setId(id);
                                try {
                                    URL url = new URL("http://thegamesdb.net/api/GetGame.php?id="+id);
                                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                    Log.d("demo", url.toString());
                                    con.connect();
                                    int statusCode = con.getResponseCode();
                                    if (statusCode==HttpURLConnection.HTTP_OK){
                                        InputStream inStream = con.getInputStream();
                                        Detail detail =  GameDetailsUtil.DetailsPullParser.parseDetail(inStream);
                                        game.setDetail(detail);
                                    }
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (XmlPullParserException e) {
                                    e.printStackTrace();
                                }

                            } else if (parser.getName().equals("GameTitle")) {
                                game.setTitle(parser.nextText().trim());
                            } else if (parser.getName().equals("ReleaseDate")) {
                                SimpleDateFormat format = new SimpleDateFormat("mm/dd/yyyy");
                                Date date = null;
                                try {
                                    //date = format.parse(parser.nextText().trim());
                                    //SimpleDateFormat df = new SimpleDateFormat("yyyy");
                                    //String year = df.format(date);
                                    String year = "2014";
                                    game.setDate(year);
                                } catch (Exception e) {
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
