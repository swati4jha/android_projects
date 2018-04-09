package com.mad.group24_hw04;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;

public class TriviaActivity extends AppCompatActivity {
    int count =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        count =0;
        Intent intent = getIntent();
        final ArrayList<Question> questions = (ArrayList<Question>)intent.getSerializableExtra("LIST");
        Log.d("SWATI",questions.size()+"");
        final ImageView img = (ImageView) findViewById(R.id.imageViewImg);
        if(questions.get(0).getImageURL()!=null && !questions.get(0).getImageURL().isEmpty()){
            Picasso.with(this)
                    .load(questions.get(0).getImageURL())
                    .placeholder(R.drawable.rolling)
                    .into(img);
            img.setVisibility(View.VISIBLE);
            img.getLayoutParams().height = 300;
        }
        else{
            img.setVisibility(View.INVISIBLE);
            img.getLayoutParams().height = 0;

        }


        final Button prev = (Button) findViewById(R.id.buttonPrev);
        prev.setEnabled(false);

        final TextView tv = (TextView) findViewById(R.id.textViewQNo);
        tv.setText((count+1)+"");

        final TextView tv1 = (TextView) findViewById(R.id.textViewQuestion);
        tv1.setText(questions.get(0).getText());

        final RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        for(int i=0;i<questions.get(0).getChoice().size();i++){
            RadioButton rb = new RadioButton(this);
            rb.setText(questions.get(0).getChoice().get(i));
            rg.addView(rb);
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(RadioGroup group, int checkedId)
                  {
                      Log.d("Check Count", count+"::"+checkedId);
                      String ans = null;
                      if(checkedId!=-1){
                          RadioButton radioButton = (RadioButton) findViewById(checkedId);
                          ans = radioButton.getText().toString();
                          questions.get(count).setSelectedAns(ans);

                          rg.removeAllViews();

                          for(int i=0;i<questions.get(count).getChoice().size();i++){
                              RadioButton rb = new RadioButton(TriviaActivity.this);
                              rb.setText(questions.get(count).getChoice().get(i));
                              if(questions.get(count).getSelectedAns()!=null &&
                                      questions.get(count).getSelectedAns().equalsIgnoreCase(questions.get(count).getChoice().get(i))){
                                  rb.setChecked(true);
                              }
                              rg.addView(rb);
                          }

                      }
                      if(questions.get(count).getSelectedAns()==null ||  questions.get(count).getSelectedAns()=="0"){
                          questions.get(count).setSelectedAns(ans);
                      }



                  }
             }
        );

        final TextView mTextField = (TextView) findViewById(R.id.mTextField);

        new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mTextField.setText("done!");

                Toast.makeText(TriviaActivity.this,
                        "Time up!!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(TriviaActivity.this, StatsActivity.class);
                intent.putExtra("LIST",questions);
                startActivity(intent);
            }
        }.start();

        final Button next = (Button) findViewById(R.id.buttonNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count = count +1;
                Log.d("Next Count", count+"");
                prev.setEnabled(true);
                if(count == questions.size()-1){
                    next.setText("FINISH");
                }
                if(count<questions.size()){
                    tv.setText((count+1)+"");
                    tv1.setText(questions.get(count).getText());
                    if(questions.get(count).getImageURL()!=null && !questions.get(count).getImageURL().isEmpty()){
                        Picasso.with(TriviaActivity.this)
                                .load(questions.get(count).getImageURL())
                                .placeholder(R.drawable.rolling)
                                .into(img);
                        img.setVisibility(View.VISIBLE);
                        img.getLayoutParams().height = 300;
                    }
                    else{
                        img.setVisibility(View.INVISIBLE);
                        img.getLayoutParams().height = 0;
                    }
                    rg.removeAllViews();

                    for(int i=0;i<questions.get(count).getChoice().size();i++){
                        RadioButton rb = new RadioButton(TriviaActivity.this);
                        rb.setText(questions.get(count).getChoice().get(i));
                        if(questions.get(count).getSelectedAns()!=null &&
                                questions.get(count).getSelectedAns().equalsIgnoreCase(questions.get(count).getChoice().get(i))){
                            rb.setChecked(true);
                        }
                        rg.addView(rb);
                    }
                }else{
                    Intent intent = new Intent(TriviaActivity.this, StatsActivity.class);
                    intent.putExtra("LIST",questions);
                    startActivity(intent);
                }

            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count = count -1;
                Log.d("Prev Count", count+"");
                next.setText("NEXT");
                if(count == 0){
                    prev.setEnabled(false);
                }
                if(count>=0){
                    tv.setText((count+1)+"");
                    tv1.setText(questions.get(count).getText());
                    if(questions.get(count).getImageURL()!=null && !questions.get(count).getImageURL().isEmpty()){
                        Picasso.with(TriviaActivity.this)
                                .load(questions.get(count).getImageURL())
                                .placeholder(R.drawable.rolling)
                                .into(img);
                        img.setVisibility(View.VISIBLE);
                        img.getLayoutParams().height = 300;
                    }
                    else{
                        img.setVisibility(View.INVISIBLE);
                        img.getLayoutParams().height = 0;

                    }
                    rg.removeAllViews();

                    for(int i=0;i<questions.get(count).getChoice().size();i++){
                        RadioButton rb = new RadioButton(TriviaActivity.this);
                        rb.setText(questions.get(count).getChoice().get(i));
                        if(questions.get(count).getSelectedAns()!=null &&
                                questions.get(count).getSelectedAns().equalsIgnoreCase(questions.get(count).getChoice().get(i))){
                            rb.setChecked(true);
                        }
                        rg.addView(rb);
                    }
                }

            }
        });




    }
}
