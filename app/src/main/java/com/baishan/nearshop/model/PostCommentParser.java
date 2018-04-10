package com.baishan.nearshop.model;

import java.util.List;

/**
 * Created by Administrator on 2017/1/11 0011.
 */

public class PostCommentParser {

    /**
     * Table : [{"Id":16,"ParentId":14,"ForumToken":"9c3266c2-c196-4794-a47f-ff17534feb90","CategoryId":4,"CategoryTitle":"热门","CategoryImgUrl":null,"CategoryDescription":null,"UserId":3,"UserToken":"b4999c2e-f41c-4d91-83bd-2b638d56a483","UserName":"(ಥ_ಥ)","UserPhoto":"http://111.47.198.193:8033/Upload/UserPhoto/1481509309105.jpg","UserSex":1,"IsPublisher":0,"ForumTitle":"123456习近平","ForumContent":"发个呵呵","CreateTime":"2017-01-11 14:13:23","IsTop":0,"IsEssence":0,"IsRecommend":0,"Location":"1","HasPic":0,"CommentCount":0,"ReturnCount":0,"ReturnLastTime":"2017-01-11 14:13:23","IsActive":1,"ShopToken":null,"RowIndex":1,"Image":null,"Comments":null},{"Id":18,"ParentId":14,"ForumToken":"ffa337db-f6e4-4a3a-95ee-693630e10599","CategoryId":4,"CategoryTitle":"热门","CategoryImgUrl":null,"CategoryDescription":null,"UserId":6,"UserToken":"8f0d5795-9fe8-4a36-ac0a-c2fa6893ec5f","UserName":"157****2674","UserPhoto":"http://111.47.198.193:8033/Upload/UserPhoto/1482475500562.jpg","UserSex":1,"IsPublisher":1,"ForumTitle":"123456习近平","ForumContent":"图图摸摸","CreateTime":"2017-01-11 14:20:05","IsTop":0,"IsEssence":0,"IsRecommend":0,"Location":"襄轴商区","HasPic":0,"CommentCount":0,"ReturnCount":0,"ReturnLastTime":"2017-01-11 14:20:05","IsActive":1,"ShopToken":null,"RowIndex":2,"Image":null,"Comments":null}]
     * PageCount : 1
     * TotalCounts : 2
     */

    public int PageCount;
    public int TotalCounts;
    public List<PostInfo> Table;
}
