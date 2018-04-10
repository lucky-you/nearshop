package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.base.SubscriberCallBack;
import com.baishan.nearshop.model.PostInfo;
import com.baishan.nearshop.model.PostReview;
import com.baishan.nearshop.view.ICommentDetailView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/11 0011.
 */
public class CommentDetailPresenter extends BasePresenter<ICommentDetailView>{
    public CommentDetailPresenter(ICommentDetailView mvpView) {
        super(mvpView);
    }

    public void sendCommit(Map<String, String> params, String content) {
        addSubscription(true,AppClient.getApiService().postBBS(params), new SubscriberCallBack<String>() {
            @Override
            protected void onSuccess(String response) {
                mvpView.sendCommitSuccess(content);
            }

        });
    }

    public void getReplyData(String commentId) {
        addSubscription(true,AppClient.getApiService().getReplyData(commentId), new SubscriberCallBack<List<PostReview>>() {
            @Override
            protected void onSuccess(List<PostReview> response) {
                mvpView.getReplyDataSuccess(response);
            }

        });
    }

    public void getPostDetail(Map<String, String> params) {
        addSubscription(AppClient.getApiService().getPostDetail(params), new SubscriberCallBack<List<PostInfo>>() {
            @Override
            protected void onSuccess(List<PostInfo> response) {
                mvpView.getPostDetailSuccess(response);
            }
        });
    }
}
