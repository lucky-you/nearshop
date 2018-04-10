package com.baishan.nearshop.utils;

import android.content.Context;

import com.baishan.nearshop.R;

/**
 * Created by RayYeung on 2016/12/5.
 */

public class StrUtils {


    public static String formatPrice(Context context, double price) {
        return String.format(context.getString(R.string.price), price);
    }

    public static String formatPrice1(Context context, double price) {
        return String.format(context.getString(R.string.price1), price);
    }

    public static String formatCoin(Context context, double price) {
        return String.format(context.getString(R.string.app_coins), (int)price);
    }
}
