package com.baishan.nearshop.ui.adapter;

import com.baishan.nearshop.R;
import com.baishan.nearshop.model.PushParser;
import com.baishan.nearshop.utils.DateUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/21.
 */
public class MessageAdapter extends BaseQuickAdapter<PushParser> {
    public MessageAdapter(List<PushParser> data) {
        super(R.layout.item_message, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, PushParser message) {
        holder.setText(R.id.tv_content, message.getPushContent())
                .setVisible(R.id.tvDot, !message.getIsRead())
                .setText(R.id.tv_time, DateUtils.getShortTime(message.getPushTime()));
    }
}
