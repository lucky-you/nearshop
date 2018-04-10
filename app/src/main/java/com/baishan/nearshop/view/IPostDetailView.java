package com.baishan.nearshop.view;

import com.baishan.nearshop.model.PostCommentParser;
import com.baishan.nearshop.model.PostInfo;
import com.baishan.nearshop.model.UserInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/1/11 0011.
 */
public interface IPostDetailView {
    void showLoading();

    void hideLoading();

    void getPostDetailSuccess(List<PostInfo> response);

    void getCommentDataSuccess(PostCommentParser response);

    void onSendCommentSuccess(String response);

    void getRewardDataSuccess(List<UserInfo> response);

    void onReportSuccess();

}
