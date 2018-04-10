package com.baishan.nearshop.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.db.DBManager;
import com.baishan.nearshop.model.SearchHistory;
import com.baishan.nearshop.presenter.SearchPresenter;
import com.baishan.nearshop.ui.adapter.SearchAdapter;
import com.baishan.nearshop.ui.adapter.TitlePagerAdapter;
import com.baishan.nearshop.ui.fragment.CategoryFragment;
import com.baishan.nearshop.ui.fragment.SearchEasyOrderFragment;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.ISearchView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseMvpActivity<SearchPresenter> implements ISearchView {

    private ImageView ivBack;
    private LinearLayout llSearch;
    private EditText etSearch;
    private ImageView ivScan;
    private TextView tvSearch;
    private LinearLayout llHistory;
    private TextView tvClear;
    private LinearLayout llResult;
    private android.support.design.widget.TabLayout tabs;
    private android.support.v4.view.ViewPager viewPager;
    private String[] titles = new String[]{"超市商品", "便民服务"};
    private List<Fragment> fragments = new ArrayList<>();

    private SearchAdapter adapter;
    private List<SearchHistory> data = new ArrayList<>();
    public static final int INTENT_MARKET = 1;
    public static final int INTENT_SERVICE = 2;
    private int type;

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void bindViews() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        etSearch = (EditText) findViewById(R.id.etSearch);
        ivScan = (ImageView) findViewById(R.id.ivScan);
        tvSearch = (TextView) findViewById(R.id.tvSearch);
        llHistory = (LinearLayout) findViewById(R.id.llHistory);
        tvClear = (TextView) findViewById(R.id.tvClear);
        llResult = (LinearLayout) findViewById(R.id.llResult);
        tabs = (android.support.design.widget.TabLayout) findViewById(R.id.tabs);
        viewPager = (android.support.v4.view.ViewPager) findViewById(R.id.viewPager);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        type = getIntent().getIntExtra(ConstantValue.TYPE, INTENT_MARKET);
        data = mvpPresenter.getHistoryData();
        adapter = new SearchAdapter(data);
        initGridRecyclerView(adapter, null, 3);
        for (int i = 0; i < titles.length; i++) {
            Fragment fragment = null;
            if (i == 0) {
                fragment = new CategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(ConstantValue.TYPE, CategoryFragment.TYPE_SEARCH);
                fragment.setArguments(bundle);
            } else {
                fragment = new SearchEasyOrderFragment();
            }

            fragments.add(fragment);
        }
        viewPager.setAdapter(new TitlePagerAdapter(getSupportFragmentManager(), fragments, titles));
        viewPager.setOffscreenPageLimit(2);
        tabs.setupWithViewPager(viewPager);
        llResult.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setListener() {
        ivBack.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        ivScan.setOnClickListener(this);
        adapter.setOnRecyclerViewItemClickListener((view, i) -> {
            etSearch.setText(data.get(i).getKey());
            search();
        });
        tvClear.setOnClickListener(v -> {
            if (data.size() > 0) {
                data.clear();
                adapter.notifyDataSetChanged();
                DBManager.getInstance().getHistoryDao().deleteAll();
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    llResult.setVisibility(View.GONE);
                    llHistory.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivScan:
                intent2Activity(ProcessCodeActivity.class);
                break;
            case R.id.tvSearch:
                search();
                break;
        }
    }

    private void search() {
        String keyword = etSearch.getText().toString();
        if (TextUtils.isEmpty(keyword)) {
            showToast("请输入要搜索的关键词");
            return;
        }
        post(new Notice(ConstantValue.MSG_TYPE_SEARCH_KEYWORD, keyword));
        llHistory.setVisibility(View.GONE);
        llResult.setVisibility(View.VISIBLE);
        if (type == INTENT_SERVICE)
            viewPager.setCurrentItem(1);
        mvpPresenter.insert(keyword);
        data.clear();
        adapter.addData(mvpPresenter.getHistoryData());
    }

}
