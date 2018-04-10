package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.ConsumeRecord;
import com.baishan.nearshop.view.IConsumeRecordView;

import java.util.List;

/**
 * Created by RayYeung on 2017/1/13.
 */

public class ConsumeRecordPresenter extends BasePresenter<IConsumeRecordView> {

    public ConsumeRecordPresenter(IConsumeRecordView mvpView) {
        super(mvpView);
    }

    public void getData(int userId,int pageNow){
        addSubscription(AppClient.getApiService().getBalanceInfo(userId,pageNow), new SubscriberCallBack<List<ConsumeRecord>>() {
            @Override
            protected void onSuccess(List<ConsumeRecord> response) {
                mvpView.getDataSuccess(response);
            }
        });
    }

}
