package com.baishan.nearshop.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/27.
 */
public class PostReview implements Serializable {

    public int commentId;
    public int TotalCounts;
    public int PageCount;
    public int ReplyUserId;
    public String ReplyUserName;
    public String ReplyUserToken;
    public String ReplyUserPhoto;
    public int UserId;
    public String UserName;
    public String UserToken;
    public String CommentContent;
    public int IsPublisher;
    public int IsComment;
    public String CreateTime;
    public String ForumTitle;
    public int ForumId;
    public ArrayList<Image> Images;


    @Override
    public String toString() {
        return "PostReviewParser{" +
                "commentId=" + commentId +
                ", TotalCounts=" + TotalCounts +
                ", ReplyUserId=" + ReplyUserId +
                ", ReplyUserName='" + ReplyUserName + '\'' +
                ", ReplyUserToken='" + ReplyUserToken + '\'' +
                ", UserId=" + UserId +
                ", UserName='" + UserName + '\'' +
                ", UserToken='" + UserToken + '\'' +
                ", CommentContent='" + CommentContent + '\'' +
                ", IsPublisher=" + IsPublisher +
                '}';
    }
}
