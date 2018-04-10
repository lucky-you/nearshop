package com.baishan.nearshopclient.model;

import java.util.List;

/**
 * Created by Administrator on 2016/12/2 0002.
 */

public class SenderOrdersDetail {

    /**
     * OrderNo : 1480647763173
     * AreaId : 1
     * Province : 湖北省
     * City : 武汉市
     * County : 洪山区
     * AreaName : 洪福添美
     * AddressInfo : [{"AddressId":0,"Contact":"LK","Phone":"13333333333","Address":"Second Address"}]
     * ProductItem : [{"CartToken":"219e8bde-3091-40f4-b404-516fad92ebd0","AreaId":1,"AreaProductId":1,"ProductId":0,"Title":"当日现摘 2016新果新鲜青橄榄 青果 孕妇宝宝水果 福建闽侯包邮","Price":15.6,"ImageUrl":"http://111.47.198.193:8033/Upload/Photos/20161128/ae989e35889b208704788a6db5ece50f.jpg|http://111.47.198.193:8033/Upload/Photos/20161128/85cfe04b980211018046540f79994b31.jpg","Num":1,"StoreList":null}]
     * PayType : 支付宝
     * Remarks :
     * HasCoupon : 0
     * CourierPrice : 3.0
     * CourierName : 我想问一下
     * CourierPhone : 15718596569
     * CouponId : -1
     * CouponPrice : 0.0
     * OrderPrice : 0.0
     * SubBalance : 18.6
     * CreateTime : 2016-12-02 11:02:43
     * OrderState : 30
     * StateName : 已接单
     */

    public String OrderNo;
    public int AreaId;
    public String Province;
    public String City;
    public String County;
    public String AreaName;
    public String PayType;
    public String Remarks;
    public int HasCoupon;
    public double CourierPrice;
    public String CourierName;
    public String CourierPhone;
    public int CouponId;
    public double CouponPrice;
    public double OrderPrice;
    public double SubBalance;
    public String CreateTime;
    public int OrderState;
    public String StateName;
    /**
     * AddressId : 0
     * Contact : LK
     * Phone : 13333333333
     * Address : Second Address
     */

    public List<Address> AddressInfo;
    /**
     * CartToken : 219e8bde-3091-40f4-b404-516fad92ebd0
     * AreaId : 1
     * AreaProductId : 1
     * ProductId : 0
     * Title : 当日现摘 2016新果新鲜青橄榄 青果 孕妇宝宝水果 福建闽侯包邮
     * Price : 15.6
     * ImageUrl : http://111.47.198.193:8033/Upload/Photos/20161128/ae989e35889b208704788a6db5ece50f.jpg|http://111.47.198.193:8033/Upload/Photos/20161128/85cfe04b980211018046540f79994b31.jpg
     * Num : 1
     * StoreList : null
     */

    public List<ProductItemBean> ProductItem;


    public static class ProductItemBean {
        public String CartToken;
        public int AreaId;
        public int AreaProductId;
        public int ProductId;
        public String Title;
        public double Price;
        public String ImageUrl;
        public String Spec;
        public int Num;
        public List<StoreListBean> StoreList;
        public String StoreName;
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
    }
    public String concatAddress() {
        return Province + City + County + AreaName;
    }
}
