package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.view.IFeedBackView;

/**
 * Created by Administrator on 2016/10/13 0013.
 */
public class FeedBackPresenter extends BasePresenter<IFeedBackView> {
    public FeedBackPresenter(IFeedBackView mvpView) {
        super(mvpView);
    }

    public void submitFeedback(int userId, String txt) {
        addSubscription(AppClient.getApiService().submitFeedback("SubmitFeedback", userId + "", txt), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.onSuccess();
            }

        });
    }
}
