package com.baishan.nearshopclient.utils;

/**
 * Created by RayYeung on 2016/12/1.
 */

public interface OrdersConstants {

    /**
     * 待支付
     */
    int ORDER_STATE_WAIT_PAY = 0;
    /**
     * 已付款
     */
    int ORDER_STATE_PAID = 10;
    /**
     * 分配派送员
     */
    int ORDER_STATE_MATCH_SENDER = 20;
    /**
     * 等待派送员操作
     */
    int ORDER_STATE_WAIT_SENDER = 25;
    /**
     * 已接单
     */
    int ORDER_STATE_RECEIVIED_ORDER = 30;
    /**
     * 配送中
     */
    int ORDER_STATE_SENDING = 40;
    /**
     * 已送达
     */
    int ORDER_STATE_DELIVERED = 45;
    /**
     * 已完成
     */
    int ORDER_STATE_FINISHED = 50;
    /**
     * 已取消
     */
    int ORDER_STATE_CANCEL_ORDER = 55;
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
    /**
     * 退款确认
     */
    int ORDER_ConfirmSupplyTk = 75;
}
