package com.baishan.nearshop.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.view.EmptyView;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpFragment;
import com.baishan.nearshop.model.ShopOrder;
import com.baishan.nearshop.presenter.GoodsOrderPresenter;
import com.baishan.nearshop.presenter.OrdersPresenter;
import com.baishan.nearshop.ui.activity.ConfirmOrdersActivity;
import com.baishan.nearshop.ui.adapter.ShopOrdersAdapter;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.utils.OrdersConstants;
import com.baishan.nearshop.view.IGoodsOrderView;
import com.baishan.nearshop.view.IOrdersView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.baishan.nearshop.utils.OrdersConstants.ORDER_STATE_WAIT_PAY;

/**
 * Created by Administrator on 2016/9/21 0021.
 * 商品订单
 */
public class ShopOrdersFragment extends BaseMvpFragment<GoodsOrderPresenter> implements IGoodsOrderView, IOrdersView {
    private List<ShopOrder> data = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout srl;
    private ShopOrdersAdapter adapter;
    private String mType;

    private OrdersPresenter ordersPresenter;

    @Override
    protected GoodsOrderPresenter createPresenter() {
        ordersPresenter = new OrdersPresenter(this);
        return new GoodsOrderPresenter(this);
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_refresh_recyclerview, null);
    }

    @Override
    protected void bindViews(View view) {
        recyclerView = get(R.id.recyclerView);
        srl = get(R.id.srl);
    }

    @Override
    protected void processLogic() {
        registerEvent();
        mType = getArguments().getString(ConstantValue.TYPE);
        initCommonRecyclerView(adapter = new ShopOrdersAdapter(data), null);
        adapter.openLoadMore(10, true);
        adapter.setEmptyView(new EmptyView(mContext));
    }

    @Subscribe
    public void onEvent(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_UPDATE_SHOP_ORDER) {
            mPageNow = 1;
            getDataList();
        }
    }

    private int mPageNow = 1;

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        mType = getArguments().getString(ConstantValue.TYPE);
        mPageNow = 1;
        getDataList();
    }

    @Override
    protected void setListener() {
        srl.setOnRefreshListener(() -> {
            mPageNow = 1;
            getDataList();
        });
        adapter.setOnLoadMoreListener(() -> {
            mPageNow++;
            getDataList();
        });
        adapter.setOnRecyclerViewItemClickListener((view, i) -> {
            Intent intent = new Intent(mContext, ConfirmOrdersActivity.class);
            intent.putExtra(ConstantValue.ORDERNO, data.get(i).OrderNo);
            intent.putExtra(ConstantValue.TYPE, ConfirmOrdersActivity.INTENT_ORDERS_LIST);
            startActivity(intent);
        });
        adapter.setOnRecyclerViewItemChildClickListener((baseQuickAdapter, view, i) -> {
            ShopOrder order = data.get(i);
            if (view.getId() == R.id.btnLeft) {
                switch (order.OrderState) {
                    case ORDER_STATE_WAIT_PAY: //0待支付
                    case OrdersConstants.ORDER_STATE_PAID://10已付款
                    case OrdersConstants.ORDER_STATE_MATCH_SENDER: //20分配派送员
                    case OrdersConstants.ORDER_STATE_WAIT_SENDER: //25等待派送员操作
                        //取消订单
                        ordersPresenter.cancelGoodsOrders(order.OrderNo, user.UserId);
                        break;
                    case OrdersConstants.ORDER_STATE_DELIVERED://45已送达
                        //申请退款
                        ordersPresenter.applyGoodsRefund(order.OrderNo, user.UserId);
                        break;
                    case OrdersConstants.ORDER_STATE_REJECT_REFUND: //65拒绝退款
                        //确认收货
                        ordersPresenter.confirmGoodsOrders(order.OrderNo, user.UserId);
                        break;
                }
            } else { //btnRight
                switch (order.OrderState) {
                    case OrdersConstants.ORDER_STATE_DELIVERED://45已送达
                    case OrdersConstants.ORDER_STATE_APPLY_REFUND: //60申请退款
                        //按钮显示为放弃退款，放弃退款就是默认确认收货
                        ordersPresenter.confirmGoodsOrders(order.OrderNo, user.UserId);
                        break;
                    case OrdersConstants.ORDER_STATE_REJECT_REFUND: //65拒绝退款
                        //申请退款
                        ordersPresenter.applyGoodsRefund(order.OrderNo, user.UserId);
                        break;
                    case OrdersConstants.ORDER_STATE_FINISHED: //50已完成
                        //联系到了拨号界面，但是实际的拨号是由用户点击实现的。
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + order.CourierPhone);
                        intent.setData(data);
                        startActivity(intent);
                        break;

                }
            }
        });
    }


    @Override
    public void getShopOrderListSuccess(List<ShopOrder> response) {
        srl.setRefreshing(false);
        if (mPageNow == 1) {
            adapter.getData().clear();
            adapter.notifyDataSetChanged();
        }
        if (response.size() > 0) {
            generateOrderItem(response);
            adapter.notifyDataChangedAfterLoadMore(mPageNow < response.get(0).PageCount);
        }
    }

    private void generateOrderItem(List<ShopOrder> response) {
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
            infoItem.RefundPrice = order.refundPrice();
            infoItem.ProductItem = order.ProductItem;
            infoItem.setItemType(ShopOrder.INFO);
            data.add(infoItem);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (ordersPresenter != null) {
            ordersPresenter.detachView();
        }
    }

    @Override
    public void cancelOrdersSuccess() {
        showToast(getString(R.string.orders_success_cancel));
        mPageNow = 1;
        getDataList();
    }

    private void getDataList() {
        try {
            mvpPresenter.getShopOrderList(mType, user.UserId, mPageNow, srl);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void confirmOrdersSuccess() {
        showToast(getString(R.string.orders_success));
        mPageNow = 1;
        getDataList();
    }

    @Override
    public void applyRefundSuccess() {
        showToast(getString(R.string.orders_success_refund));
        mPageNow = 1;
        getDataList();
    }

    @Override
    public void stopRefresh() {
        srl.setRefreshing(false);
    }
}
