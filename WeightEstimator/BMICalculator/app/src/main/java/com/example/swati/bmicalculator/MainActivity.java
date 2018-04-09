package com.example.swati.bmicalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button calculateBmi = (Button) findViewById(R.id.buttonCalculate);
        calculateBmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "";
                double weight = 0.00;
                double height = 0.00;
                double heightInches = 0.00;
                double bmi = 0.00;
                EditText wt = (EditText) findViewById(R.id.editTextwt);
                if(wt.getText()!=null && !wt.getText().toString().isEmpty()
                        && wt.getText().toString().matches("[+]?[0-9]*\\.?[0-9]+")){
                    weight = Double.parseDouble(wt.getText().toString());
                }

                EditText ht = (EditText) findViewById(R.id.editTextHeight);
                if(ht.getText()!=null && !ht.getText().toString().isEmpty()
                    && ht.getText().toString().matches("[+]?[0-9]*\\.?[0-9]+")){
                    height = Double.parseDouble(ht.getText().toString());
                }

                EditText htInches = (EditText) findViewById(R.id.editTextHeightInc);
                if(htInches.getText()!=null && !htInches.getText().toString().isEmpty()
                        && htInches.getText().toString().matches("[+]?[0-9]*\\.?[0-9]+")){
                    heightInches = Double.parseDouble(htInches.getText().toString());
                }

                double finalHeight = height * 12 + heightInches;
                if(finalHeight>0.00 && weight>0.00) {
                    bmi = (weight / (finalHeight * finalHeight)) * 703;
                }

                if(bmi <= 18.5){
                    msg = getString(R.string.under_text).toString();
                }
                else if(bmi > 18.5 && bmi <= 24.9){
                    msg = getString(R.string.normal_text).toString();
                }
                if(bmi >=25 && bmi <=29.9){
                    msg = getString(R.string.over_text).toString();
                }
                else if(bmi >=30){
                    msg = getString(R.string.obese_text).toString();
                }



                if(bmi>0.00){
                    Toast.makeText(MainActivity.this,
                            "BMI calculated", Toast.LENGTH_SHORT).show();
                    DecimalFormat df = new DecimalFormat("#.00");
                    bmi = Double.valueOf(df.format(bmi));

                    TextView bmiText = (TextView) findViewById(R.id.textViewShowBmi);
                    bmiText.setText("Your BMI:"+bmi);

                    TextView msgText = (TextView) findViewById(R.id.textViewShowmsg);
                    msgText.setText(msg);
                }
                else
                {
                    Toast.makeText(MainActivity.this,
                            "Invalid weight/height entered!!", Toast.LENGTH_SHORT).show();
                    TextView bmiText = (TextView) findViewById(R.id.textViewShowBmi);
                    bmiText.setText("");

                    TextView msgText = (TextView) findViewById(R.id.textViewShowmsg);
                    msgText.setText("");
                }

            }
        });


    }
}
