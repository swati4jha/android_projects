package swati.example.com.homework09_a;

/**
 * Created by swati on 4/3/2017.
 */
import android.content.Context;
import android.content.Intent;
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

public class NewTripRecyclerAdapter extends RecyclerView.Adapter<NewTripRecyclerAdapter.MyViewHolder> {

    private List<Trip> tripsList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private Context mContext;
    private boolean flag;

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


    public NewTripRecyclerAdapter(ArrayList<Trip> tripsList) {
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
        holder.unjoin.setVisibility(View.GONE);
        holder.delete.setVisibility(View.GONE);
        Picasso.with(holder.dp.getContext()).load(trip.getPhoto()).into(holder.dp);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(checkUserAlreadyJoined(trip.getId())){
            holder.join.setVisibility(View.GONE);
        }
        if (trip.getCreatedBy().equals(currentUser.getEmail())) {
            holder.join.setVisibility(View.GONE);
        }else{
            holder.join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    joinTrip(currentUser, trip.getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return tripsList.size();
    }

    public void joinTrip(FirebaseUser currentUser, String tripid){
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            UserTrip utrip = new UserTrip();
            ArrayList<Chat> chats = new ArrayList<Chat>();
            utrip.setChatList(chats);
            utrip.setTripId(tripid);
            utrip.setUserId(currentUser.getUid());
            utrip.setUserName(currentUser.getDisplayName());
            utrip.setId(tripid+currentUser.getUid());
            mDatabase.child("chat").child(utrip.getId()).setValue(utrip);
            Toast.makeText(mContext,"Trip joined",Toast.LENGTH_LONG).show();
    }

    private boolean checkUserAlreadyJoined(String tripID) {
        flag = false;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = mDatabase.child("chat");
        Query query = ref.orderByChild("userId").equalTo(currentUser.getUid());
        query.getRef().orderByChild("tripId").equalTo(tripID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    UserTrip userTrip = noteDataSnapshot.getValue(UserTrip.class);
                    flag = true;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getTrips:onCancelled", databaseError.toException().toString());

            }
        });
        return flag;

    }

}
