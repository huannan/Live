package com.nan.live;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nan.live.listener.LiveStateChangeListener;
import com.nan.live.manager.LivePusherManager;
import com.nan.live.pusher.PushNative;
import com.nan.live.utils.PreferenceUtils;

public class LivePushActivity extends AppCompatActivity implements View.OnClickListener, LiveStateChangeListener {

    private SurfaceView sv_preview;
    private LivePusherManager mLivePusherManager;
    private Button btn_push;
    private Button btn_camera_switch;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case PushNative.CONNECT_FAILED:
                    Toast.makeText(LivePushActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    //Log.d("jason", "连接失败..");
                    break;
                case PushNative.INIT_FAILED:
                    Toast.makeText(LivePushActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_push);
        init();
    }

    private void init() {
        sv_preview = (SurfaceView) findViewById(R.id.sv_preview);
        btn_push = (Button) findViewById(R.id.btn_push);
        btn_camera_switch = (Button) findViewById(R.id.btn_camera_switch);
        btn_push.setOnClickListener(this);
        btn_camera_switch.setOnClickListener(this);

        mLivePusherManager = new LivePusherManager(this, sv_preview.getHolder());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_push:
                if (!mLivePusherManager.isLiving) {
                    String rtmpUrl = PreferenceUtils.getInstance(this).getRTMPUrl();
                    //开始直播，并且添加直播状态监听
                    mLivePusherManager.startPush(rtmpUrl, this);
                    btn_push.setText("停止直播");
                } else {
                    mLivePusherManager.stopPush();
                    btn_push.setText("开始直播");
                }
                break;
            case R.id.btn_camera_switch:
                mLivePusherManager.switchCamera();
                break;
        }
    }

    @Override
    public void onError(int code) {
        //直播状态监听
        mHandler.sendEmptyMessage(code);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
