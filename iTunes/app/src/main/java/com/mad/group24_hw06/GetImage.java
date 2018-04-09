package com.mad.group24_hw06;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
/**
 * authors: Mihai Mehedint, Swati Jha
 * assignment: HW6
 * file name: AppListAdapter.java
 */

public class GetImage extends AsyncTask<String, Void, Bitmap> {
    App app;


    public GetImage(App app) {
        this.app = app;
    }

    InputStream in=null;
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
            Log.d("demo", "Image results: "+result.toString());
            super.onPostExecute(result);
            //app.setImage(result);
            app.setGameImage(result);
        }

    }


    static public interface GameImage{
        public void setGameImage(Bitmap result);
        public Context getContext();
    }
}

