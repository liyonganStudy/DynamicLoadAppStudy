package com.landy.runtimedex;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;

public class BaseActivity extends Activity {

    private static final String TAG = "lya";

    private Resources mResources;
    private AssetManager mAssetManager;
    private Resources.Theme mTheme;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        if (mAssetManager == null) {
            mAssetManager = createAssetManager(getFileStreamPath("runtimedex-debug.apk"));
        }
        if (mAssetManager != null) {
            mResources = createResources(mAssetManager);
            mTheme = mResources.newTheme();
            mTheme.setTo(super.getTheme());
        }
    }

    @Override
    public Resources getResources() {
        return mResources == null ? super.getResources() : mResources;
    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    private AssetManager createAssetManager(File dex) {
        try {
            if (!dex.exists()) {
                Log.w(TAG, "dex not exists");
                return null;
            }
            String dexPath = dex.getAbsolutePath();
            Log.w(TAG, "dexpath: " + dexPath);
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            return assetManager;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private Resources createResources(AssetManager assetManager) {
        if (assetManager == null) {
            return null;
        }
        Resources raw = super.getResources();
        return new Resources(assetManager, raw.getDisplayMetrics(), raw.getConfiguration());
    }
}
