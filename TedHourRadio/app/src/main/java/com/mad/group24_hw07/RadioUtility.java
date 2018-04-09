package com.mad.group24_hw07;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * authors: Swati Jha, Mihai Mehedint
 * assignment: HW7
 * file name: RadioUtility.java
 */

public class RadioUtility {

    static ArrayList<RadioDetails> parseData(InputStream in) throws XmlPullParserException {
        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setInput(in,"UTF-8");
        RadioDetails radioDetails = null;
        ArrayList<RadioDetails> radioDetailsList = new ArrayList<RadioDetails>();
        int event = parser.getEventType();
        boolean itemFound = false;
        while(event!=XmlPullParser.END_DOCUMENT){

            switch (event){
                case XmlPullParser.START_TAG:
                    if(parser.getName().equals("item")){
                        radioDetails = new RadioDetails();
                        itemFound = true;

                    }
                    else if(parser.getName().equals("title") && itemFound){
                        try {
                            radioDetails.setTitle(parser.nextText().trim());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(parser.getName().equals("pubDate") && itemFound){
                        try {

                            String date = parser.nextText();
                            Log.d("demo", date);
                            radioDetails.setPubDate(Ptime.PtimeFormatter.formatDate(date));
                            Log.d("demo", radioDetails.getPubDate());
                            radioDetails.setTimeToCompare(Long.parseLong(Ptime.PtimeFormatter.getPTimeMillis(date)));
                            Log.d("demo", radioDetails.getTimeToCompare()+"");


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(parser.getName().equals("description") && itemFound){
                        try {
                            radioDetails.setDescription(parser.nextText().trim());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(parser.getName().equals("itunes:image") && itemFound){
                        try {
                            parser.next();
                            String image = parser.getAttributeValue(null, "href");
                            radioDetails.setImageURL(image);
                            Log.d("Image",image);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }else if(parser.getName().equals("itunes:duration") && itemFound){
                        try {

                            radioDetails.setDuration(parser.nextText().trim());
                            if (radioDetails.getDuration()!=null){
                                Log.d("Duration: ",radioDetails.getDuration());
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    else if(parser.getName().equals("enclosure") && itemFound){
                        try {
                            parser.next();
                            String videoURL = parser.getAttributeValue(null, "url");
                            radioDetails.setPlayURL(videoURL);
                            Log.d("videoURL",videoURL);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                case XmlPullParser.END_TAG:
                    if(parser.getName().equals("item")){
                        radioDetailsList.add(radioDetails);
                        radioDetails = null;
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

        return radioDetailsList;
    }


}