package com.baishan.nearshopclient.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/12/1 0001.
 */

public class ShopOrder extends MultiItemEntity {
    public static final int AREA = 1;
    public static final int GOODS = 2;
    public static final int INFO = 3;
    /**
     * Id : 54
     * OrderNo : 1480493494191
     * AreaName : 洪福添美
     * OrderPrice : 18.6
     * OrderState : 0
     * StateName : 待支付
     * CourierName : null
     * CourierPhone : null
     * PageCount : 1
     * TotalCounts : 7
     * ProductItem : [{"Title":"当日现摘 2016新果新鲜青橄榄 青果 孕妇宝宝水果 福建闽侯包邮","Price":15.6,"ImageUrl":"http://111.47.198.193:8033/Upload/Photos/20161128/ae989e35889b208704788a6db5ece50f.jpg|http://111.47.198.193:8033/Upload/Photos/20161128/85cfe04b980211018046540f79994b31.jpg","Num":1}]
     */

    public int Id;
    public String OrderNo;
    public String AreaName;
    public double OrderPrice;
    public double SubBalance;
    public int OrderState;
    public String StateName;
    public String CourierName;
    public String CourierPhone;
    public String CreateTime;
    public int PageCount;
    public int TotalCounts;
    public double CourierPrice;
    public int IsRefund;
    public int Num;


    public ShopOrder(ShopOrder order) {
        Id = order.Id;
        OrderNo = order.OrderNo;
        AreaName = order.AreaName;
        OrderPrice = order.OrderPrice;
        SubBalance = order.SubBalance;
        OrderState = order.OrderState;
        StateName = order.StateName;
        CourierName = order.CourierName;
        CourierPhone = order.CourierPhone;
        CreateTime = order.CreateTime;
        PageCount = order.PageCount;
        TotalCounts = order.TotalCounts;
        CourierPrice = order.CourierPrice;
        IsRefund = order.IsRefund;
        Num = order.ProductItem.size();
    }

    public ProductItemBean GoodItem;


    public ShopOrder(ProductItemBean goods) {
        this.GoodItem = goods;
        this.itemType = GOODS;
    }

    /**
     * Title : 当日现摘 2016新果新鲜青橄榄 青果 孕妇宝宝水果 福建闽侯包邮
     * Price : 15.6
     * ImageUrl : http://111.47.198.193:8033/Upload/Photos/20161128/ae989e35889b208704788a6db5ece50f.jpg|http://111.47.198.193:8033/Upload/Photos/20161128/85cfe04b980211018046540f79994b31.jpg
     * Num : 1
     */

    public List<ProductItemBean> ProductItem;

    public static class ProductItemBean {
        public String Title;
        public double Price;
        public String ImageUrl;
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
}
