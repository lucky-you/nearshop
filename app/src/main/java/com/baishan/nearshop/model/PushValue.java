package com.baishan.nearshop.model;

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

    private int OrderState;
    private String OrderNo;
    private  int LinkType;
    private String LinkValue;
    @Generated(hash = 352044530)
    public PushValue(Long id, int OrderState, String OrderNo, int LinkType,
            String LinkValue) {
        this.id = id;
        this.OrderState = OrderState;
        this.OrderNo = OrderNo;
        this.LinkType = LinkType;
        this.LinkValue = LinkValue;
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
    public int getLinkType() {
        return this.LinkType;
    }
    public void setLinkType(int LinkType) {
        this.LinkType = LinkType;
    }
    public String getLinkValue() {
        return this.LinkValue;
    }
    public void setLinkValue(String LinkValue) {
        this.LinkValue = LinkValue;
    }


}
