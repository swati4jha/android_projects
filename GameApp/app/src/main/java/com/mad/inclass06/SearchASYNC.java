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
import java.util.ArrayList;

/**
 * Created by mihai on 2/15/17.
 */

public class SearchASYNC extends AsyncTask<RequestParameters, Void, ArrayList<Game>> {
    GameData activity;


    public SearchASYNC(GameData activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected ArrayList<Game> doInBackground(RequestParameters... params) {

        ArrayList<Game> list = new ArrayList<>();
        try {
            URL url = new URL(params[0].getEncodedURL());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            Log.d("demo", (new URL(params[0].getEncodedURL())).toString());
            con.connect();
            int statusCode = con.getResponseCode();
            if (statusCode==HttpURLConnection.HTTP_OK){
                InputStream in = con.getInputStream();
                return GameUtil.GamePullParser.parseGames(in);
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
    protected void onPostExecute(ArrayList<Game> games) {
        super.onPostExecute(games);
        activity.setGameData(games);
    }

    static public interface GameData{
        public void setGameData(ArrayList<Game> list);
        public Context getContext();
    }

}
