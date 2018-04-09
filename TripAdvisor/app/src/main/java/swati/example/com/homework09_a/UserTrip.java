package swati.example.com.homework09_a;

import java.util.ArrayList;

/**
 * Created by swati on 4/21/2017.
 */

public class UserTrip {
    private String id;
    private String userId;
    private String tripId;
    private String userName;
    private ArrayList<Chat> chatList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
