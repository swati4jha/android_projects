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
public class EditFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Movie movie;

    public EditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Spinner spinner = (Spinner) getActivity().findViewById(R.id.editmoviespinnerGenre);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.genre_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        mListener.getMovietoEdit();
        getView().findViewById(R.id.buttonSaveMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "";
                String description = "";
                String genre = "";
                int rating = 0;
                int year = 0;
                String imdbLink = "";

                EditText editName = (EditText)getActivity().findViewById(R.id.editmovieTextMovieName);
                EditText editdescription = (EditText)getActivity().findViewById(R.id.editmovieTextDesc);
                EditText editimdb = (EditText)getActivity().findViewById(R.id.editmovieeditTextImdbLink);
                EditText edityear = (EditText)getActivity().findViewById(R.id.editmovieeditTextYear);
                Spinner genreSpinner=(Spinner) getActivity().findViewById(R.id.editmoviespinnerGenre);
                SeekBar seekBar = (SeekBar)getActivity().findViewById(R.id.editmovieseekBarRating);

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

                genre = genreSpinner.getSelectedItem().toString();
                rating = seekBar.getProgress();

                Log.d("Edit Movie:",name+"::"+description+"::"+genre+"::"+imdbLink+"::"+rating+"::"+year);
                if(name!=null && !name.isEmpty()) {
                    movie = new Movie(name, description, genre, rating, year, imdbLink);
                    mListener.editMovie();
                }
                else{
                    Toast.makeText(getActivity(),
                            "Name cannot be empty!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

                //
    }

    public Movie getMovie(){
        if(movie!=null)
            return movie;
        else{
            Toast.makeText(getActivity(), "movie is null", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public void setMovie(Movie movie){
        if(movie!=null){
            final EditText editName = (EditText)getActivity().findViewById(R.id.editmovieTextMovieName);
            editName.setText(movie.getName());

            final EditText editdescription = (EditText)getActivity().findViewById(R.id.editmovieTextDesc);
            editdescription.setText(movie.getDescription());

            final EditText editimdb = (EditText)getActivity().findViewById(R.id.editmovieeditTextImdbLink);
            editimdb.setText(movie.getImdb());

            final EditText edityear = (EditText)getActivity().findViewById(R.id.editmovieeditTextYear);
            int year = movie.getYear();
            edityear.setText(String.valueOf(year));

            Spinner genreSpinner=(Spinner) getActivity().findViewById(R.id.editmoviespinnerGenre);
            genreSpinner.setSelection(0);

            SeekBar seekBar = (SeekBar)getActivity().findViewById(R.id.editmovieseekBarRating);
            seekBar.setProgress(movie.getRating());
        }
        else{
            Toast.makeText(getActivity(), "movie is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void editMovie();
        public void getMovietoEdit();
    }
}
