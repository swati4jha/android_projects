package com.example.mihai.group24_hw05;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * authors: Mihai Mehedint, Swati Jha
 * assignment: HW4
 * file name: MainActivity.java
 */


public class GameDetailsActivity extends AppCompatActivity implements GetImage.GetImageInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        final Detail detail = (Detail) getIntent().getExtras().getParcelable("details");
        Log.d("demo", "\nin details Activity detail: "+detail);

        ((TextView)findViewById(R.id.title)).setText(detail.getTitle());
        ((TextView)findViewById(R.id.overview)).setText(detail.getOverview());
        ((TextView)findViewById(R.id.genre)).setText(detail.getGenre());
        ((TextView)findViewById(R.id.publisher)).setText(detail.getPublisher());
        RequestParameters params = new RequestParameters("GET", detail.getBaseUrl()+detail.getThumb());
        //params.addParam("image", detail.get);
        new GetImage(this).execute(params);

        findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        findViewById(R.id.trailer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detail.getYoutube()!=null) {
                    Intent intent = new Intent(GameDetailsActivity.this, WebViewActivity.class);
                    intent.putExtra("videoUrl", detail);
                    startActivity(intent);
                }else{
                    Toast.makeText(GameDetailsActivity.this, "There is NO Youtube video available", Toast.LENGTH_LONG).show();
                }

            }
        });

        findViewById(R.id.similarGames).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameDetailsActivity.this, SimilarGamesActivity.class);
                intent.putExtra("gameDetails", detail);
                startActivity(intent);
            }
        });

    }

    @Override
    public void setImageDetails(Bitmap result) {
        Log.d("demo", "result bitmap: "+result.toString());
        ((ImageView)findViewById(R.id.imageView)).setImageBitmap(result);
    }



    @Override
    public Context getContext() {
        return GameDetailsActivity.this;
    }
}
