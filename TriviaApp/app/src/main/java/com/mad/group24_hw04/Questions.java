package com.mad.group24_hw04;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * authors: Mihai Mehedint, Swati Jha
 * assignment: HW4
 * file name: MainActivity.java
 */


public class Questions {

    static public class QuestionsParser {
        public static LinkedList<Question> parseQuestions(String in) {
            //Log.d("demo", "\nquestionsParse string Before parsing: \n"+in);

            LinkedList<Question> questionList = new LinkedList<>();
            try {
                JSONObject root = new JSONObject(in);
                JSONArray questionsJSONArray = root.getJSONArray("questions");
                //Log.d("demo", "\nquestionsJSONArray Before parsing: \n"+questionsJSONArray.toString());


                for(int i=0;i<questionsJSONArray.length();i++){
                    JSONObject questionJSONObject = questionsJSONArray.getJSONObject(i);
                    Question q = Question.createQuestion(questionJSONObject);
                    Log.d("demo", "\nQuestion AFTER parsing: \n"+q.toString());
                    questionList.add(q);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.d("demo", "\nQuestions AFTER parsing: \n"+questionList.toString());
            return questionList;
        }

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

    }

}

