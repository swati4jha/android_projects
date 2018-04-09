package com.mad.group24_hw07;
/**
 * authors: Swati Jha, Mihai Mehedint
 * assignment: HW7
 * file name: MainActivity.java
 */

import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements GetRadioData.RadioData{
    MediaPlayer mediaPlayer;
    private MediaController mediaController;
    //private Handler handler = new Handler();
    private Handler handler = new Handler();
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerAdapter mAdapter;
    ArrayList<RadioDetails> mradioDetailsList;
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        myToolbar.setLogo(R.drawable.icon);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        //number of columns in the grid view 2
        mGridLayoutManager = new GridLayoutManager(this, 2);

        //getSupportActionBar().setDisplayUseLogoEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.icon);
        if (isConnected()) {
            new GetRadioData(MainActivity.this).execute("https://www.npr.org/rss/podcast.php?id=510298");
        }else{
            Toast.makeText(MainActivity.this, "NO Internet Connection!", Toast.LENGTH_LONG);
        }



    }

    private boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected())
            return true;
        return false;
    }

    @Override
    public void setupData(ArrayList<RadioDetails> radioDetailsList) {
        //here setup the parsed data
        mradioDetailsList = new ArrayList<RadioDetails>();
        if (radioDetailsList!=null) {
            Collections.sort(radioDetailsList);
            mradioDetailsList =radioDetailsList;
            mAdapter = new RecyclerAdapter(mradioDetailsList,"LIST");
            mRecyclerView.setAdapter(mAdapter);
        }
            }
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private void changeLayoutManager() {
        if (mRecyclerView.getLayoutManager().equals(mLinearLayoutManager)) {
            mAdapter = new RecyclerAdapter(mradioDetailsList,"GRID");
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
        } else {
            mAdapter = new RecyclerAdapter(mradioDetailsList,"LIST");
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_switch) {
            changeLayoutManager();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) mediaPlayer.release();
    }

}
