package com.baishan.nearshopclient.view;

import com.baishan.nearshopclient.model.WithdrawLog;

import java.util.List;

/**
 * Created by Administrator on 2016/12/9 0009.
 */
public interface IWithdrawLogView {
    void getWithdrawLogSuccess(List<WithdrawLog> response);
}
