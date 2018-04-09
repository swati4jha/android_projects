package com.mad.group24_hw04;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * authors: Mihai Mehedint, Swati Jha
 * assignment: HW4
 * file name: MainActivity.java
 */


public class Question implements Serializable {

    /*
"questions": [
		{
			"id": "0",
			"text": "Who is the first President of the United States of America?",
			"image": "http://dev.theappsdr.com/apis/trivia_json/photos/georgewashington.png",
			"choices": {
				"choice": [
					"George Washington",
					"Thomas Jefferson",
					"James Monroe",
					"John Adams",
					"Barack Obama",
					"George Bush",
					"Abraham Lincoln",
					"John F. Kennedy"
				],
				"answer": "1"
			}
		}
 */

    private String id, text, imageURL, answer, selectedAns;
    private ArrayList<String> choice =new ArrayList<>();



    public Question(){

    }
    static public Question createQuestion(JSONObject js) throws JSONException {
        Question q = new Question(js.getString("id"), js.getString("text"), js.optString("image"), js.getJSONObject("choices"));
        return q;

    }

    public Question(String id, String text, String imageURL, JSONObject choices) {

        this.id = id;
        this.text = text;
        this.imageURL = imageURL;
        this.selectedAns = "0";
        try {
            JSONArray arrayChoices= choices.getJSONArray("choice");
            //Log.d("demo", "\n\nThis is the arrayChoices: \n"+arrayChoices);
            this.answer = choices.getString("answer");
            for (int i=0;i<arrayChoices.length();i++) {
                //String ch = choice.getJSONObject(i).getString("");
                String ch = arrayChoices.getString(i);
                //Log.d("demo", "\n\nThis is the arrayChoices first choice: \n"+ch);
                if(ch!=null) {
                    this.choice.add(ch);
                }
                else {
                    this.choice.add("");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSelectedAns() {
        return selectedAns;
    }
    public void setSelectedAns(String selectedAns) {
        this.selectedAns = selectedAns;
    }

    public ArrayList<String> getChoice() {
        return choice;
    }

    public void setChoice(ArrayList<String> choice) {
        this.choice = choice;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", answer='" + answer + '\'' +
                ", choice=" + choice +
                '}';
    }

    /*@Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(text);
        dest.writeString(imageURL);
        dest.writeString(answer);
        //dest.writeList(choice);

    }

    public static final Parcelable.Creator<Question> CREATOR
            = new Parcelable.Creator<Question>() {
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }

    };

    private Question(Parcel in) {
        this.id=in.readString();
        this.text=in.readString();
        this.imageURL=in.readString();
        this.answer=in.readString();
        //this.choice=in.readArrayList(String.class.getClassLoader());
    }*/
}
