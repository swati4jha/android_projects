package inclass08_group24.mihai.example.com.inclass08_group24;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * authors: Swati Jha, Mihai Mehedint
 * assignment: InClass08
 * file name: MainActivity.java
 */

public class MainActivity extends AppCompatActivity implements MyFavoriteMoviesFragment.OnFragmentInteractionListener, AddFragment.OnFragmentInteractionListener, MovieByYearFragment.OnFragmentInteractionListener, MovieByRatingFragment.OnFragmentInteractionListener, EditFragment.OnFragmentInteractionListener {
    private ArrayList<Movie> movieList = new ArrayList<>();
    public String mChosenFile;
    public Movie movieEdit;
    int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction()
                .add(R.id.container, new MyFavoriteMoviesFragment(), "my favorite")
                .commit();

    }


    @Override
    public void goToAddFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new AddFragment(), "add")
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void goToByYear() {
        //MovieByYearFragment f =(MovieByYearFragment)getFragmentManager().findFragmentByTag("add");

        if(!movieList.isEmpty() && movieList!=null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new MovieByYearFragment(), "byYear")
                    .addToBackStack(null)
                    .commit();
        }else {
            Toast.makeText(MainActivity.this,
                    "No Movie to show!!", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void goToByRating() {
        //MovieByYearFragment f =(MovieByYearFragment)getFragmentManager().findFragmentByTag("add");
        if(!movieList.isEmpty() && movieList!=null) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new MovieByRatingFragment(), "byRating")
                .addToBackStack(null)
                .commit();

         }else {
        Toast.makeText(MainActivity.this,
                "No Movie to show!!", Toast.LENGTH_SHORT).show();

    }
    }

    @Override
    public void addMovie() {
        AddFragment f =(AddFragment)getFragmentManager().findFragmentByTag("add");
        Log.d("Add Movie:",f.getMovie().toString());
        movieList.add(f.getMovie());
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new MyFavoriteMoviesFragment(), "my favorite")
                .addToBackStack(null)
                .commit();
    }



    @Override
    public void setData() {
        MovieByYearFragment f = (MovieByYearFragment)getFragmentManager().findFragmentByTag("byYear");
        //Log.d("A Movie:",f.getMovie().toString());
        f.setValues(movieList);
    }

    @Override
    public void setDataRating() {
        MovieByRatingFragment f = (MovieByRatingFragment)getFragmentManager().findFragmentByTag("byRating");
        //Log.d("A Movie:",f.getMovie().toString());
        f.setValues(movieList);
    }



    @Override
    public void goToEditFragment() {
        if (movieList.size() > 0) {
            ArrayList<String> moviesList = new ArrayList<String>();
            for (int i = 0; i < movieList.size(); i++) {
                moviesList.add(i, movieList.get(i).getName());
            }
            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
            builder1.setTitle("Pick a movie");
            final CharSequence[] cs = moviesList.toArray(new CharSequence[moviesList.size()]);
            //builder1.setView(sp);

            builder1.setItems(cs, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mChosenFile = (String) cs[which];
                    movieEdit = movieList.get(which);
                    index = which;
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, new EditFragment(), "edit")
                            .addToBackStack(null)
                            .commit();
                }
            });
            builder1.create().show();

        }
        else {
            Toast.makeText(MainActivity.this,
                    "No Movie to edit!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void editMovie() {
        EditFragment f =(EditFragment)getFragmentManager().findFragmentByTag("edit");
        Log.d("Edit Movie:",f.getMovie().toString());
        ArrayList<Movie> movieListNew = movieList;
        movieList = new ArrayList<Movie>();
        if(movieListNew!=null && f.getMovie()!=null){
            for (int i = 0; i < movieListNew.size(); i++) {
                if(movieListNew.get(i).getName().equalsIgnoreCase(f.getMovie().getName())){
                    //movieList.remove(i);
                    movieList.add(f.getMovie());
                }
                else {
                    movieList.add(movieListNew.get(i));
                }

            }
        }
        //movieList.add(f.getMovie());
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new MyFavoriteMoviesFragment(), "my favorite")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void getMovietoEdit() {
        Log.d("Demo",movieEdit.toString());
        EditFragment f =(EditFragment)getFragmentManager().findFragmentByTag("edit");
        f.setMovie(movieEdit);

    }


    @Override
    public void goToDeleteFragment() {
        if (movieList.size() > 0) {
            ArrayList<String> moviesList = new ArrayList<String>();
            for (int i = 0; i < movieList.size(); i++) {
                moviesList.add(i, movieList.get(i).getName());
            }
            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
            builder1.setTitle("Pick a movie");
            final CharSequence[] cs = moviesList.toArray(new CharSequence[moviesList.size()]);
            //builder1.setView(sp);

            builder1.setItems(cs, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mChosenFile = (String) cs[which];
                    movieEdit = movieList.get(which);
                    index = which;
                    movieList.remove(which);
                    Toast.makeText(MainActivity.this,
                            "Movie Deleted", Toast.LENGTH_SHORT).show();
                }
            });
            builder1.create().show();

        }
        else {
            Toast.makeText(MainActivity.this,
                    "No Movie to edit!!", Toast.LENGTH_SHORT).show();
        }

    }


}
