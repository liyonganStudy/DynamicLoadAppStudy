package com.landy.dynamicload.proxy;

/**
 * Created by landy on 17/6/16.
 * go go go
 */

public class ProxyShopping implements Shopping {
    private Shopping mBase;

    public ProxyShopping(Shopping base) {
        mBase = base;
    }

    @Override
   public String[] doShop(long money) {
        money = money / 2;
        String[] goods = mBase.doShop(money);
        goods[0] = "破鞋子";
        return goods;
    }

    @Override
    public void buySomeFood(long money) {
        mBase.buySomeFood(money);
    }
}
