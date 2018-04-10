package com.baishan.nearshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.view.IosDialog;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseActivity;
import com.baishan.nearshop.base.BaseApplication;
import com.baishan.nearshop.base.BaseMvpFragment;
import com.baishan.nearshop.dao.MessageDao;
import com.baishan.nearshop.dao.UserDao;
import com.baishan.nearshop.easemob.HxConstant;
import com.baishan.nearshop.model.UserInfo;
import com.baishan.nearshop.ui.fragment.EasyOrderFragment;
import com.baishan.nearshop.ui.fragment.FragmentController;
import com.baishan.nearshop.ui.fragment.ServiceListFragment;
import com.baishan.nearshop.utils.ConstantValue;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.sharesdk.framework.ShareSDK;


public class MainActivity extends BaseActivity {


    private FragmentController controller;
    private long exitTime;
    private LinearLayout llBottom;
    private TextView tvNum;
    private View lastSelected;
    private static MainActivity instance;
    private UserInfo info;
    private int shopCarNum;
    private long msgNum;
    private boolean isExceptionDialogShow;

    public static MainActivity getInstance() {
        return instance;
    }


    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void bindViews() {
        llBottom = get(R.id.llBottom);
        tvNum = get(R.id.tvNum);
    }

    @Override
    public void onBackPressed() {
        if (ServiceListFragment.getInstance() != null && controller.getCurrentPosition() == 2) {
            ServiceListFragment.getInstance().back();
            return;
        }
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            BaseApplication.getInstance().clearGlobalData();
            finish();
        }
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        instance = this;
        controller = FragmentController.getInstance(this, R.id.container, savedInstanceState == null);
        controller.showFragment(0);
        lastSelected = llBottom.getChildAt(0);
        lastSelected.setSelected(true);
        registerEvent();
        new Handler().postDelayed(() -> {
            setMessage();
        }, 100);
        Intent extraIntent = getIntent().getParcelableExtra(ConstantValue.EXTRA_INTENT);
        if (extraIntent != null) startActivity(extraIntent);
        ShareSDK.initSDK(mContext);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putAll(outState);
    }

    public void setMessage() {
        info = BaseApplication.getInstance().getUserInfo();
        msgNum = MessageDao.getUnreadMsgNum(info == null ? null : info.LoginToken);
        setMessageNum(msgNum);
    }

    @Override
    protected void setListener() {
        for (int i = 0; i < llBottom.getChildCount(); i++) {
            int finalI = i;
            llBottom.getChildAt(i).setOnClickListener(v -> {
                if (finalI == 2 && ServiceListFragment.getInstance() != null) {
                    ((EasyOrderFragment) controller.getFragment(finalI)).remove();
                }
                showFragment(finalI);
            });
        }
    }

    public void showFragment(int position) {
        controller.showFragment(position);
        lastSelected.setSelected(false);
        if (position != 3) {
            lastSelected = llBottom.getChildAt(position);
            lastSelected.setSelected(true);
        } else {
            RelativeLayout view = (RelativeLayout) llBottom.getChildAt(position);
            lastSelected = view.getChildAt(0);
            lastSelected.setSelected(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.onDestroy();
        instance = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_UPDATE_USER) {
            setMessage();
            if (info == null) {
                setShopCarNum(0);
            }
        } else if (notice.type == ConstantValue.MSG_TYPE_NEW_MESSAGE) {
            setMessageNum(++msgNum);
        }
    }

    public void setShopCarNum(int num) {
        shopCarNum = num;
        if (num == 0) {
            tvNum.setVisibility(View.GONE);
        } else {
            tvNum.setVisibility(View.VISIBLE);
            tvNum.setText(num + "");
        }
    }

    public void setMessageNum(long num) {
        for (Fragment fragment : controller.getFragments()) {
            (((BaseMvpFragment) fragment)).setMessageNum(num);
        }
    }

    public int getShopCarNum() {
        return shopCarNum;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!isExceptionDialogShow && intent.getBooleanExtra(HxConstant.ACCOUNT_CONFLICT, false)) {
            UserDao.logout();
            isExceptionDialogShow = true;
            new IosDialog(mContext).builder().setTitle("提示")
                    .setCancelable(false)
                    .setMsg(getString(R.string.connect_conflict))
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isExceptionDialogShow = false;
                        }
                    })
                    .setPositiveButton("去登陆", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isExceptionDialogShow = false;
                            intent2Activity(LoginActivity.class);
                        }
                    }).show();
        }
    }

}
