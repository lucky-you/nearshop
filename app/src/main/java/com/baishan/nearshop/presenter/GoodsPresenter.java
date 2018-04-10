package com.baishan.nearshop.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.BaseApplication;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.ui.activity.AddrManageActivity;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.IGoodsView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.baishan.mylibrary.utils.ToastUtils.showToast;

/**
 * Created by RayYeung on 2016/11/17.
 */

public class GoodsPresenter extends BasePresenter<IGoodsView> {

    private int userId;
    private int productId;
    private String spec;
    private String flag;

    public GoodsPresenter(IGoodsView mvpView) {
        super(mvpView);

    }

    public void addToShopCar(int userId, int areaId, int productId, String spec) {
        if (areaId == 0) {
            showToast("请先添加默认地址");
            if (!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().register(this);
            this.userId = userId;
            this.productId = productId;
            this.spec = spec;
            flag = UUID.randomUUID().toString();
            Intent it = new Intent(context, AddrManageActivity.class);
            it.putExtra(ConstantValue.TYPE, AddrManageActivity.INTENT_SELECT);
            it.putExtra(ConstantValue.CLASSNAME, flag);
            context.startActivity(it);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("Method", "AddShopCart");
        params.put("UserId", userId);
        params.put("AreaId", areaId);
        params.put("ProductId", productId);
        String hint = "添加商品到购物车成功";
        if (!TextUtils.isEmpty(spec)) {
            params.put("Spec", spec);
            hint += "（规格：" + spec + "）";
        }else{
            params.put("Spec", "");
        }
        String finalHint = hint;
        addSubscription(AppClient.getApiService().addGoods(params), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                EventBus.getDefault().post(new Notice(ConstantValue.MSG_TYPE_SHOPCAR_UPDATE));
                showToast(finalHint);
                mvpView.addToShopCarSuccess();
            }
        });
    }

    public void exchangeGoods(int userId, int areaId, int productId, int addressId, int num) {
        Map<String, Object> params = new HashMap<>();
        params.put("Method", "ExchangeShop");
        params.put("UserId", userId);
        params.put("AreaId", areaId);
        params.put("ProductId", productId);
        params.put("AddressId", addressId);
        params.put("Num", num);
        addSubscription(AppClient.getApiService().exchangeGoods(params), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                showToast("兑换成功");
                mvpView.exchangeGoodsSuccess();
            }
        });
    }

    @Subscribe
    public void onEvent(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_SELECTED_ADDRESS && flag.equals(notice.content1)) {
            addToShopCar(userId, BaseApplication.getInstance().getCurrentArea().AreaId, productId, spec);
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
