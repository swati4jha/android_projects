package com.example.mihai.group24_hw05;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
/**
 * authors: Mihai Mehedint, Swati Jha
 * assignment: HW4
 * file name: MainActivity.java
 */


public class SimilarGamesActivity extends AppCompatActivity implements GameDetailsAsync.GameDetails{
    ArrayList<Detail> gameList = new ArrayList<>();
    ArrayList<String> finalList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView lv;
    Detail game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_games);
        finalList.clear();
        gameList.clear();

        //adapter=new ArrayAdapter<String>(SimilarGamesActivity.this, android.R.layout.simple_list_item_1,finalList);
        adapter=new ArrayAdapter<String>(SimilarGamesActivity.this, android.R.layout.simple_list_item_1,finalList);
        lv = (ListView) findViewById(R.id.games);
        lv.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        game = (Detail) getIntent().getExtras().getParcelable("gameDetails");
        Log.d("demo", "In SimilarGamesActivity game Ids are: "+ game.getSimilar());

        ((TextView)findViewById(R.id.titleOriginalGame)).setText(game.getTitle());
        if(game.getSimilar().size()==0){
            Toast.makeText(SimilarGamesActivity.this, "There are no similar games!", Toast.LENGTH_SHORT).show();

        }else {

            for (String id : game.getSimilar()) {
                RequestParameters params = new RequestParameters("GET", "http://thegamesdb.net/api/GetGame.php");
                params.addParam("id", id);
                Log.d("demo", "Encoded params for the : " + id + " " + params.getEncodedParams());
                new GameDetailsAsync(SimilarGamesActivity.this).execute(params);
            }
        }



        findViewById(R.id.finishButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void setGameDetails(Detail details) {
        gameList.add(details);
        StringBuilder sb = new StringBuilder();
        sb.append(details.getTitle()).append(". ").append("Released in ").
                    append(details.getDate()).append(". ").append(" Platform: ").append(details.getPlatform()).append(".");
        Log.d("demo", "Similar game string: "+sb.toString());
        if(!game.getId().equals(details.getId())) {
                //finalList.add(sb.toString());
                adapter.add(sb.toString());
                Log.d("demo", String.valueOf(adapter.getCount()));
                adapter.notifyDataSetChanged();
        }
        //if(finalList.size()==gameList.size()) {
        //lv.removeAllViews();
        Log.d("demo", "Similar game string List: " + finalList);
        //adapter.notifyDataSetChanged();
        //lv.setAdapter(adapter);
        //}
    }
/*
    @Override
    public void setGameDetails(Detail details) {
        gameList.add(details);
        for(Detail current: gameList) {
            StringBuilder sb = new StringBuilder();
            //choice.getTitle() + ". " + "Released: " + choice.getDate() + "Platform: " + choice.getPlatform());
            sb.append(current.getTitle()).append(". ").append("Released in ").
                    append(current.getDate()).append(" Platform: ").append(current.getPlatform());
            Log.d("demo", "Similar game string: "+sb.toString());
            if(!finalList.contains(sb.toString())) {
                adapter.add(sb.toString());
                adapter.notifyDataSetChanged();
            }
        }
        //if(finalList.size()==gameList.size()) {
        //lv.removeAllViews();
        Log.d("demo", "Similar game string List: "+finalList);
        //adapter.notifyDataSetChanged();
        //lv.setAdapter(adapter);
        //}
    }

    */
    @Override
    public Context getContext() {
        return SimilarGamesActivity.this;
    }
}
