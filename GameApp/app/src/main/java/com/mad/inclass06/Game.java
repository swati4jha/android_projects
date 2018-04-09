package com.mad.inclass06;

/**
 * Created by mihai on 2/15/17.
 */

public class Game {
/**
 * Game>
 <id>90</id>
 <GameTitle>Halo 3</GameTitle>
 <ReleaseDate>09/25/2007</ReleaseDate>
 <Platform>Microsoft Xbox 360</Platform>
 </Game>
 */

    String id, title, date, platform,image;
    Detail detail;
    public Game(){

    }

    public Game(String id, String title, String date, String platform,String image, Detail detail) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.platform = platform;
        this.image = image;
        this.detail = detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", platform='" + platform + '\'' +
                ", image='" + image + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
