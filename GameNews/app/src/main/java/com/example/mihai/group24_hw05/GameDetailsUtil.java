package com.example.mihai.group24_hw05;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mihai on 2/16/17.
 */
/**
 * authors: Mihai Mehedint, Swati Jha
 * assignment: HW4
 * file name: MainActivity.java
 */



public class GameDetailsUtil {
    static public class DetailsPullParser{

        static Detail parseDetail(InputStream in) throws XmlPullParserException, IOException {
            int count;
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            //Log.d("demo", "input stream: "+in);
            parser.setInput(in, "UTF-8");
            Detail game=null;
            int event= parser.getEventType();

            /*
<Game>
<id>2</id>
<GameTitle>Crysis</GameTitle>
<PlatformId>1</PlatformId>
<Platform>PC</Platform>
<ReleaseDate>11/13/2007</ReleaseDate>
<Overview>...</Overview>
<ESRB>M - Mature</ESRB>
<Genres>
<genre>Shooter</genre>
</Genres>
<Players>4+</Players>
<Co-op>No</Co-op>
<Youtube>http://www.youtube.com/watch?v=i3vO01xQ-DM</Youtube>
<Publisher>Electronic Arts</Publisher>
<Developer>Crytek</Developer>
<Rating>7.3077</Rating>
<Similar>...</Similar>
<Images>...</Images>
</Game>
 */

            while(event!=XmlPullParser.END_DOCUMENT){
                switch (event){
                    case XmlPullParser.START_TAG:
                        //Log.d("demo", "Parser getName is: "+ parser.getName());
                            if (parser.getName().equals("baseImgUrl") && game==null) {
                                game = new Detail();
                                if(game !=null) {
                                    game.setBaseUrl(parser.nextText().trim());
                                }
                            }else if (parser.getName().equals("Game") && game==null) {
                                game = new Detail();
                            } else if (parser.getName().equals("id")) {
                                if(game !=null) {
                                    String currentId = parser.nextText().trim();
                                    Log.d("demo", "Current id is: "+ currentId);
                                    if(game.getId()==null && game.getSimilar().size()==0) {

                                        game.setId(currentId);
                                    }else if (game.getId()!=null && !game.getId().equals(currentId)){
                                        if (!game.getSimilar().contains(currentId)) {
                                            game.getSimilar().add(currentId);
                                        }
                                    }
                                }
                            } else if (parser.getName().equals("GameTitle") && game.getTitle()==null) {
                                if(game !=null) {
                                    game.setTitle(parser.nextText().trim());
                                }
                            } else if (parser.getName().equals("Overview") && game.getOverview()==null) {
                                if(game !=null) {
                                    game.setOverview(parser.nextText().trim());
                                }
                            } else if (parser.getName().equals("Genres") && game.getGenre()==null) {
                                if(game !=null) {
                                    parser.next();
                                    if (parser.getName().equals("genre")) {
                                        game.setGenre(parser.nextText().trim());
                                    }
                                }
                            }else if (parser.getName().equals("Publisher") && game.getPublisher()==null) {
                                if(game !=null) {
                                    game.setPublisher(parser.nextText().trim());
                                }
                            }else if (parser.getName().equals("Youtube") && game.getYoutube()==null) {
                                if(game !=null) {
                                    game.setYoutube(parser.nextText().trim());
                                }
                            }else if (parser.getName().equals("thumb") && game.getThumb()==null) {
                                if(game !=null) {
                                    game.setThumb(parser.nextText().trim());
                                }
                            }else if (parser.getName().equals("ReleaseDate")) {
                                SimpleDateFormat format = new SimpleDateFormat("mm/dd/yyyy");
                                Date date = null;
                                try {
                                    date = format.parse(parser.nextText().trim());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                SimpleDateFormat df = new SimpleDateFormat("yyyy");
                                String year = df.format(date);
                                game.setDate(year);
                            }else if (parser.getName().equals("Platform")) {
                                game.setPlatform(parser.nextText().trim());
                            }else if (parser.getName().equals("Similar") && game.getSimilar().size()==0) {
                                parser.next();
                                if (parser.getName().equals("SimilarCount")) {
                                    count = Integer.parseInt(parser.nextText());
                                }
                            }


                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("thumb")){
                            return game;
                            //
                        }
                        break;
                    default:
                        break;
                }


                event=parser.next();
            }


            return null;

        }
    }
}
