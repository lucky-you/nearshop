package com.baishan.nearshop.ui.activity;

import android.os.Bundle;

import com.baishan.mylibrary.view.EmptyView;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.model.ConfirmOrderItem;
import com.baishan.nearshop.model.ShopOrder;
import com.baishan.nearshop.presenter.ExchangeRecordPresenter;
import com.baishan.nearshop.ui.adapter.ShopOrdersAdapter;
import com.baishan.nearshop.view.IExchangeRecordView;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRecordActivity extends BaseMvpActivity<ExchangeRecordPresenter> implements IExchangeRecordView {

    private ShopOrdersAdapter adapter;
    private List<ShopOrder> data = new ArrayList<>();

    @Override
    protected ExchangeRecordPresenter createPresenter() {
        return new ExchangeRecordPresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.layout_title_recyclerview);
    }

    @Override
    protected void bindViews() {
        initTitle("我的兑换");
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        adapter = new ShopOrdersAdapter(data, 2);
        adapter.setEmptyView(new EmptyView(mContext));
        initCommonRecyclerView(adapter, null);
        mvpPresenter.getRecord(user.UserId);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void getRecordSuccess(List<ShopOrder> response) {
        for (int i = 0; i < response.size(); i++) {
            ShopOrder order = response.get(i);
            //地址
            ShopOrder areaItem = new ShopOrder(order);
            //当前商区下的索引
            areaItem.setItemType(ShopOrder.AREA);
            data.add(areaItem);
            for (int j = 0; j < order.ProductItem.size(); j++) {
                //商品
                ShopOrder shopOrder = new ShopOrder(order.ProductItem.get(j));
                shopOrder.OrderNo = order.OrderNo;
                data.add(shopOrder);
            }
            //商品下面的信息
            ShopOrder infoItem = new ShopOrder(order);
            infoItem.setItemType(ConfirmOrderItem.INFO);
            data.add(infoItem);
        }
        adapter.notifyDataSetChanged();
    }
}
