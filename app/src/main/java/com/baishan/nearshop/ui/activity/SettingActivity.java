package com.baishan.nearshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.baishan.nearshop.R;
import com.baishan.nearshop.base.ApiService;
import com.baishan.nearshop.base.BaseActivity;
import com.baishan.nearshop.base.BaseApplication;
import com.baishan.nearshop.dao.UserDao;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.utils.UpdateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {


    @BindView(R.id.relUpdatePwd)
    RelativeLayout relUpdatePwd;
    @BindView(R.id.relCheckUpdate)
    RelativeLayout relCheckUpdate;
    @BindView(R.id.relAboutMe)
    RelativeLayout relAboutMe;
    @BindView(R.id.relLogout)
    RelativeLayout relLogout;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initRedTitle("系统设置");
        if (BaseApplication.getInstance().getUserInfo() == null) {
            relLogout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.relUpdatePwd, R.id.relCheckUpdate, R.id.relAboutMe, R.id.relLogout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relUpdatePwd:
                if (BaseApplication.getInstance().getUserInfo() != null) {
                    Intent intent = new Intent(this, RegistActivity.class);
                    intent.putExtra(ConstantValue.TYPE, RegistActivity.TYPE_UPDATE);
                    startActivity(intent);
                } else {
                    intent2Activity(LoginActivity.class);
                }
                break;
            case R.id.relCheckUpdate:
                UpdateUtils.checkUpdate(this, update -> {
                    if (update) {
                        UpdateUtils.update(this);
                    } else {
                        showToast("当前已是最新版本");
                    }
                });
                break;
            case R.id.relAboutMe:
                Intent it1 = new Intent(mContext, WebActivity.class);
                it1.putExtra(ConstantValue.URL, ApiService.ABOUT_US);
                it1.putExtra(ConstantValue.TITLE, "关于我们");
                startActivity(it1);
                break;
            case R.id.relLogout:
                UserDao.logout();
                finish();
                break;
        }
    }
}
