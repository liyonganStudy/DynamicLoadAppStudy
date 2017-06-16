package com.landy.dynamicload;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.landy.dynamicload.proxy.DynamicProxyShopping;
import com.landy.dynamicload.proxy.MLog;
import com.landy.dynamicload.proxy.ProxyShopping;
import com.landy.dynamicload.proxy.Shopping;
import com.landy.dynamicload.proxy.ShoppingImp;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.openActivity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingImp shoppingImp = new ShoppingImp();
                MLog.log("================");
                String[] goods = shoppingImp.doShop(100);
                for (String good : goods) {
                    MLog.log(good);
                }
                shoppingImp.buySomeFood(100);

                ProxyShopping proxyShopping = new ProxyShopping(shoppingImp);
                MLog.log("================");
                goods = proxyShopping.doShop(100);
                for (String good : goods) {
                    MLog.log(good);
                }

                MLog.log("================");
                Shopping shopping = new DynamicProxyShopping(shoppingImp).bind();
                goods = shopping.doShop(100);
                for (String good : goods) {
                    MLog.log(good);
                }
                shopping.buySomeFood(100);

                try {
                    attachActivityContext(MainActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SecondActivity.launch(MainActivity.this);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
//        try {
//            attachContext();
//            attachActivityContext(MainActivity.this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    public static void attachContext() throws Exception {
        // 先获取到当前的ActivityThread对象
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
        currentActivityThreadMethod.setAccessible(true);
        //currentActivityThread是一个static函数所以可以直接invoke，不需要带实例参数
        Object currentActivityThread = currentActivityThreadMethod.invoke(null);

        // 拿到原始的 mInstrumentation字段
        Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
        mInstrumentationField.setAccessible(true);
        Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(currentActivityThread);

        // 创建代理对象
        Instrumentation evilInstrumentation = new MyInstrumentation(mInstrumentation);

        // 偷梁换柱
        mInstrumentationField.set(currentActivityThread, evilInstrumentation);
    }

    public static void attachActivityContext(Activity activity) throws Exception {
        Field mInstrumentationField = Activity.class.getDeclaredField("mInstrumentation");
        mInstrumentationField.setAccessible(true);
        Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(activity);

        // 创建代理对象
        Instrumentation evilInstrumentation = new MyInstrumentation(mInstrumentation);

        // 偷梁换柱
        mInstrumentationField.set(activity, evilInstrumentation);

    }
}
