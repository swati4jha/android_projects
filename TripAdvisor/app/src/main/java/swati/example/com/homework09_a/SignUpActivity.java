package swati.example.com.homework09_a;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText editTextf,editTextl,email,password;
    Button buttonSign;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        editTextf = (EditText) findViewById(R.id.editTextf);
        editTextl = (EditText) findViewById(R.id.editTextl);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        buttonSign = (Button) findViewById(R.id.buttonSign);
        buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(SignUpActivity.this);
                pd.setCancelable(false);
                pd.show();
                if(editTextf.getText()!=null && editTextf.getText().toString()!=null
                        && !editTextf.getText().toString().trim().isEmpty() &&
                        editTextl.getText()!=null && editTextl.getText().toString()!=null
                        && !editTextl.getText().toString().trim().isEmpty() &&
                        email.getText()!=null && email.getText().toString()!=null
                        && !email.getText().toString().trim().isEmpty()&&
                        password.getText()!=null && password.getText().toString()!=null
                        && !password.getText().toString().trim().isEmpty()){
                    signup(email.getText().toString().trim(),password.getText().toString().trim(),
                            editTextf.getText().toString().trim(),editTextl.getText().toString().trim());

                }
                else{
                    Toast.makeText(SignUpActivity.this,"Please fill up all the details",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void signup(String email, String password, final String fname, final String lname) {
        Log.d("Sign:",email+password);
        mAuth.createUserWithEmailAndPassword(email,password).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            createProfile(user,fname,lname);
                            pd.dismiss();
                            Toast.makeText(SignUpActivity.this,"Account created. Please login to continue.",Toast.LENGTH_LONG).show();
                            finish();

                        } else{
                            pd.dismiss();
                            Log.d("Signup failed",task.getException().getMessage());
                            Toast.makeText(SignUpActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



    private void createProfile(FirebaseUser user, String fname, String lname) {
            if (user != null) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(fname)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Profile", "User profile updated.");
                                }
                            }
                        });
            User us = new User();
            us.setId(user.getUid());
            us.setEmail(user.getEmail());
            us.setFirstName(fname);
            us.setDisplayName(fname);
            us.setLastName(lname);
            mDatabase.child("user").child(user.getUid()).setValue(us);
        }
    }

}
