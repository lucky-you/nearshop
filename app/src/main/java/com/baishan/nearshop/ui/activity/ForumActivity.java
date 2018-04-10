package com.baishan.nearshop.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseActivity;
import com.baishan.nearshop.base.BaseApplication;
import com.baishan.nearshop.dao.MessageDao;
import com.baishan.nearshop.model.UserInfo;
import com.baishan.nearshop.ui.fragment.ForumFragment;
import com.baishan.nearshop.utils.ConstantValue;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/22 0022.
 */

public class ForumActivity extends BaseActivity {

    @BindView(R.id.titlebar_iv_left)
    ImageView titlebarIvLeft;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.relMessage)
    RelativeLayout relMessage;
    @BindView(R.id.vHasMsg)
    View vHasMsg;
    String[] titles = new String[]{"当前", "全市"};
    private List<Fragment> fragments;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_forum);
        ButterKnife.bind(this);

    }

    @Override
    protected void bindViews() {
//        titlebarIvLeft = get(R.id.titlebar_iv_left);
//        tabs = get(R.id.tabs);
//        flContent = get(R.id.flContent);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        createTabsAndFragments();

    }

    private Fragment mLastShowFragment;

    /**
     * 创建Tab和fragment
     */
    private void createTabsAndFragments() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragments = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            tabs.addTab(tabs.newTab().setText(titles[i]));
            ForumFragment fragment = new ForumFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ConstantValue.DATA_TYPE, i == 0 ? "area" : "city");
            fragment.setArguments(bundle);
            fragments.add(fragment);
            fragmentTransaction.add(R.id.flContent, fragment).hide(fragment);
        }
        mLastShowFragment = fragments.get(0);
        //显示第一个fragment
        fragmentTransaction.show(fragments.get(0));
        fragmentTransaction.commit();
    }

    @Override
    protected void setListener() {
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //隐藏上一个 显示这一个
                Fragment fragment = fragments.get(tab.getPosition());
                getSupportFragmentManager().beginTransaction().hide(mLastShowFragment).show(fragment).commit();
                mLastShowFragment = fragment;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        titlebarIvLeft.setOnClickListener(v -> finish());
    }


    @OnClick({R.id.titlebar_iv_left, R.id.relMessage})
    public void onBackClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_iv_left:
                finish();
                break;
            case R.id.relMessage:
                Intent it = new Intent(mContext, MessageActivity.class);
                it.putExtra(ConstantValue.TYPE, 4);
                it.putExtra(ConstantValue.TITLE, "社区消息");
                startActivity(it);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserInfo user = BaseApplication.getInstance().getUserInfo();
        long num = MessageDao.getUnreadMsgNum(4, user.LoginToken);
        vHasMsg.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
    }
}
