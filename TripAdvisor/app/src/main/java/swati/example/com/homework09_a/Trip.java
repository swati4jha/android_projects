package swati.example.com.homework09_a;


import java.util.ArrayList;

/**
 * Created by swati on 4/20/2017.
 */

public class Trip {
    private String id;
    private String title;
    private String location;
    private String createdBy;
    private String createdByName;
    private String photo;
    private ArrayList<Chat> chatList;
    private ArrayList<PlaceLocal> placeList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public ArrayList<Chat> getChatList() {
        return chatList;
    }

    public void setChatList(ArrayList<Chat> chatList) {
        this.chatList = chatList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public ArrayList<PlaceLocal> getPlaceList() {
        return placeList;
    }

    public void setPlaceList(ArrayList<PlaceLocal> placeList) {
        this.placeList = placeList;
    }
}
