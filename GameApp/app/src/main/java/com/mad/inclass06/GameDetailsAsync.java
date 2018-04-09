package com.mad.inclass06;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mihai on 2/15/17.
 */

public class GameDetailsAsync extends AsyncTask<RequestParameters, Void, Detail> {
    GameDetails activity;


    public GameDetailsAsync(GameDetails activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Detail doInBackground(RequestParameters... params) {

        //ArrayList<Game> list = new ArrayList<>();
        try {
            URL url = new URL(params[0].getEncodedURL());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            Log.d("demo", "Details URL: "+(new URL(params[0].getEncodedURL())).toString());
            con.connect();
            int statusCode = con.getResponseCode();
            Log.d("demo", "Response code: "+String.valueOf(statusCode)+HttpURLConnection.HTTP_OK);
            if (statusCode==HttpURLConnection.HTTP_OK){
                InputStream in = con.getInputStream();
                return GameDetailsUtil.DetailsPullParser.parseDetail(in);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        Log.d("demo", "Parsing did not work");
        return null;
    }

    @Override
    protected void onPostExecute(Detail details) {
        super.onPostExecute(details);
        activity.setGameDetails(details);
    }

    static public interface GameDetails{
        public void setGameDetails(Detail details);
        public Context getContext();
    }

}
