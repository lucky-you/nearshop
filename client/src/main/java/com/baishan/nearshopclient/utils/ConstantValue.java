package com.baishan.nearshopclient.utils;

/**
 * Created by RayYeung on 2016/9/25.
 */
public interface ConstantValue {

    String GOODS_ID = "goodsId";
    String TYPE = "type";
    String INDEX = "index";
    String POSITION = "position";
    /**
     * 登陆或注销发送次通知
     */
    int MSG_TYPE_UPDATE_USER = 100;
    int MSG_TYPE_UPDATE_ORDER = 101;
    /**
     * 关闭便民订单的activity
     */
    int MSG_FINISH_ORDERS_ACTIVITY = 102;
    /**
     * 关闭商品订单详情的activity
     */
    int MSG_FINISH_GOODS_ORDERS_ACTIVITY = 103;
    /**
     * 更新超市订单
     */
    int MSG_UPDATE_SHOP_ORDERS = 104;
    String SP_USER_TOKEN = "spUserToken";
    String SP_IS_LOGIN = "spIsLogin";
    String DATA = "data";
    String ORDER_NO = "OrderNo";

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
}
