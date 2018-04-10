package com.baishan.nearshop.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/18 0018.
 */
public class ConfirmOrderItem extends MultiItemEntity {
    public static final int ADDRESS = 1;
    public static final int GOODS = 2;
    public static final int INFO = 3;
    /**
     * AreaId : 1
     * Province : 湖北省
     * City : 武汉市
     * County : 洪山区
     * AreaName : 洪福添美
     * AddressInfo : [{"AddressId":7,"Contact":"空","Phone":"15717162985","Address":"监控默默"}]
     * ProductItem : [{"CartToken":"1a435c4d-7a49-47a4-8b63-63f4a99a7650","AreaId":1,"AreaProductId":1,"ProductId":1,"Title":"当日现摘 2016新果新鲜青橄榄 青果 孕妇宝宝水果 福建闽侯包邮","Price":15.6,"ImageUrl":"http://111.47.198.193:8033/Upload/Photos/20161128/ae989e35889b208704788a6db5ece50f.jpg|http://111.47.198.193:8033/Upload/Photos/20161128/85cfe04b980211018046540f79994b31.jpg","Num":1},{"CartToken":"23fab9d3-f6ec-4588-b3fe-f9d91cf9ef17","AreaId":1,"AreaProductId":2,"ProductId":2,"Title":"今年新品 话梅干","Price":68,"ImageUrl":"http://111.47.198.193:8033/Upload/Photos/20161128/f63dd7f2d4a0d3a1ec9e3399dae5608c.jpg|http://111.47.198.193:8033/Upload/Photos/20161128/fb872c0ca62e61ca0baec2fc9af577ab.jpg|http://111.47.198.193:8033/Upload/Photos/20161128/thumb_aac6695fedcfb4402b98592ff583140d.jpg","Num":19}]
     * PayType : 支付宝支付
     * Remarks :
     * HasCoupon : 0
     * CourierPrice : 0.0
     * CouponId : -1
     * CouponPrice : 0.0
     * OrderPrice : 1307.6
     */

    public int AreaId;
    public String Province;
    public String City;
    public String County;
    public String AreaName;
    public String PayType;
    public String Remarks;
    public int HasCoupon;
    public double CourierPrice;
    public int CouponId;
    public double CouponPrice;
    public double RefundPrice;
    public String FreePostage ;
    public String OldCourierPrice ;
    //其他支付
    public double OrderPrice;
    public int ListIndex;

    /****************订单详情添加字段******************/
    public String StateName;
    public String CreateTime;
    public String OrderNo;
    public String CourierName;
    public String CourierPhone;
    //余额支付
    public double SubBalance;
    public int OrderState;
    public int IsRefund;

    /****************订单详情添加字段******************/

    public ConfirmOrderItem(ConfirmOrderItem order) {
        AreaId = order.AreaId;
        Province = order.Province;
        City = order.City;
        County = order.County;
        AreaName = order.AreaName;
        PayType = order.PayType;
        Remarks = order.Remarks;
        HasCoupon = order.HasCoupon;
        CourierPrice = order.CourierPrice;
        CouponId = order.CouponId;
        CouponPrice = order.CouponPrice;
        OrderPrice = order.OrderPrice;
        AddressInfo=order.AddressInfo;
        OrderState = order.OrderState;
        SubBalance = order.SubBalance;
        FreePostage = order.FreePostage;
        OldCourierPrice = order.OldCourierPrice;
    }



    public String concatAddress() {
        return Province + City + County + AreaName;
    }

    /**
     * AddressId : 7
     * Contact : 空
     * Phone : 15717162985
     * Address : 监控默默
     */

    public List<Address> AddressInfo;
    /**
     * CartToken : 1a435c4d-7a49-47a4-8b63-63f4a99a7650
     * AreaId : 1
     * AreaProductId : 1
     * ProductId : 1
     * Title : 当日现摘 2016新果新鲜青橄榄 青果 孕妇宝宝水果 福建闽侯包邮
     * Price : 15.6
     * ImageUrl : http://111.47.198.193:8033/Upload/Photos/20161128/ae989e35889b208704788a6db5ece50f.jpg|http://111.47.198.193:8033/Upload/Photos/20161128/85cfe04b980211018046540f79994b31.jpg
     * Num : 1
     */

    public List<ProductItemBean> ProductItem;
    public ProductItemBean GoodItem;


    public ConfirmOrderItem(ProductItemBean goods) {
        this.GoodItem = goods;
        this.itemType = GOODS;
    }


    public static class ProductItemBean {
        public String CartToken;
        public int AreaId;
        public int AreaProductId;
        public int ProductId;
        public String Title;
        public double Price;
        public double RefundPrice;
        public String ImageUrl;
        public String StoreName;
        public String Spec;
        public int Num;
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

    public double refundPrice(){
        double total = 0;
        for (ProductItemBean bean : ProductItem) {
            total+=bean.RefundPrice;
        }
        return total;
    }
}
