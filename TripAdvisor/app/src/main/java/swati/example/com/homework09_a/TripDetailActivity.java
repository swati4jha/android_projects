package swati.example.com.homework09_a;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
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

public class TripDetailActivity extends AppCompatActivity implements PlaceSelectionListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList<String> people;
    private Trip trip;
    private TextView textViewTitle,textViewLoc,textViewCreated;
    private ImageView imageButton;
    private Button unjoin,delete,chat;

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;

    private TextView mPlaceDetailsText;
    private TextView mPlaceAttribution;
    private FirebaseUser currentUser;

    private ArrayList<PlaceLocal> placeList;
    private String tripID;

    private RecyclerView mRecyclerView;
    private PlaceRecyclerAdapter mAdapterRec;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewLoc = (TextView) findViewById(R.id.textViewLoc);
        textViewCreated = (TextView) findViewById(R.id.textViewCreated);
        placeList = new ArrayList<PlaceLocal>();
        imageButton = (ImageView) findViewById(R.id.imageButton);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        tripID = getIntent().getStringExtra("TripID");
        getTripDetails(tripID);

        final EditText search = (EditText) findViewById(R.id.editTextSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(search.getText()!=null && !search.getText().toString().trim().isEmpty()){
                    Intent intent = new Intent(TripDetailActivity.this,SearchActivity.class);
                    intent.putExtra("SearchText", search.getText().toString());
                    startActivity(intent);
                }
            }
        });

        unjoin= (Button) findViewById(R.id.buttonUnjoin);
        delete= (Button) findViewById(R.id.buttonDelete);
        chat= (Button) findViewById(R.id.buttonChat);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        unjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unjoinTrip(currentUser.getUid(),tripID);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTrip(tripID);
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripDetailActivity.this,ChatActivity.class);
                intent.putExtra("TripID",tripID);
                intent.putExtra("TripName",tripID);
                startActivity(intent);
            }
        });

        //Place picker
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setOnPlaceSelectedListener(this);

        ((EditText)findViewById(R.id.place_autocomplete_search_input)).setTextSize(10.0f);


        // Retrieve the TextViews that will display details about the selected place.
        mPlaceDetailsText = (TextView) findViewById(R.id.place_details);
        mPlaceAttribution = (TextView) findViewById(R.id.place_attribution);


        Button viewMap = (Button) findViewById(R.id.buttonViewMap);
        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripDetailActivity.this,MapsActivity.class);
                intent.putExtra("places",placeList);
                startActivity(intent);
            }
        });
    }

    private void getTripDetails(String tripId) {

        DatabaseReference ref = mDatabase.child("trips");
        Query query = ref.orderByChild("id").equalTo(tripId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trip = new Trip();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    trip = noteDataSnapshot.getValue(Trip.class);

                }
                if(trip!=null){
                    textViewTitle.setText(trip.getTitle());
                    textViewLoc.setText(trip.getLocation());
                    textViewCreated.setText(trip.getCreatedByName());
                    Picasso.with(imageButton.getContext()).load(trip.getPhoto()).into(imageButton);
                    setPeople(trip.getId());
                    setPlaces(trip.getPlaceList());
                }

                else{
                    Toast.makeText(TripDetailActivity.this,"No detail found!!",Toast.LENGTH_LONG).show();
                    //finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getTrip:onCancelled", databaseError.toException().toString());

            }
        });
    }

    private void setPlaces(ArrayList<PlaceLocal> details) {
        if(details!=null && !details.isEmpty() && details.size()>0){
            Log.d("setPlaces","setPlaces");
            placeList = new ArrayList<PlaceLocal>();
            placeList = details;
            mRecyclerView = (RecyclerView) findViewById(R.id.placeRecyclerView);
            mLinearLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);

            mAdapterRec = new PlaceRecyclerAdapter(placeList);
            mRecyclerView.setAdapter(mAdapterRec);
            mAdapterRec.notifyDataSetChanged();
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
        }
    }

    private void setPeople(String id) {
        DatabaseReference ref = mDatabase.child("chat");

        Query query = ref.orderByChild("tripId").equalTo(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserTrip utrip = new UserTrip();
                people = new ArrayList<String>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    utrip = noteDataSnapshot.getValue(UserTrip.class);
                    people.add(utrip.getUserName());

                }
                if(people!=null && people.size()>0){
                    mListView = (ListView) findViewById(R.id.listView);
                    mAdapter=new ArrayAdapter<String>(TripDetailActivity.this, R.layout.list_item,people);
                    mListView.setAdapter(mAdapter);
                    mAdapter.setNotifyOnChange(true);
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getTrip:onCancelled", databaseError.toException().toString());

            }
        });
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
        Toast.makeText(TripDetailActivity.this,"Trip unjoined",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(TripDetailActivity.this,TripActivity.class);
        startActivity(intent);
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
        Toast.makeText(TripDetailActivity.this,"Trip Deleted.",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(TripDetailActivity.this,TripActivity.class);
        startActivity(intent);
    }
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        Intent intent3 = new Intent(TripDetailActivity.this,ProfileActivity.class);
                        startActivity(intent3);
                        return true;
                    case R.id.mytrips:
                        Intent intent12 = new Intent(TripDetailActivity.this,TripActivity.class);
                        startActivity(intent12);
                        return true;
                    case R.id.myFriends:
                        Intent intent1 = new Intent(TripDetailActivity.this,RequestActivity.class);
                        intent1.putExtra("Page","Accepted");
                        startActivity(intent1);
                        return true;
                    case R.id.requests:
                        Intent intent = new Intent(TripDetailActivity.this,RequestActivity.class);
                        intent.putExtra("Page","Resquest");
                        startActivity(intent);
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

    /**
     * Callback invoked when a place has been selected from the PlaceAutocompleteFragment.
     */
    @Override
    public void onPlaceSelected(final Place place) {
        Log.d("demo", "Place Selected: " + place.getName());

        // Format the returned place's details and display them in the TextView.
        //mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(), place.getId(),
                //place.getAddress(), place.getPhoneNumber(), place.getWebsiteUri()));
        AlertDialog.Builder diaolg = new AlertDialog.Builder(this);
        diaolg.setTitle("Add Place to the trip")
                .setMessage("Do you want to add the place to the trip?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PlaceLocal placeLocal = new PlaceLocal();
                        placeLocal.setId(tripID);
                        placeLocal.setLatitude(place.getLatLng().latitude);
                        placeLocal.setLongitude(place.getLatLng().longitude);
                        placeLocal.setName(place.getName().toString());
                        placeList.add(placeLocal);
                        addPlacetoTrip();
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

    /**
     * Callback invoked when PlaceAutocompleteFragment encounters an error.
     */
    @Override
    public void onError(Status status) {
        Log.d("demo", "onError: Status = " + status.toString());

        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Helper method to format information about a place nicely.
     */
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.d("demo", res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    private void addPlacetoTrip(){
        trip.setPlaceList(placeList);
        mDatabase.child("trips").child(tripID).setValue(trip);

    }


}
