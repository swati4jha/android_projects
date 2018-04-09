package swati.example.com.homework09_a;

/**
 * Created by swati on 4/3/2017.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.MyViewHolder> {

    private ArrayList<Chat> chatsList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewMsg, textViewSentBy,textViewTime;
        public ImageButton imageButtonDelMsg;
        public ImageView imageViewChat;
        public RelativeLayout rl;

        public MyViewHolder(View view) {
            super(view);
            textViewMsg = (TextView) view.findViewById(R.id.textViewMsg);
            textViewSentBy = (TextView) view.findViewById(R.id.textViewSentBy);
            textViewTime = (TextView) view.findViewById(R.id.textViewTime);
            imageButtonDelMsg = (ImageButton) view.findViewById(R.id.imageButtonDelMsg);
            imageViewChat = (ImageView) view.findViewById(R.id.imageViewChat);
            rl = (RelativeLayout) view.findViewById(R.id.relLayout);
        }
    }


    public ChatRecyclerAdapter(ArrayList<Chat> chatsList) {
        this.chatsList = chatsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Chat chat = chatsList.get(position);
        holder.textViewMsg.setText(chat.getText());
        holder.textViewSentBy.setText(chat.getSentBy());
        holder.textViewTime.setText(Ptime.PtimeFormatter.getPtimeFrom(chat.getDate()));
        if(chat.getPhoto()!=null && !chat.getPhoto().isEmpty()){
            Picasso.with(holder.imageViewChat.getContext()).load(chat.getPhoto()).into(holder.imageViewChat);
        }else{
            holder.imageViewChat.setVisibility(View.GONE);
        }

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chat.getSentBy()!=null && chat.getSentBy().equals(currentUser.getDisplayName())){
            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            linearParams.setMargins(180, 0, 0, 0);
            holder.rl.setLayoutParams(linearParams);
            holder.rl.setBackgroundColor(Color.rgb(211,211,211));
            holder.rl.requestLayout();

        }

        holder.imageButtonDelMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteChat(position,chat.getTripID());
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }



    public void deleteChat(final int pos, String tripId){

        chatsList.remove(pos);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child("chat");
        ref.child(tripId+currentUser.getUid()).child("chatList").setValue(chatsList);
    }
}
