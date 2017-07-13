package com.landy.runtimedex;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class DexActivity extends Activity {

    private static final String TAG = "lya";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dex);
        TextView textView = new TextView(this);
        textView.setBackgroundColor(Color.RED);
//        textView.setText("Hello, I am from dex Activity!!!!" + getSelfResources().getString(R.string.app_name));
        textView.setText("Hello, I am from dex Activity!!!!" + getResources().getString(R.string.app_name));
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        Log.w(TAG, "onCreate: ");
        toast("onCreate");
        setContentView(textView);
    }

    private void toast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "onResume: ");
        toast("onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy: ");
    }
}
