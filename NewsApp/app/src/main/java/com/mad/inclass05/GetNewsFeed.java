package com.mad.inclass05;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by swati on 2/13/2017.
 */

public class GetNewsFeed extends AsyncTask<String, Integer, ArrayList<News>> {
    MainActivity activity;
    ProgressDialog pb;
    //ProgressBar pb;


    public GetNewsFeed(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        pb = new ProgressDialog((activity.findViewById(R.id.linear_layout)).getContext());

        pb.setCancelable(false);
        pb.setTitle("Loading News");

        pb.setMax(100);
        pb.setProgress(0);
        pb.setIndeterminate(false);
        pb.show();

    }



    @Override
    protected ArrayList<News> doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int statusCode = con.getResponseCode();

            if(statusCode==HttpURLConnection.HTTP_OK){
                /*BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line =reader.readLine();
                StringBuilder sb = new StringBuilder();
                int i=0;
                while(line!=null){
                    i++;
                    sb.append(line);
                    line=reader.readLine();
                    for(int j=0;j<10000000;j++){}
                    publishProgress(i);
                }*/

                InputStream in = con.getInputStream();
                //return null;
                return NewsUtility.parseNews(in);

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        pb.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<News> news) {
        super.onPostExecute(news);
        //pb.setVisibility(View.GONE);
        //pb.setVisibility(View.INVISIBLE);
        pb.dismiss();
        activity.setupData(news);
        Log.d("Data", news.size()+"");




        //Log.d("demo", "\nQuestions AFTER parsing: \n"+sb.toString());
    }

    static public interface NewsData{
        public void setupData(ArrayList<News> news);
        public Context getContext();
    }
}
