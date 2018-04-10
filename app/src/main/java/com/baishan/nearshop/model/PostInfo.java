package com.baishan.nearshop.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by RayYeung on 2016/12/22.
 */

public class PostInfo implements Serializable{


    /**
     * Id : 6
     * ParentId : null
     * ForumToken : 55f8674d-a068-429b-890a-2a98fd8e8205
     * CategoryId : 1
     * CategoryTitle : 热门
     * CategoryImgUrl : null
     * CategoryDescription : null
     * UserId : 2
     * UserToken : 23823008-f6f9-45fa-bc03-f04e82fcf21b
     * UserName : error name
     * UserPhoto : http://111.47.198.193:8033/Upload/UserPhoto/1480325756096.jpg
     * UserSex : 1
     * IsPublisher : 1
     * ForumTitle : 捏人roses耳钉咩
     * ForumContent : 你摸给你非命您
     * CreateTime : 2017-01-10 15:23:17
     * IsTop : 0
     * IsEssence : 0
     * IsRecommend : 0
     * Location : 洪福添美
     * HasPic : 1
     * CommentCount : 0
     * ReturnCount : 0
     * ReturnLastTime : 2017-01-10 15:23:17
     * IsActive : 1
     * ShopToken : null
     * RowIndex : 1
     * Image : [{"ForumToken":"55f8674d-a068-429b-890a-2a98fd8e8205","OriginImage":"http://111.47.198.193:8033/Forum/1484032997994.jpg","OriginWidth":816,"OriginHeight":459,"thumbImage":"http://111.47.198.193:8033/ForumThumbnail/1484032997994.jpg","thumbWidth":300,"thumbHeight":300},{"ForumToken":"55f8674d-a068-429b-890a-2a98fd8e8205","OriginImage":"http://111.47.198.193:8033/Forum/1484032998041.jpg","OriginWidth":459,"OriginHeight":816,"thumbImage":"http://111.47.198.193:8033/ForumThumbnail/1484032998041.jpg","thumbWidth":300,"thumbHeight":300}]
     * PageCount : 1
     * TotalCounts : 5
     * ReturnList : null
     */

    public int Id;
    public int ParentId;
    public String ForumToken;
    public int CategoryId;
    public String CategoryTitle;
    public String CategoryImgUrl;
    public String CategoryDescription;
    public int UserId;
    public String UserToken;
    public String UserName;
    public String UserPhoto;
    public int UserSex;
    public int IsPublisher;
    public String ForumTitle;
    public String ForumContent;
    public String CreateTime;
    public int IsTop;
    public int IsEssence;
    public int IsRecommend;
    public String Location;
    public int HasPic;
    public int CommentCount;
    public List<PostReview> Comments;
    public int ReturnCount;
    public String ReturnLastTime;
    public int IsActive;
    public int RowIndex;
    public int PageCount;
    public int TotalCounts;
    public List<PostInfo> ReturnList;
    /**
     * ForumToken : 55f8674d-a068-429b-890a-2a98fd8e8205
     * OriginImage : http://111.47.198.193:8033/Forum/1484032997994.jpg
     * OriginWidth : 816
     * OriginHeight : 459
     * thumbImage : http://111.47.198.193:8033/ForumThumbnail/1484032997994.jpg
     * thumbWidth : 300
     * thumbHeight : 300
     */

    public List<Image> Images;

}
