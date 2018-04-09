/**
 * Assignment-2
 * Name: Swati Jha, Priyank Verma
 **/

package com.fav.movie.my_favorite_movies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class ShowYearActivity extends AppCompatActivity {
    private static int i= 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_year);

        Bundle bundle = getIntent().getExtras();
        final ArrayList<MovieVo> myList = bundle.getParcelableArrayList(MainActivity.MOVIE_KEY);

        Collections.sort(myList, MovieVo.YearComparator);
        MovieVo movie=myList.get(0);
        setValues(movie);
        Button prevBtn = (Button) findViewById(R.id.buttonYearPrev);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i>0){
                    i = i-1;
                    MovieVo movie=myList.get(i);
                    setValues(movie);
                }
                else{
                    Toast.makeText(ShowYearActivity.this,
                            "No more movies to show!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button firstBtn = (Button) findViewById(R.id.buttonYearFirst);
        firstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=0;
                MovieVo movie=myList.get(0);
                setValues(movie);

            }
        });

        Button nextBtn = (Button) findViewById(R.id.buttonYearNext);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i<(myList.size()-1)){
                    i = i+1;
                    MovieVo movie=myList.get(i);
                    setValues(movie);
                }
                else{
                    Toast.makeText(ShowYearActivity.this,
                            "No more movies to show!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button lastBtn = (Button) findViewById(R.id.buttonYearLast);
        lastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=myList.size()-1;
                MovieVo movie=myList.get(myList.size()-1);
                setValues(movie);

            }
        });

        Button finishBtn = (Button) findViewById(R.id.buttonYearfinish);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    public void setValues(MovieVo movie){
        TextView editName = (TextView)findViewById(R.id.YearTitle);
        editName.setText(movie.name);

        TextView editdescription = (TextView)findViewById(R.id.YearDesc);
        editdescription.setText(movie.description);

        TextView editimdb = (TextView)findViewById(R.id.YearImdb);
        editimdb.setText(movie.imdbLink);

        TextView edityear = (TextView)findViewById(R.id.YearYear);
        int year = movie.year;
        edityear.setText(String.valueOf(year));

        TextView genre = (TextView)findViewById(R.id.YearGenre);
        genre.setText(movie.genre);

        TextView rating = (TextView)findViewById(R.id.YearRating);
        rating.setText(String.valueOf(movie.rating));

    }

}
