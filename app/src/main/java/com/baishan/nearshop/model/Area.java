package com.baishan.nearshop.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by RayYeung on 2016/9/21.
 */
public class Area implements Serializable {

    /**
     * AreaId : 1
     * Province : 湖北省
     * City : 武汉市
     * County : 洪山区
     * AreaName : 洪福添美
     * AddressInfo : [{"AddressId":1,"Contact":"LLLL","Phone":"13000000000","Address":"SOMEWHERE"},{"AddressId":2,"Contact":"LLLLee","Phone":"13000000000","Address":"SOMEWHEREeeeeeee"},{"AddressId":3,"Contact":"LLLLee","Phone":"13000000000","Address":"SOMEWHEREeeeeeee"}]
     */

    //商区
    public int AreaId;
    public String AreaName;

    public String AdCode;

    public String Province;
    public String City;
    public String CityCode;
    public String County;
    public List<Address> AddressInfo;
    public Address defaultAddress;

    public String ChatroomId;

    public Area() {
    }

    public String concatAddress() {
        return Province + City + County + AreaName;
    }

    public String concatPartAddress() {
        return City + County + AreaName;
    }

    public String concatCC() {
        return City + County;
    }


}
