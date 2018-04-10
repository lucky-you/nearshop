package com.baishan.nearshop.model;


/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class UserInfo {

    /**
     * UserId : 2
     * LoginToken : 683df623-1da7-4861-8ac5-949efb7e0096
     * UserPhoto :
     * NickName : 157****2674
     * UserSex : 1
     * UserAge : 0
     * Balance : 0
     * SCoins : 0
     * IsActive : 1
     */
    public int UserId;
    public String LoginToken;
    public String UserToken;
    public String UserPhoto;
    public String UserPwd;
    public String NickName;
    public int UserSex;
    public int UserAge;
    public String Balance;
    public String SCoins;
    public int IsActive;
    //0 普通用户
    public int IdentityFlag;

    @Override
    public String toString() {
        return "UserInfo{" +
                "UserId=" + UserId +
                ", LoginToken='" + LoginToken + '\'' +
                ", UserPhoto='" + UserPhoto + '\'' +
                ", NickName='" + NickName + '\'' +
                ", UserSex=" + UserSex +
                ", UserAge=" + UserAge +
                ", Balance=" + Balance +
                ", SCoins=" + SCoins +
                ", IsActive=" + IsActive +
                '}';
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof UserInfo)) return false;
        return UserId == ((UserInfo) obj).UserId;
    }
}
