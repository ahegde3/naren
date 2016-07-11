package com.samsung.android.emailcommon.utility;

import com.samsung.android.emailcommon.Logging;

public class Log extends EmailLog{
    public static final String LOG_TAG = "Email";
    public static boolean LOGD = false;
    
    static public void loge(String tag, String str) {
        Log.e(Logging.LOG_TAG, tag + "\t" + str);
    }

    static public void loge(String tag, String str, Throwable tr) {
        Log.e(Logging.LOG_TAG, tag + "\t" + str, tr);
    }

    static public void logd(String tag, String str) {
        Log.d(Logging.LOG_TAG, tag + "\t" + str);
    }

    static public void logv(String tag, String str) {
        Log.v(Logging.LOG_TAG, tag + "\t" + str);
    }

    static public void logs(String tag, String str) {
        Log.v(Logging.LOG_TAG, "SOCKET " + tag + "\t" + str);
    }

    static public void logv2(String tag, String str) {
        Log.v(tag, str);
    }
        
    static public void logv2(String tag, String format, Object... args) {
        Log.v(tag, String.format(format, args));
    }

    static public void logd2(String tag, String str) {
        Log.d(tag, str);
    }
        
    static public void logd2(String tag, String format, Object... args) {
        Log.d(tag, String.format(format, args));
    }

    static public void logi2(String tag, String str) {
        Log.i(tag, str);
    }
    
    static public void logi2(String tag, String format, Object... args) {
        Log.i(tag, String.format(format, args));
    }
    
    static public void logw2(String tag, String str) {
        Log.w(tag, str);
    }
        
    static public void logw2(String tag, String format, Object... args) {
        Log.w(tag, String.format(format, args));
    }

    static public void loge2(String tag, String str) {
        Log.e(tag, str);
    }
        
    static public void loge2(String tag, String format, Object... args) {
        Log.e(tag, String.format(format, args));
    }
}
