package com.mad.hw_03_groupno24;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.CheckResult;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class MainActivity extends AppCompatActivity {
    ArrayList<EditText> editBoxes = new ArrayList<>();
    ArrayList<ImageButton> imageButtons = new ArrayList<>();
    HashMap<ImageButton, EditText> iButtonsEditBox = new HashMap<>();

    InputStream is = null;
    public static byte[] buffer=null;
    public static int count = 0;
    public static Map<String, Integer> list = new HashMap<>();
    public static Map<String, Integer> results = new HashMap<>();

    public static Handler handler;
    public static ProgressDialog progressDialog;
    int progressDial;

    static final int STATUS_START=0x00;
    static final int STATUS_READ_WORDS=0x01;
    static final int STATUS_DONE=0x02;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //progress dialog
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Searching words: ");
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressNumberFormat(null);

        iButtonsEditBox.put(((ImageButton) findViewById(R.id.imageButton)), ((EditText) findViewById(R.id.firstWord)));
        findViewById(R.id.imageButton).setClickable(false);

        //
        ((EditText) findViewById(R.id.firstWord)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                findViewById(R.id.imageButton).setClickable(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    findViewById(R.id.imageButton).setClickable(false);
                }else {
                    findViewById(R.id.imageButton).setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0){
                    findViewById(R.id.imageButton).setClickable(false);
                }else {
                    findViewById(R.id.imageButton).setClickable(true);
                }
            }
        });



        //read the file
        ExecutorService pool = Executors.newFixedThreadPool(5);
        pool.execute(new ReadFile());


        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDial = 0;
                if (checkFields()==0) {
                    ExecutorService pool2 = Executors.newFixedThreadPool(5);
                    pool2.execute(new ReadWords());
                    //Toast.makeText(MainActivity.this, "Results: "+results, Toast.LENGTH_LONG).show();
                }

                handler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        //Log.d("demo", "progress started");
                        int i=-1;
                        switch (msg.what){
                            case STATUS_START:
                                progressDial =0;
                                progressDialog.show();
                                progressDialog.setProgress(progressDial);
                                progressDialog.setMax(msg.getData().getInt("PROGRESS"));

                                Log.d("demo", "progress started");
                                break;
                            case STATUS_READ_WORDS:
                                progressDial =progressDial+1;
                                //progressDialog.setProgress(passwords.size());   //USE THE MESSAGE OBJECT UNCOMMENT THIS
                                progressDialog.setProgress(progressDial);


                                break;
                            case STATUS_DONE:

                                Log.d("Done", results.size()+"");
                                if(results!=null && results.size()>0){
                                    HashMap<String,Integer> hMap = new HashMap<String, Integer>();
                                    if (results != null && results instanceof HashMap<?, ?>) {
                                        hMap = (HashMap<String, Integer>) results;
                                    } else if (results != null) {
                                        hMap.putAll(results);
                                    }
                                    Log.d("hmap",hMap.size()+"");
                                    Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
                                    intent.putExtra("result", hMap);
                                    startActivityForResult(intent,1);
                                } else{
                                    Toast.makeText(MainActivity.this, "Words not found!!", Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();



                                break;
                        }
                        return false;
                    }
                });

                // Intent intent = new Intent(MainActivity.this, .class);
                //startActivity(intent);

            }
        });

        //initially there is one edit text box and one button
        findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //adding the current word to the list
                EditText et = ((EditText)findViewById(R.id.firstWord));
                if (Container.checkValidString(et, MainActivity.this)==0) {
                    list.put(et.getText().toString().trim(), 0);
                };
                addEditView();
                //ImageButton ib = ((ImageButton)R.id.imageButton);
                final ImageButton minusButton = ((ImageButton) v);
                minusButton.setImageResource(R.drawable.minus);
                minusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Remove button and editbox here
                        final HorizontalScrollView ll = (HorizontalScrollView) findViewById(R.id.horiz_scr);
                        LinearLayout hsv = (LinearLayout) findViewById(R.id.horiz_layout);
                        ll.removeView(hsv);
                        //for (ImageButton ib : iButtonsEditBox.keySet()){}
                        //ib.setClickable(true);

                    }
                });
                //RelativeLayout.LayoutParams editTextLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);


            }
        });
        findViewById(R.id.imageButton).setClickable(false);





    }


    /**
     * this method adds a new editText box, a new plus button and changes the view of the current plus button into minus
     */
    private void addEditView() {
        final ImageButton b = new ImageButton(this);
        b.setClickable(false);

        //Log.d("demo", editBoxes.get(0).getText().toString());
        final LinearLayout listCurrent = new LinearLayout(this);
        final EditText et = new EditText(this);
        //editBoxes.add(et);
        //imageButtons.add(b);
        iButtonsEditBox.put(b,et);
        //++i;
        //et.setTag(i);
        et.setId(View.generateViewId());
        RelativeLayout.LayoutParams editTextLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        editTextLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        editTextLayoutParams.width = 425;
        et.setLayoutParams(editTextLayoutParams);
        listCurrent.addView(et);

        b.setImageResource(R.drawable.plus);
        listCurrent.addView(b);



        //
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                b.setClickable(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    b.setClickable(false || iButtonsEditBox.size()>4);
                }else {
                    b.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0 || iButtonsEditBox.size()>20){
                    b.setClickable(false);
                }else {
                    b.setClickable(true);
                }
            }
        });

        final LinearLayout ll = (LinearLayout) findViewById(R.id.scrollList);
        ll.addView(listCurrent);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //adding the current word to the list
                if (Container.checkValidString(et, MainActivity.this)==0) {
                    list.put(et.getText().toString().trim(), 0);
                }
                if (editBoxes.size()<=4) {
                    addEditView();
                    Log.d("demo", "editBoxes "+iButtonsEditBox.size()+" imageButtons "+iButtonsEditBox.size());
                    ImageButton minusButton = ((ImageButton) v);
                    minusButton.setImageResource(R.drawable.minus);
                    minusButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Remove button and editbox here
                            ll.removeView(listCurrent);
                            //editBoxes.remove();
                            //imageButtons.remove(v);
                            iButtonsEditBox.remove(v);
                            Log.d("demo", "editBoxes "+iButtonsEditBox.size()+" imageButtons "+iButtonsEditBox.size());
                            b.setClickable(true);
                            //for(ImageButton ib: iButtonsEditBox.keySet())
                            //    ib.setClickable(true);

                        }
                    });
                }
            }

        });

        b.setClickable(false);
        // RelativeLayout.LayoutParams imageButton = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
/**
 if(iButtonsEditBox.size()<=4){
 b.setClickable(true);
 }
 else{
 b.setClickable(false);
 }
 */
    }


    /**
     * This method checks all the input text fields for the validity of the input
     * it throws explicit Toast messages
     *
     * no symbols or null values are allowed
     */
    public int checkFields(){
        Iterator it =  iButtonsEditBox.values().iterator();
        int valid=0;
        while (it.hasNext()) {
            EditText et = ((EditText) it.next());
            valid = Container.checkValidString(et, MainActivity.this);
            if (valid!=0){
                return valid;
            }

        }
        return 0;
    }

    /**
     * This class reads the file and stores the result in byte[]
     */
    //------------------------------------------------------------
    class ReadFile implements Runnable{
        public ReadFile(){

        }
        @Override
        public void run() {
            //String text = "";
            try {
                is = getAssets().open("textfile.txt");
                int size = is.available();
                Log.d("Size:",size+"");
                buffer = new byte[size];
                is.read(buffer);
                is.close();
                // text = new String(buffer);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //------------------------------------------------------------



    /**
     * This class the file and stores the result in byte[]
     */
    //------------------------------------------------------------
    class ReadWords implements Runnable{

        public ReadWords(){
        }
        @Override
        public void run() {

            Iterator it =  iButtonsEditBox.values().iterator();
            list = new HashMap<>();
            while (it.hasNext()) {
                EditText et = ((EditText)it.next());
                list.put(et.getText().toString().trim(), 0);

                //Log.d("demo", "Words "+(Container.countWord(currentWord)));
            }


            //calling the method for counting the frequency of each word in the text corpus
            results = new HashMap<>();
            CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
            boolean matchcase = checkBox.isChecked();
            results = Container.findAndCountWords(list,matchcase);
            Log.d("demo_result", "The list of words and frequencies: " + results.toString());


        }
    }
    //------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == MainActivity.RESULT_OK){
                String result=data.getStringExtra("result");
                finish();
                startActivity(getIntent());
            }
            if (resultCode == MainActivity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult
}

