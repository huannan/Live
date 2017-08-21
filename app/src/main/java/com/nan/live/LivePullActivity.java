package com.nan.live;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nan.live.utils.PreferenceUtils;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class LivePullActivity extends AppCompatActivity {

    private VideoView video_live;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_pull);

        video_live = (VideoView) findViewById(R.id.video_live);

        init();
    }

    private void init() {

        String rtmpUrl = PreferenceUtils.getInstance(this).getRTMPUrl();
        video_live.setVideoPath(rtmpUrl);
        video_live.setMediaController(new MediaController(this));
        video_live.requestFocus();

        video_live.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });

    }
}
