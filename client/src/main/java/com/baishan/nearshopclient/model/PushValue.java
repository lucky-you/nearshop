package com.baishan.nearshopclient.model;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by RayYeung on 2016/11/2.
 */
@Entity
public class PushValue {

    @Id(autoincrement = true)
    private Long id;
    @SerializedName("Id")
    private String UserId;
    private int OrderState;
    private String OrderNo;
    @Generated(hash = 2054726970)
    public PushValue(Long id, String UserId, int OrderState, String OrderNo) {
        this.id = id;
        this.UserId = UserId;
        this.OrderState = OrderState;
        this.OrderNo = OrderNo;
    }
    @Generated(hash = 2009548369)
    public PushValue() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserId() {
        return this.UserId;
    }
    public void setUserId(String UserId) {
        this.UserId = UserId;
    }
    public int getOrderState() {
        return this.OrderState;
    }
    public void setOrderState(int OrderState) {
        this.OrderState = OrderState;
    }
    public String getOrderNo() {
        return this.OrderNo;
    }
    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }




}
