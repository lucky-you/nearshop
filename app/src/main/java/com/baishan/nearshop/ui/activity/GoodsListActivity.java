package com.baishan.nearshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.baishan.mylibrary.view.EmptyView;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.model.Goods;
import com.baishan.nearshop.presenter.GoodsListPresenter;
import com.baishan.nearshop.presenter.GoodsPresenter;
import com.baishan.nearshop.ui.adapter.GoodsAdapter;
import com.baishan.nearshop.ui.view.RefreshLayout;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.IGoodsListView;
import com.baishan.nearshop.view.IGoodsView;

import java.util.ArrayList;
import java.util.List;

public class GoodsListActivity extends BaseMvpActivity<GoodsListPresenter> implements IGoodsListView, IGoodsView {

    private RecyclerView recyclerView;
    private GoodsAdapter adapter;
    private List<Goods> goods = new ArrayList<>();
    private int categoryId;
    private int currentPage = 1;
    private String title;
    private GoodsPresenter goodsPresenter;
    private RefreshLayout srl;

    @Override
    protected GoodsListPresenter createPresenter() {
        goodsPresenter = new GoodsPresenter(this);
        return new GoodsListPresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.layout_title_recyclerview);
    }

    @Override
    protected void bindViews() {
        srl = (RefreshLayout) findViewById(R.id.srl);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        adapter = new GoodsAdapter(goods);
        adapter.setEmptyView(new EmptyView(mContext));
        adapter.openLoadMore(10, true);
        recyclerView = initGridRecyclerView(adapter, null, 2);
        title = getIntent().getStringExtra(ConstantValue.TITLE);
        if (TextUtils.isEmpty(title)) {
            initTitle("分类商品");
        } else {
            initTitle(title);
        }
        categoryId = Integer.parseInt(getIntent().getStringExtra(ConstantValue.CATEGORY_TYPE));
        mvpPresenter.getShopCustomList(categoryId, 1);
    }

    @Override
    protected void setListener() {
        adapter.setOnRecyclerViewItemClickListener((v, i) -> {
            Intent it = new Intent(mContext, GoodsDetailActivity.class);
            it.putExtra(ConstantValue.GOODS, goods.get(i));
            startActivity(it);
        });

        adapter.setOnLoadMoreListener(() -> {
            mvpPresenter.getShopCustomList(categoryId, ++currentPage);
        });
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mvpPresenter.getShopCustomList(categoryId, ++currentPage);
            }
        });

        adapter.setOnRecyclerViewItemChildClickListener((baseQuickAdapter, view, i) -> {
            if (view.getId() == R.id.btnAdd) {
                if (checkLogin()) {
                    Goods item = goods.get(i);
                    String spec = null;
                    if (item.getSpec() != null) spec = item.getSpec()[0];
                    goodsPresenter.addToShopCar(user.UserId, currentArea.defaultAddress == null ? 0 : currentArea.AreaId, item.Id, spec);
                }
            }
        });
    }

    @Override
    public void getShopCustomListSuccess(List<Goods> response) {
        srl.setRefreshing(false);
        if (response.size() > 0) {
            adapter.notifyDataChangedAfterLoadMore(response, currentPage < response.get(0).PageCount);
        }
    }

    @Override
    public void addToShopCarSuccess() {

    }

    @Override
    public void exchangeGoodsSuccess() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (goodsPresenter != null) {
            goodsPresenter.detachView();
        }
    }
}
