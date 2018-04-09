package swati.example.com.homework08_group24;

/**
 * Created by swati on 4/3/2017.
 */
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{

    private ArrayList<SearchCity> weatherList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.textViewRec);
            image = (ImageView) view.findViewById(R.id.imageViewRec);
        }

    }

    public RecyclerAdapter(ArrayList<SearchCity> weatherList) {
        this.weatherList = weatherList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SearchCity cityWeather = weatherList.get(position);
        holder.date.setText(cityWeather.getDate());
        Picasso.with(holder.image.getContext()).load("http://developer.accuweather.com/sites/default/files/"
                +cityWeather.getNightIcon()+"-s.png").into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    /*public void updateNote(Note note){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d("note.get_id()",note.get_id()+"");
        mDatabase.child("notes").child(note.get_id()).setValue(note);
    }

    public void removeItem(Note note) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d("note.get_id()",note.get_id()+"");
        mDatabase.child("notes").child(note.get_id()).removeValue();
        //Toast.makeText(View.getContext(),"Task deleted!!!",Toast.LENGTH_SHORT).show();

    }*/



}
