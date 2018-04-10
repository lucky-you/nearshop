package com.baishan.nearshopclient.model;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class UserInfo {
    //   1 管理员 2商家 3服务商   4派送员

    public static final int ADMIN = 1;
    public static final int BUSINESS = 2;
    public static final int SERVICE_PROVIDER = 3;
    public static final int SENDER = 4;
    /**
     * Id : 2
     * AreaId : 15
     * AreaName : 光谷广场
     * UserId : 2
     * Phone : 13000000000
     * Contact : Susu
     * IsActive : 0
     * UserPhoto : http://111.47.198.193:8033/Upload/UserPhoto/1479377166083.jpg
     * Balance : 0.0
     * Surplus : 0.0
     * IdentityFlag : 4
     */

    public int Id;
    public int AreaId;
    public String AreaName;
    public int UserId;
    public String Phone;
    public String Contact;
    public int IsActive;
    public String UserPhoto;
    public double Balance;
    public double Surplus;
    //    1 管理员 2商家 3服务商   4派送员
    public int IdentityFlag;
    //本地数据
    public int keyId;


    public int getIdentity() {
        return IdentityFlag;
    }

    public void setIdentity(int identity) {
        this.IdentityFlag = identity;
    }


}
