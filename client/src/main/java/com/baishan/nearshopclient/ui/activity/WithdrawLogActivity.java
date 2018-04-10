package com.baishan.nearshopclient.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baishan.mylibrary.view.EmptyView;
import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.base.BaseMvpActivity;
import com.baishan.nearshopclient.model.WithdrawLog;
import com.baishan.nearshopclient.presenter.WithdrawLogPresenter;
import com.baishan.nearshopclient.ui.adapter.WithdrawLogAdapter;
import com.baishan.nearshopclient.view.IWithdrawLogView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 提现记录
 */
public class WithdrawLogActivity extends BaseMvpActivity<WithdrawLogPresenter> implements IWithdrawLogView {

    private RecyclerView recyclerView;
    private WithdrawLogAdapter adapter;
    private List<WithdrawLog> mDatas = new ArrayList<>();
    private SwipeRefreshLayout srl;


    @Override
    protected WithdrawLogPresenter createPresenter() {
        return new WithdrawLogPresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.layout_title_refresh_recyclerview);
    }

    @Override
    protected void bindViews() {
        srl = get(R.id.srl);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initTitle("提现记录");
        adapter = new WithdrawLogAdapter(mDatas);
        recyclerView = initCommonRecyclerView(adapter, null);
        mvpPresenter.getWithdrawLog(user);
        adapter.setEmptyView(new EmptyView(mContext));
    }


    @Override
    protected void setListener() {
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {

            }
        });
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mvpPresenter.getWithdrawLog(user);
            }
        });
    }

    @Override
    public void getWithdrawLogSuccess(List<WithdrawLog> response) {
        srl.setRefreshing(false);
        mDatas.clear();
        adapter.addData(response);
    }
}


