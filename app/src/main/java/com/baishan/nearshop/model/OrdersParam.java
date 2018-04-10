package com.baishan.nearshop.model;

import java.io.Serializable;

/**
 * Created by RayYeung on 2016/12/5.
 */

public class OrdersParam implements Serializable{
    public int UserId;
    public int AreaProductId;
    public int AddressId;
    public int Num;
    public String Remark;
    public String Spec;
}
