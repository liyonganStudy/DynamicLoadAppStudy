package com.landy.dynamicload.proxy;

/**
 * Created by landy on 17/6/16.
 * go go go
 */

public class ShoppingImp implements Shopping {

    @Override
    public String[] doShop(long money) {
        MLog.log("逛淘宝 ,逛商场,买买买!!");
        MLog.log(String.format("花了%s块钱", money));
        return new String[] { "鞋子", "衣服", "零食" };
    }

    @Override
    public void buySomeFood(long money) {
        MLog.log(String.format("use %s块钱买了点吃的", money));
    }

}
