package com.baishan.nearshop.ui.adapter;

import com.baishan.nearshop.R;
import com.baishan.nearshop.model.City;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/21.
 */
public class CityAdapter extends BaseQuickAdapter<City> {
    public CityAdapter(List<City> data) {
        super(R.layout.item_city, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, City city) {
        holder.setText(R.id.tvCity,city.Name);
    }
}
