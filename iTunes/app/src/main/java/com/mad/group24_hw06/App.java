package com.mad.group24_hw06;

import android.content.Context;
import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/**
 * authors: Mihai Mehedint, Swati Jha
 * assignment: HW6
 * file name: AppListAdapter.java
 */

public class App implements GetImage.GameImage, Comparable<App>{
    String name, price, rights,
            imageUrl, favorite;
    Bitmap image;


    public static App createApp(JSONObject js){
        App app = new App();
        try {

            app.setName(((JSONObject)(js.get("im:name"))).getString("label"));
            JSONObject attr = (JSONObject) (((JSONObject)(js.get("im:price"))).get("attributes"));
            if(attr.getString("amount")!=null) {
                app.setPrice(attr.getString("currency") + " " + String.valueOf(attr.getDouble("amount")));
            }else
            {
                app.setPrice("XXX");
            }
            app.setRights(((JSONObject)(js.get("im:artist"))).getString("label"));
            //app.setRights((((JSONObject)(((JSONObject)(js.get("im:price"))).getString("attributes"))).getString("label")));
            app.setImageUrl(((JSONObject)((JSONArray)(js.get("im:image"))).get(0)).getString("label"));
            //new GetImage(app).execute(app.getImageUrl());
            //app.setFavorite(js.getString(""));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return app;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }


    @Override
    public String toString() {
        return "App{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", rights='" + rights + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                //", favorite='" + favorite + '\'' +
                ", image=" + image +
                '}';
    }

    @Override
    public void setGameImage(Bitmap result) {
        this.setImage(result);
    }

    @Override
    public Context getContext() {
        return this.getContext();
    }

    @Override
    public int compareTo(App o) {
        if(null == o || null == o.getName()) {
            return -1;
        } else {
            return this.name.compareTo((o.name));
        }
    }

    @Override
    public boolean equals(Object obj) {

        if(null == obj) {
            return false;
        }

        if(!App.class.equals(obj.getClass())) {
            return false;
        }

        App app = (App) obj;
        if(isEqual(this.toString(), app.toString())) {
            return true;
        }

        return false;
    }

    boolean isEqual(String one, String two) {

        if(null == one && null == two) {
            return true;
        }

        if(null!= one && one.equals(two)) {
            return true;
        }

        return false;
    }
}
