package com.baishan.nearshop.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class AddressItem extends MultiItemEntity{
    public static final int BUSINESS = 1;
    public static final int ADDRESS = 2;
    public static final int DECORATION = 3;

    public Address address;
    public Area area;

    public AddressItem(Address address) {
        this.address = address;
        this.itemType = ADDRESS;
    }

    public AddressItem(Area area) {
        this.area = area;
        this.itemType = BUSINESS;
    }
    public AddressItem() {
        this.itemType = DECORATION;
    }


}
