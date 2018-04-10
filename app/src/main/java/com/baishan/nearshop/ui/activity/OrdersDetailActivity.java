package com.baishan.nearshop.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.model.ConfirmOrderItem;
import com.baishan.nearshop.model.OrdersPayInfo;
import com.baishan.nearshop.presenter.OrdersDetailPresenter;
import com.baishan.nearshop.presenter.OrdersPresenter;
import com.baishan.nearshop.presenter.PayPresenter;
import com.baishan.nearshop.ui.adapter.ConfirmOrderAdapter;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.utils.OrdersConstants;
import com.baishan.nearshop.utils.PermissionHelper;
import com.baishan.nearshop.view.IOrdersDetailView;
import com.baishan.nearshop.view.IOrdersView;
import com.baishan.nearshop.view.IPayView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdersDetailActivity extends BaseMvpActivity<OrdersDetailPresenter> implements IOrdersDetailView, IOrdersView,IPayView {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvCourierInfo)
    TextView tvCourierInfo;
    @BindView(R.id.btnLeft)
    Button btnLeft;
    @BindView(R.id.btnRight)
    Button btnRight;
    @BindView(R.id.llOperate)
    LinearLayout llOperate;

    private View header;
    private TextView textOrderState;
    private TextView textOrderNum;
    private TextView textCreateOrderTime;
    private TextView textPayType;
    private TextView tvLeave;
    private TextView textOrderStatePrompt;

    private String orderNo;
    private ConfirmOrderAdapter adapter;
    private List<ConfirmOrderItem> data = new ArrayList<>();

    private OrdersPresenter ordersPresenter;
    private PayPresenter payPresenter;


    @Override
    protected OrdersDetailPresenter createPresenter() {
        ordersPresenter = new OrdersPresenter(this);
        payPresenter = new PayPresenter(this);
        return new OrdersDetailPresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_orders_detail);
    }

    @Override
    protected void bindViews() {
        ButterKnife.bind(this);
        header = View.inflate(mContext, R.layout.layout_orders_info, null);
        textOrderState = (TextView) header.findViewById(R.id.textOrderState);
        textOrderNum = (TextView) header.findViewById(R.id.textOrderNum);
        textCreateOrderTime = (TextView) header.findViewById(R.id.textCreateOrderTime);
        textPayType = (TextView) header.findViewById(R.id.textPayType);
        tvLeave = (TextView) header.findViewById(R.id.tvLeave);
        textOrderStatePrompt = (TextView) header.findViewById(R.id.textOrderStatePrompt);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initTitle("订单详情");
        registerEvent();
        initCommonRecyclerView(adapter = new ConfirmOrderAdapter(data, 2), null);
        orderNo = getIntent().getStringExtra(ConstantValue.ORDERNO);
        mvpPresenter.getGoodsOrderInfo(orderNo);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void getGoodsOrderInfoSuccess(ConfirmOrderItem item) {
        setOrderInfo(item);
        adapter.addHeaderView(header);
        //地址
        ConfirmOrderItem addressItem = new ConfirmOrderItem(item);
        //当前商区下的索引
        addressItem.setItemType(ConfirmOrderItem.ADDRESS);
        data.add(addressItem);
        for (int j = 0; j < item.ProductItem.size(); j++) {
            //商品
            data.add(new ConfirmOrderItem(item.ProductItem.get(j)));
        }
        //商品下面的信息
        ConfirmOrderItem infoItem = new ConfirmOrderItem(item);
        infoItem.setItemType(ConfirmOrderItem.INFO);
        data.add(infoItem);
        adapter.notifyDataSetChanged();
        setBottomInfo(item);
    }


    private void setOrderInfo(ConfirmOrderItem item) {
        textOrderState.setText(item.StateName);
        textOrderNum.setText(String.format(getString(R.string.order_code), item.OrderNo));
        textCreateOrderTime.setText(String.format(getString(R.string.order_date), item.CreateTime));
        textPayType.setVisibility(View.VISIBLE);
        textPayType.setText(String.format(getString(R.string.order_pay_type), item.PayType));
        tvLeave.setText(String.format(getString(R.string.order_message_leave), TextUtils.isEmpty(item.Remarks) ? "" : item.Remarks));
    }

    private void setBottomInfo(ConfirmOrderItem item) {
        switch (item.OrderState) {
            case OrdersConstants.ORDER_STATE_WAIT_PAY:
                llOperate.setVisibility(View.VISIBLE);
                btnLeft.setVisibility(View.VISIBLE);
                btnRight.setVisibility(View.VISIBLE);
                btnLeft.setText(mContext.getString(R.string.orders_state_cancel));
                btnLeft.setOnClickListener(v -> ordersPresenter.cancelGoodsOrders(item.OrderNo, user.UserId));
                btnRight.setText(mContext.getString(R.string.orders_state_pay));
                btnRight.setOnClickListener(v -> pay(item));
                break;
            case OrdersConstants.ORDER_STATE_PAID:
            case OrdersConstants.ORDER_STATE_MATCH_SENDER:
                llOperate.setVisibility(View.VISIBLE);
                btnLeft.setVisibility(View.VISIBLE);
                btnLeft.setText(mContext.getString(R.string.orders_state_cancel));
                btnLeft.setOnClickListener(v -> ordersPresenter.cancelGoodsOrders(item.OrderNo, user.UserId));
                break;
            case OrdersConstants.ORDER_STATE_WAIT_SENDER:

                break;
            case OrdersConstants.ORDER_STATE_RECEIVIED_ORDER:
            case OrdersConstants.ORDER_STATE_SENDING:
                llOperate.setVisibility(View.VISIBLE);
                tvCourierInfo.setVisibility(View.VISIBLE);
                tvCourierInfo.setText(getString(R.string.orders_sender_phone, item.CourierName, item.CourierPhone));
                btnRight.setVisibility(View.VISIBLE);
                btnRight.setText(mContext.getString(R.string.contact));
                btnRight.setOnClickListener(v -> PermissionHelper.call(mContext, item.CourierPhone));
                break;
            case OrdersConstants.ORDER_STATE_DELIVERED:
                llOperate.setVisibility(View.VISIBLE);
                tvCourierInfo.setVisibility(View.VISIBLE);
                tvCourierInfo.setText(getString(R.string.orders_sender_phone, item.CourierName, item.CourierPhone));
                btnRight.setVisibility(View.VISIBLE);
                btnRight.setText(mContext.getString(R.string.orders_state_receive));
                btnRight.setOnClickListener(v -> ordersPresenter.confirmGoodsOrders(item.OrderNo, user.UserId));
                break;
            case OrdersConstants.ORDER_STATE_FINISHED:

                break;
            case OrdersConstants.ORDER_STATE_CANCEL_ORDER:

                break;
            case OrdersConstants.ORDER_STATE_APPLY_REFUND:

                break;
            case OrdersConstants.ORDER_STATE_REJECT_REFUND:
                if (item.IsRefund == 1) {
                    llOperate.setVisibility(View.VISIBLE);
                    tvCourierInfo.setVisibility(View.VISIBLE);
                    tvCourierInfo.setText(getString(R.string.orders_sender_phone, item.CourierName, item.CourierPhone));
                    btnRight.setVisibility(View.VISIBLE);
                    btnRight.setText(mContext.getString(R.string.orders_state_refund));
                    btnRight.setOnClickListener(v -> ordersPresenter.applyGoodsRefund(item.OrderNo, user.UserId));
                }
                break;
            case OrdersConstants.ORDER_STATE_REFUND_FINISHED:

                break;
        }
    }

    private void pay(ConfirmOrderItem item) {
        OrdersPayInfo payInfo = new OrdersPayInfo();
//        payInfo.UserId = user.UserId;
//        payInfo.OrderNo = item.OrderNo;
//        payInfo.PayType = item.PayType;
//        payInfo.Remarks = item.Remarks;
//        payInfo.CouponId = item.CouponId;
//        payInfo.address = item.AddressInfo;
        payPresenter.getPayInfo(payInfo);
    }


    @Override
    public void cancelOrdersSuccess() {
        post(new Notice(ConstantValue.MSG_TYPE_UPDATE_SHOP_ORDER));
        mvpPresenter.getGoodsOrderInfo(orderNo);
    }

    @Override
    public void confirmOrdersSuccess() {
        post(new Notice(ConstantValue.MSG_TYPE_UPDATE_SHOP_ORDER));
        mvpPresenter.getGoodsOrderInfo(orderNo);
    }

    @Override
    public void applyRefundSuccess() {
        post(new Notice(ConstantValue.MSG_TYPE_UPDATE_SHOP_ORDER));
        mvpPresenter.getGoodsOrderInfo(orderNo);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ordersPresenter != null) {
            ordersPresenter.detachView();
        }
        if (payPresenter != null) {
            payPresenter.detachView();
        }
    }

    @Subscribe
    public void onEvent(Integer msg) {
        if (msg == 8888) {
            post(new Notice(ConstantValue.MSG_TYPE_UPDATE_SHOP_ORDER));
//            Intent it = new Intent(mContext, OrderMessageActivity.class);
//            it.putExtra(ConstantValue.TYPE, OrderMessageActivity.INTENT_GOODS);
//            it.putExtra(ConstantValue.ORDERNO, orders.OrderNo);
//            it.putExtra(ConstantValue.PRICE, price);
//            startActivity(it);
//            finish();
        }
    }

    @Override
    public void getPayInfoSuccess() {

    }

    @Override
    public void balancePaySuccess() {

    }
}
