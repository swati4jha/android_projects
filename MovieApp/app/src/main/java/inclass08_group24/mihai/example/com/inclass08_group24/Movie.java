/**
 * authors: Mihai Mehedint, Brandon Williams
 * assignment: HW2
 * file name: Movie.java
 */

package inclass08_group24.mihai.example.com.inclass08_group24;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mihai on 1/25/17.
 */

public class Movie implements Parcelable, Comparable {
    private String name;
    private String description;
    private String genre;
    private Integer rating;
    private Integer year;
    private String imdb;

    public Movie(String name, String description, String genre, Integer rating, Integer year, String imdb) {
        this.name = name;
        this.description = description;
        this.genre = genre;
        this.rating = rating;
        this.year = year;
        this.imdb = imdb;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(description);
        out.writeString(genre);
        out.writeInt(rating);
        out.writeInt(year);
        out.writeString(imdb);
    }

    public static final Creator<Movie> CREATOR
            = new Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
        this.genre = in.readString();
        this.rating = in.readInt();
        this.year = in.readInt();
        this.imdb = in.readString();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", genre='" + genre + '\'' +
                ", rating='" + rating + '\'' +
                ", year=" + year +
                ", imdb='" + imdb + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof  Movie)) return
                false;
        Movie m = (Movie)obj;
        return m.getYear().equals(this.getYear());
    }

    @Override
    public int hashCode() {
        //int result = rating.hashCode();
        //result = 31 * result + year.hashCode();
        return this.getYear().hashCode();
    }


    @Override
    public int compareTo(Object o) {
        if (((Movie)o).hashCode()<this.hashCode())
                return 1;
        else if (((Movie)o).hashCode()>this.hashCode())
            return -1;
        return 0;
    }
}
