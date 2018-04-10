package com.baishan.nearshop.model;

import java.io.Serializable;

/**
 * Created by RayYeung on 2016/9/20.
 */
public class Address implements Serializable {

    /**
     * AddressId : 1
     * Contact : LLLL
     * Phone : 13000000000
     * Address : SOMEWHERE
     */

    public Area area;
    public boolean isDefault;

    public int AddressId;
    public String Contact;
    public String Phone;
    public String Address;

    public String concatInfo() {

        return Contact + " " + Phone;
    }

    @Override
    public String toString() {
        return "Address{" +
                "isDefault=" + isDefault +
                ", AddressId=" + AddressId +
                ", Contact='" + Contact + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Address='" + Address + '\'' +
                '}';
    }
}
