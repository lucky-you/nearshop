package com.baishan.nearshop.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class Orders implements Serializable {
    public int PageCount;

    /**
     * Id : 1
     * OrderNo : 1479518512927
     * AreaName : 光谷广场
     * Contact : HDHDD
     * Phone : 13000000000
     * Address : Default Address
     * Remarks : 我要预约
     * OrderPrice : 0.0
     * OrderState : 0
     * CourierName : null
     * CourierPhone : null
     * ImageUrl : http://111.47.198.193:8033/Upload/Photos/20161117/81f3a6a8877a8a63781744512a7522da.png|http://111.47.198.193:8033/Upload/Photos/20161117/3d1f93f8ab8cc68bf55af27fe5d23a0e.png
     * Title : 搬家公司快速搬家
     * Price : 58.8起
     * Name : 新小鱼
     * ServicePhone : 15878856666
     * HasLicense : 1
     * HasIdentity : 1
     * CreateTime : 2016-11-19 09:21:53
     * RowIndex : 1
     * TotalCounts : 17
     */

    public int Id;
    public String OrderNo;
    public String Province;
    public String City;
    public String County;
    public String AreaName;
    public String Contact;
    public String Phone;
    public String Address;
    public String Remarks;
    public String PayType;
    public String OrderPrice;
    public int OrderState;
    public String StateName;
    public String StateMessage;
    public String CourierName;
    public int CourierId;
    public String CourierPhone;
    public String CourierRemarks;
    public String ImageUrl;
    public String Title;
    public String Price;
    public String Name;
    public String ServicePhone;
    public int HasLicense;
    public int HasIdentity;
    public String CreateTime;
    //是否可以申请退款  0 不可以 1 可以
    public int IsRefund;
    public int RowIndex;
    public int TotalCounts;

    public String EndTime;


    public String[] getImages() {
        return ImageUrl.split("\\|");
    }

    public String getImage() {
        if (ImageUrl == null) {
            return null;
        }
        String[] urls = ImageUrl.split("\\|");
        if (urls.length > 0) {
            return urls[0];
        }
        return null;
    }

    public String concatAddress() {
        return Province + City + County + AreaName;
    }
}
