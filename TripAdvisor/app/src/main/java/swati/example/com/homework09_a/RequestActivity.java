package swati.example.com.homework09_a;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList<User> usersList;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    HashMap<String,String> requestedIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String page=getIntent().getStringExtra("Page");
        Log.d("Page",page);

        TextView title = (TextView) findViewById(R.id.textViewFriends);
        if(page.equals("Resquest")){
            title.setText("Pending Confirmations");
        } else {
            title.setText("My Friends");
        }
        getRequest(page);
        final EditText search = (EditText) findViewById(R.id.editTextSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(search.getText()!=null && !search.getText().toString().trim().isEmpty()){
                    Intent intent = new Intent(RequestActivity.this,SearchActivity.class);
                    intent.putExtra("SearchText", search.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    private void getRequest(final String page) {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null){
            DatabaseReference ref = mDatabase.child("friend");
            Log.d("Cur",currentUser.getUid());
            Query query = ref.orderByChild("target").equalTo(currentUser.getUid());
            query.addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    requestedIds = new HashMap<String, String>();
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        Friends friend = noteDataSnapshot.getValue(Friends.class);
                        if(friend!=null && page!=null){
                            if(page.equals("Resquest") && friend.getStatus().equals("requested")){
                                requestedIds.put(friend.getSource(),friend.getId());
                            } else if(page.equals("Accepted") && friend.getStatus().equals("accepted")){
                                Log.d("fff",friend.getSource()+"::"+friend.getId());
                                requestedIds.put(friend.getSource(),friend.getId());
                            }
                        }

                    }
                    if(requestedIds!=null && !requestedIds.isEmpty() && requestedIds.size()>0 && page.equals("Resquest")){
                        //notesList = sortUserPriority(notesList);
                        getPendingRequest(requestedIds,currentUser,page);
                    } else if (page.equals("Accepted")){
                        getRequestMy(page);
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("getUser:onCancelled", databaseError.toException().toString());

                }
            });


        }
    }

    private void getRequestMy(final String page) {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null){
            DatabaseReference ref = mDatabase.child("friend");
            Log.d("Cur",currentUser.getUid());
            Query query = ref.orderByChild("source").equalTo(currentUser.getUid());
            query.addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    requestedIds = new HashMap<String, String>();
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        Friends friend = noteDataSnapshot.getValue(Friends.class);
                        if(friend!=null && page!=null){
                            if(page.equals("Accepted") && friend.getStatus().equals("accepted")){
                                Log.d("fff",friend.getSource()+"::"+friend.getId());
                                requestedIds.put(friend.getTarget(),friend.getId());
                            }
                        }

                    }
                    if(requestedIds!=null && !requestedIds.isEmpty() && requestedIds.size()>0){
                        //notesList = sortUserPriority(notesList);
                        getPendingRequest(requestedIds,currentUser,page);
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("getUser:onCancelled", databaseError.toException().toString());

                }
            });
        }
    }

    private void getPendingRequest(final HashMap<String, String> requestedIds, FirebaseUser currentUser, final String page) {
        if(currentUser!=null){
            mDatabase.child("user").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    usersList = new ArrayList<User>();
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        User user = noteDataSnapshot.getValue(User.class);
                        Log.d("abc",user.getId());
                        if(user!=null && requestedIds.get(user.getId())!=null){
                            if(page.equals("Resquest")){
                                user.setStatus("requested");
                                user.setStatus_id(requestedIds.get(user.getId()));
                            } else if(page.equals("Accepted")){
                                user.setStatus("accepted");
                                user.setStatus_id(requestedIds.get(user.getId()));
                            }

                            usersList.add(user);
                        }
                    }
                    if(usersList!=null && !usersList.isEmpty() && usersList.size()>0){
                        //notesList = sortUserPriority(notesList);
                        setSearchDetails(usersList);
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("getUser:onCancelled", databaseError.toException().toString());

                }
            });
        }
    }

    public void setSearchDetails(ArrayList<User> details) {
        if(details!=null && !details.isEmpty() && details.size()>0){
            for(User user:details){
                Log.d("SearchResult:",user.getEmail());
            }
            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            mLinearLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);

            mAdapter = new RecyclerAdapter(details);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setLayoutManager(mLinearLayoutManager);

        }else{
            Toast.makeText(RequestActivity.this,"No record found!!",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        Intent intent3 = new Intent(RequestActivity.this,ProfileActivity.class);
                        startActivity(intent3);
                        return true;
                    case R.id.mytrips:
                        Intent intent12 = new Intent(RequestActivity.this,TripActivity.class);
                        startActivity(intent12);
                        return true;
                    case R.id.myFriends:
                        Intent intent1 = new Intent(RequestActivity.this,RequestActivity.class);
                        intent1.putExtra("Page","Accepted");
                        startActivity(intent1);
                        return true;
                    case R.id.requests:
                        Intent intent = new Intent(RequestActivity.this,RequestActivity.class);
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

}
