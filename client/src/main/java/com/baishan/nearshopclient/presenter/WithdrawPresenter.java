package com.baishan.nearshopclient.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshopclient.base.AppClient;
import com.baishan.nearshopclient.base.SubscriberCallBack;
import com.baishan.nearshopclient.model.UserInfo;
import com.baishan.nearshopclient.view.IWithdrawView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by RayYeung on 2016/9/25.
 */
public class WithdrawPresenter extends BasePresenter<IWithdrawView> {

    public WithdrawPresenter(IWithdrawView mvpView) {
        super(mvpView);
    }

    public void submitWithdrawals(UserInfo user, int money, String account, String accountName, String bankName, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("Method", "Withdrawals");
        params.put("UserId", user.IdentityFlag == 1 ? user.Id + "" : user.UserId + "");
        params.put("Price", money + "");
        params.put("AccountNo", account);
        params.put("AccountName", accountName);
        params.put("BankName", bankName);
        params.put("Password", password);
        params.put("IdentityFlag", user.IdentityFlag + "");


        addSubscription(AppClient.getApiService().withdrawals(params), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.submitWithdrawalsSuccess(response);
            }
        });
    }
}
