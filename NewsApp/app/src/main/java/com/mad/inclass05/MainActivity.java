package com.mad.inclass05;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements GetNewsFeed.NewsData{

    ArrayList<News> finalnews;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_action_name);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_main);


        findViewById(R.id.buttonNext).setClickable(false);
        findViewById(R.id.buttonLast).setClickable(false);
        findViewById(R.id.buttonFirst).setClickable(false);
        findViewById(R.id.buttonPrev).setClickable(false);

        findViewById(R.id.buttonNext).setEnabled(false);
        findViewById(R.id.buttonLast).setEnabled(false);
        findViewById(R.id.buttonFirst).setEnabled(false);
        findViewById(R.id.buttonPrev).setEnabled(false);

        Button getData = (Button) findViewById(R.id.buttonGetNews);
        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    new GetNewsFeed(MainActivity.this).execute("http://rss.cnn.com/rss/cnn_tech.rss");
                }else{
                    Toast.makeText(MainActivity.this, "NO Internet Connection!", Toast.LENGTH_LONG);
                }
            }
        });

        Button prev = (Button) findViewById(R.id.buttonPrev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.buttonNext).setClickable(true);
                findViewById(R.id.buttonLast).setClickable(true);
                findViewById(R.id.buttonNext).setEnabled(true);
                findViewById(R.id.buttonLast).setEnabled(true);
                count =count-1;
                if(count==0){
                    findViewById(R.id.buttonFirst).setClickable(false);
                    findViewById(R.id.buttonPrev).setClickable(false);
                    findViewById(R.id.buttonFirst).setEnabled(false);
                    findViewById(R.id.buttonPrev).setEnabled(false);
                }
                TextView title = (TextView)findViewById(R.id.textViewTirle);
                TextView pub = (TextView)findViewById(R.id.textViewPubOn);
                TextView desc = (TextView)findViewById(R.id.textViewDesc);
                ImageView iv =((ImageView) findViewById(R.id.imageView));
                title.setText(finalnews.get(count).getTitle());
                pub.setText(finalnews.get(count).getPubDate());
                desc.setText(finalnews.get(count).getDescription());
                Picasso.with(MainActivity.this)
                        .load(finalnews.get(count).getImageURL())
                        .into(iv);
            }
        });

        Button next = (Button) findViewById(R.id.buttonNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.buttonFirst).setClickable(true);
                findViewById(R.id.buttonPrev).setClickable(true);
                findViewById(R.id.buttonFirst).setEnabled(true);
                findViewById(R.id.buttonPrev).setEnabled(true);
                count =count+1;
                if(count==finalnews.size()-1){
                    findViewById(R.id.buttonNext).setClickable(false);
                    findViewById(R.id.buttonLast).setClickable(false);
                    findViewById(R.id.buttonNext).setEnabled(false);
                    findViewById(R.id.buttonLast).setEnabled(false);
                }
                TextView title = (TextView)findViewById(R.id.textViewTirle);
                TextView pub = (TextView)findViewById(R.id.textViewPubOn);
                TextView desc = (TextView)findViewById(R.id.textViewDesc);
                ImageView iv =((ImageView) findViewById(R.id.imageView));
                title.setText(finalnews.get(count).getTitle());
                pub.setText(finalnews.get(count).getPubDate());
                desc.setText(finalnews.get(count).getDescription());
                Picasso.with(MainActivity.this)
                        .load(finalnews.get(count).getImageURL())
                        .into(iv);

            }
        });
        Button first = (Button) findViewById(R.id.buttonFirst);
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count =0;
                findViewById(R.id.buttonFirst).setClickable(false);
                findViewById(R.id.buttonPrev).setClickable(false);
                findViewById(R.id.buttonFirst).setEnabled(false);
                findViewById(R.id.buttonPrev).setEnabled(false);
                findViewById(R.id.buttonNext).setClickable(true);
                findViewById(R.id.buttonLast).setClickable(true);
                findViewById(R.id.buttonNext).setEnabled(true);
                findViewById(R.id.buttonLast).setEnabled(true);


                TextView title = (TextView)findViewById(R.id.textViewTirle);
                TextView pub = (TextView)findViewById(R.id.textViewPubOn);
                TextView desc = (TextView)findViewById(R.id.textViewDesc);
                ImageView iv =((ImageView) findViewById(R.id.imageView));
                title.setText(finalnews.get(count).getTitle());
                pub.setText(finalnews.get(count).getPubDate());
                desc.setText(finalnews.get(count).getDescription());Picasso.with(MainActivity.this)
                        .load(finalnews.get(count).getImageURL())
                        .into(iv);


            }
        });

        Button last = (Button) findViewById(R.id.buttonLast);
        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count =finalnews.size()-1;
                findViewById(R.id.buttonFirst).setClickable(true);
                findViewById(R.id.buttonPrev).setClickable(true);
                findViewById(R.id.buttonFirst).setEnabled(true);
                findViewById(R.id.buttonPrev).setEnabled(true);
                findViewById(R.id.buttonNext).setClickable(false);
                findViewById(R.id.buttonLast).setClickable(false);
                findViewById(R.id.buttonNext).setEnabled(false);
                findViewById(R.id.buttonLast).setEnabled(false);
                TextView title = (TextView)findViewById(R.id.textViewTirle);
                TextView pub = (TextView)findViewById(R.id.textViewPubOn);
                TextView desc = (TextView)findViewById(R.id.textViewDesc);
                ImageView iv =((ImageView) findViewById(R.id.imageView));
                title.setText(finalnews.get(count).getTitle());
                pub.setText(finalnews.get(count).getPubDate());
                desc.setText(finalnews.get(count).getDescription());
                Picasso.with(MainActivity.this)
                        .load(finalnews.get(count).getImageURL())
                        .into(iv);

            }
        });

        Button finish = (Button) findViewById(R.id.buttonFinish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    private boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected())
            return true;
        return false;
    }
    @Override
    public void setupData(ArrayList<News> news) {
        //here setup the parsed data

        if(news!=null && news.size()!=0) {
            //ImageView iv =((ImageView) findViewById(R.id.imageView));
            /*iv.setImageResource(R.drawable.trivia);
            findViewById(R.id.textViewImage).setVisibility(View.VISIBLE);
            findViewById(R.id.buttonStart).setClickable(true);

            finalquestions = questions;*/
            findViewById(R.id.buttonNext).setClickable(true);
            findViewById(R.id.buttonLast).setClickable(true);
            findViewById(R.id.buttonNext).setEnabled(true);
            findViewById(R.id.buttonLast).setEnabled(true);
            finalnews = news;
            TextView title = (TextView)findViewById(R.id.textViewTirle);
            TextView pub = (TextView)findViewById(R.id.textViewPubOn);
            TextView desc = (TextView)findViewById(R.id.textViewDesc);
            TextView pubon = (TextView)findViewById(R.id.textViewpub);
            TextView textView3 = (TextView)findViewById(R.id.textView3);
            ImageView iv =((ImageView) findViewById(R.id.imageView));
            title.setText(finalnews.get(count).getTitle());
            pub.setText(finalnews.get(count).getPubDate());
            desc.setText(finalnews.get(count).getDescription());
            pubon.setText("Published on::");
            textView3.setText("Description");
            Picasso.with(MainActivity.this)
                    .load(finalnews.get(count).getImageURL())
                    .into(iv);
            Log.d("demo", "\nNews AFTER parsing: \n"+finalnews.toString());
        }


    }

    @Override
    public Context getContext() {
        return this;
    }
}
