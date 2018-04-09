package com.mad.group24_hw04;


/**
 * authors: Mihai Mehedint, Swati Jha
 * assignment: HW4
 * file name: MainActivity.java
 */




        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.Handler;
        import android.os.Message;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.Toast;

        import java.util.LinkedList;



public class MainActivity extends AppCompatActivity implements GetTriviaAsyncTask.TriviaData{
    LinkedList<Question> finalquestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.buttonStart).setClickable(false);
        findViewById(R.id.textViewImage).setVisibility(View.INVISIBLE);

        if (isConnected()) {
            new GetTriviaAsyncTask(MainActivity.this).execute("http://dev.theappsdr.com/apis/trivia_json/index.php");
        }else{
            Toast.makeText(MainActivity.this, "NO Internet Connection!", Toast.LENGTH_LONG);
        }
        findViewById(R.id.buttonExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.buttonStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TriviaActivity.class);
                intent.putExtra("LIST",finalquestions);
                startActivity(intent);
            }
        });

    }


    private boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected())
            return true;
        return false;
    }

    @Override
    public void setupData(LinkedList<Question> questions) {
        //here setup the parsed data
        Log.d("demo", "\nQuestions AFTER parsing: \n"+questions.toString());
        if(questions!=null && questions.size()!=0) {
            ImageView iv =((ImageView) findViewById(R.id.imageView));
            iv.setImageResource(R.drawable.trivia);
            findViewById(R.id.textViewImage).setVisibility(View.VISIBLE);
            findViewById(R.id.buttonStart).setClickable(true);
            finalquestions = questions;
        }


    }

    @Override
    public Context getContext() {
        return this;
    }
}

