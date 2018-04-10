package com.baishan.nearshop.ui.fragment;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpFragment;
import com.baishan.nearshop.model.Category;
import com.baishan.nearshop.presenter.SupermarketPresenter;
import com.baishan.nearshop.ui.activity.SearchActivity;
import com.baishan.nearshop.ui.activity.SelectAddrActivity;
import com.baishan.nearshop.ui.adapter.CategoryAdapter;
import com.baishan.nearshop.ui.adapter.TitlePagerAdapter;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.ISupermarketView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class SupermarketFragment extends BaseMvpFragment<SupermarketPresenter> implements ISupermarketView {

    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.ivExpand)
    ImageView ivExpand;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private String[] titles;
    private List<Fragment> fragments = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private List<Category> mDatas = new ArrayList<>();
    private int height;
    private boolean isExpand = false;


    @Override
    protected SupermarketPresenter createPresenter() {
        return new SupermarketPresenter(this);
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_supermarket, null);
    }

    @Override
    protected void bindViews(View view) {
        ButterKnife.bind(this, rootView);
    }

    @Override
    protected void processLogic() {
        registerEvent();
        mvpPresenter.getAllCategory(getCommonAreaParams());
        categoryAdapter = new CategoryAdapter(mDatas);
        initGridRecyclerView(categoryAdapter, null, 4);
        setAddress();
    }

    private void setAddress() {
        try {
            String address = mCurrentArea.County + mCurrentArea.AreaName;
            if (address.length() > 8) {
                address = mCurrentArea.AreaName;
            }
            tvAddress.setText(String.format(getString(R.string.send_address), address));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setListener() {
        tvAddress.setOnClickListener(v -> intent2Activity(SelectAddrActivity.class));
        ivSearch.setOnClickListener(v -> intent2Activity(SearchActivity.class));
        ivMenu.setOnClickListener(v -> showMoreWindow(ivMenu));
        categoryAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
            viewPager.setCurrentItem(i);
            hideRecyclerView();
        });
        recyclerView.setOnTouchListener((v, event) -> {
            hideRecyclerView();
            return true;
        });
    }

    private void hideRecyclerView() {
        recyclerView.setVisibility(View.GONE);
        isExpand = false;
        ivExpand.setImageResource(R.drawable.ic_arrow_down);
    }


    @OnClick({R.id.ivExpand})
    void onclick(View v) {
        switch (v.getId()) {
            case R.id.ivExpand:
                expandRv();
                break;
        }
    }

    private void expandRv() {
        if (height == 0) {
            height = recyclerView.getMeasuredHeight();
        }
        ValueAnimator animator;
        if (isExpand) {
            ivExpand.setImageResource(R.drawable.ic_arrow_down);
            isExpand = false;
            animator = ValueAnimator.ofInt(height, 0);
        } else {
            ivExpand.setImageResource(R.drawable.ic_arrow_up);
            recyclerView.setVisibility(View.VISIBLE);
            isExpand = true;
            animator = ValueAnimator.ofInt(0, height);
        }
        animator.addUpdateListener(animation -> {
            ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
            params.height = (int) animation.getAnimatedValue();
            recyclerView.setLayoutParams(params);
        });
        animator.start();
    }

    @Override
    public void getAllCategorySuccess(List<Category> response) {
        categoryAdapter.getData().clear();
        //切换商区 销毁以前的数据
        FragmentManager manager = getActivity().getSupportFragmentManager();
        if (fragments.size() > 0) {
            FragmentTransaction transaction = manager.beginTransaction();
            for (Fragment fragment : fragments) {
                transaction.remove(fragment);
            }
            transaction.commitAllowingStateLoss();
        }
        fragments.clear();
        response.add(0, new Category(-99, "全部"));
        categoryAdapter.addData(response);
        titles = new String[response.size()];
        for (int i = 0; i < response.size(); i++) {
            titles[i] = response.get(i).Title;
            CategoryFragment fragment = new CategoryFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ConstantValue.CATEGORY_FLAG, i == 0 ? "all" : "true");
            bundle.putInt(ConstantValue.CATEGORY_TYPE, response.get(i).CategoryId);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        TitlePagerAdapter pagerAdapter = new TitlePagerAdapter(manager, fragments, titles);
        viewPager.setAdapter(pagerAdapter);
        tab.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(titles.length);
        tab.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                hideRecyclerView();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_UPDATE_AREA) {
            getCommonData();
            setAddress();
            mvpPresenter.getAllCategory(getCommonAreaParams());
        }
    }

    /**
     * 根据分类切换显示页面
     *
     * @param categoryId
     */
    public void show(int categoryId) {
        int position = -1;
        for (Category data : mDatas) {
            if (data.CategoryId == categoryId) {
                position = mDatas.indexOf(data);
                break;
            }
        }
        if (position != -1) {
            viewPager.setCurrentItem(position);
        } else {
            viewPager.setCurrentItem(0);
        }
    }

}
