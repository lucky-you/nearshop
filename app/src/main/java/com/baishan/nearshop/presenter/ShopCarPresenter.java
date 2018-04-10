package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.Shopcar;
import com.baishan.nearshop.view.IShopCarView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class ShopCarPresenter extends BasePresenter<IShopCarView> {

    public ShopCarPresenter(IShopCarView mvpView) {
        super(mvpView);
    }

    public void getMyShopCart(int userId) {
        addSubscription(AppClient.getApiService().getMyShopCart(userId), new SubscriberCallBack<List<Shopcar>>() {
            @Override
            protected void onSuccess(List<Shopcar> response) {
                mvpView.getMyShopCartSuccess(response);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                mvpView.stopRefresh();
            }
        });
    }

    public void changeAddress(int userId, int AreaId, int addressId) {
        Map<String, Object> params = new HashMap<>();
        params.put("Method", "CartChangeAddress");
        params.put("UserId", userId);
        params.put("AreaId", AreaId);
        params.put("AddressId", addressId);
        addSubscription(AppClient.getApiService().changeAddress(params), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.changeAddressSuccess();
            }
        });
    }

    public void changeNum(int userId, String cartToken, int num) {
        Map<String, Object> params = new HashMap<>();
        params.put("Method", "CartChangeNum");
        params.put("UserId", userId);
        params.put("CartToken", cartToken);
        params.put("Num", num);
        addSubscription(AppClient.getApiService().changeNum(params), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.changeNumSuccess();
            }
        });
    }

    public void deleteGoods(String cartToken, int userId) {
        addSubscription(true,AppClient.getApiService().deleteGoods("CartDelItem", cartToken, userId), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.deleteGoodsSuccess();
            }
        });
    }
}
