package com.landy.dynamicload;

import android.os.IBinder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by landy on 17/6/18.
 * go go go
 */

public class IBinderInvocationHandler implements InvocationHandler {

    private IBinder mBase;

    public IBinder bind(IBinder base) {
        mBase = base;
        return (IBinder) Proxy.newProxyInstance(base.getClass().getClassLoader(), base.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("queryLocalInterface".equals(method.getName())) {
            MLog.log("hook queryLocalInterface");
            // 这里直接返回真正被Hook掉的Service接口
            // 这里的 queryLocalInterface 就不是原本的意思了
            // 我们肯定不会真的返回一个本地接口, 因为我们接管了 asInterface方法的作用
            // 因此必须是一个完整的 asInterface 过的 IInterface对象, 既要处理本地对象,也要处理代理对象
            // 这只是一个Hook点而已, 它原始的含义已经被我们重定义了; 因为我们会永远确保这个方法不返回null
            // 让 IClipboard.Stub.asInterface 永远走到if语句的else分支里面

            Class<?> stub = null;
            Object clipboard = null;
            try {
                stub = Class.forName("android.content.IClipboard$Stub");
                Method asInterfaceMethod = stub.getDeclaredMethod("asInterface", IBinder.class);
                clipboard = asInterfaceMethod.invoke(null, mBase);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return new ClipboardInvocationHandler().bind(clipboard);
        }
        return method.invoke(mBase, args);
    }
}
