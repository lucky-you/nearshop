package com.baishan.nearshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseActivity;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.utils.StrUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderMessageActivity extends BaseActivity {


    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.btnDetail)
    Button btnDetail;
    @BindView(R.id.btnGotoMain)
    Button btnGotoMain;

    private String orderNo;
    private double price;

    public static final int INTENT_GOODS = 1;
    public static final int INTENT_SERVICE = 2;
    private int type;


    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_order_success);
    }

    @Override
    protected void bindViews() {
        ButterKnife.bind(this);
        initTitle("订单提醒");
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        type = getIntent().getIntExtra(ConstantValue.TYPE, INTENT_GOODS);
        orderNo = getIntent().getStringExtra(ConstantValue.ORDERNO);
        price = getIntent().getDoubleExtra(ConstantValue.PRICE, 0);
        tvPrice.setText(StrUtils.formatPrice(mContext, price));
        if (type == INTENT_SERVICE) {
            btnGotoMain.setText("返回便民主页");
        }
    }

    @Override
    protected void setListener() {

    }


    @OnClick({R.id.btnDetail, R.id.btnGotoMain})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDetail:
                if (type == INTENT_SERVICE) {
                    Intent it = new Intent(mContext, ServiceOrderDetailActivity.class);
                    it.putExtra(ConstantValue.ORDERNO, orderNo);
                    startActivity(it);
                } else {
                    Intent it = new Intent(mContext,OrdersActivity.class);
                    it.putExtra(ConstantValue.TABPOSITION,1);
                    startActivity(it);
                }
                finish();
                break;
            case R.id.btnGotoMain:
                intent2Activity(MainActivity.class);
                if (type == INTENT_SERVICE) {
                    MainActivity.getInstance().showFragment(2);
                } else {
                    MainActivity.getInstance().showFragment(1);
                }
                finish();
                break;
        }
    }
}
