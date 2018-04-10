package com.baishan.nearshopclient.base;

import com.baishan.mylibrary.LibFragment;
import com.baishan.nearshopclient.ui.view.TitleBuilder;

/**
 * Created by RayYeung on 2016/8/9.
 */
public abstract class BaseFragment extends LibFragment {

    public TitleBuilder initTitle(Object obj) {
        if (obj instanceof String) {
            return new TitleBuilder(mContext).setTitleText((String) obj);
        } else {
            return new TitleBuilder(mContext).setTitleText((int) obj);
        }
    }


//    public boolean checkLogin() {
//        UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
//        if (userInfo == null)
//            intent2Activity(LoginActivity.class);
//        return userInfo != null;
//    }




}
