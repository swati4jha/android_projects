package swati.example.com.homework08_group24;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by swati,,Mihai on 4/8/2017.
 */

public class RecyclerAdapter1 extends RecyclerView.Adapter<RecyclerAdapter1.MyViewHolder> {

    private ArrayList<CityVo> weatherList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date,temp,city;
        public ImageButton button;

        public MyViewHolder(View view) {
            super(view);
            city = (TextView) view.findViewById(R.id.textViewSavedCity);
            temp = (TextView) view.findViewById(R.id.textViewSaveTemp);
            date = (TextView) view.findViewById(R.id.textViewSaveLU);
            button = (ImageButton) view.findViewById(R.id.imageButton);
        }

    }

    public RecyclerAdapter1(ArrayList<CityVo> weatherList) {
        this.weatherList = weatherList;
    }

    @Override
    public RecyclerAdapter1.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item1, parent, false);

        return new RecyclerAdapter1.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapter1.MyViewHolder holder, final int position) {
        final CityVo cityWeather = weatherList.get(position);
        holder.date.setText(cityWeather.getDate());
        holder.city.setText(cityWeather.getCityname()+" ,"+cityWeather.getCountry());
        holder.temp.setText(cityWeather.getTemperature());

        if(cityWeather.getFavorite().equalsIgnoreCase("true")){
            holder.button.setBackgroundResource(R.drawable.star_gold);
        }else{
            holder.button.setBackgroundResource(R.drawable.star_gray);
        }

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cityWeather.getFavorite().equalsIgnoreCase("true")){
                    holder.button.setBackgroundResource(R.drawable.star_gray);
                    cityWeather.setFavorite("false");
                }else{
                    holder.button.setBackgroundResource(R.drawable.star_gold);
                    cityWeather.setFavorite("true");
                }
                setFavourite(cityWeather);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                removeCity(cityWeather);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public void setFavourite(CityVo cityVo){
        //FirebaseApp.initializeApp(MainActivity.this);
        Log.d("cityVo",cityVo.getId()+"");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("cities").child(cityVo.getId()).setValue(cityVo);
    }

    public void removeCity(CityVo cityVo){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d("cities",cityVo.getId()+"");
        mDatabase.child("cities").child(cityVo.getId()).removeValue();
    }


}
