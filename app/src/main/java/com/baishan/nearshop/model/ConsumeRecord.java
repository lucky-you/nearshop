package com.baishan.nearshop.model;

/**
 * Created by RayYeung on 2017/1/13.
 */

public class ConsumeRecord {
    /**
     * "State": 2,
     * "Moneys": 130.00,
     * "OrderNo": "1490325641994",
     * "Contents": "商品订单-购买服务",
     * "PayType": "钱包余额",
     * "Balance": 81880.40,
     * "CreateTime": "2017-03-24 11:22:27",
     * "RowIndex": 1,
     * "PageCount": 12,
     * "TotalCounts": 112
     */
    // 1 收入 2 支出
    public int State;
    public double Moneys;
    public String OrderNo;
    public String Contents;
    public String PayType;
    public double Balance;
    public String CreateTime;
    public int RowIndex;
    public int PageCount;
    public int TotalCounts;


}
