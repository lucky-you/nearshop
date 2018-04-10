package com.baishan.nearshop.ui.adapter;

import android.widget.CheckBox;

import com.baishan.nearshop.R;
import com.baishan.nearshop.model.Address;
import com.baishan.nearshop.model.AddressItem;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class AddrManageAdapter extends BaseMultiItemQuickAdapter<AddressItem> {
    public AddrManageAdapter(List<AddressItem> data) {
        super(data);
        addItemType(AddressItem.ADDRESS, R.layout.item_addr_manage);
        addItemType(AddressItem.BUSINESS, R.layout.item_addr_business);
        addItemType(AddressItem.DECORATION, R.layout.item_decoration_grey);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, AddressItem address) {
        switch (baseViewHolder.getItemViewType()) {
            case AddressItem.ADDRESS:
                Address a = address.address;
                baseViewHolder.setText(R.id.tvDetail, a.Address + " " + a.Contact + " " + a.Phone)
                        .setOnClickListener(R.id.tvEdit, new OnItemChildClickListener())
                        .setOnClickListener(R.id.tvDelete, new OnItemChildClickListener())
                        .setOnClickListener(R.id.cbDefault, new OnItemChildClickListener());
                CheckBox cbDefault = baseViewHolder.getView(R.id.cbDefault);
                if (a.isDefault) {
                    baseViewHolder.setBackgroundColor(R.id.llArea, mContext.getResources().getColor(R.color.font_pink))
                            .setTextColor(R.id.tvDetail, mContext.getResources().getColor(R.color.white));
                    cbDefault.setChecked(true);
                    cbDefault.setClickable(false);
                    cbDefault.setText("默认地址");
                } else {
                    baseViewHolder.setBackgroundColor(R.id.llArea, mContext.getResources().getColor(R.color.bg_grey))
                            .setTextColor(R.id.tvDetail, mContext.getResources().getColor(R.color.font_black));
                    cbDefault.setChecked(false);
                    cbDefault.setClickable(true);
                    cbDefault.setText("设为默认地址");
                }
                break;
            case AddressItem.BUSINESS:
                baseViewHolder.setText(R.id.tvArea, address.area.concatAddress())
                        .setOnClickListener(R.id.tvAdd, new OnItemChildClickListener());
                break;
        }
    }
}
