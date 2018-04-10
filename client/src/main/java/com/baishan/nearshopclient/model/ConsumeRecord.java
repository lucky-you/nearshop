package com.baishan.nearshopclient.model;

/**
 * 作者：ZhouBin  2017/3/17 14:02
 * 邮箱：1021237228@qq.com
 * 作用：金额明细的实体类
 */

public class ConsumeRecord {


    /**
     * State : 1
     * Moneys : 20.0
     * Contents : 商品订单-无货退款
     * PayType : null
     * Balance : 9.99999452769E9
     * CreateTime : 2017-01-10 18:10:49
     */
    // 1 收入 2 支出
    public int State;
    public String Moneys;
    public String Contents;
    public String PayType;
    public String Balance;
    public String CreateTime;
    public int PageCount;
    public int TotalCounts;
    public String OrderNo;

}
