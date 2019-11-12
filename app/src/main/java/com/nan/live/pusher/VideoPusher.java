package com.nan.live.pusher;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import com.nan.live.params.VideoParams;

import java.util.List;

/**
 * Created by huannan on 2017/4/7.
 */

@SuppressWarnings("deprecation")
public class VideoPusher extends BasePusher implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private static final String TAG = VideoPusher.class.getSimpleName();
    private final PushNative mPushNative;
    private SurfaceHolder mSurfaceHolder;
    private VideoParams mVideoParams;
    @Deprecated
    private Camera mCamera;
    private byte[] buffers;

    public VideoPusher(SurfaceHolder holder, VideoParams videoParams, PushNative pushNative) {
        mPushNative = pushNative;
        mSurfaceHolder = holder;
        mVideoParams = videoParams;
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void startPush() {
        mPushNative.setVideoOptions(
                mVideoParams.getWidth(),
                mVideoParams.getHeight(),
                mVideoParams.getBitRate(),
                mVideoParams.getFps());
        isPushing = true;
    }

    @Override
    public void stopPush() {
        isPushing = false;
    }

    @Override
    public void release() {
        stopPreview();
    }

    @Override
    @Deprecated
    public void surfaceCreated(SurfaceHolder holder) {
        //SurfaceView初始化完成以后，开始初始化摄像头，并且进行预览
        startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * 开始预览
     */
    @Deprecated
    private void startPreview() {
        try {
            //SurfaceView初始化完成，可以进行预览
            mCamera = Camera.open(mVideoParams.getCameraId());
            Camera.Parameters param = mCamera.getParameters();

            // 魅族机器不支持设置预览宽高，因此在这里更新mVideoParams

            List<Camera.Size> supportedPreviewSizes = param.getSupportedPreviewSizes();
//            int width = param.getPreviewSize().width;
//            int height = param.getPreviewSize().height;
//            mVideoParams.setWidth(width);
//            mVideoParams.setHeight(height);

            //设置预览图像的像素格式为NV-21
            param.setPreviewFormat(ImageFormat.NV21);
            //设置预览画面宽高，魅族机器不支持
            param.setPreviewSize(mVideoParams.getWidth(), mVideoParams.getHeight());
            //设置预览帧频，但是x264压缩的时候还是有另外一个帧频的
            //param.setPreviewFpsRange(mVideoParams.getFps() - 1, mVideoParams.getFps());
            mCamera.setParameters(param);

            mCamera.setPreviewDisplay(mSurfaceHolder);

            //如果是正在直播的话需要实时获取预览图像数据
            //缓冲区，大小需要根据摄像头的分辨率而定，x4换算为字节
            buffers = new byte[mVideoParams.getWidth() * mVideoParams.getHeight() * 4];
            mCamera.addCallbackBuffer(buffers);
            mCamera.setPreviewCallbackWithBuffer(this);

            //开始预览
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止预览
     */
    @Deprecated
    private void stopPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 切换摄像头
     */
    @Deprecated
    public void switchCamera() {
        if (mVideoParams.getCameraId() == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mVideoParams.setCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);
        } else {
            mVideoParams.setCameraId(Camera.CameraInfo.CAMERA_FACING_BACK);
        }

        //切换不同的摄像头需要停止，并且重新打开
        stopPreview();
        startPreview();
    }

    /**
     * 摄像头数据更新回调，获取摄像头的数据，并且推流
     *
     * @param data
     * @param camera
     */
    @Override
    @Deprecated
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (mCamera != null) {
            mCamera.addCallbackBuffer(buffers);
        }

        if (isPushing) {
            mPushNative.fireVideo(data);
        }
    }
}
