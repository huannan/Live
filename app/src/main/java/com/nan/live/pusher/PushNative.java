package com.nan.live.pusher;

import com.nan.live.listener.LiveStateChangeListener;

/**
 * Created by huannan on 2017/4/7.
 */

public class PushNative {

    public static final int CONNECT_FAILED = 101;
    public static final int INIT_FAILED = 102;

    LiveStateChangeListener liveStateChangeListener;

    /**
     * 接收Native层抛出的错误
     *
     * @param code
     */
    public void throwNativeError(int code) {
        if (liveStateChangeListener != null) {
            liveStateChangeListener.onError(code);
        }
    }

    public void setLiveStateChangeListener(LiveStateChangeListener liveStateChangeListener) {
        this.liveStateChangeListener = liveStateChangeListener;
    }

    public void removeLiveStateChangeListener() {
        this.liveStateChangeListener = null;
    }

    public native void startPush(String url);

    public native void stopPush();

    public native void release();

    public native void setVideoOptions(int width, int height, int bitRate, int fps);

    public native void setAudioOptions(int sampleRateInHz, int channel);

    public native void fireVideo(byte[] buffer);

    public native void fireAudio(byte[] buffer, int length);

}
