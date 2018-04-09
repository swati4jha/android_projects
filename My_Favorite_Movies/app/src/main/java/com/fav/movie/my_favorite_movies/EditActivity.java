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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        Spinner spinner = (Spinner) findViewById(R.id.editmoviespinnerGenre);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genre, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();
        final ArrayList<MovieVo> myList = bundle.getParcelableArrayList(MainActivity.MOVIE_KEY);
        final String movieToBeEdited = bundle.getString(MainActivity.SELECTED_MOVIE_KEY);
        MovieVo movie=new MovieVo();
        for(int i=0;i<myList.size();i++){
            if(myList.get(i).name.equalsIgnoreCase(movieToBeEdited)){
                movie = myList.get(i);
            }

        }

        final EditText editName = (EditText)findViewById(R.id.editmovieTextMovieName);
        editName.setText(movie.name);

        final EditText editdescription = (EditText)findViewById(R.id.editmovieTextDesc);
        editdescription.setText(movie.description);

        final EditText editimdb = (EditText)findViewById(R.id.editmovieeditTextImdbLink);
        editimdb.setText(movie.imdbLink);

        final EditText edityear = (EditText)findViewById(R.id.editmovieeditTextYear);
        int year = movie.year;
        edityear.setText(String.valueOf(year));

        Spinner genreSpinner=(Spinner) findViewById(R.id.editmoviespinnerGenre);
        genreSpinner.setSelection(0);

        SeekBar seekBar = (SeekBar)findViewById(R.id.editmovieseekBarRating);
        seekBar.setProgress(movie.rating);

        Button saveMovie = (Button) findViewById(R.id.buttonSaveMovie);
        saveMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = "";
                String description = "";
                String genre = "";
                int rating = 0;
                int year = 0;
                String imdbLink = "";

                if(editName!=null && editName.getText()!=null){
                    name = editName.getText().toString();
                }


                if(editdescription!=null && editdescription.getText()!=null){
                    description = editdescription.getText().toString();
                }


                if(editimdb!=null && editimdb.getText()!=null){
                    imdbLink = editimdb.getText().toString();
                }



                if(edityear!=null && edityear.getText()!=null && !edityear.getText().toString().isEmpty() && String.valueOf(edityear.getText()) !=null){
                    year = Integer.parseInt(edityear.getText().toString());
                }

                Spinner genreSpinner=(Spinner) findViewById(R.id.editmoviespinnerGenre);
                genre = genreSpinner.getSelectedItem().toString();

                SeekBar seekBar = (SeekBar)findViewById(R.id.editmovieseekBarRating);
                rating = seekBar.getProgress();

                Log.d("Edit Movie:",name+"::"+description+"::"+genre+"::"+imdbLink+"::"+rating+"::"+year);



                Log.d("Select:",movieToBeEdited);
                if(name!=null && !name.isEmpty()) {
                    ArrayList<MovieVo> editedMoviesList = new ArrayList<MovieVo>();
                    for (int i = 0; i < myList.size(); i++) {
                        if (myList.get(i).name.equalsIgnoreCase(movieToBeEdited)) {
                            MovieVo movie = new MovieVo(name, description, genre, rating, year, imdbLink);
                            editedMoviesList.add(i, movie);
                        } else {
                            editedMoviesList.add(i, myList.get(i));
                        }
                    }
                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.MOVIE_KEY, editedMoviesList);
                    setResult(RESULT_OK, intent);
                    Toast.makeText(EditActivity.this,
                            "Movie Saved!!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(EditActivity.this,
                            "Name cannot be empty!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
