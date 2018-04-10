package com.baishan.nearshop.view;

import com.baishan.nearshop.model.PostInfo;
import com.baishan.nearshop.model.PostReview;

import java.util.List;

/**
 * Created by Administrator on 2017/1/11 0011.
 */
public interface ICommentDetailView {
    void sendCommitSuccess(String response);

    void getReplyDataSuccess(List<PostReview> response);

    void getPostDetailSuccess(List<PostInfo> response);
}
