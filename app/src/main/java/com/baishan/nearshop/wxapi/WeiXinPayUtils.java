package com.baishan.nearshop.wxapi;

import android.content.Context;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WeiXinPayUtils {

    public static final String APP_ID = "wx3917faea8d5aa0e3";

    public static void pay(Context context, PayReq req) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, APP_ID);
        api.registerApp(APP_ID);
        api.sendReq(req);
    }

    public static boolean isExist(Context context) {
        IWXAPI wxapi = WXAPIFactory.createWXAPI(context, null);
        return wxapi.isWXAppInstalled();
    }

}
