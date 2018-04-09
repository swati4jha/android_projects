package com.mad.group24_hw07;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mihai on 2/12/17.
 */

public class GetImage extends AsyncTask<String, Void, Bitmap> {
    PlayActivity activity;
    ProgressDialog pb;
    InputStream in=null;

    public GetImage(PlayActivity activity) {
        this.activity = activity;
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            in = con.getInputStream();
            Bitmap image= BitmapFactory.decodeStream(in);
            return image;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if(result!=null){
            activity.setupData(result);
            //iv.setImageBitmap(result);
        }

    }


    static public interface ImageData{
        public void setupData(Bitmap result);
        public Context getContext();
    }
}

