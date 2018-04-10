package com.baishan.nearshop.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by RayYeung on 2016/9/20.
 */
public class ShopcarItem extends MultiItemEntity {

    public static final int ADDRESS = 1;
    public static final int GOODS = 2;

    public Shopcar address;
    public CartGoods goods;
    public boolean isChecked;

    public ShopcarItem(Shopcar address) {
        this.address = address;
        this.itemType = ADDRESS;
    }

    public ShopcarItem(CartGoods goods) {
        this.goods = goods;
        this.itemType = GOODS;
    }

    public ShopcarItem() {
    }
}
