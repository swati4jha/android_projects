/**
 * Assignment-2
 * Name: Swati Jha, Priyank Verma
 **/

package com.fav.movie.my_favorite_movies;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    final static String MOVIE_KEY = "MovieList";
    final static String SELECTED_MOVIE_KEY = "MovieSelected";
    final static int REQ_CODE = 1001;
    ArrayList<MovieVo> movies = new ArrayList<MovieVo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addBtn = (Button) findViewById(R.id.buttonAdd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddMovieActivity.class);
                //MovieVo movie = new MovieVo("DDLJ","Best Movie in the world", "Romantic", 4,1998,"www.movie.com");
                //movies.add(movie);
                intent.putExtra(MOVIE_KEY, movies);
                startActivityForResult(intent,REQ_CODE);
            }
        });



        Button editBtn = (Button) findViewById(R.id.buttonEdit);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movies.size() > 0) {
                    ArrayList<String> moviesList = new ArrayList<String>();

                    for (int i = 0; i < movies.size(); i++) {
                        moviesList.add(i, movies.get(i).name);
                    }
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setTitle("Pick a movie");
                    final CharSequence[] cs = moviesList.toArray(new CharSequence[moviesList.size()]);
                    //builder1.setView(sp);

                    builder1.setItems(cs, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String mChosenFile = (String) cs[which];
                            Intent intent = new Intent(MainActivity.this, EditActivity.class);
                            //MovieVo movie = new MovieVo("DDLJ","Best Movie in the world", "Romantic", 4,1998,"www.movie.com");
                            //movies.add(movie);
                            intent.putExtra(MOVIE_KEY, movies);
                            intent.putExtra(SELECTED_MOVIE_KEY, mChosenFile);
                            startActivityForResult(intent, REQ_CODE);
                        }
                    });
                    builder1.create().show();

                }
                else {
                    Toast.makeText(MainActivity.this,
                            "No Movie to edit!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button deleteBtn = (Button) findViewById(R.id.buttonDel);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movies.size() > 0) {
                    ArrayList<String> moviesList = new ArrayList<String>();

                    for (int i = 0; i < movies.size(); i++) {
                        moviesList.add(i, movies.get(i).name);
                    }
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setTitle("Pick a movie");
                    final CharSequence[] cs = moviesList.toArray(new CharSequence[moviesList.size()]);
                    //builder1.setView(sp);

                    builder1.setItems(cs, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String mChosenFile = (String) cs[which];
                            ArrayList<MovieVo> editedMoviesList = new ArrayList<MovieVo>();
                            for (int i = 0; i < movies.size(); i++) {
                                if (!movies.get(i).name.equalsIgnoreCase(mChosenFile)) {
                                    editedMoviesList.add(movies.get(i));
                                }

                            }
                            movies.clear();
                            movies.addAll(editedMoviesList);
                            Toast.makeText(MainActivity.this,
                                    "Movie Deleted", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder1.create().show();
                }
                else {
                    Toast.makeText(MainActivity.this,
                            "No Movie to delete!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button showRatingBtn = (Button) findViewById(R.id.buttonShowRating);
        showRatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.fav.movie.my_favorite_movies.intent.action.VIEW_RATING");
                //MovieVo movie = new MovieVo("DDLJ","Best Movie in the world", "Romantic", 4,1998,"www.movie.com");
                //movies.add(movie);
                if(movies.size()>0)
                {
                    intent.putExtra(MOVIE_KEY, movies);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this,
                            "No Movie to show!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button showYearBtn = (Button) findViewById(R.id.buttonShowYear);
        showYearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.fav.movie.my_favorite_movies.intent.action.VIEW_YEAR");
                //MovieVo movie = new MovieVo("DDLJ","Best Movie in the world", "Romantic", 4,1998,"www.movie.com");
                //movies.add(movie);
                if(movies.size()>0)
                {
                    intent.putExtra(MOVIE_KEY, movies);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this,
                            "No Movie to show!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQ_CODE) {
            Log.d("requestCode:",requestCode+"");
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                ArrayList<MovieVo> myList = bundle.getParcelableArrayList(MainActivity.MOVIE_KEY);
                movies.clear();
                movies.addAll(myList);

            }
        }
    }
}
