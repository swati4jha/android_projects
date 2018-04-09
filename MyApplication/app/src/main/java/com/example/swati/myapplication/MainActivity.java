//Assignment :: Homework 1
//File Name :: MainActivity.java
//Full name :: Swati jha

package com.example.swati.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText inputAmt = (EditText) findViewById(R.id.editTextinputAmt);
        final EditText convertedAmt = (EditText) findViewById(R.id.editTextConverted);
        final RadioGroup inputCurrency = (RadioGroup)findViewById(R.id.inputCurrency);
        final RadioGroup outputCurrency = (RadioGroup)findViewById(R.id.outputCurrency);
        Button convertBtn = (Button) findViewById(R.id.buttonConvert);
        Button clearBtn = (Button) findViewById(R.id.buttonClear);
        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedInputId = inputCurrency.getCheckedRadioButtonId();
                int selectedOutputId = outputCurrency.getCheckedRadioButtonId();
                Log.d("Input", selectedInputId + "");
                Log.d("Output", selectedOutputId + "");
                double convertedAmount = 0;
                if (inputAmt.getText().toString() != null && !inputAmt.getText().toString().isEmpty()
                        && inputAmt.getText().toString().matches("[+]?[0-9]*\\.?[0-9]+")){
                    if (selectedInputId != -1 && selectedOutputId != -1) {
                        RadioButton radioButtonInput = (RadioButton) findViewById(selectedInputId);
                        RadioButton radioButtonOutput = (RadioButton) findViewById(selectedOutputId);
                        Log.d("in", radioButtonInput.getText() + "");
                        if (radioButtonInput.getText().equals("AUD")) {
                            convertedAmount = Double.parseDouble(inputAmt.getText().toString()) * 0.7477;
                        } else if (radioButtonInput.getText().equals("CAD")) {
                            convertedAmount = Double.parseDouble(inputAmt.getText().toString()) * 0.7576;
                        } else {
                            convertedAmount = Double.parseDouble(inputAmt.getText().toString()) * 0.015;
                        }
                        if (radioButtonOutput.getText().equals("GBP")) {
                            convertedAmount = convertedAmount * 1.2048;
                        }
                        DecimalFormat df = new DecimalFormat("#.00");
                        convertedAmount = Double.valueOf(df.format(convertedAmount));
                    }
                    else{
                        Toast.makeText(MainActivity.this,
                                "Please select currency", Toast.LENGTH_SHORT).show();
                        convertedAmt.setText("");
                    }
                Log.d("Amount", inputAmt.getText() + "");
                convertedAmt.setText(Double.toString(convertedAmount));
            }
                else{
                    Toast.makeText(MainActivity.this,
                            "Invalid input", Toast.LENGTH_SHORT).show();
                    convertedAmt.setText("");
                }
            }
        });

    }
    public void clearButtonclicked(View v) {
        EditText inputAmt = (EditText) findViewById(R.id.editTextinputAmt);
        EditText convertedAmt = (EditText) findViewById(R.id.editTextConverted);
        RadioGroup inputCurrency = (RadioGroup)findViewById(R.id.inputCurrency);
        RadioGroup outputCurrency = (RadioGroup)findViewById(R.id.outputCurrency);
        inputAmt.setText("");
        convertedAmt.setText("");
        inputCurrency.clearCheck();
        outputCurrency.clearCheck();
    }


}
