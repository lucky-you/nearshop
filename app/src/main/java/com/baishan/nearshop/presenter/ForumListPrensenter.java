package com.baishan.nearshop.presenter;

import android.support.v4.widget.SwipeRefreshLayout;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.PostInfo;
import com.baishan.nearshop.view.IForumListView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/23 0023.
 */
public class ForumListPrensenter extends BasePresenter<IForumListView>{
    public ForumListPrensenter(IForumListView mvpView) {
        super(mvpView);
    }

    public void getForumList(SwipeRefreshLayout srl,Map<String, String> params) {
        addSubscription(srl,AppClient.getApiService().getPostList(params), new SubscriberCallBack<List<PostInfo>>() {
            @Override
            protected void onSuccess(List<PostInfo> response) {
                mvpView.getForumListSuccess(response);
            }

        });
    }
}
