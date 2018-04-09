package com.mad.inclass07;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * authors: Mihai Mehedint, Swati Jha
 * assignment: HW6
 * file name: AppListAdapter.java
 */

public class ArrayListAdapter extends ArrayAdapter<Note> {
    List<Note> mData;
    Context mContext;
    int mResource;
    public ArrayListAdapter(Context context, int resource, List<Note> objects) {
        super(context, resource, objects);//resource is a layout for each row item
        this.mContext = context;
        this.mData = objects;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }
        final Note note= mData.get(position);

        TextView title = (TextView) convertView.findViewById(R.id.textViewNote);
        title.setText(note.getSubject());
        title.setClickable(false);
        TextView priority = (TextView) convertView.findViewById(R.id.textViewPriority);
        Log.d("Priority",note.getPriority());
        priority.setText(note.getPriority()+ " Priority");
        TextView time = (TextView) convertView.findViewById(R.id.textViewTime);
        time.setText(Ptime.PtimeFormatter.getPtimeFrom(note.getTime()));
        priority.setClickable(false);
        String msg;
        if(note.getStatus().equalsIgnoreCase("Pending")){
            msg = "Do you really want to delete the task?";
        }
        else{
            msg = "Do you really want to make it as pending?";
        }
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo","checked");
                final DatabaseDataManager dbManager = new DatabaseDataManager(getContext());
                if(note.getStatus().equalsIgnoreCase("Pending")){
                    new AlertDialog.Builder(getContext())
                            .setMessage("Do you really want to delete the task?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    note.setStatus("Completed");
                                    dbManager.updateNote(note);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
                else{
                    new AlertDialog.Builder(getContext())
                            .setMessage("Do you really want to make it as pending?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    note.setStatus("Pending");
                                    dbManager.updateNote(note);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }


            }
        });

        if(position%2 ==0){
            convertView.setBackgroundColor(android.graphics.Color.WHITE);
        }else {
            convertView.setBackgroundColor(Color.GRAY);
        }


        return convertView;
    }
}
