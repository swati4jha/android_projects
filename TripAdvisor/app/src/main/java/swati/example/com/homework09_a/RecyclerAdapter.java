package swati.example.com.homework09_a;

/**
 * Created by swati on 4/3/2017.
 */
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.transition.Visibility;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import java.util.Date;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<User> userList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private Context mContext;
    boolean addFriendFlag = false;
    private Friends friend;
    private boolean flag = false;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageButton add,accept,reject,delete;
        public ImageView dp;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textViewSearcchName);
            add = (ImageButton) view.findViewById(R.id.imageButtonAdd);
            accept = (ImageButton) view.findViewById(R.id.imageButtonAccept);
            reject = (ImageButton) view.findViewById(R.id.imageButtonReject);
            delete = (ImageButton) view.findViewById(R.id.imageButtonDeleteFriend);
            dp = (ImageView) view.findViewById(R.id.imageViewSearchDp);
        }
    }


    public RecyclerAdapter(ArrayList<User> userList) {
        this.userList = userList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final User user = userList.get(position);
        holder.name.setText(user.getDisplayName());
        Log.d("requested12",user.getStatus()+"::"+user.getStatus_id());
        Picasso.with(holder.dp.getContext()).load(user.getPhoto()).into(holder.dp);
        if(user.getStatus()!=null && user.getStatus().equals("requested")){
            holder.add.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
            Log.d("requested",user.getStatus()+"::"+user.getStatus_id());
        } else if(user.getStatus()!=null && user.getStatus().equals("accepted")){
            holder.add.setVisibility(View.GONE);
            holder.accept.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);
        }else{
            holder.accept.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(user.getStatus()!=null && user.getStatus().equals("accepted")){
                    Intent intent = new Intent(mContext,FriendsDetailActivity.class);
                    intent.putExtra("userid",user.getId());
                    mContext.startActivity(intent);
                }

                return false;
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setMessage("Do you want to add user as friend?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                checkifAlreadyRequested(user);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setMessage("Do you want to delete user as friend?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                unFriend(user);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptUser(user);

            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectUser(user);

            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void addFriend(User user){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Friends friend = new Friends();
        friend.setSource(currentUser.getUid());
        friend.setTarget(user.getId());
        friend.setStatus("requested");
        friend.setId(mDatabase.child("friend").push().getKey());
        mDatabase.child("friend").child(friend.getId()).setValue(friend);
        Toast.makeText(mContext,"Friend Request Send.",Toast.LENGTH_LONG).show();
    }

    private void checkifAlreadyRequested(final User user) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase.child("friend").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                    friend = noteDataSnapshot.getValue(Friends.class);
                    if (friend.getSource().equals(currentUser.getUid()) && friend.getTarget().equals(user.getId())) {
                        flag = true;
                    } else if (friend.getSource().equals(user.getId()) && friend.getTarget().equals(currentUser.getUid())) {
                        flag = true;
                    }
                }
                if (flag) {
                    Toast.makeText(mContext, "Already added.", Toast.LENGTH_LONG).show();

                }else{
                    addFriend(user);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getFriends:onCancelled", databaseError.toException().toString());

            }
        });

    }

    public void acceptUser(User user){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Friends friend = new Friends();
        friend.setTarget(currentUser.getUid());
        friend.setSource(user.getId());
        friend.setStatus("accepted");
        friend.setId(user.getStatus_id());
        mDatabase.child("friend").child(user.getStatus_id()).setValue(friend);
        Toast.makeText(mContext,"Friend Request Accepted.",Toast.LENGTH_LONG).show();
        Intent intent1 = new Intent(mContext,RequestActivity.class);
        intent1.putExtra("Page","Accepted");
        mContext.startActivity(intent1);
    }

    public void rejectUser(User user){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("friend").child(user.getStatus_id()).removeValue();
        Toast.makeText(mContext,"Friend Request Deleted.",Toast.LENGTH_LONG).show();
        Intent intent1 = new Intent(mContext,RequestActivity.class);
        intent1.putExtra("Page","Resquest");
        mContext.startActivity(intent1);
    }

    public void unFriend(User user){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("friend").child(user.getStatus_id()).removeValue();
        Toast.makeText(mContext,"Friend Request Deleted.",Toast.LENGTH_LONG).show();
        Intent intent1 = new Intent(mContext,RequestActivity.class);
        intent1.putExtra("Page","Accepted");
        mContext.startActivity(intent1);
    }
}
