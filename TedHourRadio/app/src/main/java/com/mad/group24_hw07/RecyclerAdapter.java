package com.mad.group24_hw07;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * authors: Swati Jha, Mihai Mehedint
 * assignment: HW7
 * file name: RecyclerAdapter.java
 */


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.PlaceHolder>{

    private ArrayList<RadioDetails> mRadio;
    //private ArrayList<RadioDetails mRadio;
    private static String mviewType;


    public static class PlaceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //2
        private ImageView mItemImage;
        private TextView mItemDate;
        private TextView mItemTitle;
        private ImageButton mPlayButton;
        private TextView mPlayNow;
        private TextView mGridTitle;
        private RadioDetails mRadio;
        private LinearLayout mLayout;
        private ImageButton mPlayGrid;

        //3
        private static final String PLACE_KEY = "RADIO";

        //4
        public PlaceHolder(View v) {
            super(v);

            mItemImage = (ImageView) v.findViewById(R.id.imageView);
            mItemDate = (TextView) v.findViewById(R.id.textViewDate);
            mItemTitle = (TextView) v.findViewById(R.id.textViewTitle);
            mPlayButton = (ImageButton) v.findViewById(R.id.imageButton);
            mPlayNow = (TextView) v.findViewById(R.id.textViewPlay);
            mGridTitle = (TextView) v.findViewById(R.id.textViewGridTitle);
            mLayout = (LinearLayout) v.findViewById(R.id.listLayout);
            mPlayGrid = (ImageButton) v.findViewById(R.id.imageButtonPlayGrid);
            v.setOnClickListener(this);
        }

        //5
        @Override
        public void onClick(View v) {
            Context context = v.getContext();

            Intent intent = new Intent(context, PlayActivity.class);
            intent.putExtra("detail", mRadio);
            context.startActivity(intent);

            //Intent showPhotoIntent = new Intent(context, PhotoActivity.class);
            //showPhotoIntent.putExtra(PLACE_KEY, mRadio);
            //context.startActivity(showPhotoIntent);
        }

        public void bindRadio(final RadioDetails radioDetails) {
            mRadio = radioDetails;
            Picasso.with(mItemImage.getContext()).load(radioDetails.getImageURL()).into(mItemImage);
            mItemDate.setText(radioDetails.getPubDate());
            mItemTitle.setText(radioDetails.getTitle());
            mGridTitle.setText(radioDetails.getTitle());
            if(mviewType.equalsIgnoreCase("GRID")){
                mLayout.setVisibility(View.INVISIBLE);
                mGridTitle.setVisibility(View.VISIBLE);
                mPlayGrid.setVisibility(View.VISIBLE);
            }
            else{
                mLayout.setVisibility(View.VISIBLE);
                mGridTitle.setVisibility(View.INVISIBLE);
                mPlayGrid.setVisibility(View.INVISIBLE);
            }
/*
            mItemTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), PlayActivity.class);
                    intent.putExtra("detail", radioDetails);
                    itemView.getContext().startActivity(intent);
                }
            });
            */
        }
    }

    public RecyclerAdapter(ArrayList<RadioDetails> radio, String viewType) {
        mRadio = radio;
        mviewType = viewType;
    }

    @Override
    public RecyclerAdapter.PlaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("viewType",mviewType+"");
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_row, parent, false);
        inflatedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int itemPosition = .getChildLayoutPosition(v);
               // RadioDetails item = mRadio.get(itemPosition);
            }
        });
        return new PlaceHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.PlaceHolder holder, int position) {
        RadioDetails itemRadio = mRadio.get(position);
        holder.bindRadio(itemRadio);

    }

    @Override
    public int getItemCount() {
        return mRadio.size();
    }
}
