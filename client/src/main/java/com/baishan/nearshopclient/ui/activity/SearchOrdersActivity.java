package com.baishan.nearshopclient.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.base.BaseMvpActivity;
import com.baishan.nearshopclient.dao.MessageDao;
import com.baishan.nearshopclient.listener.OnStateListener;
import com.baishan.nearshopclient.model.SenderGoodsOrders;
import com.baishan.nearshopclient.presenter.SearchOrdersPresenter;
import com.baishan.nearshopclient.ui.adapter.SenderGoodsOrdersAdpater;
import com.baishan.nearshopclient.view.ISearchOrdersView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.type;

public class SearchOrdersActivity extends BaseMvpActivity<SearchOrdersPresenter> implements ISearchOrdersView, OnStateListener {

    private LinearLayout llSearch;
    private ImageView ivFinish;
    private EditText etKeywords;
    private TextView tvSearch;
    private List<SenderGoodsOrders> mData = new ArrayList<>();
    private SenderGoodsOrdersAdpater mAdapter;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_search_orders);
    }

    @Override
    protected void bindViews() {
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        ivFinish = (ImageView) findViewById(R.id.ivFinish);
        etKeywords = (EditText) findViewById(R.id.etKeywords);
        tvSearch = (TextView) findViewById(R.id.tvSearch);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initCommonRecyclerView(mAdapter = new SenderGoodsOrdersAdpater(mData), null);
        mAdapter.openLoadMore(10, true);
        mAdapter.setOnStateListener(this);
    }

    private String mKeywords;
    private int mPageNow = 1;

    @Override
    protected void setListener() {
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeywords = etKeywords.getText().toString();
                if (TextUtils.isEmpty(mKeywords)) {
                    showToast("请输入要搜索的内容");
                    return;
                }
                mPageNow = 1;
                mvpPresenter.searchKeywords(user.Id, mKeywords, mPageNow);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPageNow++;
                mvpPresenter.searchKeywords(user.Id, mKeywords, mPageNow);
            }
        });
        ivFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected SearchOrdersPresenter createPresenter() {
        return new SearchOrdersPresenter(this);
    }


    @Override
    public void searchKeywordsSuccess(List<SenderGoodsOrders> response) {
        if (mPageNow == 1)
            mData.clear();
        if (response.size() > 0) {
            mAdapter.notifyDataChangedAfterLoadMore(response, mPageNow < response.get(0).PageCount);
        } else {
            showToast("未搜索到相关信息！");
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void changeStateSuccess(String response) {
        mPageNow = 1;
        mvpPresenter.searchKeywords(user.Id, mKeywords, mPageNow);
    }

    @Override
    public void onState(String method, String orderNo) {
        MessageDao.read(orderNo);
        if ("RobOrder".equals(method)) {
            //为什么要单独判断 ，罗凯 搞事情！！
            mvpPresenter.changeState(method, orderNo, user.Id, type);
        } else {
            mvpPresenter.changeState(method, orderNo, user.Id);
        }
    }

    @Override
    public void onState(String method, String orderNo, String remark) {
        //派送端完成工作
        mvpPresenter.finishWork(method, orderNo, user.Id, remark);
    }
}
