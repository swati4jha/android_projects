package com.mad.group24_hw04;
/**
 * authors: Mihai Mehedint, Swati Jha
 * assignment: HW4
 * file name: MainActivity.java
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.mad.group24_hw04.MainActivity;
import com.mad.group24_hw04.Question;
import com.mad.group24_hw04.Questions;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by mihai on 2/7/17.
 */

public class GetTriviaAsyncTask extends AsyncTask<String, Integer, LinkedList<Question>> {
    MainActivity activity;
    ProgressDialog pb;
    //ProgressBar pb;


    public GetTriviaAsyncTask(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //pb = (ProgressBar) activity.findViewById(R.id.progressBar2);
        pb = new ProgressDialog((activity.findViewById(R.id.linear_Layout)).getContext());
        //setContentView(pb);
        pb.setCancelable(false);
        pb.setTitle("Loading Trivia");
        //pb.setMessage("Loading Trivia");
        pb.setMax(100);
        pb.setProgress(0);
        pb.setIndeterminate(false);
        pb.show();

        //pb = new ProgressBar(activity.findViewById(R.id.linear_Layout).getContext());
        //pb.setClickable(false);
        //pb.setVisibility(View.VISIBLE);
        //pb.setTitle();
        //pb.setProgress(0);
        //pb.setMax(10);

    }



    @Override
    protected LinkedList<Question> doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int statusCode = con.getResponseCode();

            if(statusCode==HttpURLConnection.HTTP_OK){
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line =reader.readLine();
                StringBuilder sb = new StringBuilder();
                int i=0;
                while(line!=null){
                    i++;
                    sb.append(line);
                    line=reader.readLine();
                    for(int j=0;j<10000000;j++){}
                    publishProgress(i);
                }
                Log.d("demo", "\nQuestions BEFORE parsing: \n"+sb.toString());
                //return null;
                return Questions.QuestionsParser.parseQuestions(sb.toString());

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        pb.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(LinkedList<Question> questions) {
        super.onPostExecute(questions);
        //pb.setVisibility(View.GONE);
        //pb.setVisibility(View.INVISIBLE);
        pb.dismiss();
        activity.setupData(questions);




        //Log.d("demo", "\nQuestions AFTER parsing: \n"+sb.toString());
    }

    static public interface TriviaData{
        public void setupData(LinkedList<Question> questions);
        public Context getContext();
    }
}
