package com.nan.live.params;

/**
 * Created by huannan on 2017/4/7.
 */

public class AudioParams {

    // 采样率
    private int sampleRateInHz = 44100;
    // 声道个数
    private int channel = 1;

    public int getSampleRateInHz() {
        return sampleRateInHz;
    }

    public void setSampleRateInHz(int sampleRateInHz) {
        this.sampleRateInHz = sampleRateInHz;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}
