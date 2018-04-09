package com.example.mihai.group24_inclass03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class GeneratedPasswordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_passwords);

        //ArrayList<String> threadPass = (ArrayList<String>) Arrays.asList(getIntent().getExtras().getStringArray("thread"));
        //ArrayList<String> asyncPass = (ArrayList<String>) Arrays.asList(getIntent().getExtras().getStringArray("thread"));

        if (getIntent().getExtras()!=null) {
            ArrayList<String> th = getIntent().getExtras().getStringArrayList("thread");
            ArrayList<String> async = getIntent().getExtras().getStringArrayList("async");

            //String[] threadPass = getIntent().getData() getExtras().getStringArray("thread");
            //String[] asyncPass = getIntent().getExtras().getStringArray("async");
           // Log.d("demo", "GeneratedPassAct first pass in thread: " + th.get(0));

            //getIntent().getExtras().getStringArray("async");

            //(ScrollView)(findViewById(R.id.))

            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView tv = new TextView(this);
            tv.setLayoutParams(lparams);
            tv.setText("");
            LinearLayout ll = (LinearLayout) this.findViewById(R.id.threadList);
            ll.addView(tv);


            findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
