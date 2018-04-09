package com.mad.inclass05;

import android.util.Log;

import com.mad.inclass05.News;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by swati on 2/13/2017.
 */

public class NewsUtility {

    static ArrayList<News> parseNews(InputStream in) throws XmlPullParserException{
        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setInput(in,"UTF-8");
        News news = null;
        ArrayList<News> newsList = new ArrayList<News>();
        int event = parser.getEventType();
        boolean itemFound = false;
        while(event!=XmlPullParser.END_DOCUMENT){

            switch (event){
                case XmlPullParser.START_TAG:
                    if(parser.getName().equals("item")){
                        news = new News();
                        itemFound = true;

                    }
                    else if(parser.getName().equals("title") && itemFound){
                        try {

                            //if(parser.nextText()!=null){
                                news.setTitle(parser.nextText().trim());
                            //}

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(parser.getName().equals("pubdate") && itemFound){
                        try {
                            //if(parser.nextText()!=null){
                                //news.setPubDate(parser.nextText().trim());
                            //}
                            Log.d("Date",parser.nextText());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(parser.getName().equals("description") && itemFound){
                        try {
                            //if(parser.nextText()!=null){
                                news.setDescription(parser.nextText().trim());
                            //}

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(parser.getName().equals("media:content") && itemFound){
                        try {
                            parser.next();

                            String height = parser.getAttributeValue(null, "height");
                            String width = parser.getAttributeValue(null, "width");
                            if(height.equals(width)){
                                news.setImageURL(parser.getAttributeValue(null, "url"));
                                Log.d("media count -->", parser.getAttributeValue(null, "url"));
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                case XmlPullParser.END_TAG:
                    if(parser.getName().equals("item")){
                        newsList.add(news);
                        news = null;
                    }
                    break;

                default:
                    break;
            }

            try {
                event = parser.next();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return newsList;
    }


}
