package com.mad.inclass06;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by swati on 2/20/2017.
 */

public class SearchListAdapter extends ArrayAdapter<Game> {
    public SearchListAdapter(Context context, ArrayList<Game> games) {
        super(context, 0, games);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Game game = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_item, parent, false);
        }
        // Lookup view for data population
        ImageView iv = (ImageView) convertView.findViewById(R.id.imageView2);
        TextView details = (TextView) convertView.findViewById(R.id.textViewDetails);

        details.setText(game.getTitle() + ". " + "Released: " + game.getDate() + ". Platform: " + game.getPlatform()+ ".");
        if(game.getDetail()!=null){
            Log.d("Swati",game.getDetail().baseUrl+game.getDetail().getClearLogo());
            Picasso.with(getContext())
                    .load(game.getDetail().baseUrl+game.getDetail().getClearLogo())
                    .into(iv);
        }
        else
        {
            Picasso.with(getContext())
                    .load(R.drawable.trivia)
                    .into(iv);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
