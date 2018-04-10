package com.baishan.nearshopclient.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.base.BaseMvpActivity;
import com.baishan.nearshopclient.model.Goods;
import com.baishan.nearshopclient.presenter.MyGoodsPresenter;
import com.baishan.nearshopclient.ui.adapter.GoodsAdapter;
import com.baishan.nearshopclient.utils.ConstantValue;
import com.baishan.nearshopclient.view.IMyGoodsView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 商家中心的商品订单
 */
public class MyGoodsActivity extends BaseMvpActivity<MyGoodsPresenter> implements IMyGoodsView {


    private RecyclerView recyclerView;
    private GoodsAdapter adapter;
    private List<Goods> mDatas = new ArrayList<>();
    private int pageNow = 1;
    private SwipeRefreshLayout srl;

    @Override
    protected MyGoodsPresenter createPresenter() {
        return new MyGoodsPresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.layout_title_recyclerview);
    }

    @Override
    protected void bindViews() {
        initTitle("我的商品");
        srl = get(R.id.srl);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        registerEvent();
        adapter = new GoodsAdapter(mDatas);
        adapter.openLoadMore(10, true);
        recyclerView = initCommonRecyclerView(adapter, null);
        mvpPresenter.getStoreProducts(user.Id, pageNow);
    }

    @Subscribe
    public void onEvent(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_UPDATE_ORDER || notice.type == ConstantValue.MSG_UPDATE_SHOP_ORDERS) {
            pageNow = 1;
            mvpPresenter.getStoreProducts(user.Id, pageNow);
        }
    }

    @Override
    protected void setListener() {
//        adapter.setOnRecyclerViewItemClickListener((view, i) -> {
//            Intent it = new Intent(mContext, GoodsDetailActivity.class);
//            it.putExtra(ConstantValue.GOODS_ID, mDatas.get(i).Id);
//            startActivity(it);
//        });

        //下拉刷新
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNow = 1;
                mvpPresenter.getStoreProducts(user.Id, pageNow);
            }
        });
        //上拉加载

        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mvpPresenter.getStoreProducts(user.Id, ++pageNow);
            }
        });
    }

    @Override
    public void getStoreProductsSuccess(List<Goods> response) {
        srl.setRefreshing(false);
        if (pageNow == 1) {
            adapter.getData().clear();
            adapter.notifyDataSetChanged();
        }
        if (response.size() > 0) {
            adapter.notifyDataChangedAfterLoadMore(response, pageNow < response.get(0).PageCount);
        }
    }

}
