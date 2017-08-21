package com.nan.live.listener;

/**
 * Created by huannan on 2017/4/9.
 */
public interface LiveStateChangeListener {

    /**
     * 发送错误
     *
     * @param code
     */
    void onError(int code);

}
