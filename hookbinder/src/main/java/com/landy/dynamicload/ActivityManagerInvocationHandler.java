package com.landy.dynamicload;

import android.content.ComponentName;
import android.content.Intent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * Created by landy on 17/6/18.
 * go go go
 */

public class ActivityManagerInvocationHandler implements InvocationHandler {

    private Object mBase;

    public Object bind(Object base) throws ClassNotFoundException {
        mBase = base;
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), mBase.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        MLog.log("==============method is hooked==================");
//        MLog.log("method:" + method.getName() + " called with args:" + Arrays.toString(args));
        if ("startActivity".equals(method.getName())) {
            // 只拦截这个方法
            // 替换参数, 任你所为;甚至替换原始Activity启动别的Activity偷梁换柱
            // API 23:
            // public final Activity startActivityNow(Activity parent, String id,
            // Intent intent, ActivityInfo activityInfo, IBinder token, Bundle state,
            // Activity.NonConfigurationInstances lastNonConfigurationInstances) {
            // 找到参数里面的第一个Intent 对象
            Intent raw;
            int index = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }
            raw = (Intent) args[index];
            Intent newIntent = new Intent();
            // 这里包名直接写死,如果再插件里,不同的插件有不同的包  传递插件的包名即可
            String targetPackage = "com.landy.dynamicload";
            // 这里我们把启动的Activity临时替换为 StubActivity
            ComponentName componentName = new ComponentName(targetPackage, StubActivity.class.getCanonicalName());
            newIntent.setComponent(componentName);

            // 把我们原始要启动的TargetActivity先存起来
            newIntent.putExtra(SystemServiceHookHelper.EXTRA_TARGET_INTENT, raw);
            // 替换掉Intent, 达到欺骗AMS的目的
            args[index] = newIntent;
            MLog.log("hook start activity success");
            MLog.log("method:" + method.getName() + " called with args:" + Arrays.toString(args));
            return method.invoke(mBase, args);
        }
        return method.invoke(mBase, args);
    }
}
