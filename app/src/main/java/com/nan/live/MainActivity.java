package com.nan.live;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nan.live.utils.PreferenceUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_push;
    private Button btn_pull;
    private EditText et_rtmp_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_push = (Button) findViewById(R.id.btn_push);
        btn_pull = (Button) findViewById(R.id.btn_pull);
        et_rtmp_url = (EditText) findViewById(R.id.et_rtmp_url);
        requestPermission();
    }


    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //默认授予权限，开始初始化
        init();
    }

    private void init() {

        btn_push.setOnClickListener(this);
        btn_pull.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        //保存RTMP的地址
        String rtmpUrl = et_rtmp_url.getText().toString().trim();
        PreferenceUtils.getInstance(this).setRTMPUrl(rtmpUrl);

        switch (v.getId()) {
            case R.id.btn_push:
                startActivity(new Intent(this, LivePushActivity.class));
                break;
            case R.id.btn_pull:
                startActivity(new Intent(this, LivePullActivity.class));
                break;
        }
    }
}
