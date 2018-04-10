package com.baishan.nearshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.BaseActivity;
import com.baishan.nearshop.base.BaseApplication;
import com.baishan.nearshop.base.ResultResponse;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.Goods;
import com.baishan.nearshop.utils.ConstantValue;
import com.google.zxing.client.android.Intents;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/12/8 0008.
 */

public class ProcessCodeActivity extends BaseActivity {
    @Override
    protected void loadViewLayout() {
//        setContentView(R.layout.activity_transparent);
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        startActivityForResult(new Intent(this, BarcodeScannerActivity.class), 11);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK) {
            String result = data.getStringExtra(Intents.Scan.RESULT);
            Map<String, Object> params = getCommonAreaParams();
            params.put("SearchShopList", "CODE");
            params.put("Keyword", result);
            params.put("PageNow", "1");

            new CompositeSubscription().add(AppClient.getApiService().getGoodsList(params)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SubscriberCallBack<List<Goods>>() {
                        @Override
                        protected void onSuccess(List<Goods> response) {
                            if (response.size() == 0) {
                                showToast("当前还没有录入此商品");
                                finish();
                                return;
                            }
                            Goods goods = response.get(0);
                            Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                            intent.putExtra(ConstantValue.DATA, goods.Id);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            finish();
                        }

                        @Override
                        protected void onFailure(ResultResponse response) {
                            super.onFailure(response);
                            finish();
                        }
                    }));
        } else {
            finish();
        }
    }

    public Map<String, Object> getCommonAreaParams() {
        Area currentArea = BaseApplication.getInstance().getCurrentArea();
        Map<String, Object> params = new HashMap<>();
        if (currentArea != null) {
            params.put("AreaId", currentArea.AreaId);
            params.put("AdCode", currentArea.AdCode);
            params.put("CityCode", currentArea.CityCode);
        }
        return params;
    }
}
