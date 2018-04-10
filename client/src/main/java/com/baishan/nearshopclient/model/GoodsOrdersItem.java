package com.baishan.nearshopclient.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by RayYeung on 2016/9/25.
 */
public class GoodsOrdersItem extends MultiItemEntity {

    public static final int TOP = 1;
    public static final int GOODS = 2;
    public static final int BOTTOM =3;

    private GoodsOrdersTop top;
    private Goods goods;
    private GoodsOrdersBottom bottom;

    public GoodsOrdersItem(GoodsOrdersTop top) {
        this.top = top;
        setItemType(TOP);
    }

    public GoodsOrdersItem(Goods goods) {
        this.goods = goods;
        setItemType(GOODS);
    }

    public GoodsOrdersItem(GoodsOrdersBottom bottom) {
        this.bottom = bottom;
        setItemType(BOTTOM);
    }
}
