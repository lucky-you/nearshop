package com.baishan.nearshopclient.ui.adapter;


import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.model.PushParser;
import com.baishan.nearshopclient.utils.DateUtils;
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
    protected void convert(BaseViewHolder baseViewHolder, PushParser message) {
        baseViewHolder.setText(R.id.tv_content, message.getPushTitle())
                .setVisible(R.id.tvDot, !message.getIsRead())
                .setText(R.id.tv_time, DateUtils.getShortTime(message.getPushTime()));
    }
}
