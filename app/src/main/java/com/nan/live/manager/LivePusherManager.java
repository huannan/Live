package com.nan.live.manager;

import android.content.Context;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.nan.live.listener.LiveStateChangeListener;
import com.nan.live.params.AudioParams;
import com.nan.live.params.VideoParams;
import com.nan.live.pusher.AudioPusher;
import com.nan.live.pusher.PushNative;
import com.nan.live.pusher.VideoPusher;

/**
 * Created by huannan on 2017/4/7.
 */

@SuppressWarnings("deprecation")
public class LivePusherManager implements SurfaceHolder.Callback {

    private static final String TAG = LivePusherManager.class.getSimpleName();
    private VideoPusher mVideoPusher;
    private AudioPusher mAudioPusher;
    private VideoParams mVideoParams;
    public boolean isLiving = false;
    private AudioParams mAudioParams;
    private Context mContext;
    private PushNative mPushNative;

    public LivePusherManager(Context context, SurfaceHolder holder) {
        mContext = context.getApplicationContext();
        init(holder);
    }

    private void init(SurfaceHolder holder) {
        mPushNative = new PushNative();

        //拿到窗口信息，但是这样画面会太大了，因此采用较低的分辨率
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //添加回调，在SurfaceView销毁的时候释放所有资源
        holder.addCallback(this);

        //初始化视频采集与推流
        mVideoParams = new VideoParams(176, 144, Camera.CameraInfo.CAMERA_FACING_BACK);
//        mVideoParams = new VideoParams(width, height, Camera.CameraInfo.CAMERA_FACING_BACK);
        mVideoPusher = new VideoPusher(holder, mVideoParams, mPushNative);

        //初始化音频采集与推流
        mAudioParams = new AudioParams();
        mAudioPusher = new AudioPusher(mAudioParams, mPushNative);
    }


    /**
     * 切换摄像头
     */
    public void switchCamera() {
        mVideoPusher.switchCamera();
    }

    /**
     * 开始推流
     */
    public void startPush(String pushUrl, LiveStateChangeListener listener) {
        mVideoPusher.startPush();
        mAudioPusher.startPush();
        mPushNative.startPush(pushUrl);
        mPushNative.setLiveStateChangeListener(listener);
        isLiving = true;
    }

    /**
     * 停止推流
     */
    public void stopPush() {
        mVideoPusher.stopPush();
        mAudioPusher.stopPush();
        mPushNative.stopPush();
        mPushNative.removeLiveStateChangeListener();
        isLiving = false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //释放资源
        mVideoPusher.release();
        mAudioPusher.release();
        try {
            mPushNative.release();
        } catch (Throwable t) {
            Log.e(TAG, "surfaceDestroyed: " + t.getMessage());
        }
    }
}
