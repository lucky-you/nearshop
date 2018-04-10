package com.baishan.nearshopclient.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshopclient.base.AppClient;
import com.baishan.nearshopclient.base.SubscriberCallBack;
import com.baishan.nearshopclient.model.UserInfo;
import com.baishan.nearshopclient.model.WithdrawLog;
import com.baishan.nearshopclient.view.IWithdrawLogView;

import java.util.List;

/**
 * Created by Administrator on 2016/12/9 0009.
 */
public class WithdrawLogPresenter extends BasePresenter<IWithdrawLogView>{
    public WithdrawLogPresenter(IWithdrawLogView mvpView) {
        super(mvpView);
    }

    public void getWithdrawLog(UserInfo user) {
        addSubscription(AppClient.getApiService().getWithdrawLog(user.IdentityFlag == 1 ? user.Id + "" : user.UserId + "",user.IdentityFlag+""), new SubscriberCallBack<List<WithdrawLog>>() {
            @Override
            protected void onSuccess(List<WithdrawLog> response) {
                mvpView.getWithdrawLogSuccess(response);
            }
        });
    }
}
