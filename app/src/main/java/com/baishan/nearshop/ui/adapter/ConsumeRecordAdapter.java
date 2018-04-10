package com.baishan.nearshop.ui.adapter;

import com.baishan.nearshop.R;
import com.baishan.nearshop.model.ConsumeRecord;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by RayYeung on 2017/1/13.
 */

public class ConsumeRecordAdapter extends BaseQuickAdapter<ConsumeRecord> {
    public ConsumeRecordAdapter(List<ConsumeRecord> data) {
        super(R.layout.item_consume_record, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ConsumeRecord record) {
        String s = (record.State == 1 ? "+" : "-") + record.Moneys + "[" + record.PayType + "]";
        holder.setText(R.id.tvMoney, s)
                .setText(R.id.tvLeftMoney, "￥" + record.Balance)
                .setText(R.id.tvDesc, record.Contents)
                .setText(R.id.tvDate, record.CreateTime)
                .setText(R.id.orderNumber, "订单编号：" + record.OrderNo);
    }
}
