package swati.example.com.homework09_a;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendsDetailActivity extends AppCompatActivity {

    TextView name;
    ImageView im;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList<Trip> tripsList;
    private RecyclerView mRecyclerView;
    private NewTripRecyclerAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private ArrayList<String> tripIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_detail2);
        String userId = getIntent().getStringExtra("userid");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        name = (TextView) findViewById(R.id.textViewDisplayName);
        im = (ImageView) findViewById(R.id.imageButton) ;
        getProfile(userId);
        getTrips(userId);

        final EditText search = (EditText) findViewById(R.id.editTextSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(search.getText()!=null && !search.getText().toString().trim().isEmpty()){
                    Intent intent = new Intent(FriendsDetailActivity.this,SearchActivity.class);
                    intent.putExtra("SearchText", search.getText().toString());
                    startActivity(intent);
                }
            }
        });

    }

    private void getProfile(String id) {
        DatabaseReference ref = mDatabase.child("user");
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fuser!=null){
            String email = fuser.getEmail();
            Query phoneQuery = ref.orderByChild("id").equalTo(id);
            phoneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        User user = new User();
                        user = singleSnapshot.getValue(User.class);
                        Log.d("USer",user.toString());
                        if(user!=null){
                            if(user.getFirstName()!=null && !user.getFirstName().isEmpty()){
                                name.setText(user.getFirstName());
                            }
                            if(user.getPhoto()!=null){
                                Picasso.with(im.getContext()).load(user.getPhoto()).into(im);
                            }

                        }

                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("onCancelled", databaseError.toException().getMessage());
                }
            });
        }

    }

    private void getTrips(String id) {
        //final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference ref = mDatabase.child("chat");
            Query query = ref.orderByChild("userId").equalTo(id);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    tripIds = new ArrayList<String>();
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        UserTrip userTrip = noteDataSnapshot.getValue(UserTrip.class);
                        if(userTrip!=null){
                            tripIds.add(userTrip.getTripId());
                        }
                    }
                    if(tripIds!=null && !tripIds.isEmpty() && tripIds.size()>0){
                        //notesList = sortUserPriority(notesList);
                        getTripDetails(tripIds);
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("getTrips:onCancelled", databaseError.toException().toString());

                }
            });

    }

    private void getTripDetails(final ArrayList<String> tripIds) {
            DatabaseReference ref = mDatabase.child("trips");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    tripsList = new ArrayList<Trip>();
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        Trip trip = noteDataSnapshot.getValue(Trip.class);
                        if(trip!=null){
                            for(String tripid : tripIds){
                                if(tripid.equals(trip.getId())){
                                    tripsList.add(trip);
                                }
                            }
                        }
                    }
                    if(tripsList!=null && !tripsList.isEmpty() && tripsList.size()>0){
                        setTripDetails(tripsList);
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("getTrips:onCancelled", databaseError.toException().toString());

                }
            });
    }


    public void setTripDetails(ArrayList<Trip> details) {
        if(details!=null && !details.isEmpty() && details.size()>0){
            for(Trip trip:details){
                Log.d("SearchResult:",trip.getId());
            }
            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            mLinearLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);

            mAdapter = new NewTripRecyclerAdapter(details);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
        }
    }
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        Intent intent = new Intent(FriendsDetailActivity.this,ProfileActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.mytrips:
                        Intent intent12 = new Intent(FriendsDetailActivity.this,TripActivity.class);
                        startActivity(intent12);
                        return true;
                    case R.id.myFriends:
                        Intent intent1 = new Intent(FriendsDetailActivity.this,RequestActivity.class);
                        intent1.putExtra("Page","Accepted");
                        startActivity(intent1);
                        return true;
                    case R.id.requests:
                        Intent intent2 = new Intent(FriendsDetailActivity.this,RequestActivity.class);
                        intent2.putExtra("Page","Resquest");
                        startActivity(intent2);
                        return true;
                    case R.id.logout:
                        Intent intent23 = new Intent(getApplicationContext(), MainActivity.class);
                        intent23.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent23.putExtra("EXIT", true);
                        startActivity(intent23);
                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.main_menu, popup.getMenu());
        popup.show();
    }

}
