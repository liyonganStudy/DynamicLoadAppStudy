package com.landy.dynamicload.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by landy on 17/6/16.
 * go go go
 */

public class DynamicProxyShopping implements InvocationHandler {

    private Object mBase;

    public DynamicProxyShopping(Object base) {
        mBase = base;
    }

    public Shopping bind() {
        return (Shopping) Proxy.newProxyInstance(mBase.getClass().getClassLoader(), mBase.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("doShop")) {
            Long money = (Long) args[0];
            long readCost = (long) (money * 0.5);
            MLog.log(String.format("偷偷拿了%s块钱", readCost));

            String[] goods = (String[]) method.invoke(mBase, readCost);
            goods[0] = "破鞋子";
            return goods;
        } else if (method.getName().equals("buySomeFood")) {
            Long money = (Long) args[0];
            long readCost = (long) (money * 0.5);
            MLog.log(String.format("偷偷拿了%s块钱", readCost));
            return method.invoke(mBase, readCost);
        }
        return null;
    }
}
