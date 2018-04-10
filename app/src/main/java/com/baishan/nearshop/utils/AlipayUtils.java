package com.baishan.nearshop.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.baishan.nearshop.base.BaseApplication;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by RayYeung on 2016/8/24.
 */
public class AlipayUtils {


    public static void pay(final Activity activity, final String orderInfo) {

        Observable.create(new Observable.OnSubscribe<Map<String, String>>() {
            @Override
            public void call(Subscriber<? super Map<String, String>> subscriber) {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                subscriber.onNext(result);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Map<String, String>>() {
                    @Override
                    public void call(Map<String, String> result) {
                        String resultStatus = result.get("resultStatus");

                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        if (TextUtils.equals(resultStatus, "9000")) {
                            Toast.makeText(BaseApplication.getInstance(), "支付成功",
                                    Toast.LENGTH_SHORT).show();
                            // ------------------支付成功。改变订单状态----------------------
                            EventBus.getDefault().post(8888);
                        } else {
                            // 判断resultStatus 为非“9000”则代表可能支付失败
                            // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (TextUtils.equals(resultStatus, "8000")) {
                                Toast.makeText(BaseApplication.getInstance(), "支付结果确认中",
                                        Toast.LENGTH_SHORT).show();

                            } else if (TextUtils.equals(resultStatus, "6001")) {
                                Toast.makeText(BaseApplication.getInstance(), "支付取消",
                                        Toast.LENGTH_SHORT).show();

                            } else {
                                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                Toast.makeText(BaseApplication.getInstance(), "支付失败"+resultStatus,
                                        Toast.LENGTH_SHORT).show();

                            }
                        }


                    }
                });
    }

    public static boolean isExist(Context context) {
        PackageManager manager = context.getPackageManager();
        List<PackageInfo> pkgList = manager.getInstalledPackages(0);
        for (int i = 0; i < pkgList.size(); i++) {
            PackageInfo pI = pkgList.get(i);
            //com.alipay.android.app 支付宝快捷服务
            if (pI.packageName.equalsIgnoreCase("com.eg.android.AlipayGphone"))
                return true;
        }
        return false;
    }

}
