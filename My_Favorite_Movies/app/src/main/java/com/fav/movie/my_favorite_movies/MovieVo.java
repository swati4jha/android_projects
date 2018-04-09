/**
 * Assignment-2
 * Name: Swati Jha, Priyank Verma
 **/

package com.fav.movie.my_favorite_movies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

/**
 * Created by swati on 1/28/2017.
 */

public class MovieVo implements Parcelable {
    String name;
    String description;
    String genre;
    int rating;
    int year;
    String imdbLink;


    public MovieVo(){

    }

    public MovieVo(String name, String description, String genre, int rating, int year, String imdbLink) {
        super();
        this.name = name;
        this.description = description;
        this.genre = genre;
        this.rating = rating;
        this.year = year;
        this.imdbLink = imdbLink;
    }

    private MovieVo(Parcel in) {
        name = in.readString();
        description = in.readString();
        genre = in.readString();
        rating = in.readInt();
        year = in.readInt();
        imdbLink = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(genre);
        dest.writeInt(rating);
        dest.writeInt(year);
        dest.writeString(imdbLink);

    }

    public static final Parcelable.Creator<MovieVo> CREATOR = new Parcelable.Creator<MovieVo>() {
        public MovieVo createFromParcel(Parcel in) {
            return new MovieVo(in);
        }

        public MovieVo[] newArray(int size) {
            return new MovieVo[size];

        }
    };

    @Override
    public String toString() {
        return name;
    }

    public static Comparator<MovieVo> RateComparator = new Comparator<MovieVo>() {

        public int compare(MovieVo m1, MovieVo m2) {
            int rate1 = m1.rating;
            int rate2 = m2.rating;

            //descending order
            return rate2-rate1;


        }};

    /*Comparator for sorting the list by year*/
    public static Comparator<MovieVo> YearComparator = new Comparator<MovieVo>() {

        public int compare(MovieVo m1, MovieVo m2) {
            int year1 = m1.year;
            int year2 = m2.year;

            //ascending order
            return year1-year2;


        }};
}
