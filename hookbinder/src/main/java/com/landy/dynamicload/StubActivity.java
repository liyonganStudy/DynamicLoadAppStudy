package com.landy.dynamicload;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by landy on 17/6/19.
 * go go go
 */
public class StubActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("StubActivity");
        MLog.log("onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLog.log("onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        MLog.log("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLog.log("onDestroy");
    }
}
