package com.baishan.mylibrary.utils;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class DistanceUtils {
    public static String getFormatDistance(double dis)
    {
        int distance= (int) dis;
        int km = distance / 1000;
        if(km>0)
        {
            //km
            return km+"km";
        }else {
            return distance+"m";
        }
    }

}
