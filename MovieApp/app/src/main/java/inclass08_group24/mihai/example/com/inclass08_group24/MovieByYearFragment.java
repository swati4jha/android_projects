package inclass08_group24.mihai.example.com.inclass08_group24;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;



public class MovieByYearFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Movie movie;
    protected ArrayList<Movie> list;
     int globalIndex=0;

    public MovieByYearFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_by_year, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener.setData();
        Log.d("View::", getView().toString());

        getView().findViewById(R.id.button_rating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        getView().findViewById(R.id.buttonrateFirst).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMovie(0);
            }
        });


        getView().findViewById(R.id.buttonratePrev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(globalIndex>0) {
                    globalIndex--;
                    showMovie(globalIndex);
                }
            }
        });
        getView().findViewById(R.id.buttonrateNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(globalIndex<list.size()-1) {
                    globalIndex++;
                    showMovie(globalIndex);
                }

            }
        });
        getView().findViewById(R.id.buttonrateLast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalIndex=list.size()-1;
                showMovie(globalIndex);

            }
        });
    }


    protected void setValues(ArrayList<Movie> m){
        list = m;
        Collections.sort(list);
        showMovie(globalIndex);
    }

    protected void showMovie(int index){


        //Log.d("demo", m.toString());
        TextView name = (TextView)getView().findViewById(R.id.textName);
        name.setText(list.get(index).getName());

        TextView description = (TextView)getView().findViewById(R.id.editTextDescription);
        description.setText(list.get(index).getDescription());

        TextView genre = (TextView)getView().findViewById(R.id.textGenre);
        genre.setText(list.get(index).getGenre());

        TextView rating = (TextView)getView().findViewById(R.id.textRating);
        rating.setText(list.get(index).getRating().toString());

        TextView year = (TextView)getView().findViewById(R.id.textYear);
        year.setText(list.get(index).getYear().toString());

        TextView imdb = (TextView)getView().findViewById(R.id.textIMDB);
        imdb.setText(list.get(index).getImdb());




    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MovieByYearFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
            public void setData();
    }

}





