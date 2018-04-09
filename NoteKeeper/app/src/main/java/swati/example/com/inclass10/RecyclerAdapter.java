package swati.example.com.inclass10;

/**
 * Created by swati on 4/3/2017.
 */
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<Note> notesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, priority, time;
        public CheckBox cb;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textViewNote);
            priority = (TextView) view.findViewById(R.id.textViewPriority);
            time = (TextView) view.findViewById(R.id.textViewTime);
            cb = (CheckBox) view.findViewById(R.id.checkBox);

        }

    }


    public RecyclerAdapter(ArrayList<Note> notesList) {
        this.notesList = notesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Note note = notesList.get(position);
        holder.title.setText(note.getSubject());
        holder.priority.setText(note.getPriority()+ " Priority");
        holder.time.setText(Ptime.PtimeFormatter.getPtimeFrom(note.getTime()));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setMessage("Do you really want to delete the task?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                note.setStatus("Completed");
                                removeItem(note);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return false;
            }
        });
        if(note.getStatus().equalsIgnoreCase("Completed")){
            holder.cb.setChecked(true);
        }
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo1","checked");
                //final DatabaseDataManager dbManager = new DatabaseDataManager(getContext());
                if(note.getStatus().equalsIgnoreCase("Pending")){
                    new AlertDialog.Builder(v.getContext())
                            .setMessage("Do you really want to mark task as completed?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    note.setStatus("Completed");
                                    note.setTime(new Date().toString());
                                    updateNote(note);

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    holder.cb.setChecked(false);
                                }
                            })
                            .show();
                }
                else{
                    new AlertDialog.Builder(v.getContext())
                            .setMessage("Do you really want to make it as pending?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    note.setStatus("Pending");
                                    updateNote(note);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    holder.cb.setChecked(true);
                                }
                            })
                            .show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void updateNote(Note note){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d("note.get_id()",note.get_id()+"");
        mDatabase.child("notes").child(note.get_id()).setValue(note);
    }

    public void removeItem(Note note) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d("note.get_id()",note.get_id()+"");
        mDatabase.child("notes").child(note.get_id()).removeValue();
        //Toast.makeText(View.getContext(),"Task deleted!!!",Toast.LENGTH_SHORT).show();

    }

}
