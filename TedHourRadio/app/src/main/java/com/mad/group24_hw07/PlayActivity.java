package com.mad.group24_hw07;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class PlayActivity extends AppCompatActivity implements GetImage.ImageData, MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {
    RadioDetails details;
    MediaPlayer mediaPlayer;
    private MediaController mediaController;
    //private Handler handler = new Handler();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        myToolbar.setLogo(R.drawable.icon);

        if (getIntent().getExtras()!=null) {
            details = (RadioDetails) getIntent().getExtras().getParcelable("detail");
            Log.d("demo", "Play details: "+details.toString());

            new GetImage(PlayActivity.this).execute(details.getImageURL());

            TextView title = (TextView) findViewById(R.id.title2);
            title.setText(details.getTitle());
            TextView descr = (TextView) findViewById(R.id.viewDescription);
            descr.setText(details.getDescription());
            TextView date = (TextView) findViewById(R.id.pubDate);
            date.setText(details.getPubDate());
            TextView duration = (TextView) findViewById(R.id.duration);
            duration.setText(details.getDuration());


            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnPreparedListener((MediaPlayer.OnPreparedListener) this);

                mediaController = new MediaController(this);
                //MediaPlayer player =MediaPlayer.create(this.getContext(), );// new MediaPlayer();

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(details.getPlayURL());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception e) {

                Log.d("demo", "Could not open audio file ");
            }

        }else {
            Toast.makeText(this, "Details NOT received", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void setupData(Bitmap result) {
        //pb.setVisibility(View.INVISIBLE);

        ImageView iv = (ImageView) findViewById(R.id.imageViewPlay);
        iv.setImageBitmap(result);
    }

    @Override
    public Context getContext() {
        return PlayActivity.this;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) mediaPlayer.release();
    }



    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    //--------------------------------------------------------------------------------

    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d("demo", "onPrepared");
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(findViewById(R.id.mediaPlayerLayout));

        handler.post(new Runnable() {
            public void run() {
                mediaController.setEnabled(true);
                mediaController.show();
            }
        });
    }
}
