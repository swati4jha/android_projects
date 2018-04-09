package com.mad.group24_hw04;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Intent intent = getIntent();
        final ArrayList<Question> questions = (ArrayList<Question>)intent.getSerializableExtra("LIST");
        ArrayList<Question> wrongList = new ArrayList<Question>();
        for(Question question: questions){

            if(!question.getChoice().get(Integer.parseInt(question.getAnswer())-1)
                    .equalsIgnoreCase(question.getSelectedAns())){
                    wrongList.add(question);
            }
        }
        Log.d("Question",wrongList.size()+"");
        QuestionAdapter qApp = new QuestionAdapter(this,wrongList);
        //qApp.addAll(wrongList);
        ListView listView = (ListView) findViewById(R.id.listViewStats);
        listView.setAdapter(qApp);
        View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_view, null, false);
        listView.addFooterView(footerView);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
       //progressBar.setProgress(((questions.size()-wrongList.size())/questions.size())*100);
        progressBar.setMax(questions.size());
        //progressBar.setMax(20);
        progressBar.setProgress((questions.size()-wrongList.size()));

        TextView ptv = (TextView) findViewById(R.id.textViewPer);
        int ques = questions.size();
        int wrong = wrongList.size();
        double percentage = (ques-wrong)*100/ques;
        Log.d("perce", percentage+"");
        ptv.setText(percentage+"%");

        Button finish = (Button) findViewById(R.id.buttonfinish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

    }


}
