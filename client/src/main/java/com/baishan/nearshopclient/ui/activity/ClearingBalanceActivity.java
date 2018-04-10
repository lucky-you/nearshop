package com.baishan.nearshopclient.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.baishan.mylibrary.view.EmptyView;
import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.base.BaseMvpActivity;
import com.baishan.nearshopclient.model.ConsumeRecord;
import com.baishan.nearshopclient.presenter.ConsumeRecordPresenter;
import com.baishan.nearshopclient.ui.adapter.ConsumeRecordAdapter;
import com.baishan.nearshopclient.ui.view.VerticalItemDecoration;
import com.baishan.nearshopclient.view.IConsumeRecordView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 结算余额
 */
public class ClearingBalanceActivity extends BaseMvpActivity<ConsumeRecordPresenter> implements IConsumeRecordView {

    private static final String TAG = "ClearingBalanceActivity";
    private RecyclerView recyclerView;
    private SwipeRefreshLayout srl;
    private ConsumeRecordAdapter adapter;
    private List<ConsumeRecord> data = new ArrayList<>();
    private int curPage = 1;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_clearing_balance);
    }

    @Override
    protected void bindViews() {
        initTitle("金额明细");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        srl = (SwipeRefreshLayout) findViewById(R.id.srl);

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        adapter = new ConsumeRecordAdapter(data);
        adapter.openLoadMore(10, true);
        initCommonRecyclerView(adapter, new VerticalItemDecoration(mContext, getResources().getColor(R.color.bg_grey), 1));
        mvpPresenter.getData(user.UserId, curPage);
        adapter.setEmptyView(new EmptyView(mContext));
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
    protected ConsumeRecordPresenter createPresenter() {
        return new ConsumeRecordPresenter(this);
    }

    @Override
    public void getDataSuccess(List<ConsumeRecord> response) {
        srl.setRefreshing(false);
        if (response.size() > 0) {
            adapter.notifyDataChangedAfterLoadMore(response, curPage < response.get(0).PageCount);
        }
    }
}
