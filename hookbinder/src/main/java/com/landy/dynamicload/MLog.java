package com.landy.dynamicload;

import android.util.Log;

/**
 * Created by landy on 17/6/16.
 */

public class MLog {

    private static final String TAG = "lyaProxy";

    public static void log(String content) {
        Log.w(TAG, content);
    }
}
