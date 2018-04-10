package com.baishan.nearshopclient.base;

import com.baishan.nearshopclient.model.ConsumeRecord;
import com.baishan.nearshopclient.model.Goods;
import com.baishan.nearshopclient.model.OrderCount;
import com.baishan.nearshopclient.model.Orders;
import com.baishan.nearshopclient.model.SenderGoodsOrders;
import com.baishan.nearshopclient.model.SenderOrdersDetail;
import com.baishan.nearshopclient.model.ServiceOrders;
import com.baishan.nearshopclient.model.ShopOrder;
import com.baishan.nearshopclient.model.ShopStore;
import com.baishan.nearshopclient.model.UserInfo;
import com.baishan.nearshopclient.model.WithdrawLog;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;


/**
 *
 */
public interface ApiService {
    //baseUrl
//    String API_SERVER_URL = "http://192.168.0.116:88/";
    String HOST = "http://111.47.198.193:8033/";
    String GOODS_DETAIL_URL = "http://111.47.198.193:8080/html/shop/";
    String API_SERVER_URL = HOST + "api/";
    String URL_POST_USER_DATA = API_SERVER_URL + "PostUserData";
    String URL_POST_ORDER_DATA = API_SERVER_URL + "PostOrderData";
    String URL_POST_SERVICE_DATA = API_SERVER_URL + "PostServiceData";
    String URL_GET_ORDER_DATA = API_SERVER_URL + "GetOrderData";
    String URL_GET_USER_DATA = API_SERVER_URL + "GetUserData";
    String URL_GET_SUPERMARKET_DATA = API_SERVER_URL + "GetSuperMarketData";

    /**
     * 获取验证码
     */
    Observable<ResultResponse<String>> getCode(@Field("phone") String phone, @Field("state") int state);

    /**
     * 登陆
     */
    @FormUrlEncoded
    @POST(URL_POST_USER_DATA)
    Observable<ResultResponse<List<UserInfo>>> login(@Field("Method") String method, @Field("Account") String phone, @Field("Password") String password);

    /**
     * 我的订单
     */
    @GET(URL_GET_ORDER_DATA)
    Observable<ResultResponse<List<ServiceOrders>>> getServiceOrderList(@Query("CourierServiceOrder") String type, @Query("CourierId") int userId, @Query("PageNow") int pageNow, @Query("AreaId") int areaId);

    /**
     * 我的订单
     */
    @GET(URL_GET_ORDER_DATA)
    Observable<ResultResponse<List<SenderGoodsOrders>>> getSenderShopOrderList(@Query("CourierShopOrder") String type, @Query("CourierId") int userId, @Query("PageNow") int pageNow, @Query("AreaId") int areaId);

    /**
     * 管理员 超市订单
     */
    @GET(URL_GET_ORDER_DATA)
    Observable<ResultResponse<List<ShopOrder>>> getAdminShopOrderList(@Query("AdminShopOrder") String type, @Query("AdminId") int userId, @Query("PageNow") int pageNow, @Query("AreaId") int areaId);

    /**
     * 管理员 服务订单
     */
    @GET(URL_GET_ORDER_DATA)
    Observable<ResultResponse<List<Orders>>> getAdminServiceOrderList(@Query("AdminServiceOrder") String type, @Query("AdminId") int userId, @Query("PageNow") int pageNow, @Query("AreaId") int areaId);

    /**
     * 商家 超市订单
     */
    @GET(URL_GET_ORDER_DATA)
    Observable<ResultResponse<List<ShopOrder>>> getOrderList(@Query("StoreShopOrder") String type, @Query("StoreId") int userId, @Query("PageNow") int pageNow, @Query("AreaId") int areaId);

    /**
     * 更改订单状态
     */
    @FormUrlEncoded
    @POST(URL_POST_ORDER_DATA)
    Observable<ResultResponse<String>> changeState(@Field("Method") String method, @Field("OrderNo") String orderNo, @Field("CourierId") String courierId);

    /**
     * 更改订单状态
     */
    @FormUrlEncoded
    @POST(URL_POST_ORDER_DATA)
    Observable<ResultResponse<String>> changeState(@Field("Method") String method, @Field("OrderNo") String orderNo, @Field("CourierId") String courierId, @Field("OrderType") int orderType);

    /**
     * 更改订单状态
     */
    @FormUrlEncoded
    @POST(URL_POST_ORDER_DATA)
    Observable<ResultResponse<String>> changeState(@Field("Method") String method, @Field("OrderNo") String orderNo, @Field("CourierId") String courierId, @Field("ProductInfo") String productInfo);

    /**
     * 完成工作
     */
    @FormUrlEncoded
    @POST(URL_POST_ORDER_DATA)
    Observable<ResultResponse<String>> finishWork(@FieldMap Map<String, String> params);

    /**
     * 提现
     */
    @FormUrlEncoded
    @POST(URL_POST_USER_DATA)
    Observable<ResultResponse<String>> withdrawals(@FieldMap Map<String, String> map);

    /**
     * 修改派送员状态
     */
    @FormUrlEncoded
    @POST(URL_POST_USER_DATA)
    Observable<ResultResponse<String>> changeWorkState(@Field("Method") String method, @Field("CourierId") String orderNo, @Field("State") String state);

    /**
     * 提现记录
     */
    @GET(URL_GET_USER_DATA + "?Withdrawalslog=true")
    Observable<ResultResponse<List<WithdrawLog>>> getWithdrawLog(@Query("UserId") String userId, @Query("Identity") String identity);

    /**
     * 派送员获取订单数量
     */
    @GET(URL_GET_USER_DATA + "?orderCountInfo=true")
    Observable<ResultResponse<OrderCount>> getOrderCountInfo(@Query("courierId") String courierId, @Query("areaId") String areaId);

    /**
     * 商品订单信息
     */
    @GET(URL_GET_ORDER_DATA + "?ShopOrderInfo=true")
    Observable<ResultResponse<List<SenderOrdersDetail>>> getGoodsOrderInfo(@Query("OrderNo") String orderNo);

    /**
     * 搜索订单列表
     */
    @GET(URL_GET_ORDER_DATA + "?SearchOrderList=true")
    Observable<ResultResponse<List<SenderGoodsOrders>>> searchOrder(@Query("CourierId") String CourierId, @Query("Keyword") String Keyword, @Query("PageNow") int pageNow);

    /**
     * 重新分配派送员  管理员
     */
    @FormUrlEncoded
    @POST(URL_POST_ORDER_DATA)
    Observable<ResultResponse<String>> reDistribute(@Field("Method") String method, @Field("OrderNo") String orderNo, @Field("ResetType") int type);

    /**
     * 商家确认供货
     */
    @FormUrlEncoded
    @POST(URL_POST_ORDER_DATA)
    Observable<ResultResponse<String>> confirmSupply(@Field("Method") String method, @Field("OrderNo") String orderNo, @Field("StoreId") int id);

    /**
     * 预约订单信息
     */
    @GET(URL_GET_ORDER_DATA + "?OrderInfo=true")
    Observable<ResultResponse<List<Orders>>> getServiceOrderInfo(@Query("OrderNo") String orderNo);

    /**
     * 商家 我的商品列表
     */
    @GET(URL_GET_ORDER_DATA + "?StoreProducts=true")
    Observable<ResultResponse<List<Goods>>> getStoreProducts(@Query("StoreId") String storeId, @Query("pageNow") int pageNow);

    /**
     * 供货商家信息
     */
    @GET(URL_GET_SUPERMARKET_DATA + "?ShopStoreList=true")
    Observable<ResultResponse<List<ShopStore>>> getShopStoreList(@Query("AreaId") int areaId, @Query("ProductId") int productId);


    /**
     * 商品详情
     */
    @GET(URL_GET_SUPERMARKET_DATA + "?ShopInfo=true")
    Observable<ResultResponse<List<Goods>>> getGoodsInfo(@Query("ShopId") int id);


    /**
     * 获取消费金额明细
     */
    @GET(URL_GET_USER_DATA + "?managerBalance=true")
    Observable<ResultResponse<List<ConsumeRecord>>> getBalanceInfo(@Query("userId") int userId, @Query("PageNow") int pageNow);


}
