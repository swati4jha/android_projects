package swati.example.com.homework09_a;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList<Chat> chatsList;
    private RecyclerView mRecyclerView;
    private ChatRecyclerAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseUser currentUser;
    private EditText msg;
    private TextView textViewTripName;
    private static Uri uploadUrl;
    private static int RESULT_LOAD_IMAGE = 1;
    private ImageView im;
    private FirebaseStorage mStorage;

    String tripID,title;
    public static final String URL_STORAGE_REFERENCE = "gs://alefirebase-b6a81.appspot.com";
    public static final String FOLDER_STORAGE_IMG = "images";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tripID = getIntent().getStringExtra("TripID");
        title = getIntent().getStringExtra("TripName");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorage = FirebaseStorage.getInstance();


        msg = (EditText) findViewById(R.id.editTextMsg);
        textViewTripName = (TextView) findViewById(R.id.textViewTripName);
        textViewTripName.setText(title);
        getChats(tripID,currentUser.getUid());
        im = (ImageView) findViewById(R.id.imageViewPhoto);
        im.setVisibility(View.INVISIBLE);
        ImageButton send = (ImageButton) findViewById(R.id.imageButtonSend);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(msg.getText()!=null && msg.getText().toString()!=null && !msg.getText().toString().isEmpty()){
                    Chat chat = new Chat();
                    chat.setSentBy(currentUser.getDisplayName());
                    chat.setText(msg.getText().toString());
                    chat.setDate(new Date().toString());
                    chat.setTripID(tripID);
                    chat.setPhoto("");
                    if(chatsList==null){
                        chatsList = new ArrayList<Chat>();
                    }
                    chatsList.add(chat);
                    sendMessage(tripID,chat);
                }

            }
        });

        ImageButton upload = (ImageButton) findViewById(R.id.imageButtonUpload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        final EditText search = (EditText) findViewById(R.id.editTextSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(search.getText()!=null && !search.getText().toString().trim().isEmpty()){
                    Intent intent = new Intent(ChatActivity.this,SearchActivity.class);
                    intent.putExtra("SearchText", search.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    private void getImage() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    private void sendMessage(final String tripId, final Chat chat) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child("chat");
        ref.child(tripId+currentUser.getUid()).child("chatList").setValue(chatsList);



        final Query query = ref.orderByChild("tripId").equalTo(tripId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserTrip trip = new UserTrip();
                        ArrayList<Chat> chats = new ArrayList<Chat>();
                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                            trip = noteDataSnapshot.getValue(UserTrip.class);
                            if(trip!=null && !trip.getUserId().equals(currentUser.getUid())){
                                chats = trip.getChatList();
                                chats.add(chat);
                                query.getRef().child("chatList").setValue(chats);
                                query.getRef().child(tripId+trip.getUserId()).child("chatList").setValue(chatsList);
                            }
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("getchat:onCancelled", databaseError.toException().toString());

                    }
                });
    }

    private void getChats(String tripID, String uid) {
        DatabaseReference ref = mDatabase.child("chat");
        Query query = ref.orderByChild("tripId").equalTo(tripID+uid);
        query.getRef().orderByChild("userId").equalTo(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserTrip trip = new UserTrip();
                        for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                            trip = noteDataSnapshot.getValue(UserTrip.class);
                            if(trip!=null){
                                chatsList = trip.getChatList();
                            }
                            if(chatsList!=null && !chatsList.isEmpty() && chatsList.size()>0){
                                //notesList = sortUserPriority(notesList);
                                setChatDetails(chatsList);
                            }
                            else{
                                Toast.makeText(ChatActivity.this,"No chats result found!!",Toast.LENGTH_LONG).show();
                                //finish();
                            }
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("getchat:onCancelled", databaseError.toException().toString());

                    }
                });
    }

    public void setChatDetails(ArrayList<Chat> details) {
        if(details!=null && !details.isEmpty() && details.size()>0){
            for(Chat chat:details){
                Log.d("chat:",chat.getText());
            }
            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            mLinearLayoutManager = new LinearLayoutManager(this);
            mAdapter = new ChatRecyclerAdapter(details);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        StorageReference storageRef = mStorage.getReference();
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            if (selectedImage != null){
                sendFileFirebase(storageRef,selectedImage);
            }else{
                //URI IS NULL
            }
        }
    }

    private void sendFileFirebase(StorageReference storageReference, final Uri file){
        if (storageReference != null){
            final String name = DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString();
            StorageReference imageGalleryRef = storageReference.child(name+"_gallery");
            UploadTask uploadTask = imageGalleryRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Photo","onFailure sendFileFirebase "+e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i("Photo1","onSuccess sendFileFirebase");
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Chat chat = new Chat();
                    chat.setSentBy(currentUser.getDisplayName());
                    chat.setText("");
                    chat.setDate(new Date().toString());
                    chat.setTripID(tripID);
                    chat.setPhoto(downloadUrl.toString());
                    chatsList.add(chat);
                    sendMessage(tripID,chat);
                   /* FileModel fileModel = new FileModel("img",downloadUrl.toString(),name,"");
                    ChatModel chatModel = new ChatModel(userModel,"", Calendar.getInstance().getTime().getTime()+"",fileModel);
                    mDatabase.child(CHAT_REFERENCE).push().setValue(chatModel);*/
                }
            });
        }else{
            //IS NULL
        }

    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        Intent intent3 = new Intent(ChatActivity.this,ProfileActivity.class);
                        startActivity(intent3);
                        return true;
                    case R.id.mytrips:
                        Intent intent12 = new Intent(ChatActivity.this,TripActivity.class);
                        startActivity(intent12);
                        return true;
                    case R.id.myFriends:
                        Intent intent1 = new Intent(ChatActivity.this,RequestActivity.class);
                        intent1.putExtra("Page","Accepted");
                        startActivity(intent1);
                        return true;
                    case R.id.requests:
                        Intent intent = new Intent(ChatActivity.this,RequestActivity.class);
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
