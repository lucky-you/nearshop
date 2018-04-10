package com.baishan.nearshop.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseActivity;
import com.baishan.nearshop.ui.adapter.TitlePagerAdapter;
import com.baishan.nearshop.ui.fragment.EasyOrdersFragment;
import com.baishan.nearshop.ui.fragment.ShopOrdersFragment;
import com.baishan.nearshop.utils.ConstantValue;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单
 */
public class OrdersActivity extends BaseActivity {
    private ViewPager vp;
    private TabLayout tab;

    /**
     * 商品订单
     */
    public static final int TYPE_SHOP_ORDERS = 1;
    /**
     * 便民订单
     */
    public static final int TYPE_EASY_ORDERS = 2;

    int mType = TYPE_SHOP_ORDERS;

    private String[] titles = new String[]{"待支付", "进行中", "已完成", "退款"};

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_orders);
    }

    @Override
    protected void bindViews() {
        vp = (ViewPager) findViewById(R.id.vp);
        tab = (TabLayout) findViewById(R.id.tab);

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

        mType = getIntent().getIntExtra(ConstantValue.TYPE, TYPE_SHOP_ORDERS);
        initTitle(mType == TYPE_SHOP_ORDERS ? "我的商品订单" : "我的便民订单");
        if (mType == TYPE_EASY_ORDERS) {
            titles[0] = "已预约";
        }

        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            String type = "";
            switch (i) {
                case 0:
                    type = mType == TYPE_EASY_ORDERS ? "YYY" : "DZF";
                    break;
                case 1:
                    type = "JXZ";
                    break;
                case 2:
                    type = "YWC";
                    break;
                case 3:
                    type = "TK";
                    break;

            }
            Bundle bundle = new Bundle();
            Fragment fragment;
            if (mType == TYPE_SHOP_ORDERS) {
                //商品订单
                fragment = new ShopOrdersFragment();
            } else {
                //便民订单
                fragment = new EasyOrdersFragment();
            }
            bundle.putString(ConstantValue.TYPE, type);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        vp.setOffscreenPageLimit(titles.length);
        vp.setAdapter(new TitlePagerAdapter(getSupportFragmentManager(), fragments, titles));
        vp.setCurrentItem(getIntent().getIntExtra(ConstantValue.TABPOSITION, 0));
        tab.setupWithViewPager(vp);
    }

    @Override
    protected void setListener() {

    }
}
