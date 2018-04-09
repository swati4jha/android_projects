package swati.example.com.homework09_a;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swati on 4/28/2017.
 */

public class PlaceRecyclerAdapter extends RecyclerView.Adapter<PlaceRecyclerAdapter.MyViewHolder> {

    private List<PlaceLocal> placeLocalList;
    private DatabaseReference mDatabase;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewPlace;
        public ImageButton buttonPlaceDelete;

        public MyViewHolder(View view) {
            super(view);
            textViewPlace = (TextView) view.findViewById(R.id.textViewPlace);
            buttonPlaceDelete = (ImageButton) view.findViewById(R.id.buttonPlaceDelete);
        }
    }


    public PlaceRecyclerAdapter(ArrayList<PlaceLocal> placeLocalList) {
        this.placeLocalList = placeLocalList;
    }

    @Override
    public PlaceRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);

        return new PlaceRecyclerAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final PlaceRecyclerAdapter.MyViewHolder holder, final int position) {
        final PlaceLocal placeLocal = placeLocalList.get(position);
        holder.textViewPlace.setText(placeLocal.getName());
        holder.buttonPlaceDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder diaolg = new AlertDialog.Builder(mContext);
                diaolg.setTitle("Remove Place from the trip")
                        .setMessage("Do you want to remove the place from the trip?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removePlace(position,placeLocal.getId());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                diaolg.show();
            }
        });
    }

    private void removePlace(int position, String tripid) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        List<PlaceLocal> placeList = new ArrayList<>();
        placeList = placeLocalList;
        placeList.remove(position);
        mDatabase.child("trips").child(tripid).child("placeList").setValue(placeList);
    }

    @Override
    public int getItemCount() {
        return placeLocalList.size();
    }

}

