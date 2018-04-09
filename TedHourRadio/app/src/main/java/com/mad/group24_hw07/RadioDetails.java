package com.mad.group24_hw07;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * authors: Swati Jha, Mihai Mehedint
 * assignment: HW7
 * file name: RadioDetails.java
 */

public class RadioDetails implements Comparable, Parcelable{
    String imageURL;
    String title;
    String pubDate;
    String description;
    String duration;
    String playURL;
    Long timeToCompare;

    public RadioDetails(){}

    public Long getTimeToCompare() {
        return timeToCompare;
    }

    public void setTimeToCompare(Long timeToCompare) {
        this.timeToCompare = timeToCompare;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPlayURL() {
        return playURL;
    }

    public void setPlayURL(String playURL) {
        this.playURL = playURL;
    }

    @Override
    public int compareTo(Object o) {
        if(o==null){
            return -1;
        }else{
            if (this.getTimeToCompare()<((((RadioDetails)o).getTimeToCompare()))){
                return -1;
            }else if(this.getTimeToCompare()>((((RadioDetails)o).getTimeToCompare()))){
                return 1;
            }else if(this.getTimeToCompare()==((((RadioDetails)o).getTimeToCompare()))){
                return 0;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return "RadioDetails{" +
                "imageURL='" + imageURL + '\'' +
                ", title='" + title + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", description='" + description + '\'' +
                ", duration='" + duration + '\'' +
                ", playURL='" + playURL + '\'' +
                ", timeToCompare='" + timeToCompare + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(imageURL);
        dest.writeString(title);
        dest.writeString(pubDate);
        dest.writeString(description);
        dest.writeString(duration);
        dest.writeString(playURL);
        dest.writeLong(timeToCompare);

    }


    public RadioDetails(Parcel in) {
        this.imageURL = in.readString();
        this.title = in.readString();
        this.pubDate = in.readString();
        this.description = in.readString();
        this.duration = in.readString();
        this.playURL = in.readString();
        this.timeToCompare = in.readLong();
        //this.choice = in.readList(choice, String.class.getClassLoader());
        //this.choice = in.createStringArrayList();

    }

    public static final Parcelable.Creator<RadioDetails> CREATOR
            = new Parcelable.Creator<RadioDetails>() {
        public RadioDetails createFromParcel(Parcel in) {
            return new RadioDetails(in);
        }

        public RadioDetails[] newArray(int size) {
            return new RadioDetails[size];
        }
    };
}
