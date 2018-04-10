package com.baishan.nearshopclient.listener;

/**
 * Created by Administrator on 2016/12/2 0002.
 */

public interface OnStateListener {
    void onState(String method, String orderNo);
    void onState(String method, String orderNo,String remark);
}
