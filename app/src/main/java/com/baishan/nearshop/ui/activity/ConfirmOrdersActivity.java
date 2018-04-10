package com.baishan.nearshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.dao.MessageDao;
import com.baishan.nearshop.model.ConfirmOrderItem;
import com.baishan.nearshop.model.Coupon;
import com.baishan.nearshop.model.OrdersParam;
import com.baishan.nearshop.model.OrdersPayInfo;
import com.baishan.nearshop.presenter.ConfirmOrdersPresenter;
import com.baishan.nearshop.presenter.OrdersPresenter;
import com.baishan.nearshop.presenter.PayPresenter;
import com.baishan.nearshop.ui.adapter.ConfirmOrderAdapter;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.utils.OrdersConstants;
import com.baishan.nearshop.utils.PermissionHelper;
import com.baishan.nearshop.view.IConfirmOrdersView;
import com.baishan.nearshop.view.IOrdersView;
import com.baishan.nearshop.view.IPayView;
import com.tencent.mm.sdk.modelpay.PayReq;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ConfirmOrdersActivity extends BaseMvpActivity<ConfirmOrdersPresenter> implements IConfirmOrdersView, IOrdersView, IPayView {

    public static final int INTENT_SHOPCAR = 1;
    public static final int INTENT_DETAIL = 2;
    public static final int INTENT_ORDERS_LIST = 3;
    private int type;
    private List<ConfirmOrderItem> datas = new ArrayList<>();

    private Button btnCommit;
    private ConfirmOrderAdapter adapter;
    private String mPayType = "支付宝";
    private TextView tvLeftMoney;
    private TextView tvPayHint;
    private TextView tvTotalPrice;
    private EditText cod_mark;

    @BindView(R.id.tvCourierInfo)
    TextView tvCourierInfo;
    @BindView(R.id.btnLeft)
    Button btnLeft;
    @BindView(R.id.btnRight)
    Button btnRight;
    @BindView(R.id.llOperate)
    LinearLayout llOperate;
    @BindView(R.id.relPay)
    RelativeLayout relPay;

    private View header;
    private TextView textOrderState;
    private TextView textOrderNum;
    private TextView textCreateOrderTime;
    private TextView textPayType;
    private TextView tvLeave;
    private TextView textOrderStatePrompt;

    /**
     * 服务器获取的原始数据
     */
    private List<ConfirmOrderItem> mStartData;
    private String orderNo;

    private OrdersPresenter ordersPresenter;
    private PayPresenter payPresenter;
    private View footerView;

    private double otherPrice;

    @Override
    protected ConfirmOrdersPresenter createPresenter() {
        ordersPresenter = new OrdersPresenter(this);
        payPresenter = new PayPresenter(this);
        return new ConfirmOrdersPresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_confirm_orders);
    }

    @Override
    protected void bindViews() {
        ButterKnife.bind(this);
        btnCommit = (Button) findViewById(R.id.btnCommit);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);
        footerView = View.inflate(this, R.layout.footer_confirm_order, null);
        LinearLayout ll_pay_alipay = (LinearLayout) footerView.findViewById(R.id.ll_pay_alipay);
        LinearLayout ll_pay_weixin = (LinearLayout) footerView.findViewById(R.id.ll_pay_weixin);
        CheckBox cb_pay_alipay = (CheckBox) footerView.findViewById(R.id.cb_pay_alipay);
        CheckBox cb_pay_weixin = (CheckBox) footerView.findViewById(R.id.cb_pay_weixin);
        tvLeftMoney = (TextView) footerView.findViewById(R.id.tvLeftMoney);
        tvPayHint = (TextView) footerView.findViewById(R.id.tvPayHint);
        cod_mark = (EditText) footerView.findViewById(R.id.cod_mark);
        header = View.inflate(mContext, R.layout.layout_orders_info, null);
        textOrderState = (TextView) header.findViewById(R.id.textOrderState);
        textOrderNum = (TextView) header.findViewById(R.id.textOrderNum);
        textCreateOrderTime = (TextView) header.findViewById(R.id.textCreateOrderTime);
        textPayType = (TextView) header.findViewById(R.id.textPayType);
        tvLeave = (TextView) header.findViewById(R.id.tvLeave);
        textOrderStatePrompt = (TextView) header.findViewById(R.id.textOrderStatePrompt);
        ll_pay_alipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_pay_alipay.setChecked(true);
                cb_pay_weixin.setChecked(false);
                mPayType = "支付宝";
                setPayHint();
            }
        });
        ll_pay_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_pay_weixin.setChecked(true);
                cb_pay_alipay.setChecked(false);
                mPayType = "微信";
                setPayHint();
            }
        });
        footerView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
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
        if (EventBus.getDefault().isRegistered(adapter))
            EventBus.getDefault().unregister(adapter);

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        registerEvent();
        type = getIntent().getIntExtra(ConstantValue.TYPE, INTENT_SHOPCAR);
        if (type == INTENT_ORDERS_LIST) {
            initTitle("订单详情");
            adapter = new ConfirmOrderAdapter(datas, 2);
        } else {
            initTitle("提交订单");
            adapter = new ConfirmOrderAdapter(datas);
        }
        initCommonRecyclerView(adapter, null);
        if (type == INTENT_SHOPCAR) {
            String shopcars = getIntent().getStringExtra(ConstantValue.DATA);
            mvpPresenter.getPreOrderInfo(shopcars, user.UserId);
        } else if (type == INTENT_DETAIL) {
            OrdersParam param = (OrdersParam) getIntent().getSerializableExtra(ConstantValue.DATA);
            mvpPresenter.getPreOrderInfo(param);
        } else {
            relPay.setVisibility(View.GONE);
            orderNo = getIntent().getStringExtra(ConstantValue.ORDERNO);
            mvpPresenter.getGoodsOrderInfo(orderNo);
            MessageDao.read(orderNo);
        }
    }

    @Subscribe
    public void onEvent(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_SELECTED_COUPON_FROM_ADAPTER) {
            Coupon coupon = (Coupon) notice.content;
            ConfirmOrderItem item = (ConfirmOrderItem) notice.content1;
            mRealPrice = mTempRealPrice;
            mLastBalance = mTempLastBalance;
            mRealPrice -= coupon.Price;
            //-----------------------------------
            if (mLastBalance > mRealPrice)
                mLastBalance = mRealPrice;
            else {
                otherPrice = mRealPrice - mLastBalance;
            }
            tvTotalPrice.setText(String.format(getString(R.string.price1), mRealPrice));
            tvLeftMoney.setText("-" + String.format("%.2f", mLastBalance));
            setPayHint();
            //重新赋值
            mStartData.get(item.ListIndex).CouponId = coupon.Id;

        } else if (notice.type == ConstantValue.MSG_TYPE_SELECTED_ADDRESS_FROM_ADAPTER) {
            //重新赋值
            ConfirmOrderItem item = (ConfirmOrderItem) notice.content;
            mStartData.get(item.ListIndex).AddressInfo = item.AddressInfo;
        }
    }

    @Subscribe
    public void onPaySuccess(Integer msg) {
        if (msg == 8888) {
            paySuccess();
        }
    }

    private void paySuccess() {
        if (type == INTENT_ORDERS_LIST) {
            post(new Notice(ConstantValue.MSG_TYPE_UPDATE_SHOP_ORDER));
        } else if (type == INTENT_SHOPCAR) {
            post(new Notice(ConstantValue.MSG_TYPE_SHOPCAR_UPDATE));
        }
        Intent it = new Intent(mContext, OrderMessageActivity.class);
        it.putExtra(ConstantValue.TYPE, OrderMessageActivity.INTENT_GOODS);
        it.putExtra(ConstantValue.ORDERNO, orderNo);
        it.putExtra(ConstantValue.PRICE, mRealPrice);
        startActivity(it);
        finish();
    }


    @Override
    protected void setListener() {
        if (type != INTENT_ORDERS_LIST) {
            btnCommit.setOnClickListener(v -> {
//            intent2Activity(OrderMessageActivity.class)
                for (int i = 0; i < datas.size(); i++) {
                    ConfirmOrderItem confirmOrderItem = datas.get(i);
                    if (!TextUtils.isEmpty(confirmOrderItem.Remarks)) {
                        mStartData.get(confirmOrderItem.ListIndex).Remarks = confirmOrderItem.Remarks;
                    }
                }
                mStartData.get(0).PayType = mPayType;
                if (type == INTENT_SHOPCAR) {
                    mvpPresenter.generateOrder(user.UserId, mStartData);
                } else {
                    mvpPresenter.generateFastOrder(user.UserId, mStartData);
                }
            });
        }
    }

    @Override
    public void getPreOrderInfoSuccess(List<ConfirmOrderItem> response) {
        datas.clear();
        mStartData = response;
        generateOrderItem(response);
        adapter.notifyDataSetChanged();
        if (type == INTENT_ORDERS_LIST) {
            if (response.get(0).OrderState == OrdersConstants.ORDER_STATE_WAIT_PAY) {
                mvpPresenter.getLastBalance(user.LoginToken);
            }
            setOrderInfo(response.get(0));
            adapter.addHeaderView(header);
            setBottomInfo(response.get(0));
        } else {
            mvpPresenter.getLastBalance(user.LoginToken);
            adapter.addFooterView(footerView);
        }
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
        relPay.setVisibility(View.GONE);
        llOperate.setVisibility(View.GONE);
        switch (item.OrderState) {
            case OrdersConstants.ORDER_STATE_WAIT_PAY://0待支付
                adapter.addFooterView(footerView);
                relPay.setVisibility(View.VISIBLE);
                btnCommit.setText(getString(R.string.orders_state_pay));
                btnCommit.setOnClickListener(v -> pay(item));
                break;
            case OrdersConstants.ORDER_STATE_PAID://10已付款
                llOperate.setVisibility(View.VISIBLE);
                btnLeft.setVisibility(View.GONE);
//                btnLeft.setText(mContext.getString(R.string.orders_state_cancel));
//                btnLeft.setOnClickListener(v -> ordersPresenter.cancelGoodsOrders(item.OrderNo, user.UserId));
                break;
            case OrdersConstants.ORDER_STATE_MATCH_SENDER://20分配派送员
                llOperate.setVisibility(View.VISIBLE);
                btnLeft.setVisibility(View.GONE);
//                btnLeft.setText(mContext.getString(R.string.orders_state_cancel));
//                btnLeft.setOnClickListener(v -> ordersPresenter.cancelGoodsOrders(item.OrderNo, user.UserId));
                break;
            case OrdersConstants.ORDER_STATE_WAIT_SENDER://25等待派送员操作

                break;
            case OrdersConstants.ORDER_STATE_RECEIVIED_ORDER://已接单
            case OrdersConstants.ORDER_STATE_SENDING://40配送中
                llOperate.setVisibility(View.VISIBLE);
                tvCourierInfo.setVisibility(View.VISIBLE);
                tvCourierInfo.setText(getString(R.string.orders_sender_phone, item.CourierName, item.CourierPhone));
                btnRight.setVisibility(View.VISIBLE);
                btnRight.setText(mContext.getString(R.string.contact));
                btnRight.setOnClickListener(v -> PermissionHelper.call(mContext, item.CourierPhone));
                break;
            case OrdersConstants.ORDER_STATE_DELIVERED: //45已送达
                llOperate.setVisibility(View.VISIBLE);
                //tvCourierInfo.setVisibility(View.VISIBLE);
                btnLeft.setVisibility(View.VISIBLE);
                btnLeft.setText(mContext.getString(R.string.orders_state_refund));
                btnLeft.setOnClickListener(v -> ordersPresenter.applyGoodsRefund(item.OrderNo, user.UserId));
                //tvCourierInfo.setText(getString(R.string.orders_sender_phone, item.CourierName, item.CourierPhone));
                btnRight.setVisibility(View.VISIBLE);
                btnRight.setText(mContext.getString(R.string.orders_state_receive));
                btnRight.setOnClickListener(v -> ordersPresenter.confirmGoodsOrders(item.OrderNo, user.UserId));
                break;
            case OrdersConstants.ORDER_STATE_FINISHED://50已完成
                llOperate.setVisibility(View.VISIBLE);
                tvCourierInfo.setVisibility(View.VISIBLE);
                tvCourierInfo.setText(getString(R.string.orders_sender_phone, item.CourierName, item.CourierPhone));

                break;
            case OrdersConstants.ORDER_STATE_CANCEL_ORDER://55已取消

                break;
            case OrdersConstants.ORDER_STATE_APPLY_REFUND: // 60申请退款
                llOperate.setVisibility(View.VISIBLE);
                tvCourierInfo.setVisibility(View.VISIBLE);
                tvCourierInfo.setText(getString(R.string.orders_sender_phone, item.CourierName, item.CourierPhone));
                btnRight.setVisibility(View.VISIBLE);
                btnRight.setText(mContext.getString(R.string.orders_state_give_refund));
                btnRight.setOnClickListener(v -> ordersPresenter.confirmGoodsOrders(item.OrderNo, user.UserId));
                break;
            case OrdersConstants.ORDER_STATE_REJECT_REFUND://65拒绝退款
                if (item.IsRefund == 1) {
                    llOperate.setVisibility(View.VISIBLE);
                    tvCourierInfo.setVisibility(View.VISIBLE);
                    tvCourierInfo.setText(getString(R.string.orders_sender_phone, item.CourierName, item.CourierPhone));
                    btnRight.setVisibility(View.VISIBLE);
                    btnRight.setText(mContext.getString(R.string.orders_state_refund));//申请退款
                    btnLeft.setVisibility(View.VISIBLE);
                    btnLeft.setText(mContext.getString(R.string.orders_state_receive));//确认收获
                    btnRight.setOnClickListener(v -> ordersPresenter.applyGoodsRefund(item.OrderNo, user.UserId));
                    btnLeft.setOnClickListener(v -> ordersPresenter.confirmGoodsOrders(item.OrderNo, user.UserId));
                }
                break;
            case OrdersConstants.ORDER_STATE_REFUND_FINISHED: //70退款成功
                llOperate.setVisibility(View.VISIBLE);
                btnLeft.setVisibility(View.INVISIBLE);
                tvCourierInfo.setVisibility(View.VISIBLE);
                tvCourierInfo.setText(getString(R.string.orders_sender_phone, item.CourierName, item.CourierPhone));
                break;
        }
    }

    private void pay(ConfirmOrderItem item) {
        OrdersPayInfo payInfo = new OrdersPayInfo();
        payInfo.UserId = user.UserId;
        payInfo.OrderNo = item.OrderNo;
        payInfo.PayType = mPayType;
        payInfo.Remarks = item.Remarks;
        payInfo.CouponId = item.CouponId;
        payInfo.address = item.AddressInfo.get(0);
        payPresenter.getPayInfo(payInfo);
    }

    /**
     * 最后一次的余额
     */
    private double mLastBalance;
    /**
     * 临时存储的余额
     */
    private double mTempLastBalance;

    @Override
    public void getLastBalanceSuccess(String response) {
        double balance = Double.parseDouble(response);
        if (balance > mRealPrice)
            mLastBalance = mRealPrice;
        else {
            mLastBalance = balance;
            otherPrice = mRealPrice - balance;
        }
        mTempLastBalance = mLastBalance;
        tvLeftMoney.setText("-" + String.format("%.2f", mLastBalance));
        setPayHint();
    }

    private void setPayHint() {
        tvPayHint.setText(String.format(getString(R.string.orders_pay_hint), mPayType, otherPrice, mLastBalance));
    }

    @Override
    public void commitOrderSuccess(PayReq response) {
        if (TextUtils.isEmpty(response.appId) && TextUtils.isEmpty(response.extData)) {
            showToast("付款成功!");
            paySuccess();
        }

    }


    /**
     * 实付价格
     */
    private double mRealPrice;
    /**
     * 临时存储
     */
    private double mTempRealPrice;
    /**
     * 派送费
     */
    private double mPostagePrice;
    /**
     * 优惠券
     */
    private double mCouponPrice;

    private void generateOrderItem(List<ConfirmOrderItem> response) {
        mRealPrice = 0;
        mPostagePrice = 0;
        for (int i = 0; i < response.size(); i++) {
            ConfirmOrderItem confirmOrderItem = response.get(i);
            mRealPrice += confirmOrderItem.OrderPrice;
            mPostagePrice += confirmOrderItem.CouponPrice;
            //地址
            ConfirmOrderItem addressItem = new ConfirmOrderItem(confirmOrderItem);
            //当前商区下的索引
            addressItem.ListIndex = i;
            addressItem.setItemType(ConfirmOrderItem.ADDRESS);
            datas.add(addressItem);
            for (int j = 0; j < confirmOrderItem.ProductItem.size(); j++) {
                //商品
                datas.add(new ConfirmOrderItem(confirmOrderItem.ProductItem.get(j)));
            }
            //商品下面的信息
            ConfirmOrderItem infoItem = new ConfirmOrderItem(confirmOrderItem);
            infoItem.RefundPrice = confirmOrderItem.refundPrice();
            infoItem.ListIndex = i;
            infoItem.setItemType(ConfirmOrderItem.INFO);
            datas.add(infoItem);
        }
        mRealPrice += mPostagePrice;
        mTempRealPrice = mRealPrice;
        tvTotalPrice.setText(String.format(getString(R.string.price1), mRealPrice));
    }

    @Override
    public void cancelOrdersSuccess() {
        commonSuccess();
    }

    private void commonSuccess() {
        showToast(getString(R.string.orders_success));
        post(new Notice(ConstantValue.MSG_TYPE_UPDATE_SHOP_ORDER));
        mvpPresenter.getGoodsOrderInfo(orderNo);
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
    public void getPayInfoSuccess() {

    }

    @Override
    public void balancePaySuccess() {

    }

}
