package com.landy.dynamicload;

import android.os.Handler;
import android.os.IBinder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by landy on 17/6/16.
 * go go go
 */

public class SystemServiceHookHelper {
    public static final String EXTRA_TARGET_INTENT = "extra_target_intent";
    private static final String CLIPBOARD_SERVICE = "clipboard";

    public static void hookClipboardService() throws Exception {

        // 下面这一段的意思实际就是: ServiceManager.getService("clipboard");
        // 只不过 ServiceManager这个类是@hide的
        Class<?> serviceManager = Class.forName("android.os.ServiceManager");
        Method getService = serviceManager.getDeclaredMethod("getService", String.class);

        // ServiceManager里面管理的原始的Clipboard Binder对象
        // 一般来说这是一个Binder代理对象
        IBinder rawBinder = (IBinder) getService.invoke(null, CLIPBOARD_SERVICE);

        // Hook 掉这个Binder代理对象的 queryLocalInterface 方法
        // 然后在 queryLocalInterface 返回一个IInterface对象, hook掉我们感兴趣的方法即可.
        IBinder hookedBinder = new IBinderInvocationHandler().bind(rawBinder);

        // 把这个hook过的Binder代理对象放进ServiceManager的cache里面
        // 以后查询的时候 会优先查询缓存里面的Binder, 这样就会使用被我们修改过的Binder了
        Field cacheField = serviceManager.getDeclaredField("sCache");
        cacheField.setAccessible(true);
        Map<String, IBinder> cache = (Map) cacheField.get(null);
        cache.put(CLIPBOARD_SERVICE, hookedBinder);
    }

    public static void hookActivityManager() throws IllegalAccessException, ClassNotFoundException, NoSuchFieldException {
        Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");

        // 获取 gDefault 这个字段, 想办法替换它
        Field gDefaultField = activityManagerNativeClass.getDeclaredField("gDefault");
        gDefaultField.setAccessible(true);

        Object gDefault = gDefaultField.get(null);

        // 4.x以上的gDefault是一个 android.util.Singleton对象; 我们取出这个单例里面的字段
        Class<?> singleton = Class.forName("android.util.Singleton");
        Field mInstanceField = singleton.getDeclaredField("mInstance");
        mInstanceField.setAccessible(true);

        // ActivityManagerNative 的gDefault对象里面原始的 IActivityManager对象
        Object rawIActivityManager = mInstanceField.get(gDefault);

        // 创建一个这个对象的代理对象, 然后替换这个字段, 让我们的代理对象帮忙干活
        mInstanceField.set(gDefault, new ActivityManagerInvocationHandler().bind(rawIActivityManager));
    }

    /**
     * 由于之前我们用替身欺骗了AMS; 现在我们要换回我们真正需要启动的Activity
     * <p/>
     * 不然就真的启动替身了, 狸猫换太子...
     * <p/>
     * 到最终要启动Activity的时候,会交给ActivityThread 的一个内部类叫做 H 来完成
     * H 会完成这个消息转发; 最终调用它的callback
     */
    public static void hookActivityThreadHandler() throws Exception {

        // 先获取到当前的ActivityThread对象
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Field currentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
        currentActivityThreadField.setAccessible(true);
        Object currentActivityThread = currentActivityThreadField.get(null);

        // 由于ActivityThread一个进程只有一个,我们获取这个对象的mH
        Field mHField = activityThreadClass.getDeclaredField("mH");
        mHField.setAccessible(true);
        Handler mH = (Handler) mHField.get(currentActivityThread);

        // 设置它的回调, 根据源码:
        // 我们自己给他设置一个回调,就会替代之前的回调;

        //        public void dispatchMessage(Message msg) {
        //            if (msg.callback != null) {
        //                handleCallback(msg);
        //            } else {
        //                if (mCallback != null) {
        //                    if (mCallback.handleMessage(msg)) {
        //                        return;
        //                    }
        //                }
        //                handleMessage(msg);
        //            }
        //        }

        Field mCallBackField = Handler.class.getDeclaredField("mCallback");
        mCallBackField.setAccessible(true);

        mCallBackField.set(mH, new ActivityThreadHandlerCallback(mH));

    }
}
