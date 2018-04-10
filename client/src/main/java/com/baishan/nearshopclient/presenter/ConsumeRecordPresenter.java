package com.baishan.nearshopclient.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshopclient.base.AppClient;
import com.baishan.nearshopclient.base.SubscriberCallBack;
import com.baishan.nearshopclient.model.ConsumeRecord;
import com.baishan.nearshopclient.view.IConsumeRecordView;

import java.util.List;

/**
 * 作者：ZhouBin  2017/3/17 14:04
 * 邮箱：1021237228@qq.com
 * 作用： 金额明细的presenter
 */

public class ConsumeRecordPresenter extends BasePresenter<IConsumeRecordView> {
    public ConsumeRecordPresenter(IConsumeRecordView mvpView) {
        super(mvpView);
    }

    //获取金额明细的数据
    public void getData(int userId, int pageNow) {
        addSubscription(AppClient.getApiService().getBalanceInfo(userId, pageNow), new SubscriberCallBack<List<ConsumeRecord>>() {
            @Override
            protected void onSuccess(List<ConsumeRecord> response) {
                mvpView.getDataSuccess(response);
            }
        });
    }


}
