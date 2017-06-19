package com.landy.dynamicload;

import android.app.Activity;
import android.os.Bundle;

public class TargetActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        setTitle("TargetActivity");
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
