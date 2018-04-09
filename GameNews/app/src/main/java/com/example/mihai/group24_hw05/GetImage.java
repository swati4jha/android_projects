package com.example.mihai.group24_hw05;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mihai on 2/16/17.
 */

public class GetImage extends AsyncTask<RequestParameters, Void, Bitmap> {
    InputStream in=null;
    GetImageInterface activity;

    public GetImage(GetImageInterface activity) {
        this.activity=activity;
    }

    @Override
    protected Bitmap doInBackground(RequestParameters... params) {
        try {
            URL url = new URL(params[0].getEncodedURL());
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
            activity.setImageDetails(result);
        }
    }

    static public interface GetImageInterface{
        public void setImageDetails(Bitmap result);
        public Context getContext();
    }
}

