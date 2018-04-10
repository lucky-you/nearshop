package com.baishan.nearshop.model;

import java.util.ArrayList;

/**
 * Created by RayYeung on 2017/1/10.
 */

public class PostParam {

    public String title;
    public String content;
    public int userId;
    public String userToken;
    public int categoryId;

    //帖子Id
    public int parentId;
    public String parentToken;

    //是否是楼主
    public int isPublisher;
    public  String location;

    public  String cityCode;
    public  int areaId;

    //图片本地地址
    public ArrayList<String> pics;




}
