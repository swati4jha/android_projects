package com.example.swati.weightestimator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cal = (Button) findViewById(R.id.buttonCalculate);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double height = 0.00;
                double heightInches = 0.00;

                EditText ht = (EditText) findViewById(R.id.editTextFeet);
                if(ht.getText()!=null && !ht.getText().toString().isEmpty()
                        && ht.getText().toString().matches("[+]?[0-9]*\\.?[0-9]+")){
                    height = Double.parseDouble(ht.getText().toString());
                }

                EditText htInches = (EditText) findViewById(R.id.editTextInches);
                if(htInches.getText()!=null && !htInches.getText().toString().isEmpty()
                        && htInches.getText().toString().matches("[+]?[0-9]*\\.?[0-9]+")){
                    heightInches = Double.parseDouble(htInches.getText().toString());
                }

                double finalHeight = height * 12 + heightInches;

                final RadioGroup inputWeight = (RadioGroup)findViewById(R.id.radioGroup);
                int selectedInputId = inputWeight.getCheckedRadioButtonId();
                RadioButton radioButtonInput = (RadioButton) findViewById(selectedInputId);
                if (selectedInputId != -1 && finalHeight > 0.00) {

                    double minWeight = 0.00;
                    double maxWeight = 0.00;
                    TextView showHt = (TextView) findViewById(R.id.textViewShowWeight);
                    Log.d("demo", radioButtonInput.getText()+"");

                    if(radioButtonInput.getText().toString().equals("< 18.5"))
                    {
                        minWeight = 0.00;
                        maxWeight = (finalHeight * finalHeight * 18.4)/703;
                        DecimalFormat df = new DecimalFormat("#.00");
                        maxWeight = Double.valueOf(df.format(maxWeight));
                        showHt.setText("Your weight should be in between "+minWeight+" to "+maxWeight+" lb");
                    }
                    else if(radioButtonInput.getText().toString().equals("18.5 to 24.9"))
                    {
                        minWeight = (finalHeight * finalHeight * 18.5)/703;;
                        maxWeight = (finalHeight * finalHeight * 24.9)/703;
                        DecimalFormat df = new DecimalFormat("#.00");
                        minWeight = Double.valueOf(df.format(minWeight));
                        maxWeight = Double.valueOf(df.format(maxWeight));
                        showHt.setText("Your weight should be in between "+minWeight+" to "+maxWeight+" lb");
                    }
                    else if(radioButtonInput.getText().toString().equals("25.0 to 29.9 "))
                    {
                        minWeight = (finalHeight * finalHeight * 25.00)/703;;
                        maxWeight = (finalHeight * finalHeight * 29.9)/703;
                        DecimalFormat df = new DecimalFormat("#.00");
                        minWeight = Double.valueOf(df.format(minWeight));
                        maxWeight = Double.valueOf(df.format(maxWeight));
                        showHt.setText("Your weight should be in between "+minWeight+" to "+maxWeight+" lb");
                    }
                    else if(radioButtonInput.getText().toString().equals("> 29.9"))
                    {
                        minWeight = (finalHeight * finalHeight * 30)/703;;
                        maxWeight = 0.00;
                        DecimalFormat df = new DecimalFormat("#.00");
                        minWeight = Double.valueOf(df.format(minWeight));
                        showHt.setText("Your weight should be greater than or equal to " +minWeight+" lb");
                    }
                    Toast.makeText(MainActivity.this,
                            "Weight calculated", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this,
                            "Invalid weight", Toast.LENGTH_SHORT).show();
                }

            }


        });

    }
}
