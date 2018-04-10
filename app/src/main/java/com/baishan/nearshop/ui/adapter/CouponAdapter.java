package com.baishan.nearshop.ui.adapter;

import com.baishan.nearshop.R;
import com.baishan.nearshop.model.Coupon;
import com.baishan.nearshop.ui.fragment.MyCouponFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/3/30.
 */
public class CouponAdapter extends BaseQuickAdapter<Coupon> {


    private final int mType;

    public CouponAdapter(List<Coupon> data, int type) {
        super(R.layout.item_coupon_list, data);
        this.mType = type;
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, Coupon couponParser) {



//        Date end = null;
//        try {
//            end = new SimpleDateFormat("yyyy-MM-dd").parse(couponParser.EndTime);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        if (DateUtils.calculateDayStatus(end, new Date()) < 0) {
//            baseViewHolder.setText(R.id.tvCouponExpired,"已过期")
//                    .setVisible(R.id.tvCouponExpired, true);
//        } else {
//            baseViewHolder.setVisible(R.id.tvCouponExpired, false);
//        }

        baseViewHolder.setText(R.id.tvCouponPrompt,couponParser.Title)
                .setText(R.id.tvdenomination, String.format("%.2f",couponParser.Price))
                .setText(R.id.tvUseCondition, couponParser.Description)
                .setText(R.id.tvAreaName, couponParser.AreaName)
                .setText(R.id.tvValidityDate, "有效期：" + couponParser.StartTime.substring(0, 10) + "~" + couponParser.EndTime.substring(0, 10));
        if(mType== MyCouponFragment.TYPE_HISTORY)
        {
            //已过期
            baseViewHolder.setBackgroundRes(R.id.imgEdgeLeft,R.drawable.ic_coupon_edge_left_enable_n)
                .setTextColor(R.id.tvCouponPrompt, mContext.getResources().getColor(R.color.font_grey))
                .setTextColor(R.id.tvdenomination,mContext.getResources().getColor(R.color.font_grey))
                .setTextColor(R.id.tvPriceTag, mContext.getResources().getColor(R.color.font_grey))
                .setTextColor(R.id.tvAreaName, mContext.getResources().getColor(R.color.font_grey));
        }else {
            //未过期
            baseViewHolder.setBackgroundRes(R.id.imgEdgeLeft, R.drawable.ic_coupon_edge_left_n);
        }
    }

}
