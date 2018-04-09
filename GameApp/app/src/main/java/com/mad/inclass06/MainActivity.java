package com.mad.inclass06;
/**
 * authors: Mihai Mehedint, Swati Jha
 * assignment: HW4
 * file name: MainActivity.java
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements SearchASYNC.GameData, GameDetailsAsync.GameDetails {
    ProgressDialog pd;
    private HashMap<Integer,Game> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*findViewById(R.id.go_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup rg = (RadioGroup) findViewById(R.id.radioResults);
                int id = rg.getCheckedRadioButtonId();
                Log.d("demo", "Id of radio: " + String.valueOf(id));

                //RadioButton rb = (RadioButton) rg.getChildAt(id);
                //Log.d("demo", rb.getText().toString());
                if (id != -1) {
                    Log.d("demo", "Id of game: " + list.get(id));
                    RequestParameters params = new RequestParameters("GET", "http://thegamesdb.net/api/GetGame.php");
                    params.addParam("id", list.get(id).getId());
                    new GameDetailsAsync(MainActivity.this).execute(params);
                } else if (id == -1) {
                    Toast.makeText(MainActivity.this, "Need to select one item.", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        final Button search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo", ((EditText) findViewById(R.id.searchBox)).getText().toString());
                if (isConnected()) {
                    //new SearchASYNC(MainActivity.this).execute("http://thegamesdb.net/api/GetGamesList.php");
                    pd = new ProgressDialog(MainActivity.this);
                    pd.setCancelable(false);
                    pd.show();
                    RequestParameters params = new RequestParameters("GET", "http://thegamesdb.net/api/GetGamesList.php");
                    params.addParam("name", ((EditText) findViewById(R.id.searchBox)).getText().toString());

                    new SearchASYNC(MainActivity.this).execute(params);

                } else {
                    Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });


        if(list==null) {
            //((Button) findViewById(R.id.go_button)).setClickable(false);
            search.setClickable(false);
        }

        final EditText searchWord = (EditText) findViewById(R.id.searchBox);
        if (isConnected()){
        searchWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                search.setClickable(false);
                Toast.makeText(MainActivity.this, "Enter a search word.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search.setClickable(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    search.setClickable(false);
                    //((Button) findViewById(R.id.go_button)).setClickable(false);
                    Toast.makeText(MainActivity.this, "Need to have a search word.", Toast.LENGTH_SHORT).show();
                } else {
                    search.setClickable(true);

                }

            }
        });




        }

    }


    @Override
    public void setGameData(ArrayList<Game> list) {
        pd.dismiss();

        this.list = new HashMap<>();
        if(list!=null) {
            Log.d("demo", "LIst of Games: "  + list.toString());
            this.list.clear();
            final ArrayList<Game> listNew = list;
            SearchListAdapter adaptor = new SearchListAdapter(this,list);
            ListView listView = (ListView) findViewById(R.id.listViewSearchList);
            listView.setAdapter(adaptor);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    /*RequestParameters params = new RequestParameters("GET", "http://thegamesdb.net/api/GetGame.php");
                    params.addParam("id", listNew.get(position).getId());
                    new GameDetailsAsync(MainActivity.this).execute(params);*/
                    Intent intent = new Intent(MainActivity.this, GameDetailsActivity.class);
                    intent.putExtra("details", listNew.get(position).getDetail());
                    startActivity(intent);
                }
            });
        }else{

            Toast.makeText(MainActivity.this, "The search for the game "+((EditText)findViewById(R.id.searchBox)).getText().toString()+" yielded NO results.", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void setGameDetails(Detail details) {
        Log.d("demo", "Game Details: "  + details);
        if(details!=null){
            Intent intent = new Intent(MainActivity.this, GameDetailsActivity.class);
            intent.putExtra("details", details);
            startActivity(intent);
            //startActivity(intent);
        }


        //intent.
        //intent.putExtra("details", details);

    }

    @Override
    public Context getContext() {
        return MainActivity.this;
    }

    private boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected())
            return true;
        return false;
    }
}
