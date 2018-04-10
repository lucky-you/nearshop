package com.baishan.mylibrary.utils;

import android.widget.Toast;

import com.baishan.mylibrary.LibApplication;

public class ToastUtils {

    private static Toast mToast;

    /**
     * 显示Toast
     */
    public static void showToast( CharSequence text) {
        if (mToast == null) {
            mToast = Toast.makeText(LibApplication.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }


}
