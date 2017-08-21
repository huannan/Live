package com.nan.live.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

    public static final String PREFERENCE_NAME = "live_info";
    private static SharedPreferences mSharedPreferences;
    private static PreferenceUtils mPreferenceUtils;
    private static SharedPreferences.Editor editor;

    private static final String KEY_RTMP_URL = "key_rtmp_path";

    private PreferenceUtils(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static PreferenceUtils getInstance(Context cxt) {
        if (mPreferenceUtils == null) {
            mPreferenceUtils = new PreferenceUtils(cxt);
        }
        editor = mSharedPreferences.edit();
        return mPreferenceUtils;
    }

    public void setRTMPUrl(String url) {
        editor.putString(KEY_RTMP_URL, url);
        editor.commit();
    }

    public String getRTMPUrl() {
        return mSharedPreferences.getString(KEY_RTMP_URL, "");
    }

}
