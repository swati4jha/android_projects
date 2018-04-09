package com.mad.group24_hw06;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;

/**
 * authors: Mihai Mehedint, Swati Jha
 * assignment: HW6
 * file name: AppListAdapter.java
 */

public class MainActivity extends AppCompatActivity implements GetAppsAsyncTask.GameDetails {
    ArrayList<App> apps;
    ArrayList<App> favapps;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        new GetAppsAsyncTask(MainActivity.this).execute("https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json");
    }


    @Override
    public void setGameDetails(final ArrayList<App> details) {
        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonText = sharedpreferences.getString("key", null);
        favapps = new ArrayList<App>();
        if(jsonText!=null && !jsonText.isEmpty()){
            favapps = gson.fromJson(jsonText, new TypeToken<ArrayList<App>>() {}.getType());
        }
        int count=0;
        for(App app: details) {
            if(favapps!=null && favapps.contains(app)){
                details.get(count).setFavorite("yes");
            }
            count = count+1;
        }
        apps = details;

        final ListView appList = (ListView)findViewById(R.id.appList);
        AppListAdapter appListAdapter = new AppListAdapter(MainActivity.this, R.layout.activity_item, apps);
        appList.setAdapter(appListAdapter);
        appListAdapter.setNotifyOnChange(true);
        appList.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                appList.removeOnLayoutChangeListener(this);
                ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
                findViewById(R.id.loading).setVisibility(View.INVISIBLE);
            }
        });

        appListAdapter.notifyDataSetChanged();


        appList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                final View thisView = view;
                if(apps.get(position).getFavorite()==null || apps.get(position).getFavorite().equalsIgnoreCase("no")
                        || apps.get(position).getFavorite().isEmpty()){
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Add to Favourites")
                            .setMessage("Are you sure that you want to add this app to favourites?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    apps.get(pos).setFavorite("yes");
                                    favapps.add(apps.get(pos));
                                    ImageView imageView = (ImageView)thisView.findViewById(R.id.imageView);
                                    imageView.setImageResource(R.drawable.blackstar);
                                    saveArray();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
                else{
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Add to Favourites")
                            .setMessage("Are you sure that you want to remove this app to favourites?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    apps.get(pos).setFavorite("no");
                                    favapps.remove(apps.get(pos));
                                    ImageView imageView = (ImageView)thisView.findViewById(R.id.imageView);
                                    imageView.setImageResource(R.drawable.whitestar);
                                    saveArray();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            }
        });

    }

    @Override
    public Context getContext() {
        return MainActivity.this;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }


    public ArrayList<App> sortUserAsc(ArrayList<App> appsList) {
        ArrayList<App> sortedList = new ArrayList<App>(appsList);
        Collections.sort(sortedList);
        return sortedList;
    }

    public ArrayList<App> sortUserDesc(ArrayList<App> appsList) {
        ArrayList<App> sortedList = new ArrayList<App>(appsList);
        Collections.sort(sortedList, Collections.<App>reverseOrder());
        return sortedList;
    }

    public void saveArray()
    {
        Gson gson = new Gson();
        String jsonText = gson.toJson(favapps);
        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.putString("key", jsonText);
        editor.commit();
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.refList:
                        ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
                        findViewById(R.id.loading).setVisibility(View.VISIBLE);
                        new GetAppsAsyncTask(MainActivity.this).execute("https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json");
                        return true;
                    case R.id.fav:
                        Intent intent = new Intent(MainActivity.this, FavActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.sortAsc:
                        ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
                        findViewById(R.id.loading).setVisibility(View.VISIBLE);
                        ArrayList<App> sortUser = sortUserAsc(apps);
                        setGameDetails(sortUser);
                        return true;
                    case R.id.sortDesc:
                        ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
                        findViewById(R.id.loading).setVisibility(View.VISIBLE);
                        ArrayList<App> sortUserDesc = sortUserDesc(apps);
                        setGameDetails(sortUserDesc);
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
}
