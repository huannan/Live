package com.nan.live.pusher;

/**
 * Created by huannan on 2017/4/7.
 */

public abstract class BasePusher {

    protected boolean isPushing;

    public abstract void startPush();

    public abstract void stopPush();

    public abstract void release();

}
