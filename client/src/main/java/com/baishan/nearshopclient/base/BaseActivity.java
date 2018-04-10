package com.baishan.nearshopclient.base;


import android.os.Bundle;

import com.baishan.mylibrary.LibActivity;
import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.model.UserInfo;
import com.baishan.nearshopclient.ui.view.TitleBuilder;

/**
 * Created by RayYeung on 2016/8/9.
 */
public abstract class BaseActivity extends LibActivity {
    protected UserInfo user;

    public TitleBuilder initTitle(Object obj) {
        if (obj instanceof String) {
            return new TitleBuilder(this).setTitleText((String) obj);
        } else {
            return new TitleBuilder(this).setTitleText((int) obj);
        }
    }
    public TitleBuilder initRedTitle(Object obj) {
        return initTitle(obj).setTitleTextColor(getResources().getColor(R.color.white))
                .setTitleBgColor(getResources().getColor(R.color.font_red));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = BaseApplication.getInstance().getUserInfo();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        user = BaseApplication.getInstance().getUserInfo();
        super.onResume();
    }
}
