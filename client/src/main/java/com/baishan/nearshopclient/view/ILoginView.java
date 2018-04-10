package com.baishan.nearshopclient.view;

import com.baishan.nearshopclient.model.UserInfo;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/25.
 */
public interface ILoginView {


    /**
     * 登录成功
     *
     * @param response
     * @param phone
     * @param pwd
     */
    void onLoginSuccess(List<UserInfo> response, String phone, String pwd);
}
