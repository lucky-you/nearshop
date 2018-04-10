package com.baishan.nearshopclient.ui.adapter;

import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.model.ConsumeRecord;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 作者：ZhouBin  2017/3/17 14:10
 * 邮箱：1021237228@qq.com
 * 作用：金额明细的adapter
 */

public class ConsumeRecordAdapter extends BaseQuickAdapter<ConsumeRecord> {

    public ConsumeRecordAdapter(List<ConsumeRecord> data) {
        super(R.layout.item_consume_record, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ConsumeRecord record) {
        String s = (record.State == 1 ? "+" : "-") + record.Moneys;
        holder.setText(R.id.tvMoney, s)
                .setText(R.id.tvLeftMoney, "￥" + record.Balance)
                .setText(R.id.tvDate, record.CreateTime)
                .setText(R.id.tv_orderNumber, "订单编号：" + record.OrderNo);
    }
}
