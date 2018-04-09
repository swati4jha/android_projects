package com.example.mihai.group24_inclass03;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    public int passwordSize=8;
    public int count=0, lenght=0;
    int passwordCountAsync=0, passwordLengthAsync=0, progressDial, progressLenght;
    ArrayList<String> passwords = new ArrayList<String>();;
    Handler handler;
    ProgressDialog progressDialog;

    boolean asyncFinished = false;
    boolean threadFinished = false;


    ArrayList<String> asyncPwds = new ArrayList<String>(); //swati
    ArrayList<String> asyncPwdList = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //progress dialog
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Generating Passwords: ");
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);



        SeekBar sb_count = (SeekBar) findViewById(R.id.seekBar_count);
        sb_count.setProgress(0);
        sb_count.setMax(10);
        SeekBar sb_lenght = (SeekBar) findViewById(R.id.seekBar_length);
        sb_lenght.setProgress(0);
        sb_lenght.setMax(16); //retrieve the value and add 7  (7-23)



        Log.d("demo", count+"   "+lenght);
        final TextView count_tx = ((TextView) findViewById(R.id.thread_Number_count));
        final TextView lenght_tx = ((TextView)findViewById(R.id.password_Length_text));

        sb_count.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                count = (Integer)seekBar.getProgress();
                count_tx.setText(String.valueOf(count));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        sb_lenght.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lenght = ((Integer)seekBar.getProgress()+7);
                lenght_tx.setText(String.valueOf(lenght));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        final ExecutorService taskPool = Executors.newFixedThreadPool(2);

        Button generate = (Button) findViewById(R.id.Generate);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();


                asyncFinished = false;
                threadFinished =false;
                progressDial=0;
                TextView pwdCountView = (TextView) findViewById(R.id.textViewPwdCount);
                passwordCountAsync = Integer.parseInt(pwdCountView.getText().toString());
                progressLenght = count+passwordCountAsync;
                passwords.clear();
                progressDialog.setMax(progressLenght);
                progressDialog.setProgress(passwords.size());
                //progressDialog.setProgress(0);

                taskPool.execute(new ComputePasswordsThread());




                handler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        //Log.d("demo", "progress started");
                        int i=-1;
                        switch (msg.what){
                            case ComputePasswordsThread.STATUS_START:
                                progressDialog.show();
                                Log.d("demo", "progress started");
                                break;
                            case ComputePasswordsThread.STATUS_CALC_PASSWORD:

                                passwords.add(msg.getData().getString("PROGRESS")); //USE THE BUNDLE UNCOMMENT THIS
                                progressDial =progressDial+1;
                                Log.d("demo", "Mihai " + progressDial);
                                //progressDialog.setProgress(passwords.size());   //USE THE MESSAGE OBJECT UNCOMMENT THIS
                                progressDialog.setProgress(progressDial);
                                Log.d("demo", "passwords current size: "+passwords.size());
                                //i=i+1;
                                Log.d("demo", msg.getData().toString());
                                Log.d("demo", "Stored passwords : "+ passwords);

                                break;
                            case ComputePasswordsThread.STATUS_DONE:
                                //while(asyncFinished) {}
                                //threadFinished = true;
                                progressDialog.dismiss();


                                Log.d("demo", "thread DONE");
                                break;
                        }
                        return false;
                    }
                });
                //new Thread(new ComputePasswordsThread()).start();


                ////------------============================================
                //swati
                ////------------============================================

                TextView pwdLengthView = (TextView) findViewById(R.id.textViewPwdLength);
                passwordLengthAsync = Integer.parseInt(pwdLengthView.getText().toString());
                Log.d("Len::", passwordCountAsync + "::" + passwordLengthAsync);
                new MyTask().execute(passwordCountAsync, passwordLengthAsync);



                ////------------============================================
                ////------------============================================
                //while(!asyncFinished || !threadFinished) {
                 //       Log.d("demo", "testing");
                  // try {
                   //    Thread.sleep(300);
                  //  } catch (InterruptedException e) {
                   //     e.printStackTrace();
                    //}
               // }

                //while(asyncFinished) {}

                Intent intent = new Intent(MainActivity.this, GeneratedPasswordsActivity.class);
                intent.putExtra("passwords", passwords);
                intent.putExtra("async", asyncPwdList);
                try {
                      Thread.sleep(2000);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                }

                startActivity(intent);  // the uncomment will pass the intent



            }



        });



        ////------------============================================
        //swati
        ////------------============================================
        SeekBar seekBarCount = (SeekBar) findViewById(R.id.seekBarAsyncPwdCount);
        seekBarCount.setMax(9);
        seekBarCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int passwordCountAsync = seekBar.getProgress() + 1;
                TextView pwdCountView = (TextView) findViewById(R.id.textViewPwdCount);
                pwdCountView.setText(String.valueOf(passwordCountAsync));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        SeekBar seekBarLength = (SeekBar) findViewById(R.id.seekBarpwdLength);
        seekBarLength.setMax(16);
        seekBarLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int passwordLengthAsync = seekBar.getProgress() + 7;
                TextView pwdLengthView = (TextView) findViewById(R.id.textViewPwdLength);
                pwdLengthView.setText(String.valueOf(passwordLengthAsync));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        ////------------============================================
        ////------------============================================




    }


    class ComputePasswordsThread implements Runnable{
        static final int STATUS_START=0x00;
        static final int STATUS_CALC_PASSWORD=0x01;
        static final int STATUS_DONE=0x02;


        @Override
        public void run() {
            //String[] passwords = new String[count];
            String password;
            Log.d("demo", "count "+count+" length: "+lenght);
            Message msg = new Message();
            msg.what=STATUS_START;
            handler.sendMessage(msg);
            for(int i=0;i<count;i++) {
                password=Util.getPassword(lenght);
                //progressDialog.setProgress(i);
                msg = new Message();
                msg.what=STATUS_CALC_PASSWORD;

                Bundle data = new Bundle();    //this is like Extras, like a hashmap
                data.putString("PROGRESS", password);    //set key-value pairs
                msg.setData(data);              //set data to bundle
                handler.sendMessage(msg);
            }
            msg = new Message();
            msg.what= STATUS_DONE;
            handler.sendMessage(msg);
        }
    }


    ////------------============================================
    //swati
    ////------------============================================

    private class MyTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Integer doInBackground(Integer... params) {
            if (params != null && params.length > 0) {
                int count = params[0];
                int length = params[1];
                Log.d("Len::", count + "::" + length);
                for (int i = 0; i < count; i++) {
                    asyncPwds.add(Util.getPassword(length));
                    publishProgress();
                }
            }
            return 100;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDial =progressDial+1;
            //progressDialog.setProgress(passwords.size());   //USE THE MESSAGE OBJECT UNCOMMENT THIS
            progressDialog.setProgress(progressDial);
            Log.d("demo", "Swati " + progressDial);

            //pb.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Log.d("PWdLength:", asyncPwds.size() + "");
            for (int i = 0; i < asyncPwds.size(); i++) {
                Log.d("PWd:", asyncPwds.get(i));
            }

            //asyncFinished = true;
           // while(threadFinished) {}
            Log.d("demo", "async done");


        }
    }

    ////------------============================================
    ////------------============================================


}

