package com.baishan.nearshopclient.model;

/**
 * Created by Administrator on 2017/1/10 0010.
 */

public class OrderCount {

    /**
     * ServiceQD : 1
     * ServiceDCL : 0
     * ServiceYJD : 1
     * ServiceGZZ : 64
     * ServiceYWC : 19
     * ShopQD : 3
     * ShopDCL : 0
     * ShopJXZ : 21
     * ShopYWC : 27
     * ShopYQX : 0
     */

    public int ServiceQD;
    public int ServiceDCL;
    public int ServiceYJD;
    public int ServiceGZZ;
    public int ServiceYWC;
    public int ShopQD;
    public int ShopDCL;
    public int ShopJXZ;
    public int ShopYWC;
    public int ShopYQX;

    /// 服务订单超时次数
    public  int ServiceOverOrderCount;

    /// 服务订单严重超时次数
    public  int ServiceOver2OrderCount ;

    /// 服务订单忽略次数
    public  int ServiceIgnoreOrderCount;

    /// 商品订单超时次数
    public  int ShopOverOrderCount;

    /// 商品订单严重超时次数
    public  int ShopOver2OrderCount;

    /// 商品订单忽略次数
    public  int ShopIgnoreOrderCount;

}
