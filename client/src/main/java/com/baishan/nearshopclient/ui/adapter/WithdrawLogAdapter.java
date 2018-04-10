package com.baishan.nearshopclient.ui.adapter;

import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.model.WithdrawLog;
import com.baishan.nearshopclient.utils.DateUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/12/9 0009.
 */

public class WithdrawLogAdapter extends BaseQuickAdapter<WithdrawLog> {
    public WithdrawLogAdapter(List<WithdrawLog> data) {
        super(R.layout.item_withdraw_log, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, WithdrawLog withdrawLog) {
        baseViewHolder.setText(R.id.tv_content, "余额-" + withdrawLog.Price + "元!")
                .setText(R.id.tv_time, DateUtils.getShortTime(withdrawLog.CreateTime));
    }
}
