package com.baishan.nearshop.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseActivity;
import com.baishan.nearshop.ui.adapter.TitlePagerAdapter;
import com.baishan.nearshop.ui.fragment.MyCouponFragment;
import com.baishan.nearshop.utils.ConstantValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/30.
 */
public class CouponActivity extends BaseActivity {


    private ViewPager vp;
    private TabLayout tab;
    private String[] titles = new String[]{"可使用", "已失效"};
    public final static int INTENT_TYPE_NORMAL = 1;
    public final static int INTENT_TYPE_SELECTED = 2;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_my_coupon);
    }

    @Override
    protected void bindViews() {
        vp = (ViewPager) findViewById(R.id.vp);
        tab = (TabLayout) findViewById(R.id.tab);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initTitle("优惠券");
        int price = getIntent().getIntExtra(ConstantValue.PRICE, -1);
        int type = getIntent().getIntExtra(ConstantValue.TYPE, INTENT_TYPE_NORMAL);
        int areaId = getIntent().getIntExtra(ConstantValue.AREA_ID, -1);
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            MyCouponFragment fragment = new MyCouponFragment();
            Bundle bundle = new Bundle();
            if (i == 0)
                bundle.putInt(ConstantValue.COUPON_TYPE, MyCouponFragment.TYPE_NOW);
            else if (i == 1)
                bundle.putInt(ConstantValue.COUPON_TYPE, MyCouponFragment.TYPE_HISTORY);

            bundle.putInt(ConstantValue.PRICE, price);
            bundle.putInt(ConstantValue.TYPE, type);
            bundle.putInt(ConstantValue.AREA_ID, areaId);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        vp.setOffscreenPageLimit(titles.length);
        vp.setAdapter(new TitlePagerAdapter(getSupportFragmentManager(), fragments, titles));
        tab.setupWithViewPager(vp);
    }


    @Override
    protected void setListener() {

    }


}
