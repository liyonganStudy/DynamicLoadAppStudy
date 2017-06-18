package com.landy.dynamicload;

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
        MLog.log("==============method is hooked==================");
        MLog.log("method:" + method.getName() + " called with args:" + Arrays.toString(args));
        return method.invoke(mBase, args);
    }
}
