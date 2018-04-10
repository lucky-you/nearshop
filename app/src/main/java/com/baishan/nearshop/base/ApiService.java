package com.baishan.nearshop.base;

import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.Banner;
import com.baishan.nearshop.model.Category;
import com.baishan.nearshop.model.ChatRoom;
import com.baishan.nearshop.model.City;
import com.baishan.nearshop.model.ConfirmOrderItem;
import com.baishan.nearshop.model.ConsumeRecord;
import com.baishan.nearshop.model.Coupon;
import com.baishan.nearshop.model.EasyService;
import com.baishan.nearshop.model.FirstProduct;
import com.baishan.nearshop.model.ForumCategory;
import com.baishan.nearshop.model.Goods;
import com.baishan.nearshop.model.GridType;
import com.baishan.nearshop.model.HotNews;
import com.baishan.nearshop.model.Orders;
import com.baishan.nearshop.model.PostCommentParser;
import com.baishan.nearshop.model.PostInfo;
import com.baishan.nearshop.model.PostReview;
import com.baishan.nearshop.model.ShopOrder;
import com.baishan.nearshop.model.ShopStore;
import com.baishan.nearshop.model.Shopcar;
import com.baishan.nearshop.model.UpdateInfo;
import com.baishan.nearshop.model.UserInfo;
import com.tencent.mm.sdk.modelpay.PayReq;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;


/**
 *
 */
public interface ApiService {
    //baseUrl
//    String HOST = "http://192.168.0.121:8012/";
    String HOST = "http://111.47.198.193:8033/";
    String GOODS_DETAIL_URL = "http://111.47.198.193:8080/html/shop/";
    String SERVICE_DETAIL_URL = "http://111.47.198.193:8080/html/service/";
    String API_SERVER_URL = HOST + "api/";
    String URL_POST_USER_DATA = API_SERVER_URL + "PostUserData";
    String URL_POST_SETTING_DATA = API_SERVER_URL + "PostSettingData";
    String URL_GET_USER_DATA = API_SERVER_URL + "GetUserData";
    String URL_POST_SUPERMARKET_DATA = API_SERVER_URL + "PostSuperMarketData";
    String URL_GET_SUPERMARKET_DATA = API_SERVER_URL + "GetSuperMarketData";
    String URL_GET_ORDER_DATA = API_SERVER_URL + "GetOrderData";
    String URL_POST_ORDER_DATA = API_SERVER_URL + "PostOrderData";
    String URL_GET_AREA_DATA = API_SERVER_URL + "GetAreaData";
    String URL_GET_SERVICE_DATA = API_SERVER_URL + "GetServiceData";
    String URL_POST_SERVICE_DATA = API_SERVER_URL + "PostServiceData";
    String URL_GET_BBS_DATA = API_SERVER_URL + "GetBBSData";
    String URL_POST_BBS_DATA = API_SERVER_URL + "PostBBSData";

    String SHARE_URL = HOST + "ShareApp/Index?";


    //注册协议
    String REGISTER_PROTOCOL = "http://sbgo.cc:8080/html/protocol.html";
    //关于我们
    String ABOUT_US = "http://sbgo.cc:8080/html/about.html";

    String APP_UPDATE = "http://sbgo.cc:8080/html/update_user.html";

    //身边够官网
    String  SBG_OFFICIAL="http://www.sbgo.cc";


    /**
     * 获取验证码
     */
    Observable<ResultResponse<String>> getCode(@Field("phone") String phone, @Field("state") int state);

    /**
     * 获取首页banner
     */
    @GET(URL_GET_SUPERMARKET_DATA + "?IndexBanner=true")
    Observable<ResultResponse<List<Banner>>> getHomeBanner(@QueryMap Map<String, Object> params);

    @GET(URL_GET_USER_DATA)
    Observable<ResultResponse<String>> getCode(@Query("Phone") String phone);

    /**
     * 登陆
     */
    @FormUrlEncoded
    @POST(URL_POST_USER_DATA)
    Observable<ResultResponse<List<UserInfo>>> login(@Field("Method") String method, @Field("Account") String phone, @Field("Password") String password);

    /**
     * 静默登陆
     */
    @GET(URL_GET_USER_DATA)
    Observable<ResultResponse<List<UserInfo>>> quietLogin(@Query("LoginToken") String token);

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST(URL_POST_USER_DATA)
    Observable<ResultResponse<List<UserInfo>>> regist(@Field("Method") String method, @Field("Account") String phone, @Field("Password") String password, @Field("ValidCode") String code);

    /**
     * 意见反馈
     */
    @FormUrlEncoded
    @POST(URL_POST_SETTING_DATA)
    Observable<ResultResponse<String>> submitFeedback(@Field("Method") String method, @Field("UserId") String userId, @Field("Contents") String contents);

    /**
     * 预约服务
     */
    @FormUrlEncoded
    @POST(URL_POST_SERVICE_DATA)
    Observable<ResultResponse<String>> reservationService(@Field("Method") String method, @Field("UserId") String userId, @Field("AreaId") String areaId, @Field("ServiceId") String serviceId, @Field("Contact") String contact, @Field("Phone") String phone, @Field("Address") String address, @Field("Remarks") String remarks);

    /**
     * 找回密码
     */
    @FormUrlEncoded
    @POST(URL_POST_USER_DATA)
    Observable<ResultResponse<String>> changePassword(@Field("Method") String method, @Field("Account") String phone, @Field("Password") String password, @Field("ValidCode") String code);

    /**
     * 编辑用户资料
     */
    @FormUrlEncoded
    @POST(URL_POST_USER_DATA)
    Observable<ResultResponse<String>> editUserInfo(@Field("Method") String method, @Field("LoginToken") String loginToken, @Field("ModifyType") String modifyType, @Field("ModifyValue") String modifyValue);

    /**
     * 上传头像
     */
    @Multipart
    @POST(URL_POST_USER_DATA)
    Observable<ResultResponse<String>> uploadAvatar(@PartMap Map<String, RequestBody> params);

    /**
     * 申请合作
     */
    @Multipart
    @POST(URL_POST_SETTING_DATA)
    Observable<ResultResponse<String>> applyPartner(@PartMap Map<String, RequestBody> params);

    /**
     * 申请合作
     */
    @Multipart
    @POST(URL_POST_SETTING_DATA)
    Observable<ResultResponse<String>> applyAreas(@PartMap Map<String, RequestBody> params);

    /**
     * 快捷服务
     */
    @GET(URL_GET_SUPERMARKET_DATA + "?IndexFast=true")
    Observable<ResultResponse<List<GridType>>> getFastService(@QueryMap Map<String, Object> params);

    /**
     * 头条新闻
     */
    @GET(URL_GET_SUPERMARKET_DATA + "?HotNews=true")
    Observable<ResultResponse<List<HotNews>>> getHotNews();

    /**
     * 抢购商品
     */
    @GET(URL_GET_SUPERMARKET_DATA + "?FirstProducts=true")
    Observable<ResultResponse<List<FirstProduct>>> getFirstProducts(@Query("AreaId") int areaId);

    /**
     * 存在商区的市区列表
     */
    @GET(URL_GET_AREA_DATA + "?CountyList=true")
    Observable<ResultResponse<List<Area>>> getAreaList(@Query("CityCode") String cityCode);

    /**
     * 商区列表
     */
    @GET(URL_GET_AREA_DATA + "?ShopAreas=true")
    Observable<ResultResponse<List<Area>>> getShopAreaList(@Query("AdCode") String adCode);

    /**
     * 商品分类
     */
    @GET(URL_GET_SUPERMARKET_DATA + "?AllCategory=true")
    Observable<ResultResponse<List<Category>>> getAllCategory(@QueryMap Map<String, Object> params);

    /**
     * 分类商品列表
     */
    @GET(URL_GET_SUPERMARKET_DATA)
    @Headers("Cache-Control: public, max-age=60")
    Observable<ResultResponse<List<Goods>>> getGoodsList(@QueryMap Map<String, Object> params);

    /**
     * 自定义商品列表
     */
    @GET(URL_GET_SUPERMARKET_DATA + "?ShopCustomList=true")
    Observable<ResultResponse<List<Goods>>> getShopCustomList(@Query("CategoryId") int categoryId, @Query("PageNow") int pageNow);


    /**
     * 商品详情
     */
    @GET(URL_GET_SUPERMARKET_DATA + "?ShopInfo=true")
    Observable<ResultResponse<List<Goods>>> getGoodsInfo(@Query("ShopId") int id);

    /**
     * 我的便民订单
     */
    @GET(URL_GET_ORDER_DATA)
    Observable<ResultResponse<List<Orders>>> getServiceOrderList(@Query("ServiceOrder") String type, @Query("UserId") int userId, @Query("PageNow") int pageNow);

    /**
     * 我的商品订单
     */
    @GET(URL_GET_ORDER_DATA)
    Observable<ResultResponse<List<ShopOrder>>> getShopOrderList(@Query("ShopOrder") String type, @Query("UserId") int userId, @Query("PageNow") int pageNow);

    /**
     * 热卖商品列表
     */
    @GET(URL_GET_SUPERMARKET_DATA + "?HotShopList=true")
    Observable<ResultResponse<List<Goods>>> getHotShopList(@Query("AreaId") int areaId, @Query("PageNow") int pageNow);

    /**
     * S币商城商品列表
     */
    @GET(URL_GET_SUPERMARKET_DATA + "?ShopCoinsList=true")
    Observable<ResultResponse<List<Goods>>> getShopCoinsList(@Query("AreaId") int areaId, @Query("PageNow") int pageNow);
    /**
     * 帖子详情
     */
    @GET(URL_GET_BBS_DATA )
    Observable<ResultResponse<List<PostInfo>>> getPostDetail(@QueryMap Map<String, String> params);
    /**
     * 帖子评论
     */
    @GET(URL_GET_BBS_DATA )
    Observable<ResultResponse<PostCommentParser>> getCommentData(@QueryMap Map<String, String> params);

    /**
     * 便民快捷服务
     */
    @GET(URL_GET_SERVICE_DATA + "?ServiceFast=true")
    Observable<ResultResponse<List<GridType>>> getServiceFast(@QueryMap Map<String, Object> params);

    /**
     * 推荐服务列表
     */
    @GET(URL_GET_SERVICE_DATA + "?RecommendServiceList=true")
    Observable<ResultResponse<List<EasyService>>> getRecommendServiceList(@Query("AreaId") int areaId, @Query("PageNow") int pageNow);

    /**
     * 搜索推荐服务列表
     */
    @GET(URL_GET_SERVICE_DATA + "?SearchServiceList=true")
    Observable<ResultResponse<List<EasyService>>> getSearchRecommendServiceList(@QueryMap Map<String, Object> params);

    /**
     * 供货商家信息
     */
    @GET(URL_GET_SUPERMARKET_DATA + "?ShopStoreList=true")
    Observable<ResultResponse<List<ShopStore>>> getShopStoreList(@Query("AreaId") int areaId, @Query("ProductId") int productId);

    /**
     * 收货地址
     */
    @POST(URL_POST_USER_DATA)
    @FormUrlEncoded
    Observable<ResultResponse<Boolean>> address(@FieldMap Map<String, Object> params);

    /**
     * 提交订单
     */
    @POST(URL_POST_ORDER_DATA)
    @FormUrlEncoded
    Observable<ResultResponse<PayReq>> commitOrder(@Field("Method") String method, @Field("UserId") String userId, @Field("PerOrderJson") String json);

    /**
     * 我的收货地址
     */
    @GET(URL_GET_USER_DATA + "?AddressList=true")
    Observable<ResultResponse<List<Area>>> getAddressList(@Query("UserId") int userId, @Query("AreaId") int areaId);

    /***
     * 服务分类列表
     */
    @GET(URL_GET_SERVICE_DATA + "?ServiceList=true")
    Observable<ResultResponse<List<EasyService>>> getServiceList(@QueryMap Map<String, Object> params);

    /***
     * 服务详情
     */
    @GET(URL_GET_SERVICE_DATA + "?ServiceInfo=true")
    Observable<ResultResponse<List<EasyService>>> getServiceInfo(@Query("ServiceId") int id);


    /***
     * 优惠券列表
     */
    @GET(URL_GET_USER_DATA)
    Observable<ResultResponse<List<Coupon>>> getUserCoupons(@Query("UserCoupons") String method, @Query("AreaId") int areaId, @Query("userId") int userId, @Query("orderPrice") int price);

    /**
     * 获取最新s币
     */
    @GET(URL_GET_USER_DATA + "?LastSCoins=true")
    Observable<ResultResponse<String>> getLastCoins(@Query("AreaId") int areaId, @Query("LoginToken") String loginToken);

    /**
     * 获取最新余额
     */
    @GET(URL_GET_USER_DATA + "?LastBalance=true")
    Observable<ResultResponse<String>> getLastBalance(@Query("LoginToken") String loginToken);

    /***
     * 获取用户当前所在区域
     */
    @GET(URL_GET_USER_DATA + "?CurrentArea=true")
    Observable<ResultResponse<Area>> getUserCurrentArea(@Query("Adcode") String adCode, @Query("Longitude") double longitude, @Query("Latitude") double latitude);

    /***
     * 默认收货地址
     */
    @GET(URL_GET_USER_DATA + "?DefaultAddress=true")
    Observable<ResultResponse<Area>> getUserDefaultAddress(@Query("AreaId") int areaId, @Query("UserId") int userId);


    /***
     * 添加商品到购物车
     */
    @POST(URL_POST_SUPERMARKET_DATA)
    @FormUrlEncoded
    Observable<ResultResponse<String>> addGoods(@FieldMap Map<String, Object> params);


    /**
     * 我的购物车
     */
    @GET(URL_GET_USER_DATA + "?myShopCart=true")
    Observable<ResultResponse<List<Shopcar>>> getMyShopCart(@Query("userId") int userId);


    /**
     * 获取支付信息
     */
    @POST(URL_POST_ORDER_DATA)
    @FormUrlEncoded
    Observable<ResultResponse<PayReq>> getPayInfo(@FieldMap Map<String, Object> params);

    /**
     * 订单操作  取消订单 确认无误、  申请退款
     */
    @POST(URL_POST_ORDER_DATA)
    @FormUrlEncoded
    Observable<ResultResponse<String>> ordersOperate(@Field("Method") String method, @Field("OrderNo") String orderNo, @Field("UserId") int userId);

    /**
     * 获取预提交订单信息
     */
    @POST(URL_POST_USER_DATA)
    @FormUrlEncoded
    Observable<ResultResponse<List<ConfirmOrderItem>>> getPreOrderInfo(@Field("Method") String method, @Field("PreOrderInfo") String carToken, @Field("UserId") int userId);

    @POST(URL_POST_USER_DATA)
    @FormUrlEncoded
    Observable<ResultResponse<List<ConfirmOrderItem>>> getPreOrderInfo(@FieldMap Map<String, Object> params);


    /**
     * 预约订单信息
     */
    @GET(URL_GET_ORDER_DATA + "?OrderInfo=true")
    Observable<ResultResponse<List<Orders>>> getServiceOrderInfo(@Query("OrderNo") String orderNo);
    /**
     * 获取帖子分类
     */
    @GET(URL_GET_BBS_DATA + "?allcategory=true")
    Observable<ResultResponse<List<ForumCategory>>> getAllForumCategory();
    /**
     * 获取帖子列表
     */
    @GET(URL_GET_BBS_DATA)
    Observable<ResultResponse<List<PostInfo>>> getPostList(@QueryMap Map<String, String> params);

    /**
     * 购物车选择其他地址
     */
    @POST(URL_POST_SUPERMARKET_DATA)
    @FormUrlEncoded
    Observable<ResultResponse<String>> changeAddress(@FieldMap Map<String, Object> params);

    /**
     * 购物车修改商品数量
     */
    @POST(URL_POST_SUPERMARKET_DATA)
    @FormUrlEncoded
    Observable<ResultResponse<String>> changeNum(@FieldMap Map<String, Object> params);

    /**
     * 购物车删除商品
     */
    @POST(URL_POST_SUPERMARKET_DATA)
    @FormUrlEncoded
    Observable<ResultResponse<String>> deleteGoods(@Field("Method") String method, @Field("CartToken") String cartToken, @Field("UserId") int userId);

    /**
     * 商品订单信息
     */
    @GET(URL_GET_ORDER_DATA + "?ShopOrderInfo=true")
    Observable<ResultResponse<List<ConfirmOrderItem>>> getGoodsOrderInfo(@Query("OrderNo") String orderNo);

    /**
     * 兑换商品
     */
    @POST(URL_POST_SUPERMARKET_DATA)
    @FormUrlEncoded
    Observable<ResultResponse<String>> exchangeGoods(@FieldMap Map<String, Object> params);

    /**
     * 兑换记录
     */
    @GET(URL_GET_USER_DATA + "?MyExchange=true")
    Observable<ResultResponse<List<ShopOrder>>> getRecord(@Query("UserId") int userId);

    /**
     * 热门城市
     */
    @GET(URL_GET_AREA_DATA + "?HotCity=true")
    Observable<ResultResponse<List<City>>> getHotCities();

    /**
     * 商区搜索
     */
    @GET(URL_GET_AREA_DATA + "?SearchAreas=true")
    Observable<ResultResponse<List<Area>>> searchArea(@Query("Keyword") String key);


    /**
     * 获取商区信息
     */
    @GET(URL_GET_AREA_DATA + "?AreaInfo=true")
    Observable<ResultResponse<List<Area>>> getAreaInfo(@Query("AreaId") int areaId);

    /**
     * 获取评论列表
     */
    @GET(URL_GET_BBS_DATA + "?commentlist=true")
    Observable<ResultResponse<List<PostReview>>> getReplyData(@Query("forumid") String forumid);
    /**
     * 获得打赏数据
     */
    @GET(URL_GET_BBS_DATA + "?rewardList=true")
    Observable<ResultResponse<List<UserInfo>>> getRewardData(@Query("forumid") String forumid);

    /**
     * 更新信息
     */
    @GET(APP_UPDATE)
    Observable<UpdateInfo> getUpdateInfo();

    /**
     * 下载更新apk
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadApk(@Url String path);


    /**
     * 发帖
     */
    @POST(URL_POST_BBS_DATA)
    @Multipart
    Observable<ResultResponse<String>> post(@PartMap Map<String, RequestBody> params);
    /**
     * 帖子公共接口
     */
    @POST(URL_POST_BBS_DATA)
    @FormUrlEncoded
    Observable<ResultResponse<String>> postBBS(@FieldMap Map<String, String> params);

    /**
     *  获取消费记录
     */
    @GET(URL_GET_USER_DATA + "?myBalance=true")
    Observable<ResultResponse<List<ConsumeRecord>>> getBalanceInfo(@Query("userId") int userId, @Query("PageNow") int pageNow);

    /**
     * 获取聊天室信息
     */
    @GET(URL_GET_AREA_DATA + "?chatRoomInfo=true")
    Observable<ResultResponse<ChatRoom>> getChatRoomInfo(@Query("userId")int userId, @Query("roomId")String chatRoomId, @Query("areaId")int areaId);
}
