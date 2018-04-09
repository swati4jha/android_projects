package swati.example.com.homework08_group24;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CityWeather extends AppCompatActivity {

    ProgressDialog progressDialog;
    ArrayList<SearchCity> weatherArrayList;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    public static final String PREFS_NAME = "MyPrefsFile";
    private DatabaseReference mDatabase;
    boolean flag;
    SearchCity cityObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);

        //progress dialog
        progressDialog = new ProgressDialog(CityWeather.this);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Intent intent = getIntent();
        final String city = (String )intent.getStringExtra("City");
        final String country = (String )intent.getStringExtra("Country");
        Log.d("City",city);
        if(city!=null && !city.isEmpty() && country!=null && !country.isEmpty()){

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
                            setfiveDayForecast(data,city,country);

                        }
                        //finish();
                        //startActivity(getIntent());


                    } catch (Exception e){
                        Log.d("error: ", e.toString());
                    }


                }
            });

        }
        else{
            progressDialog.dismiss();
            finish();
        }
    }

    public void setfiveDayForecast(final String key, final String city, final String country){
        weatherArrayList = new ArrayList<SearchCity>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://dataservice.accuweather.com/forecasts/v1/daily/5day/"+key+"?apikey=iwaeDP8uASrX2bQcCS73orwIL2iKsgYA")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Fail: ", "fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                progressDialog.dismiss();
                try{
                    JSONObject obj = new JSONObject(res);
                    JSONObject obj1 = (JSONObject) obj.get("Headline");
                    String headline = obj1.getString("Text");
                    String details = obj1.getString("MobileLink");

                    JSONArray jsonArray = obj.getJSONArray("DailyForecasts");

                    for(int i=0;i<jsonArray.length();i++){
                        SearchCity searchCity = new SearchCity();
                        searchCity.setCity(city);
                        searchCity.setCountry(country);
                        searchCity.setHeadlline(headline);
                        searchCity.setKey(key);
                        JSONObject obj3 = (JSONObject)jsonArray.get(i);
                        JSONObject temp = (JSONObject)obj3.get("Temperature");
                        JSONObject min = (JSONObject)temp.get("Minimum");
                        JSONObject max = (JSONObject)temp.get("Maximum");
                        searchCity.setDate(Ptime.PtimeFormatter.getPtimeFrom(obj3.getString("Date")));

                        searchCity.setMintemp(min.getString("Value"));
                        searchCity.setMaxtemp(max.getString("Value"));

                        JSONObject day = (JSONObject)obj3.get("Day");
                        JSONObject night = (JSONObject)obj3.get("Night");

                        searchCity.setDayIcon(day.getString("Icon"));
                        searchCity.setDayPhrase(day.getString("IconPhrase"));

                        searchCity.setNightIcon(night.getString("Icon"));
                        searchCity.setNightPhrase(night.getString("IconPhrase"));

                        searchCity.setExtendedDeatils(details);
                        searchCity.setMoreDetails(obj3.getString("MobileLink"));
                        weatherArrayList.add(searchCity);
                        Log.d("Search",searchCity.toString());
                    }
                    Log.d("WE","WEer11");
                    setNoteDetails(0);
                    Log.d("WE","WE1231");



                } catch (Exception e){
                    Log.d("error: ", e.toString());
                }


            }
        });
    }
    public void setNoteDetails(final int position) {
        Log.d("WE",weatherArrayList.size()+"");

        if(weatherArrayList!=null && !weatherArrayList.isEmpty() && weatherArrayList.size()>0){
            this.cityObj = weatherArrayList.get(position);
            int count=0;
            CityWeather.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView tv1 = (TextView) findViewById(R.id.textViewDaily);
                    tv1.setText("Daily forecast for "+weatherArrayList.get(position).getCity()+" ,"
                            +weatherArrayList.get(position).getCountry());
                    TextView tv2 = (TextView) findViewById(R.id.textViewHeadline);
                    tv2.setText(weatherArrayList.get(position).getHeadlline());

                    TextView tv3 = (TextView) findViewById(R.id.textViewForcastDate);
                    Log.d("dddd",weatherArrayList.get(position).getNightIcon());
                    tv3.setText("Forecast on "+(weatherArrayList.get(position).getDate()));

                    TextView tv4 = (TextView) findViewById(R.id.textViewTemp);

                    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    String unit = prefs.getString("current_temp","");
                    String mintemp="weatherArrayList.get(position).getMintemp()";
                    String maxtemp="weatherArrayList.get(position).getMaxtemp()";
                    if(unit.equalsIgnoreCase("Celsius")){
                        double mntemp = (Double.parseDouble(mintemp)-32)*5/9;
                        double matemp = (Double.parseDouble(maxtemp)-32)*5/9;
                        tv4.setText("Temperature: "+matemp+"\u2103 / "+mntemp+"\u2103");
                    }else{
                        tv4.setText("Temperature: "+weatherArrayList.get(position).getMaxtemp()+"\u2109 / "+weatherArrayList.get(0).getMintemp()+"\u2109");
                    }


                    TextView tv5 = (TextView) findViewById(R.id.textViewDayPhrase);
                    tv5.setText(weatherArrayList.get(position).getDayPhrase());

                    TextView tv6 = (TextView) findViewById(R.id.textViewNight);
                    tv6.setText(weatherArrayList.get(position).getNightPhrase());

                    ImageView im = (ImageView) findViewById(R.id.imageViewDay);
                    if(Integer.parseInt(weatherArrayList.get(position).getDayIcon())<10){
                        Picasso.with(im.getContext()).load("http://developer.accuweather.com/sites/default/files/0"
                                +weatherArrayList.get(position).getDayIcon()+"-s.png").into(im);
                    }
                    else{
                        Picasso.with(im.getContext()).load("http://developer.accuweather.com/sites/default/files/"
                                +weatherArrayList.get(position).getDayIcon()+"-s.png").into(im);
                    }

                    ImageView im1 = (ImageView) findViewById(R.id.imageViewNight);

                    if(Integer.parseInt(weatherArrayList.get(position).getNightIcon())<10){
                        Picasso.with(im.getContext()).load("http://developer.accuweather.com/sites/default/files/0"
                                +weatherArrayList.get(position).getNightIcon()+"-s.png").into(im);
                    }else{
                        Picasso.with(im1.getContext()).load("http://developer.accuweather.com/sites/default/files/"
                                +weatherArrayList.get(position).getNightIcon()+"-s.png").into(im1);
                    }



                    TextView tv7 = (TextView) findViewById(R.id.textViewMore);
                    tv7.setMovementMethod(LinkMovementMethod.getInstance());
                    tv7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri adress= Uri.parse(weatherArrayList.get(position).getMoreDetails());
                            Intent browser= new Intent(Intent.ACTION_VIEW, adress);
                            startActivity(browser);
                        }
                    });

                    TextView tv8 = (TextView) findViewById(R.id.textViewExt);
                    tv8.setMovementMethod(LinkMovementMethod.getInstance());
                    tv8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri adress= Uri.parse(weatherArrayList.get(position).getExtendedDeatils());
                            Intent browser= new Intent(Intent.ACTION_VIEW, adress);
                            startActivity(browser);
                        }
                    });

                    mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                    mAdapter = new RecyclerAdapter(weatherArrayList);
                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(CityWeather.this, LinearLayoutManager.HORIZONTAL, false);
                    mRecyclerView.setLayoutManager(horizontalLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.addOnItemTouchListener(
                            new RecyclerItemClickListener(getBaseContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                                @Override public void onItemClick(View view, int position) {
                                    Log.d("Clicked",position+"");
                                    setNoteDetails(position);
                                }

                                @Override public void onLongItemClick(View view, int position) {
                                    // do whatever
                                }
                            })
                    );
                }
            });





        }



    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.citysettings:
                        Intent intent = new Intent(CityWeather.this,PrefrenceActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.save:
                        saveCity();
                        return true;
                    case R.id.curCity:
                        setCurrentCity();
                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.city_menu, popup.getMenu());
        popup.show();
    }

    public void saveCity(){
        flag=false;
        final CityVo cityVo = new CityVo();
        cityVo.setCitykey(cityObj.getKey());
        cityVo.setCityname(cityObj.getCity());
        cityVo.setCountry(cityObj.getCountry());
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String unit = prefs.getString("current_temp","");
        String maxtemp="cityObj.getMaxtemp()";
        if(unit.equalsIgnoreCase("Celsius")){
            double matemp = (Double.parseDouble(maxtemp)-32)*5/9;
            cityVo.setTemperature("Temperature: "+matemp+"\u2103 ");
        }else{
            cityVo.setTemperature("Temperature: "+cityObj.getMaxtemp()+"\u2109 ");
        }
        cityVo.setFavorite("false");
        cityVo.setDate(cityObj.getDate());
        FirebaseApp.initializeApp(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("cities").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<CityVo> weatherList = new ArrayList<CityVo>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    CityVo cityVo1 = noteDataSnapshot.getValue(CityVo.class);
                    boolean counter = false;
                    for (DataSnapshot noteDataSnapshotInner : dataSnapshot.getChildren()) {
                        CityVo cityVo2 = noteDataSnapshotInner.getValue(CityVo.class);
                        Log.d("update",cityVo2.getCitykey()+"cityVo1");
                        if(cityVo1.getCitykey().equalsIgnoreCase(cityVo2.getCitykey())){

                            if(counter) {
                                CityVo cityVo = noteDataSnapshotInner.getValue(CityVo.class);
                                mDatabase.child("cities").child(cityVo.getId()).removeValue();
                            }
                            counter = true;
                        }
                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getUser:onCancelled", databaseError.toException().toString());

            }
        });
        Log.d("update12",flag+"");
        if(flag){
            mDatabase.child("cities").child(cityObj.getId()).setValue(cityVo);
        }else{
            cityVo.setId(mDatabase.child("cities").push().getKey());
            mDatabase.child("cities").child(cityVo.getId()).setValue(cityVo);
        }


    }

    public void setCurrentCity(){
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.clear();
        editor.putString("current_city",cityObj.getCity());
        editor.putString("current_country",cityObj.getCountry());
        editor.putString("current_key",cityObj.getKey());
        editor.commit();
    }
}
