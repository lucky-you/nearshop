package com.baishan.nearshop.utils;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public interface ConstantValue {
    /**
     * 登陆或注销发送次通知
     */
    int MSG_TYPE_UPDATE_USER = 100;
    int MSG_TYPE_EDIT_INFO = 101;
    /**
     * 添加或修改地址成功
     */
    int MSG_TYPE_UPDATE_ADDRESS = 102;
    /**
     * 商区变更
     */
    int MSG_TYPE_UPDATE_AREA = 103;
    /**
     * 重新测量ViewPager
     */
    int MSG_TYPE_MEASURE_VIEW_PAGER = 104;
    //选择地址
    int MSG_TYPE_SELECTED_ADDRESS = 105;

    //更新服务订单信息
    int MSG_TYPE_UPDATE_ORDERS_SERVICE = 106;


    /**
     * 订单已付款
     */
    int MSG_TYPE_ORDERS_PAID = 107;
    //选择优惠券
    int MSG_TYPE_SELECTED_COUPON = 108;

    //ConfirmOrderAdapter发出的，选择地址主界面更新最新的地址字段的值
    int MSG_TYPE_SELECTED_ADDRESS_FROM_ADAPTER = 109;
    //ConfirmOrderAdapter发出的，选择优惠券主界面更新最新的优惠券字段的值
    int MSG_TYPE_SELECTED_COUPON_FROM_ADAPTER = 110;
    /**
     * 更新商品订单信息
     */
    int MSG_TYPE_UPDATE_SHOP_ORDER = 111;
    int MSG_TYPE_NEW_MESSAGE = 112;
    /**
     * 更新购物车
     */
    int MSG_TYPE_SHOPCAR_UPDATE = 113;
    /**
     * 搜索
     */
    int MSG_TYPE_SEARCH_KEYWORD = 114;

    int MSG_TYPE_SHOPCAR_UPDATE_FINISH = 115;
    int MSG_TYPE_MESSAGE_POST_REPLY = 116;
    int MSG_TYPE_UPDATE_REWARD = 117;

    long VP_TURN_TIME = 5000;

    String CATEGORY_TYPE = "categoryType";
    String CATEGORY_FLAG = "categoryFlag";
    String TYPE = "type";
    String TITLE = "title";
    String GOODS = "goods";
    String SP_USER_TOKEN = "spUserToken";
    String SP_AREAID = "spAreaId";
    String IS_FIRST = "isFirst";
    String ADDRESS = "address";
    String AREA = "area";
    String AREA_ID = "areaId";
    String SERVICE = "service";
    String DATA_CATEGORY = "dataCategory";
    String DATA_TYPE = "dataType";

    String COUNT = "count";
    String ADAPTER = "adapter";
    String LIST = "list";
    String COUPON_TYPE = "couponType";
    String PRICE = "price";
    String DATA = "DATA";
    String ORDERNO = "orderNo";
    String CLASSNAME = "className";
    String URL = "url";
    String TABPOSITION = "tabPosition";
    String POST_ID = "postId";


    /**
     * 分配派送员
     */
    int ORDER_STATE_MATCH_SENDER = 0;
    /**
     * 已预约
     */
    int ORDER_STATE_RESERVED = 10;
    /**
     * 已接单
     */
    int ORDER_STATE_RECEIVIED_ORDER = 20;
    /**
     * 开始工作
     */
    int ORDER_STATE_START_WORK = 25;
    /**
     * 完成工作
     */
    int ORDER_STATE_FINISH_WORK = 30;
    /**
     * 已付款
     */
    int ORDER_STATE_PAID = 35;
    /**
     * 已完成
     */
    int ORDER_STATE_FINISHED = 40;
    /**
     * 派送员取消订单
     */
    int ORDER_STATE_SENDER_CANCEL_ORDER = 50;
    /**
     * 用户取消订单
     */
    int ORDER_STATE_USER_CANCEL_ORDER = 55;
    /**
     * 申请退款
     */
    int ORDER_STATE_APPLY_REFUND = 60;
    /**
     * 拒绝退款
     */
    int ORDER_STATE_REJECT_REFUND = 65;
    /**
     * 退款成功
     */
    int ORDER_STATE_REFUND_FINISHED = 70;


    String EXTRA_INTENT = "extraIntent";
    String DBID = "db_Id";

    /**
     * 发帖长度限制
     */
    int POST_LENGTH = 200;
    int REQUEST_IMAGE = 222;

    String CHAT_NAME = "chatName";
    String CHAT_IMAGE = "chatImage";

    /**
     * 不是全市够
     */
    int CATEGORYID = 4;

}
