package com.mad.group24_hw06;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * authors: Mihai Mehedint, Swati Jha
 * assignment: HW6
 * file name: AppListAdapter.java
 */

public class FavActivity extends AppCompatActivity {

    ArrayList<App> favapps;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonText = sharedpreferences.getString("key", null);
        favapps = new ArrayList<App>();
        favapps = gson.fromJson(jsonText, new TypeToken<ArrayList<App>>() {}.getType());
        Log.d("demo", "Details in Fav: " + favapps.size());
        for(App app: favapps) {
            Log.d("demo", "Details in Fav: " + app.getName());
        }
        setGameDetails();

    }

    public void setGameDetails() {
        final ListView appList = (ListView)findViewById(R.id.favList);
        AppListAdapter appListAdapter = new AppListAdapter(FavActivity.this, R.layout.activity_item, favapps);
        appList.setAdapter(appListAdapter);
        appList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                final View thisView = view;
                Log.d("demo", "Position "+position+" , Value "+ favapps.get(position).getFavorite());
                new AlertDialog.Builder(FavActivity.this)
                            .setTitle("Add to Favourites")
                            .setMessage("Are you sure that you want to remove this app to favourites?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    favapps.remove(favapps.get(pos));
                                    appList.invalidateViews();
                                    saveArray();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
            }
        });

    }

    public void saveArray()
    {
        Gson gson = new Gson();
        String jsonText = gson.toJson(favapps);
        sharedpreferences = FavActivity.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.putString("key", jsonText);
        editor.commit();
        Toast.makeText(FavActivity.this,"App removed from favourites.",Toast.LENGTH_SHORT).show();
    }


}
