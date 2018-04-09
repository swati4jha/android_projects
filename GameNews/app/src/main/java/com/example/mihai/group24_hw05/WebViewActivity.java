package com.example.mihai.group24_hw05;
/**
 * authors: Mihai Mehedint, Swati Jha
 * assignment: HW4
 * file name: MainActivity.java
 */


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.net.URI;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class WebViewActivity extends AppCompatActivity{ //extends YouTubeBaseActivity {
    //private YouTubePlayerView playerView;
    //private YouTubePlayer.OnInitializedListener onInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        if(getIntent().getExtras()!=null) {
            final Detail game = (Detail) getIntent().getExtras().getParcelable("videoUrl");

            //Uri videoUrl = Uri.parse(getIntent().getExtras().get("videoUrl").toString());
        /*
        VideoView video = ((VideoView)findViewById(R.id.videoView));
        MediaController mc = new MediaController(this);
        mc.setAnchorView(video);
        mc.setMediaPlayer(video);
        video.setMediaController(mc);
        video.setVideoURI(videoUrl);
        video.start();
*/
            /**
             playerView = (YouTubePlayerView) findViewById(R.id.youtube_View);
             onInitializedListener=new YouTubePlayer.OnInitializedListener() {
            @Override public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
            youTubePlayer.loadVideo(videoUrl);
            playerView.initialize("AIzaSyBjlLsMC-dFKLcIO-E1OZS_rztwl38wr6k", onInitializedListener);
            }

            @Override public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
            };
             */


            // String frameVideo = "<html><body>Video From YouTube<br><iframe width=\"420\" height=\"315\" src=\"<iframe width=\"400\" height=\"300\" src=\"http://www.youtube.com/watch?v=xhzJumt6264\" frameborder=\"0\" allowfullscreen></iframe> </body></html>";
            //String frameVideo = "<html><body>Video From YouTube<br><iframe width=\"420\" height=\"315\" src=\""+videoUrl+"\" frameborder=\"0\" allowfullscreen></iframe></body></html>";


            ((TextView) findViewById(R.id.title_video)).setText(game.getTitle() + " " + game.getDate() + " @ Trailer");

            WebView webview = (WebView) findViewById(R.id.youtube);
            webview.setWebViewClient(new WebViewClient());
            WebSettings webSettings = webview.getSettings();
            webSettings.setJavaScriptEnabled(true);
            //webview.loadData(frameVideo, "text/html", "utf-8");

            if(game.getYoutube()!=null) {
                webview.loadUrl(game.getYoutube());//this works
            }else{
                Toast.makeText(WebViewActivity.this, "This game version does not have a trailer movie!", Toast.LENGTH_SHORT);
            }


            //   //startActivity(new Intent(Intent.ACTION_VIEW,videoUrl)); //this works too
        }else{
            Toast.makeText(WebViewActivity.this, "There is no data to display!", Toast.LENGTH_SHORT);
        }


    }
}
