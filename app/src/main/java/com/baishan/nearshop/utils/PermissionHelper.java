package com.baishan.nearshop.utils;

import android.content.Context;

import com.baishan.mylibrary.utils.CommonUtil;
import com.baishan.permissionlibrary.PermissionUtils;

import static android.Manifest.permission.CALL_PHONE;

/**
 * Created by RayYeung on 2016/12/9.
 */

public class PermissionHelper {

    public static void call(Context context, String phone) {
        PermissionUtils.getInstance().checkSinglePermission(context, CALL_PHONE, new PermissionUtils.PerPermissionCallback() {
            @Override
            public void onDeny() {

            }

            @Override
            public void onGuarantee() {
                CommonUtil.callPhone(context, phone);
            }
        });
    }
}
