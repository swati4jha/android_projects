package swati.example.com.homework09_a;

/**
 * Created by swati on 4/3/2017.
 */
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.getIntent;

public class TripRecyclerAdapter extends RecyclerView.Adapter<TripRecyclerAdapter.MyViewHolder> {

    private List<Trip> tripsList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, location;
        public ImageButton unjoin, delete,join;
        public ImageView dp;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textTripName);
            location = (TextView) view.findViewById(R.id.textViewLocation);
            unjoin = (ImageButton) view.findViewById(R.id.imageButtonUnjoin);
            join = (ImageButton) view.findViewById(R.id.imageButtonjoin);
            delete = (ImageButton) view.findViewById(R.id.imageButtonDelete);
            dp = (ImageView) view.findViewById(R.id.imageViewTripDp);
        }
    }


    public TripRecyclerAdapter(ArrayList<Trip> tripsList) {
        this.tripsList = tripsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_trip, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Trip trip = tripsList.get(position);
        holder.title.setText(trip.getTitle());
        holder.location.setText(trip.getLocation());
        holder.join.setVisibility(View.GONE);
        Picasso.with(holder.dp.getContext()).load(trip.getPhoto()).into(holder.dp);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (trip.getCreatedBy().equals(currentUser.getEmail())) {
            holder.unjoin.setVisibility(View.GONE);
        } else{
            holder.delete.setVisibility(View.GONE);
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTrip(trip.getId());
            }
        });

        holder.unjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unjoinTrip(currentUser.getUid(), trip.getId());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(mContext,TripDetailActivity.class);
                intent.putExtra("TripID",trip.getId());
                intent.putExtra("TripName",trip.getTitle());
                mContext.startActivity(intent);
                return false;
            }
        });

    }




    @Override
    public int getItemCount() {
        return tripsList.size();
    }




    public void unjoinTrip(String userid, String tripid){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child("chat");
        Query query = ref.orderByChild("userId").equalTo(currentUser.getUid());
        final Query query1 = query.getRef().orderByChild("tripId").equalTo(tripid);

        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    UserTrip trip = noteDataSnapshot.getValue(UserTrip.class);
                    if(trip!=null && trip.getUserId().equals(currentUser.getUid())){
                        query1.getRef().child(trip.getId()).removeValue();
                        Log.d("trip",trip.getId());
                        Log.d("trip",trip.getTripId());
                        Log.d("trip",trip.getUserId());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getTrips:onCancelled", databaseError.toException().toString());

            }
        });
        Toast.makeText(mContext,"Trip unjoined",Toast.LENGTH_LONG).show();
    }

    public void deleteTrip(final String tripid){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child("chat");
        Query query = ref.orderByChild("userId").equalTo(currentUser.getUid());
        final Query query1 = query.getRef().orderByChild("tripId").equalTo(tripid);

        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    UserTrip trip = noteDataSnapshot.getValue(UserTrip.class);
                    if(trip!=null && trip.getUserId().equals(currentUser.getUid())){
                        query1.getRef().child(trip.getId()).removeValue();
                        Log.d("trip",trip.getId());
                        Log.d("trip",trip.getTripId());
                        Log.d("trip",trip.getUserId());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getTrips:onCancelled", databaseError.toException().toString());

            }
        });

        //query1.getRef().removeValue();

        mDatabase.child("trips").child(tripid).removeValue();
        Toast.makeText(mContext,"Trip Deleted.",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(mContext,TripActivity.class);
        mContext.startActivity(intent);
    }
}
