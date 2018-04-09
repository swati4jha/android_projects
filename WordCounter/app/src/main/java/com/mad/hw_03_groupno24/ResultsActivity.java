package com.mad.hw_03_groupno24;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.resultLayout);
        Intent intent = getIntent();
        HashMap<String, Integer> hashMap = (HashMap<String, Integer>)intent.getSerializableExtra("result");
        for(Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            Log.d("HashMapTest", entry.getKey()+":"+entry.getValue());
            TextView tv =new TextView(this);
            tv.setText(entry.getKey()+":"+entry.getValue());
            linearLayout.addView(tv);
        }

        Button finishBtn = (Button) findViewById(R.id.finish);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result","DONE");
                setResult(ResultsActivity.RESULT_OK,returnIntent);
                finish();

            }
        });


    }
}
