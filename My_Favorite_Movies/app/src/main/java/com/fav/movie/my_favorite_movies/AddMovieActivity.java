/**
 * Assignment-2
 * Name: Swati Jha, Priyank Verma
 **/


package com.fav.movie.my_favorite_movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class AddMovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerGenre);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genre, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button addMovie = (Button) findViewById(R.id.buttonAddMovie);
        addMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                String name = "";
                String description = "";
                String genre = "";
                int rating = 0;
                int year = 0;
                String imdbLink = "";
                EditText editName = (EditText)findViewById(R.id.editTextMovieName);
                if(editName!=null && editName.getText()!=null){
                    name = editName.getText().toString();
                }

                EditText editdescription = (EditText)findViewById(R.id.editTextDesc);
                if(editdescription!=null && editdescription.getText()!=null){
                    description = editdescription.getText().toString();
                }

                EditText editimdb = (EditText)findViewById(R.id.editTextImdbLink);
                if(editimdb!=null && editimdb.getText()!=null){
                    imdbLink = editimdb.getText().toString();
                }


                EditText edityear = (EditText)findViewById(R.id.editTextYear);
                if(edityear!=null && edityear.getText()!=null && !edityear.getText().toString().isEmpty() && String.valueOf(edityear.getText()) !=null){
                        year = Integer.parseInt(edityear.getText().toString());
                }

                Spinner genreSpinner=(Spinner) findViewById(R.id.spinnerGenre);
                genre = genreSpinner.getSelectedItem().toString();

                SeekBar seekBar = (SeekBar)findViewById(R.id.seekBarRating);
                rating = seekBar.getProgress();
                if(name!=null && !name.isEmpty()){
                    Log.d("Add Movie:",name+"::"+description+"::"+genre+"::"+imdbLink+"::"+rating+"::"+year);

                    ArrayList<MovieVo> myList = bundle.getParcelableArrayList(MainActivity.MOVIE_KEY);

                    Intent intent = new Intent(AddMovieActivity.this, MainActivity.class);
                    MovieVo movie = new MovieVo(name,description, genre, rating,year,imdbLink);
                    myList.add(movie);
                    intent.putExtra(MainActivity.MOVIE_KEY, myList);
                    setResult(RESULT_OK,intent);
                    Toast.makeText(AddMovieActivity.this,
                            "Movie Added!!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(AddMovieActivity.this,
                            "Name cannot be empty!!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
