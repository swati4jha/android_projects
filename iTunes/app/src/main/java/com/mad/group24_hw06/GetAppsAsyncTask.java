package com.mad.group24_hw06;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * authors: Mihai Mehedint, Swati Jha
 * assignment: HW6
 * file name: AppListAdapter.java
 */

public class GetAppsAsyncTask extends AsyncTask<String, Void, ArrayList<App>>{
    GameDetails activity;

    public GetAppsAsyncTask(GameDetails activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<App> doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);
            HttpURLConnection con= (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statusCode = con.getResponseCode();
            if(statusCode==HttpURLConnection.HTTP_OK){
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = reader.readLine();
                while (line!=null){
                    sb.append(line);
                    line = reader.readLine();
                }
                return AppsUtil.AppsJSONParser.parseApps(sb.toString());

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<App> apps) {
        super.onPostExecute(apps);
        if(apps!=null){
            activity.setGameDetails(apps);
            //Log.d("demo", apps.toString());
        }
    }



    static public interface GameDetails{
        public void setGameDetails(ArrayList<App> details);

        public Context getContext();

        boolean onMenuItemClick(MenuItem item);
    }

}
