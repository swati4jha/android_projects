package swati.example.com.homework08_group24;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    private DatabaseReference mDatabase;
    private ArrayList<CityVo> weatherList;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter1 mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        mDatabase.child("cities").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                weatherList = new ArrayList<CityVo>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    CityVo cityVo = noteDataSnapshot.getValue(CityVo.class);
                    weatherList.add(cityVo);
                }
                if(weatherList!=null && !weatherList.isEmpty() && weatherList.size()>0){
                    TextView tv = (TextView) findViewById(R.id.textViewMsg);
                    tv.setText("");
                }
                setSavedCity(weatherList);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getUser:onCancelled", databaseError.toException().toString());

            }
        });

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl1);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String key = prefs.getString("current_key","");
        if(key!=null && !key.isEmpty()){
            rl.removeAllViews();
            setCurrentCity(this,key);
            Button search = (Button) findViewById(R.id.buttonSearch);
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText et1 = (EditText) findViewById(R.id.editTextCity);
                    EditText et2 = (EditText) findViewById(R.id.editTextCountry);
                    Intent intent = new Intent(MainActivity.this,CityWeather.class);
                    intent.putExtra("City",et1.getText().toString());
                    intent.putExtra("Country",et2.getText().toString());
                    startActivity(intent);
                }
            });
        }

        else{
            Button setCurCity = (Button) findViewById(R.id.buttonSetCurCity);
            setCurCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LayoutInflater linf = LayoutInflater.from(MainActivity.this);
                    final View inflator = linf.inflate(R.layout.alert_text_view, null);
                    final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                    alert.setTitle("Enter City Details:");
                    alert.setView(inflator);

                    final EditText et1 = (EditText) inflator.findViewById(R.id.editTextAlertCity);
                    final EditText et2 = (EditText) inflator.findViewById(R.id.editTextAlertCountry);

                    alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton)
                        {
                            final String city=et1.getText().toString();
                            final String country=et2.getText().toString();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://dataservice.accuweather.com/locations/v1/"+country+"/search?apikey=iwaeDP8uASrX2bQcCS73orwIL2iKsgYA&q="+city+"")
                                    .build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.d("Fail: ", "fail");
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String res = response.body().string();
                                    try{
                                        JSONArray jsonArray = new JSONArray(res);
                                        JSONObject obj =(JSONObject) jsonArray.get(0);
                                        Log.d("token: ", obj.getString("Key").toString());
                                        String data = obj.getString("Key").toString();
                                        if(data!=null && !data.isEmpty()){
                                            //Toast.makeText(MainActivity.this,"Current City details saved.",Toast.LENGTH_LONG).show();
                                            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                                            editor.putString("current_city",city);
                                            editor.putString("current_country",country);
                                            editor.putString("current_key",data);
                                            editor.commit();
                                        }
                                        finish();
                                        startActivity(getIntent());


                                    } catch (Exception e){
                                        Log.d("error: ", e.toString());
                                    }


                                }
                            });
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });

                    alert.show();
                }
            });
        }


    }

    private void setCurrentCity(final MainActivity mainActivity, String key) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://dataservice.accuweather.com/currentconditions/v1/"+key+"?apikey=iwaeDP8uASrX2bQcCS73orwIL2iKsgYA")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Fail: ", "fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                try{
                    JSONArray jsonArray = new JSONArray(res);
                    final JSONObject obj =(JSONObject) jsonArray.get(0);

                    final City city = City.createCity(obj);
                    Log.d("city: ", city.toString());

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LayoutInflater inflater = (LayoutInflater)getBaseContext()
                                    .getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE);
                            RelativeLayout main =(RelativeLayout)findViewById(R.id.rl1);
                            View view = inflater.inflate(R.layout.city_details ,main,false);
                            main.addView(view);
                            TextView tv1 = (TextView) findViewById(R.id.textViewCityName);
                            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                            String city_name = prefs.getString("current_city","");
                            String country = prefs.getString("current_country","");
                            tv1.setText(city_name+" ,"+country);
                            TextView tv2 = (TextView) findViewById(R.id.textViewCloudy);
                            tv2.setText(city.getCloudy());
                            String unit = prefs.getString("current_temp","");
                            TextView tv3 = (TextView) findViewById(R.id.textViewTemp);
                            Log.d("Swati","Swati");
                            Log.d("Swati",unit+"Sw");
                            if(unit.equalsIgnoreCase("Fahrenheit")){
                                double temp = (Double.parseDouble(city.getTemp())*9/5)+32;
                                tv3.setText("Temperature:: "+temp+" \u2109");
                            }else{
                                tv3.setText("Temperature:: "+city.getTemp()+" \u2103");
                            }
                            TextView tv4 = (TextView) findViewById(R.id.textViewTime);
                            tv4.setText(city.getTime());
                            ImageView im = (ImageView) findViewById(R.id.imageView2);
                            Picasso.with(im.getContext()).load("http://developer.accuweather.com/sites/default/files/0"+city.getWearherIcon()+"-s.png").into(im);

                        }
                    });



                } catch (Exception e){
                    Log.d("error: ", e.toString());
                }


            }
        });
    }


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.settings:
                        Intent intent = new Intent(MainActivity.this,PrefrenceActivity.class);
                        startActivity(intent);
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

    public void setSavedCity(final ArrayList<CityVo> details){
        ArrayList<CityVo> fav = new ArrayList<CityVo>();
        Collections.sort(details);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mAdapter = new RecyclerAdapter1(details);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
    }
}
