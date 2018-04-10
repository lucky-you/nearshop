package com.baishan.mylibrary.mvp;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import com.baishan.mylibrary.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by WuXiaolong on 2016/3/30.
 */
public class BasePresenter<V> implements Presenter<V> {
    public V mvpView;
    private CompositeSubscription mCompositeSubscription;
    protected Activity context;
    private Dialog dialog;

    @Override
    public void attachView(V mvpView) {
        this.mvpView = mvpView;
        if (mvpView instanceof Activity) {
            this.context = (Activity) mvpView;
        } else if (mvpView instanceof Fragment) {
            this.context = ((Fragment) mvpView).getActivity();
        }
    }

    public BasePresenter(V mvpView) {
        attachView(mvpView);
    }


    @Override
    public void detachView() {
        this.mvpView = null;
        onUnsubscribe();
    }


    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void post(Object o) {
        EventBus.getDefault().post(o);
    }


    public void addSubscription(Observable observable, Subscriber subscriber) {
        addSubscription(false, observable, subscriber);
    }
    public void addSubscription(boolean isShowDialog, Observable observable, Subscriber subscriber)
    {
        addSubscription(null, isShowDialog, observable, subscriber);
    }


    public void addSubscription(SwipeRefreshLayout srl, Observable observable, Subscriber subscriber) {
        addSubscription(srl, false, observable, subscriber);
    }

    public void addSubscription(final SwipeRefreshLayout srl, boolean isShowDialog, Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        if (isShowDialog) {
            dialog = LoadingDialog.show(context);
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (dialog != null && dialog.isShowing())
                            dialog.dismiss();
                        if (srl != null && srl.isRefreshing())
                            srl.setRefreshing(false);
                    }
                })
                .doOnNext(new Action1() {
                    @Override
                    public void call(Object o) {
                        if (dialog != null && dialog.isShowing())
                            dialog.dismiss();
                        if (srl != null && srl.isRefreshing())
                            srl.setRefreshing(false);
                    }
                })
                .subscribe(subscriber));
    }
}
