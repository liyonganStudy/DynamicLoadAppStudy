package com.landy.dynamicload;

import android.content.ClipData;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by landy on 17/6/16.
 * go go go
 */

public class ClipboardInvocationHandler implements InvocationHandler {

    private Object mBase;

    public Object bind(Object delegate) {
        mBase = delegate;
        return Proxy.newProxyInstance(mBase.getClass().getClassLoader(), mBase.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("getPrimaryClip".equals(method.getName())) {
            MLog.log("hook getPrimaryClip");
            return ClipData.newPlainText(null, "you are hooked");
        } else if ("hasPrimaryClip".equals(method.getName())) {
            return true;
        }
        return method.invoke(mBase, args);
    }
}
