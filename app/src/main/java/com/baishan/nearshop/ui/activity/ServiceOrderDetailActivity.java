package com.baishan.nearshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.dao.MessageDao;
import com.baishan.nearshop.model.Orders;
import com.baishan.nearshop.presenter.OrdersPresenter;
import com.baishan.nearshop.presenter.PayPresenter;
import com.baishan.nearshop.presenter.ServiceOrderDetailPresenter;
import com.baishan.nearshop.utils.CashierInputFilter;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.IOrdersView;
import com.baishan.nearshop.view.IPayView;
import com.baishan.nearshop.view.IServiceOrderDetailView;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 预约详情
 */
public class ServiceOrderDetailActivity extends BaseMvpActivity<ServiceOrderDetailPresenter> implements IServiceOrderDetailView, IPayView, IOrdersView {


    @BindView(R.id.imgAddr)
    ImageView imgAddr;
    @BindView(R.id.tvConsignee)
    TextView tvConsignee;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvAddressDetail)
    TextView tvAddressDetail;
    @BindView(R.id.relAddr)
    RelativeLayout relAddr;
    @BindView(R.id.textOrderStatePrompt)
    TextView textOrderStatePrompt;
    @BindView(R.id.textOrderState)
    TextView textOrderState;
    @BindView(R.id.textOrderNum)
    TextView textOrderNum;
    @BindView(R.id.textCreateOrderTime)
    TextView textCreateOrderTime;
    @BindView(R.id.textPayType)
    TextView textPayType;
    @BindView(R.id.tvLeave)
    TextView tvLeave;
    @BindView(R.id.tvCompany)
    TextView tvCompany;
    @BindView(R.id.tvLicense)
    TextView tvLicense;
    @BindView(R.id.tvIdentity)
    TextView tvIdentity;
    @BindView(R.id.ivImg)
    ImageView ivImg;
    @BindView(R.id.tvDesc)
    TextView tvDesc;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.btnOrder)
    Button btnOrder;
    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.rgPayType)
    RadioGroup rgPayType;
    @BindView(R.id.rbAlipay)
    RadioButton rbAlipay;
    @BindView(R.id.rbWxPay)
    RadioButton rbWxPay;
    @BindView(R.id.llPay)
    LinearLayout llPay;
    @BindView(R.id.llPayInfo)
    LinearLayout llPayInfo;
    @BindView(R.id.tvPayType)
    TextView tvPayType;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.btnLeft)
    Button btnLeft;
    @BindView(R.id.btnRight)
    Button btnRight;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;

    @BindView(R.id.llCourierInfo)
    LinearLayout llCourierInfo;
    @BindView(R.id.tvCourierName)
    TextView tvCourierName;
    @BindView(R.id.tvCourierPhone)
    TextView tvCourierPhone;
    @BindView(R.id.tvCourierRemark)
    TextView tvCourierRemark;

    private PayPresenter payPresenter;
    private String payType = "余额";
    private Orders orders;
    private String orderNo;
    private String price;
    private OrdersPresenter ordersPresenter;

    @Override
    protected ServiceOrderDetailPresenter createPresenter() {
        ordersPresenter = new OrdersPresenter(this);
        payPresenter = new PayPresenter(this);
        return new ServiceOrderDetailPresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_service_order_detail);
    }

    @Override
    protected void bindViews() {
        ButterKnife.bind(this);
        btnOrder.setVisibility(View.GONE);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initTitle("预约详情");
        registerEvent();
        orderNo = getIntent().getStringExtra(ConstantValue.ORDERNO);
        if (TextUtils.isEmpty(orderNo)) {
            orders = (Orders) getIntent().getSerializableExtra(ConstantValue.DATA);
            orderNo = orders.OrderNo;
            setOrderInfo(orders);
        } else {
            mvpPresenter.getOrderInfo(orderNo);
            MessageDao.read(orderNo);
        }
    }

    private void setOrderInfo(Orders orders) {
        llPay.setVisibility(View.GONE);
        llPayInfo.setVisibility(View.GONE);
        llBottom.setVisibility(View.GONE);
        tvConsignee.setText(String.format(getString(R.string.address_consignee), orders.Contact));
        tvPhone.setText(orders.Phone);
        tvAddress.setText(String.format(getString(R.string.address_detail), orders.concatAddress()));
        tvAddressDetail.setText(orders.Address);
        textOrderState.setText(orders.StateName);
        textOrderNum.setText(String.format(getString(R.string.order_code), orders.OrderNo));
        textCreateOrderTime.setText(String.format(getString(R.string.order_date), orders.CreateTime));
        tvLeave.setText(String.format(getString(R.string.order_message_leave), orders.Remarks));
        if (orders.CourierId > 0) {
            llCourierInfo.setVisibility(View.VISIBLE);
            tvCourierName.setText("姓名：" + orders.CourierName);
            tvCourierPhone.setText("电话：" + orders.CourierPhone);
            if (!TextUtils.isEmpty(orders.CourierRemarks))
                tvCourierRemark.setText("备注：" + orders.CourierRemarks);
        }
        tvCompany.setText(orders.Name);
        ImageLoaderUtils.displayImage(orders.getImage(), ivImg);
        tvLicense.setVisibility(orders.HasLicense == 1 ? View.VISIBLE : View.GONE);
        tvIdentity.setVisibility(orders.HasIdentity == 1 ? View.VISIBLE : View.GONE);
        tvDesc.setText(orders.Title);
        tvPrice.setText(orders.Price);
        switch (orders.OrderState) {
            case ConstantValue.ORDER_STATE_RECEIVIED_ORDER:
            case ConstantValue.ORDER_STATE_START_WORK:
                llBottom.setVisibility(View.VISIBLE);
                btnLeft.setVisibility(View.VISIBLE);
                btnLeft.setText(getString(R.string.orders_state_cancel));
                btnLeft.setOnClickListener(v -> ordersPresenter.cancelOrders(orders.OrderNo, user.UserId));
                break;
            case ConstantValue.ORDER_STATE_FINISH_WORK://30完成工作
                llPay.setVisibility(View.VISIBLE);
                //对输入的金额进行过滤
                InputFilter[] filters = {new CashierInputFilter()};
                etMoney.setFilters(filters);
                llBottom.setVisibility(View.VISIBLE);
                btnLeft.setVisibility(View.VISIBLE);
                btnLeft.setText(getString(R.string.orders_state_cancel));
                btnRight.setVisibility(View.VISIBLE);//确认支付
                btnLeft.setOnClickListener(v -> ordersPresenter.cancelOrders(orders.OrderNo, user.UserId));
                btnRight.setOnClickListener(v -> {
                    price = etMoney.getText().toString();
                    payPresenter.getPayInfo(orders.OrderNo, user.UserId, payType, price);
                });
                break;
            case ConstantValue.ORDER_STATE_PAID://35已付款
                llPayInfo.setVisibility(View.VISIBLE);
                tvPayType.setText(String.format(getString(R.string.order_pay_type), orders.PayType));
                tvMoney.setText(String.format(getString(R.string.order_pay_money), orders.OrderPrice));
                llBottom.setVisibility(View.VISIBLE);
                btnRight.setVisibility(View.VISIBLE);
                btnRight.setText(getString(R.string.orders_state_confirm));
                btnRight.setOnClickListener(v -> ordersPresenter.confirmOrders(orders.OrderNo, user.UserId));
                if (orders.IsRefund == 1) {
                    btnLeft.setVisibility(View.VISIBLE);
                    btnLeft.setText(getString(R.string.orders_state_refund));
                    btnLeft.setOnClickListener(v -> ordersPresenter.applyRefund(orders.OrderNo, user.UserId));
                }
                break;
            case ConstantValue.ORDER_STATE_FINISHED:
                setPayInfo(orders);
                if (orders.IsRefund == 1) {
                    llBottom.setVisibility(View.VISIBLE);
                    btnLeft.setVisibility(View.VISIBLE);
                    btnLeft.setText(getString(R.string.orders_state_refund));
                    btnLeft.setOnClickListener(v -> ordersPresenter.applyRefund(orders.OrderNo, user.UserId));
                }
                break;
            case ConstantValue.ORDER_STATE_APPLY_REFUND:
                setPayInfo(orders);
                break;
            case ConstantValue.ORDER_STATE_REJECT_REFUND:
                setPayInfo(orders);
                if (orders.IsRefund == 1) {
                    llBottom.setVisibility(View.VISIBLE);
                    btnLeft.setVisibility(View.VISIBLE);
                    btnLeft.setText(getString(R.string.orders_state_refund));
                    btnLeft.setOnClickListener(v -> ordersPresenter.applyRefund(orders.OrderNo, user.UserId));
                }
                break;
            case ConstantValue.ORDER_STATE_REFUND_FINISHED:
                setPayInfo(orders);
                break;
        }
    }

    private void setPayInfo(Orders orders) {
        llPayInfo.setVisibility(View.VISIBLE);
        tvPayType.setText(String.format(getString(R.string.order_pay_type), orders.PayType));
        tvMoney.setText(String.format(getString(R.string.order_pay_money), orders.OrderPrice));
    }

    @Override
    protected void setListener() {
        rgPayType.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbBalPay:
                    payType = "余额";
                    break;
                case R.id.rbAlipay:
                    payType = "支付宝";
                    break;
                case R.id.rbWxPay:
                    payType = "微信";
                    break;
            }
        });
    }

    @Override
    public void getPayInfoSuccess() {

    }

    @Override
    public void balancePaySuccess() {
        paySuccess();
    }

    @Subscribe
    public void onEvent(Integer msg) {
        if (msg == 8888) {
            paySuccess();
        }
    }

    private void paySuccess() {
        post(new Notice(ConstantValue.MSG_TYPE_UPDATE_ORDERS_SERVICE));
        Intent it = new Intent(mContext, OrderMessageActivity.class);
        it.putExtra(ConstantValue.TYPE, OrderMessageActivity.INTENT_SERVICE);
        it.putExtra(ConstantValue.ORDERNO, orders.OrderNo);
        it.putExtra(ConstantValue.PRICE, Double.parseDouble(price));
        startActivity(it);
        finish();
    }


    @Override
    public void getOrderInfoSuccess(Orders orders) {
        this.orders = orders;
        setOrderInfo(orders);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (payPresenter != null) {
            payPresenter.detachView();
        }
        if (ordersPresenter != null) {
            ordersPresenter.detachView();
        }
    }

    @Override
    public void cancelOrdersSuccess() {
        commonSuccess();
    }

    @Override
    public void confirmOrdersSuccess() {
        commonSuccess();
    }

    @Override
    public void applyRefundSuccess() {
        commonSuccess();
    }

    private void commonSuccess() {
        showToast(getString(R.string.orders_success));
        post(new Notice(ConstantValue.MSG_TYPE_UPDATE_ORDERS_SERVICE));
        mvpPresenter.getOrderInfo(orders.OrderNo);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String on = intent.getStringExtra(ConstantValue.ORDERNO);
        if (on.equals(orderNo)) {
            mvpPresenter.getOrderInfo(orders.OrderNo);
        } else {
            finish();
            startActivity(intent);
        }
    }
}
