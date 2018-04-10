package com.baishan.nearshopclient.ui.adapter;


import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.model.Account;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/1/10 0010.
 */

public class AccountAdapter extends BaseQuickAdapter<Account> {
    public AccountAdapter( List<Account> data) {
        super(R.layout.item_account, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Account account) {
        baseViewHolder.setText(R.id.tvPhone,account.Phone)
                .setOnClickListener(R.id.llItem,new OnItemChildClickListener());
    }
}
