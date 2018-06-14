package com.baishan.nearshop.base;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.baishan.mylibrary.utils.ToastUtils;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * 处理服务器获取的数据
 *
 * @param <T>
 */
public abstract class SubscriberCallBack<T> extends Subscriber<ResultResponse<T>> {

    private static Handler mDelivery;

    public SubscriberCallBack() {
        if (mDelivery == null)
            mDelivery = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(final Throwable e) {
        e.printStackTrace();
        mDelivery.post(() -> {
            if (e instanceof SocketTimeoutException) {
                ToastUtils.showToast("网络连接超时");
            } else if (e instanceof SocketException) {
                if (e instanceof ConnectException) {
                    ToastUtils.showToast("网络未连接");
                } else {
                    ToastUtils.showToast("网络错误");
                }
            }
            onError();
        });
    }

    @Override
    public void onNext(ResultResponse response) {
        if (response.Code == 200) {
            onSuccess((T) response.Result);
        } else {
            if (response.Code == 400 && !TextUtils.isEmpty(response.Message)) {
                ToastUtils.showToast(response.Message);
            }
            onFailure(response);
        }
    }

    protected abstract void onSuccess(T response);

    protected void onError() {
    }

    protected void onFailure(ResultResponse response) {
    }

}
