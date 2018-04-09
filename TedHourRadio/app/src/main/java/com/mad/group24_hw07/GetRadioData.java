package com.mad.group24_hw07;

import android.app.ProgressDialog;
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
 * authors: Swati Jha, Mihai Mehedint
 * assignment: HW7
 * file name: GetRadioData.java
 */

public class GetRadioData extends AsyncTask<String, Integer, ArrayList<RadioDetails>> {
    MainActivity activity;
    ProgressDialog pb;
    //ProgressBar pb;


    public GetRadioData(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        pb = new ProgressDialog((activity.findViewById(R.id.linear_layout)).getContext());
        pb.setCancelable(false);
        pb.setTitle("Loading Episodes...");

        pb.show();
    }



    @Override
    protected ArrayList<RadioDetails> doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int statusCode = con.getResponseCode();

            if(statusCode==HttpURLConnection.HTTP_OK){
                InputStream in = con.getInputStream();
                //return null;
                return RadioUtility.parseData(in);

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
    protected void onPostExecute(ArrayList<RadioDetails> radio) {
        super.onPostExecute(radio);
        pb.dismiss();
        activity.setupData(radio);
        Log.d("Data", radio.size()+"");




        //Log.d("demo", "\nQuestions AFTER parsing: \n"+sb.toString());
    }

    static public interface RadioData{
        public void setupData(ArrayList<RadioDetails> news);
        public Context getContext();
    }
}
