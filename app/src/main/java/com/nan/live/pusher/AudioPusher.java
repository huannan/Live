package com.nan.live.pusher;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import com.nan.live.params.AudioParams;

/**
 * Created by huannan on 2017/4/7.
 */

public class AudioPusher extends BasePusher {

    private static final String TAG = AudioPusher.class.getSimpleName();
    private PushNative mPushNative;
    private AudioParams mAudioParams;
    private int minBufferSize;
    private AudioRecord mAudioRecord;

    public AudioPusher(AudioParams audioParams, PushNative pushNative) {
        mAudioParams = audioParams;
        mPushNative = pushNative;

        int channelConfig = audioParams.getChannel() == 1 ?
                AudioFormat.CHANNEL_IN_MONO : AudioFormat.CHANNEL_IN_STEREO;
        //最小缓冲区大小
        minBufferSize = AudioRecord.getMinBufferSize(audioParams.getSampleRateInHz(), channelConfig, AudioFormat.ENCODING_PCM_16BIT);
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                audioParams.getSampleRateInHz(),
                channelConfig,
                AudioFormat.ENCODING_PCM_16BIT, minBufferSize);
    }

    @Override
    public void startPush() {
        mPushNative.setAudioOptions(
                mAudioParams.getSampleRateInHz(),
                mAudioParams.getChannel());

        isPushing = true;
        //启动一个录音子线程
        new Thread(new AudioRecordTask()).start();
    }

    @Override
    public void stopPush() {
        isPushing = false;
        mAudioRecord.release();
    }

    @Override
    public void release() {
        if (mAudioRecord != null) {
            mAudioRecord.release();
            mAudioRecord = null;
        }
    }

    private class AudioRecordTask implements Runnable {
        @Override
        public void run() {
            //开始录音
            mAudioRecord.startRecording();

            while (isPushing) {
                //通过AudioRecord不断读取音频数据
                byte[] buffer = new byte[minBufferSize];
                int len = mAudioRecord.read(buffer, 0, buffer.length);
                if (len > 0) {
                    //传给Native代码，进行音频编码
                    mPushNative.fireAudio(buffer, len);
                }
            }
        }
    }
}
