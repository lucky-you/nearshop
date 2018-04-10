package com.baishan.nearshopclient.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.base.BaseMvpActivity;
import com.baishan.nearshopclient.dao.MessageDao;
import com.baishan.nearshopclient.model.Orders;
import com.baishan.nearshopclient.presenter.ServiceOrderDetailPresenter;
import com.baishan.nearshopclient.utils.ConstantValue;
import com.baishan.nearshopclient.view.IServiceOrderDetailView;


public class ServiceOrderDetailActivity extends BaseMvpActivity<ServiceOrderDetailPresenter> implements IServiceOrderDetailView {

    private RelativeLayout relAddr;
    private ImageView imgAddr;
    private TextView tvConsignee;
    private TextView tvPhone;
    private TextView tvAddress;
    private TextView textOrderState;
    private TextView textOrderNum;
    private TextView textCreateOrderTime;
    private TextView textPayType;
    private TextView tvLeave;
    private TextView textOrderStatePrompt;
    private TextView tvArea;
    private ImageView goods_img;
    private RelativeLayout layoutContent;
    private TextView goods_attr;
    private TextView goods_name;
    private TextView goods_price;
    private TextView tvPriceDesc;
    private TextView tvMoney;
    private Button btnDistribute;

    private Orders orders;
    private String orderNo;

    @Override
    protected ServiceOrderDetailPresenter createPresenter() {
        return new ServiceOrderDetailPresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_service_order_detail);
    }

    @Override
    protected void bindViews() {
        relAddr = (RelativeLayout) findViewById(R.id.relAddr);
        imgAddr = (ImageView) findViewById(R.id.imgAddr);
        tvConsignee = (TextView) findViewById(R.id.tvConsignee);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        textOrderState = (TextView) findViewById(R.id.textOrderState);
        textOrderNum = (TextView) findViewById(R.id.textOrderNum);
        textCreateOrderTime = (TextView) findViewById(R.id.textCreateOrderTime);
        textPayType = (TextView) findViewById(R.id.textPayType);
        tvLeave = (TextView) findViewById(R.id.tvLeave);
        textOrderStatePrompt = (TextView) findViewById(R.id.textOrderStatePrompt);
        tvArea = (TextView) findViewById(R.id.tvArea);
        goods_img = (ImageView) findViewById(R.id.goods_img);
        layoutContent = (RelativeLayout) findViewById(R.id.layoutContent);
        goods_attr = (TextView) findViewById(R.id.goods_attr);
        goods_name = (TextView) findViewById(R.id.goods_name);
        goods_price = (TextView) findViewById(R.id.goods_price);
        tvPriceDesc = (TextView) findViewById(R.id.tvPriceDesc);
        tvMoney = (TextView) findViewById(R.id.tvMoney);
        btnDistribute = (Button) findViewById(R.id.btnDistribute);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initTitle("预约详情");
        orderNo = getIntent().getStringExtra(ConstantValue.ORDER_NO);
        if (TextUtils.isEmpty(orderNo)) {
            orders = (Orders) getIntent().getSerializableExtra(ConstantValue.DATA);
            orderNo = orders.OrderNo;
            setOrderInfo(orders);
        } else {
            mvpPresenter.getOrderInfo(orderNo);
        }
        MessageDao.read(orderNo);
    }

    private void setOrderInfo(Orders orders) {
        tvConsignee.setText(String.format(getString(R.string.address_consignee), orders.Contact));
        tvPhone.setText(String.format(getString(R.string.address_phone), orders.Phone));
        tvAddress.setText(String.format(getString(R.string.address_detail), orders.Address));
        textOrderState.setText(orders.StateName);
        textOrderNum.setText(String.format(getString(R.string.order_code), orders.OrderNo));
        textCreateOrderTime.setText(String.format(getString(R.string.order_date), orders.CreateTime));
        textPayType.setText(String.format(getString(R.string.order_pay_type), orders.PayType==null?"无":orders.PayType));
        tvLeave.setText(String.format(getString(R.string.order_message_leave), orders.Remarks));
        tvArea.setText(orders.AreaName);
        ImageLoaderUtils.displayImage(orders.getImage(), goods_img);
        goods_name.setText(orders.Title);
        goods_attr.setText(orders.Name);
        goods_price.setText(orders.Price);
        tvMoney.setText("实付金额："+orders.OrderPrice);
        if(orders.OrderState==ConstantValue.ORDER_STATE_MATCH_SENDER){
            btnDistribute.setVisibility(View.VISIBLE);
        }
    }



    @Override
    protected void setListener() {
        btnDistribute.setOnClickListener(v -> mvpPresenter.reDistribute(orderNo, 2));
    }



    @Override
    public void getOrderInfoSuccess(Orders orders) {
        this.orders = orders;
        setOrderInfo(orders);
    }

    @Override
    public void reDistributeSuccess() {
        showToast("操作成功");
    }


    @Override
    protected void onNewIntent(Intent intent) {
        String on = intent.getStringExtra(ConstantValue.ORDER_NO);
        if (on.equals(orderNo)) {
            mvpPresenter.getOrderInfo(orders.OrderNo);
        } else {
            finish();
            startActivity(intent);
        }
    }
}
