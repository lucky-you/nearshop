package com.baishan.nearshopclient.utils;

import android.content.Context;

import com.baishan.nearshopclient.R;


/**
 * Created by RayYeung on 2016/12/5.
 */

public class StrUtils {


    public static String formatPrice(Context context, double price) {
        return String.format(context.getString(R.string.price), price);
    }

    public static String formatCoin(Context context, double price) {
        return String.format(context.getString(R.string.app_coins), (int)price);
    }
}
