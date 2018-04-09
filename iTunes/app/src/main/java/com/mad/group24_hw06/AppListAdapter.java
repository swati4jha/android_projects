package com.mad.group24_hw06;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * authors: Mihai Mehedint, Swati Jha
 * assignment: HW6
 * file name: AppListAdapter.java
 */

public class AppListAdapter extends ArrayAdapter<App> {
    List<App> mData;
    Context mContext;
    int mResource;
    public AppListAdapter(Context context, int resource, List<App> objects) {
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
        App app= mData.get(position);
        if(app.getFavorite()  != null && app.getFavorite().equalsIgnoreCase("yes")){
            Log.d("demofav:",app.toString());
            ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.blackstar);
        }
        else{
            ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.whitestar);
        }
        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(app.getName()+" - "+app.getRights());
        title.setClickable(false);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        price.setText(app.getPrice());
        price.setClickable(false);
        ImageView thumb = (ImageView)convertView.findViewById(R.id.thumbImage);
        //thumb.setImageBitmap(app.getImage());
        Picasso.with(getContext())
                .load(app.getImageUrl())
                .into(thumb);
        thumb.setClickable(false);
        //TextView colorId = (TextView) convertView.findViewById(R.id.colorId);
        //colorId.setText(app.colorName);

        title.setTextColor(android.graphics.Color.parseColor("#0000FF"));
        price.setTextColor(android.graphics.Color.parseColor("#00FF00"));
        if(position%2 ==0){
            convertView.setBackgroundColor(android.graphics.Color.WHITE);
        }else {
            convertView.setBackgroundColor(Color.GRAY);
        }


        return convertView;
    }
}
