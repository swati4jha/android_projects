package com.example.mihai.group24_hw05;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mihai on 2/16/17.
 */
/**
 * authors: Mihai Mehedint, Swati Jha
 * assignment: HW4
 * file name: MainActivity.java
 */


public class Detail implements Parcelable{
    String title;
    String id;
    String overview;
    String genre;
    String publisher;
    String youtube;
    String baseUrl;
    String thumb;
    String date;
    String platform;

    List<String> similar = new ArrayList<>();

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }


    public List<String> getSimilar() {
        return similar;
    }

    public void setSimilar(ArrayList<String> similar) {
        this.similar = similar;
    }

    public Detail() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //HashMap<String, String> similar = new HashMap<>();
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.id);
        dest.writeString(this.overview);
        dest.writeString(this.genre);
        dest.writeString(this.publisher);
        dest.writeString(this.youtube);
        dest.writeString(this.baseUrl);
        dest.writeString(this.thumb);
        dest.writeString(this.date);
        dest.writeString(this.platform);
        dest.writeStringList(this.similar);

    }

    public static final Parcelable.Creator<Detail> CREATOR
            = new Parcelable.Creator<Detail>() {
        public Detail createFromParcel(Parcel in) {
            return new Detail(in);
        }

        public Detail[] newArray(int size) {
            return new Detail[size];
        }
    };

    private Detail(Parcel in) {

        this.title=in.readString();
        this.id=in.readString();
        this.overview=in.readString();
        this.genre=in.readString();
        this.publisher=in.readString();
        this.youtube=in.readString();
        this.baseUrl=in.readString();
        this.thumb=in.readString();
        this.date=in.readString();
        this.platform=in.readString();
        in.readStringList(this.similar);
        //this.similar=in.readArrayList(new ClassLoader() {

        //});
    }

    @Override
    public String toString() {
        return "Detail{" +
                "title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", overview='" + overview + '\'' +
                ", genre='" + genre + '\'' +
                ", publisher='" + publisher + '\'' +
                ", youtube='" + youtube + '\'' +
                ", baseUrl='" + baseUrl + '\'' +
                ", thumb='" + thumb + '\'' +
                ", date='" + date + '\'' +
                ", platform='" + platform + '\'' +
                ", similar=" + similar +
                '}';
    }
}
