package com.baishan.nearshop.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.baishan.mylibrary.view.EmptyView;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.model.ConsumeRecord;
import com.baishan.nearshop.presenter.ConsumeRecordPresenter;
import com.baishan.nearshop.ui.adapter.ConsumeRecordAdapter;
import com.baishan.nearshop.ui.view.RefreshLayout;
import com.baishan.nearshop.ui.view.VerticalItemDecoration;
import com.baishan.nearshop.view.IConsumeRecordView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 消费记录
 */
public class ConsumeRecordActivity extends BaseMvpActivity<ConsumeRecordPresenter> implements IConsumeRecordView {
    private static final String TAG = "ConsumeRecordActivity";
    private ConsumeRecordAdapter adapter;
    private List<ConsumeRecord> data = new ArrayList<>();
    private int curPage = 1;
    private RefreshLayout srl;
    private RecyclerView recyclerView;

    @Override
    protected ConsumeRecordPresenter createPresenter() {
        return new ConsumeRecordPresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.layout_title_recyclerview);
    }

    @Override
    protected void bindViews() {
        initTitle("消费记录");
        srl = (RefreshLayout) findViewById(R.id.srl);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mvpPresenter.getData(user.UserId, curPage);
        adapter = new ConsumeRecordAdapter(data);
        adapter.openLoadMore(10, true);
        adapter.setEmptyView(new EmptyView(mContext));
        recyclerView = initCommonRecyclerView(adapter, new VerticalItemDecoration(mContext, getResources().getColor(R.color.bg_grey), 1));
    }

    @Override
    protected void setListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                mvpPresenter.getData(user.UserId, curPage);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mvpPresenter.getData(user.UserId, ++curPage);
            }
        });
    }

    @Override
    public void getDataSuccess(List<ConsumeRecord> response) {
        srl.setRefreshing(false);
        if (curPage == 1) {
            adapter.getData().clear();
            adapter.notifyDataSetChanged();
        }
        adapter.addData(data);
        if (response.size() > 0) {
            adapter.notifyDataChangedAfterLoadMore(response, curPage < response.get(0).PageCount);
        }
    }
}
