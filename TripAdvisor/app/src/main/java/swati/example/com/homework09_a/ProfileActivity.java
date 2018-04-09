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
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
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

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextView fname,lname,gender;
    EditText firstname,lastname;
    ImageView im;
    RadioGroup mRadioGroup;
    RadioButton rb;
    Button update;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseStorage mStorage;
    private static int RESULT_LOAD_IMAGE = 1;
    private static Uri downloadUrl;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fname=(TextView) findViewById(R.id.textViewName);
        fname.setOnClickListener(this);
        lname=(TextView) findViewById(R.id.textViewLName);
        lname.setOnClickListener(this);
        gender=(TextView) findViewById(R.id.textViewGender);
        gender.setOnClickListener(this);
        im = (ImageView) findViewById(R.id.imageButton);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();
        getProfile();
        update = (Button)findViewById(R.id.buttonUpdateProfile);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(ProfileActivity.this);
                pd.setCancelable(false);
                pd.show();
                updateProfile();
            }
        });

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

        final EditText search = (EditText) findViewById(R.id.editTextSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(search.getText()!=null && !search.getText().toString().trim().isEmpty()){
                    Intent intent = new Intent(ProfileActivity.this,SearchActivity.class);
                    intent.putExtra("SearchText", search.getText().toString());
                    startActivity(intent);
                }
            }
        });

    }

    private void getProfile() {
        DatabaseReference ref = mDatabase.child("user");
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fuser!=null){
            String email = fuser.getEmail();
            Query phoneQuery = ref.orderByChild("email").equalTo(fuser.getEmail());
            phoneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        User user = new User();
                        user = singleSnapshot.getValue(User.class);
                        Log.d("USer",user.toString());
                        if(user!=null){
                            if(user.getFirstName()!=null && !user.getFirstName().isEmpty()){
                                fname.setText(user.getFirstName());
                            }else{
                                fname.setText("Enter First Name");
                            }
                            if(user.getLastName()!=null && !user.getLastName().isEmpty()){
                                lname.setText(user.getLastName());
                            }else{
                                lname.setText("Enter Last Name");
                            }
                            if(user.getGender()!=null && !user.getGender().isEmpty()){
                                gender.setText(user.getGender());
                            }else{
                                gender.setText("Set Gender?");
                            }


                            if(user.getPhoto()!=null){
                                Picasso.with(im.getContext()).load(user.getPhoto()).into(im);
                            }

                        }
                        else{
                            fname.setText("Enter First Name");
                            lname.setText("Enter Last Name");
                            gender.setText("Set Gender?");
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

    private void updateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            firstname = (EditText) findViewById(R.id.editTextName);
            lastname = (EditText) findViewById(R.id.editTextLName);
            im = (ImageView) findViewById(R.id.imageButton);
            mRadioGroup = (RadioGroup) findViewById(R.id.gender);
            int checkedRadioButtonId = mRadioGroup.getCheckedRadioButtonId();

            User us = new User();
            if(checkedRadioButtonId!=-1){
                rb = (RadioButton)findViewById(checkedRadioButtonId);
                us.setGender(rb.getText().toString());
            }else{
                us.setGender(gender.getText().toString());
            }
            us.setId(user.getUid());
            us.setEmail(user.getEmail());

            if(firstname.getText()!=null && firstname.getText().toString()!=null && !firstname.getText().toString().isEmpty()){
                us.setFirstName(firstname.getText().toString());
                us.setDisplayName(firstname.getText().toString());
            } else{
                us.setFirstName(fname.getText().toString());
                us.setDisplayName(fname.getText().toString());
            }

            if(lastname.getText()!=null && lastname.getText().toString()!=null && !lastname.getText().toString().isEmpty()){
                us.setLastName(lastname.getText().toString());
            } else{
                us.setLastName(lname.getText().toString());
            }




            //Log.d("Image12",downloadUrl.toString());
            if(downloadUrl!=null && downloadUrl.toString()!=null){
                us.setPhoto(downloadUrl.toString());
            }

            mDatabase.child("user").child(user.getUid()).setValue(us);
        }
        pd.dismiss();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onClick(View v) {
        ViewSwitcher switcher = (ViewSwitcher) v.getParent();
        switcher.showNext();
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        startActivity(getIntent());
                        return true;
                    case R.id.mytrips:
                        Intent intent12 = new Intent(ProfileActivity.this,TripActivity.class);
                        startActivity(intent12);
                        return true;
                    case R.id.myFriends:
                        Intent intent1 = new Intent(ProfileActivity.this,RequestActivity.class);
                        intent1.putExtra("Page","Accepted");
                        startActivity(intent1);
                        return true;
                    case R.id.requests:
                        Intent intent = new Intent(ProfileActivity.this,RequestActivity.class);
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
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                StorageReference storageRef = mStorage.getReference();
                StorageReference imagesRef = storageRef.child("user/"+user.getUid()+"/photo/photo.jpg");
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
                    }
                });
            }catch (FileNotFoundException ex){
                Log.d("Exception",ex.toString());
            }


        }
    }
}
