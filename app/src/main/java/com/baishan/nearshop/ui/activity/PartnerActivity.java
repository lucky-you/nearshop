package com.baishan.nearshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseActivity;
import com.baishan.nearshop.utils.ConstantValue;

public class PartnerActivity extends BaseActivity {

    private RelativeLayout relNewBusiness;
    private RelativeLayout relCommercialTenant;
    private RelativeLayout relProvider;
    private RelativeLayout relCourier;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_partner);
    }

    @Override
    protected void bindViews() {
        relNewBusiness = (RelativeLayout) findViewById(R.id.relNewBusiness);
        relCommercialTenant = (RelativeLayout) findViewById(R.id.relCommercialTenant);
        relProvider = (RelativeLayout) findViewById(R.id.relProvider);
        relCourier = (RelativeLayout) findViewById(R.id.relCourier);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initRedTitle("加盟合作");
    }

    @Override
    protected void setListener() {
        relNewBusiness.setOnClickListener(v -> intent2Activity(ApplyBusinessAreaActivity.class));
        relCommercialTenant.setOnClickListener(v -> {
            Intent intent = new Intent(this, ApplyPartnerActivity.class);
            intent.putExtra(ConstantValue.TYPE, ApplyPartnerActivity.APPlY_TYPE_COMMERCIAL_TENANT);
            startActivity(intent);
        });
        relProvider.setOnClickListener(v -> {
            Intent intent=new Intent(this,ApplyPartnerActivity.class);
            intent.putExtra(ConstantValue.TYPE,ApplyPartnerActivity.APPlY_TYPE_PROVIDER);
            startActivity(intent);
        });
        relCourier.setOnClickListener(v -> {
            Intent intent=new Intent(this,ApplyPartnerActivity.class);
            intent.putExtra(ConstantValue.TYPE,ApplyPartnerActivity.APPlY_TYPE_COURIER);
            startActivity(intent);
        });
    }
}
