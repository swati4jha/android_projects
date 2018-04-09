package swati.example.com.homework09_a;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;

public class CreateTripActivity extends AppCompatActivity {

    EditText title,location;
    ImageView im;
    Button create;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseStorage mStorage;
    private static int RESULT_LOAD_IMAGE = 1;
    private static Uri downloadUrl;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();

        title=(EditText) findViewById(R.id.editTextTripTitle);
        location=(EditText) findViewById(R.id.editTextTripLocation);
        im = (ImageView) findViewById(R.id.imageButton);

        ImageButton buttonLoadImage = (ImageButton) findViewById(R.id.changePhoto);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        create = (Button)findViewById(R.id.buttonCreateTrip);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(CreateTripActivity.this);
                pd.setCancelable(false);
                pd.show();
                uploadImage(title.getText().toString());
            }
        });

        final EditText search = (EditText) findViewById(R.id.editTextSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(search.getText()!=null && !search.getText().toString().trim().isEmpty()){
                    Intent intent = new Intent(CreateTripActivity.this,SearchActivity.class);
                    intent.putExtra("SearchText", search.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    private void createTrip() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Trip trip = new Trip();
            if(title!=null && title.getText()!=null && title.getText().toString().trim()!=null && !title.getText().toString().trim().isEmpty() &&
            location!=null && location.getText()!=null && location.getText().toString().trim()!=null && !location.getText().toString().trim().isEmpty()){
                trip.setLocation(location.getText().toString());
                trip.setTitle(title.getText().toString());
                trip.setCreatedBy(user.getEmail());
                trip.setCreatedByName(user.getDisplayName());
                Chat chat = new Chat();
                chat.setDate(new Date().toString());
                chat.setText("Trip Created");
                chat.setSentBy(user.getDisplayName());
                chat.setPhoto("");
                trip.setId(mDatabase.child("trips").push().getKey());
                chat.setTripID(trip.getId());
                ArrayList<Chat> chatlist = new ArrayList<Chat>();
                chatlist.add(chat);
                trip.setChatList(chatlist);
                //uploadImage(title.getText().toString());
                if(downloadUrl!=null && downloadUrl.toString()!=null){
                    trip.setPhoto(downloadUrl.toString());
                }

                mDatabase.child("trips").child(trip.getId()).setValue(trip);
                
                updateUserWithChat(chatlist,trip.getId(),user);
            }

        }

    }

    private void updateUserWithChat(ArrayList<Chat> chatlist, String tripId, FirebaseUser currentUser) {
        UserTrip utrip = new UserTrip();
        utrip.setChatList(chatlist);
        utrip.setTripId(tripId);
        utrip.setUserId(currentUser.getUid());
        utrip.setUserName(currentUser.getDisplayName());
        utrip.setId(tripId+currentUser.getUid());
        mDatabase.child("chat").child(utrip.getId()).setValue(utrip);
        pd.dismiss();
        Toast.makeText(CreateTripActivity.this,"Trip Created",Toast.LENGTH_LONG).show();
        finish();
    }


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        Intent intent3 = new Intent(CreateTripActivity.this,ProfileActivity.class);
                        startActivity(intent3);
                        return true;
                    case R.id.mytrips:
                        Intent intent12 = new Intent(CreateTripActivity.this,TripActivity.class);
                        startActivity(intent12);
                        return true;
                    case R.id.myFriends:
                        Intent intent1 = new Intent(CreateTripActivity.this,RequestActivity.class);
                        intent1.putExtra("Page","Accepted");
                        startActivity(intent1);
                        return true;
                    case R.id.requests:
                        Intent intent = new Intent(CreateTripActivity.this,RequestActivity.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            im = (ImageView) findViewById(R.id.imageButton);
            Log.d("Picture",picturePath);
            try{
                im.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage)));
            }catch (FileNotFoundException ex){
                Log.d("Exception",ex.toString());
            }
        }
    }

    public void uploadImage(String title){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference storageRef = mStorage.getReference();
        StorageReference imagesRef = storageRef.child("user/"+user.getUid()+title+"/photo/photo.jpg");
        im.setDrawingCacheEnabled(true);
        im.buildDrawingCache();
        Bitmap bitmap = im.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data1 = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data1);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("Image","fails");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                downloadUrl = taskSnapshot.getDownloadUrl();

                Log.d("Image",downloadUrl.toString());
                createTrip();
            }
        });
    }
}
