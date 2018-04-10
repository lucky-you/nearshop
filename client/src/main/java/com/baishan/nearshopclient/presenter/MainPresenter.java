package com.baishan.nearshopclient.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshopclient.base.AppClient;
import com.baishan.nearshopclient.base.SubscriberCallBack;
import com.baishan.nearshopclient.dao.MessageDao;
import com.baishan.nearshopclient.model.OrderCount;
import com.baishan.nearshopclient.model.UserInfo;
import com.baishan.nearshopclient.view.IMainView;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/25.
 */
public class MainPresenter extends BasePresenter<IMainView> {

    public MainPresenter(IMainView mvpView) {
        super(mvpView);
    }

    //改变工作状态
    public void changeWorkState(int userId, int state, int type) {
        addSubscription(AppClient.getApiService().changeWorkState("ChangeWorkState", userId + "", state + ""), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.changeWorkState(state, type);
            }
        });
    }

    public int getMessageCount(String userId) {
        return MessageDao.getUnReadMsgCount(userId);
    }

    public void getOrderCount(String courierId, String areaId) {
        addSubscription(AppClient.getApiService().getOrderCountInfo(courierId, areaId), new SubscriberCallBack<OrderCount>() {
            @Override
            protected void onSuccess(OrderCount response) {
                mvpView.getOrderCountSuccess(response);
            }
        });
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo(String method, String phone, String password) {

        addSubscription(AppClient.getApiService().login(method, phone, password), new SubscriberCallBack<List<UserInfo>>() {
            @Override
            protected void onSuccess(List<UserInfo> response) {
                mvpView.onLoginSuccess(response, phone, password);
            }
        });

    }


}
