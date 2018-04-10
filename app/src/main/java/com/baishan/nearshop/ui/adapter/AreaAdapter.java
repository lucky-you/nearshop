package com.baishan.nearshop.ui.adapter;

import com.baishan.nearshop.R;
import com.baishan.nearshop.model.Area;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by RayYeung on 2016/9/21.
 */
public class AreaAdapter extends BaseQuickAdapter<Area> {

    private int type;

    public AreaAdapter(List<Area> data, int type) {
        super(R.layout.item_area, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Area area) {
        switch (type) {
            case 1:
                baseViewHolder.setText(R.id.tvArea, area.AreaId == 0 ? area.County : area.AreaName);
                break;
            case 2:
            case 3:
                baseViewHolder.setVisible(R.id.tvCityCounty,true)
                        .setText(R.id.tvCityCounty, area.concatCC())
                        .setText(R.id.tvArea, area.AreaName);
                break;
        }
    }
}
