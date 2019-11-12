package com.nan.live;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.nan.live.player.VideoPlayerIJK;
import com.nan.live.player.VideoPlayerListener;
import com.nan.live.utils.PreferenceUtils;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class LivePullActivity extends AppCompatActivity {

    public static final String TAG = "LivePullActivity";
    private VideoPlayerIJK video_live;
    private Handler mHandler;
    String rtmpUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_pull);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        video_live = findViewById(R.id.video_live);
        init();
    }

    private void init() {
        mHandler = new Handler();

        rtmpUrl = PreferenceUtils.getInstance(this).getRTMPUrl();
        video_live.setVideoPath(rtmpUrl);
        video_live.requestFocus();

        //加载native库
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Exception e) {
            this.finish();
        }

        video_live.setListener(new VideoPlayerListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int percent) {
                Log.e(TAG, "onBufferingUpdate: " + percent);
            }

            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                Log.e(TAG, "onCompletion: ");
            }

            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
                Log.e(TAG, "onError: " + what + " " + extra);
                return false;
            }

            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
                Log.e(TAG, "onInfo: " + what + " " + extra);
                return false;
            }

            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                // 视频准备好播放了，但是他不会自动播放，需要手动让他开始
                Log.e(TAG, "onPrepared: ");
                iMediaPlayer.start();
            }

            @Override
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                Log.e(TAG, "onSeekComplete: ");
            }

            @Override
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int width, int height, int sar_num, int sar_den) {
                //在此可以获取到视频的宽和高
                Log.e(TAG, "onVideoSizeChanged: " + width + " " + height + " " + sar_num + " " + sar_den);
            }
        });

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                video_live.setVideoPath(rtmpUrl);
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IjkMediaPlayer.native_profileEnd();
    }
}
