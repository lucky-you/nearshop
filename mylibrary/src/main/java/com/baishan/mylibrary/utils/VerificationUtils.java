package com.baishan.mylibrary.utils;

import android.text.TextUtils;

/**
 * Created by Administrator on 2016/8/18 0018.
 */
public class VerificationUtils {
    /**
     * 手机号验证
     *
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        if (phone.length() != 11 || !phone
                .matches("^[1][3,4,5,7,8]\\d{9}$")) {
            return false;
        }
        return true;
    }
}
