package com.baishan.nearshopclient.view;

import com.baishan.nearshopclient.model.OrderCount;
import com.baishan.nearshopclient.model.UserInfo;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/25.
 */
public interface IMainView {
    void changeWorkState(int state, int type);

    void getOrderCountSuccess(OrderCount response);

    void onLoginSuccess(List<UserInfo> response, String phone, String pwd);
}
