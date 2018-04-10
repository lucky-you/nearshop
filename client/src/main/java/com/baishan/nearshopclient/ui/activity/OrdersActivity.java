package com.baishan.nearshopclient.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.base.BaseActivity;
import com.baishan.nearshopclient.model.UserInfo;
import com.baishan.nearshopclient.ui.adapter.TitlePagerAdapter;
import com.baishan.nearshopclient.ui.fragment.OrdersFragment;
import com.baishan.nearshopclient.utils.ConstantValue;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends BaseActivity {

    public static final int ORDER_GOODS = 1;
    public static final int ORDER_Service = 2;
    private int type;

    private LinearLayout llSearch;
    private TextView tvSearch;
    private TabLayout tab;
    private ViewPager viewPager;

    private String[] titles;
    private List<Fragment> fragments = new ArrayList<>();
    private TitlePagerAdapter pagerAdapter;


    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_orders);
    }

    @Override
    protected void bindViews() {
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        tvSearch = (TextView) findViewById(R.id.tvSearch);
        tab = (TabLayout) findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        post(new Notice(ConstantValue.MSG_FINISH_ORDERS_ACTIVITY));
        registerEvent();
        type = getIntent().getIntExtra(ConstantValue.TYPE, ORDER_GOODS);
        int arrayId = 0;
        if (type == ORDER_GOODS) {
            switch (user.getIdentity()) {
                case UserInfo.SENDER:
                    initTitle("我的订单");
                    llSearch.setVisibility(View.VISIBLE);
                    arrayId = R.array.order_goods_sender;
                    break;
                //商家的
                case UserInfo.BUSINESS:
                    initTitle("我的订单");
                    arrayId = R.array.order_goods_business;
                    break;
                case UserInfo.ADMIN:
                    initTitle("超市订单");
                    arrayId = R.array.order_goods_admin;
                    break;
            }
        } else {
            initTitle("便民订单");
            if (user.getIdentity() == UserInfo.SENDER) {
                arrayId = R.array.order_service_sender;
            } else {
                arrayId = R.array.order_goods_admin;
            }
        }
        titles = getResources().getStringArray(arrayId);
        for (int i = 0; i < titles.length; i++) {
            OrdersFragment fragment = new OrdersFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ConstantValue.TYPE, type);
            bundle.putInt(ConstantValue.INDEX, i);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        pagerAdapter = new TitlePagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(titles.length);
        tab.setupWithViewPager(viewPager);
        int position = getIntent().getIntExtra(ConstantValue.POSITION, -1);
        if (position != -1) {
            viewPager.setCurrentItem(position);
        }
    }

    @Subscribe
    public void onEvent(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_UPDATE_ORDER) {
            int position = notice.content == null ? -1 : (int) notice.content;
            if (position == -1) return;
            viewPager.post(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(position);
                }
            });

        } else if (notice.type == ConstantValue.MSG_FINISH_ORDERS_ACTIVITY) {
            finish();
        }
    }

    @Override
    protected void setListener() {
        llSearch.setOnClickListener(v -> intent2Activity(SearchOrdersActivity.class));
    }
}
