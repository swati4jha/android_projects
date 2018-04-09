package inclass08_group24.mihai.example.com.inclass08_group24;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AddFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Movie movie;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Spinner spinner = (Spinner) getActivity().findViewById(R.id.spinnerGenre);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.genre_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Log.d("View::",getView().toString());
        getView().findViewById(R.id.buttonAddMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "";
                String description = "";
                String genre = "";
                int rating = 0;
                int year = 0;
                String imdbLink = "";
                EditText editName = (EditText)getView().findViewById(R.id.editTextMovieName);
                if(editName!=null && editName.getText()!=null){
                    name = editName.getText().toString();
                }

                EditText editdescription = (EditText)getView().findViewById(R.id.editTextDesc);
                if(editdescription!=null && editdescription.getText()!=null){
                    description = editdescription.getText().toString();
                }

                EditText editimdb = (EditText)getView().findViewById(R.id.editTextImdbLink);
                if(editimdb!=null && editimdb.getText()!=null){
                    imdbLink = editimdb.getText().toString();
                }


                EditText edityear = (EditText)getView().findViewById(R.id.editTextYear);
                if(edityear!=null && edityear.getText()!=null && !edityear.getText().toString().isEmpty() && String.valueOf(edityear.getText()) !=null){
                    year = Integer.parseInt(edityear.getText().toString());
                }

                Spinner genreSpinner=(Spinner) getView().findViewById(R.id.spinnerGenre);
                genre = genreSpinner.getSelectedItem().toString();

                SeekBar seekBar = (SeekBar)getView().findViewById(R.id.seekBarRating);
                rating = seekBar.getProgress();
                if(name!=null && !name.isEmpty()){
                    Log.d("Add Movie:",name+"::"+description+"::"+genre+"::"+imdbLink+"::"+rating+"::"+year);


                    movie = new Movie(name,description, genre, rating,year,imdbLink);
                    Toast.makeText(getActivity(),
                            "Movie Added!!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(),
                            "Name cannot be empty!!", Toast.LENGTH_SHORT).show();
                }
                mListener.addMovie();
            }
        });
    }

    public Movie getMovie(){

        if(movie!=null)
            return movie;
        else{
            Toast.makeText(getActivity(), "movie is null", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void addMovie();
    }


}
