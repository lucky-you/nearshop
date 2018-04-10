package com.baishan.nearshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseActivity;
import com.baishan.nearshop.base.BaseApplication;
import com.baishan.nearshop.dao.MessageDao;
import com.baishan.nearshop.model.UserInfo;
import com.baishan.nearshop.utils.ConstantValue;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyMessageActivity extends BaseActivity {

    @BindView(R.id.llContainer)
    LinearLayoutCompat llContainer;
    @BindView(R.id.tvSystemNum)
    TextView tvSystemNum;
    @BindView(R.id.llSystem)
    LinearLayout llSystem;
    @BindView(R.id.tvGoodsNum)
    TextView tvGoodsNum;
    @BindView(R.id.llGoods)
    LinearLayout llGoods;
    @BindView(R.id.tvServiceNum)
    TextView tvServiceNum;
    @BindView(R.id.llService)
    LinearLayout llService;
    @BindView(R.id.tvForumNum)
    TextView tvForumNum;
    @BindView(R.id.llForum)
    LinearLayout llForum;


    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_my_message);
    }

    @Override
    protected void bindViews() {
        ButterKnife.bind(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initTitle("我的消息");
    }

    @Override
    protected void onResume() {
        super.onResume();
        long num1 = MessageDao.getUnreadMsgNum(1, null);
        setMessageNum(tvSystemNum, num1);
        UserInfo user = BaseApplication.getInstance().getUserInfo();
        if (user != null) {
            long num2 = MessageDao.getUnreadMsgNum(2, user.LoginToken);
            setMessageNum(tvGoodsNum, num2);
            long num3 = MessageDao.getUnreadMsgNum(3, user.LoginToken);
            setMessageNum(tvServiceNum, num3);
            long num4 = MessageDao.getUnreadMsgNum(4, user.LoginToken);
            setMessageNum(tvForumNum, num4);
        }
    }

    private void setMessageNum(TextView tv, long num) {
        if (num == 0) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(num + "");
        }
    }

    @Override
    protected void setListener() {
        for (int i = 1; i < llContainer.getChildCount(); i++) {
            LinearLayout view = (LinearLayout) llContainer.getChildAt(i);
            int finalI = i;
            view.setOnClickListener(v -> {
                Intent it = new Intent(mContext, MessageActivity.class);
                it.putExtra(ConstantValue.TYPE, finalI);
                it.putExtra(ConstantValue.TITLE, ((TextView) view.getChildAt(1)).getText());
                startActivity(it);
            });
        }
    }
}
