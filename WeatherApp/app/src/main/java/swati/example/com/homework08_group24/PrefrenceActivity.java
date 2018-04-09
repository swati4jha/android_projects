package swati.example.com.homework08_group24;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PrefrenceActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    String unit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefrence);

        TextView tv1 = (TextView) findViewById(R.id.textViewTempChange);
        TextView tv2 = (TextView) findViewById(R.id.textViewTempChange1);

        TextView tv3 = (TextView) findViewById(R.id.textViewCityChange);
        TextView tv4 = (TextView) findViewById(R.id.textViewCityChange1);

        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater linf = LayoutInflater.from(PrefrenceActivity.this);
                final View inflator = linf.inflate(R.layout.alert_text_view, null);
                final AlertDialog.Builder alert = new AlertDialog.Builder(PrefrenceActivity.this);
                alert.setTitle("Enter City Details:");
                alert.setView(inflator);

                final EditText et1 = (EditText) inflator.findViewById(R.id.editTextAlertCity);
                final EditText et2 = (EditText) inflator.findViewById(R.id.editTextAlertCountry);

                String pLabel = "Set";
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                String key = prefs.getString("current_key","");
                if(key!=null && !key.isEmpty()){
                    String city = prefs.getString("current_city","");
                    String country = prefs.getString("current_country","");
                    et1.setText(city);
                    et2.setText(country);
                    pLabel="Update";
                }
                alert.setPositiveButton(pLabel, new DialogInterface.OnClickListener() {
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



        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(PrefrenceActivity.this);
                builderSingle.setTitle("Choose Temperature Unit");
                final List<String> units = new ArrayList<String>();
                units.add("Celsius");
                units.add("Fahrenheit");

                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                        PrefrenceActivity.this, // Context
                        android.R.layout.simple_list_item_single_choice, // Layout
                        units // List
                );

                builderSingle.setSingleChoiceItems(
                        arrayAdapter, // Items list
                        -1, // Index of checked item (-1 = no selection)
                        new DialogInterface.OnClickListener() // Item click listener
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                unit = units.get(i);

                            }
                        });

                // Set the a;ert dialog positive button
                builderSingle.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dialogInterface.dismiss();
                        Log.d("Unit",unit);
                        if(unit.equalsIgnoreCase("Celsius")){
                            Toast.makeText(PrefrenceActivity.this,"Temperature Unit has been changed to oC",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(PrefrenceActivity.this,"Temperature Unit has been changed to oF",Toast.LENGTH_SHORT).show();
                        }
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("current_temp",unit);
                        editor.commit();
                    }
                });

                // Create the alert dialog
                AlertDialog dialog = builderSingle.create();
                dialog.show();
            }
        });


    }
}
