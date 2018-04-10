package com.baishan.nearshop.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.view.EmptyView;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpFragment;
import com.baishan.nearshop.model.Orders;
import com.baishan.nearshop.presenter.EasyOrdersPresenter;
import com.baishan.nearshop.presenter.OrdersPresenter;
import com.baishan.nearshop.ui.activity.ServiceOrderDetailActivity;
import com.baishan.nearshop.ui.adapter.EasyOrdersAdapter;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.IEasyOrdersView;
import com.baishan.nearshop.view.IOrdersView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/21 0021.
 * 便民订单
 */
public class EasyOrdersFragment extends BaseMvpFragment<EasyOrdersPresenter> implements IEasyOrdersView, IOrdersView {

    private View recyclerView;
    private SwipeRefreshLayout srl;
    private List<Orders> data = new ArrayList<>();
    private EasyOrdersAdapter adapter;
    private String mType;
    private int position;

    private OrdersPresenter ordersPresenter;

    @Override
    protected EasyOrdersPresenter createPresenter() {
        ordersPresenter = new OrdersPresenter(this);
        return new EasyOrdersPresenter(this);
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_refresh_recyclerview, null);
    }

    @Override
    protected void bindViews(View view) {
        recyclerView = get(R.id.recyclerView);
        srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
    }

    @Override
    protected void processLogic() {
        mType = getArguments().getString(ConstantValue.TYPE);
        initCommonRecyclerView(adapter = new EasyOrdersAdapter(data), null);
        adapter.setEmptyView(new EmptyView(mContext));
        adapter.openLoadMore(10, true);
        registerEvent();
    }

    private int mPageNow = 1;

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        mType = getArguments().getString(ConstantValue.TYPE);
        getData();
    }

    private void getData() {
        mvpPresenter.getServiceOrderList(mType, user.UserId, mPageNow, srl);
    }

    @Override
    protected void setListener() {
        adapter.setOnLoadMoreListener(() -> {
            mPageNow++;
            getData();
        });
        adapter.setOnRecyclerViewItemClickListener((view, i) -> {
            position = i;
            Intent it = new Intent(mContext, ServiceOrderDetailActivity.class);
            it.putExtra(ConstantValue.DATA, data.get(i));
            mContext.startActivity(it);
        });
        srl.setOnRefreshListener(() -> {
            mPageNow = 1;
            getData();
        });
        adapter.setOnRecyclerViewItemChildClickListener((baseQuickAdapter, view, i) -> {
            position = i;
            Orders orders = data.get(i);
            if (view.getId() == R.id.btnLeft) {
                switch (orders.OrderState) {
                    case ConstantValue.ORDER_STATE_PAID://35 已付款
                    case ConstantValue.ORDER_STATE_REJECT_REFUND://65拒绝退款
                        //确认无误
                        ordersPresenter.confirmOrders(orders.OrderNo, user.UserId);
                        break;
                }
            } else if (view.getId() == R.id.btnRight) {
                switch (orders.OrderState) {
                    case ConstantValue.ORDER_STATE_MATCH_SENDER: //0分配派送员
                    case ConstantValue.ORDER_STATE_RESERVED: //10已预约
                    case ConstantValue.ORDER_STATE_RECEIVIED_ORDER: //20已接单
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("提示")
                                .setNegativeButton("取消", null)
                                .setMessage("您确定要取消订单吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //取消订单
                                        ordersPresenter.cancelOrders(orders.OrderNo, user.UserId);
                                    }
                                }).create().show();
                        break;
                    case ConstantValue.ORDER_STATE_PAID://35 已付款
                    case ConstantValue.ORDER_STATE_FINISHED://40已完成
                        //申请退款
                        ordersPresenter.applyRefund(orders.OrderNo, user.UserId);
                        break;
                    case ConstantValue.ORDER_STATE_REJECT_REFUND://65拒绝退款
                        //拒绝退款
                        ordersPresenter.applyRefund(orders.OrderNo, user.UserId);
                        break;
                    case ConstantValue.ORDER_STATE_APPLY_REFUND://60申请退款:
                        //确认无误
                        ordersPresenter.confirmOrders(orders.OrderNo, user.UserId);
                        break;
                }
            }
        });
    }

    @Override
    public void getServiceOrderListSuccess(List<Orders> response) {
        srl.setRefreshing(false);
        if (mPageNow == 1) {
            adapter.getData().clear();
            adapter.notifyDataSetChanged();
        }
        if (response.size() > 0) {
            adapter.notifyDataChangedAfterLoadMore(response, mPageNow < response.get(0).PageCount);
        }
    }

    @Override
    public void cancelOrdersSuccess() {
        commonSuccess();
    }

    private void commonSuccess() {
        showToast(getString(R.string.orders_success));
        mPageNow = 1;
        getData();
    }

    @Override
    public void confirmOrdersSuccess() {
        commonSuccess();
    }

    @Override
    public void applyRefundSuccess() {
        commonSuccess();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (ordersPresenter != null) {
            ordersPresenter.detachView();
        }
    }

    @Subscribe
    public void onEvent(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_UPDATE_ORDERS_SERVICE) {
//            Orders o = data.get(position);
//            Orders orders = (Orders) notice.content;
//            if (o.OrderNo.equals(orders.OrderNo)) {
//                o.OrderState = orders.OrderState;
//                o.StateName = orders.StateName;
//                o.StateMessage = orders.StateMessage;
//                adapter.notifyItemChanged(position);
//            }
            mPageNow = 1;
            getData();
        } else if (notice.type == ConstantValue.MSG_TYPE_ORDERS_PAID) {
//            if (data.get(position).OrderNo.equals(notice.content)) {
//                data.remove(position);
//                adapter.notifyItemRemoved(position);
//            }
        }
    }

    @Override
    public void stopRefresh() {
        srl.setRefreshing(false);
    }
}
