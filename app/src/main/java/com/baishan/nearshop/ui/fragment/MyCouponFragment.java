package com.baishan.nearshop.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.view.EmptyView;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpFragment;
import com.baishan.nearshop.model.Coupon;
import com.baishan.nearshop.presenter.CouponPresenter;
import com.baishan.nearshop.ui.activity.CouponActivity;
import com.baishan.nearshop.ui.adapter.CouponAdapter;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.ICouponView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/11 0011.
 */
public class MyCouponFragment extends BaseMvpFragment<CouponPresenter> implements ICouponView {
    private RecyclerView rvMyCoupon;
    private CouponAdapter adapter;
    public final static int TYPE_NOW = 1;
    public final static int TYPE_HISTORY = 2;
    private int mCouponType = 1;
    private int mAreaId ;
    private int mType ;
    private int mPrice;
    private List<Coupon> data = new ArrayList<>();

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return View.inflate(getActivity(), R.layout.fragment_my_coupon, null);
    }

    @Override
    protected void bindViews(View view) {
        rvMyCoupon = (RecyclerView) view.findViewById(R.id.rvMyCoupon);
    }


    @Override
    protected void processLogic() {
        mCouponType = getArguments().getInt(ConstantValue.COUPON_TYPE);
        mType = getArguments().getInt(ConstantValue.TYPE);
        mAreaId = getArguments().getInt(ConstantValue.AREA_ID);
        mPrice = getArguments().getInt(ConstantValue.PRICE, -1);
        adapter = new CouponAdapter(data, mCouponType);
        adapter.setEmptyView(new EmptyView(mContext));
        rvMyCoupon.setLayoutManager(new LinearLayoutManager(mContext));
        rvMyCoupon.setAdapter(adapter);
        if (mCouponType == TYPE_NOW) {
            mvpPresenter.getUserCoupons("True", mAreaId,user.UserId, mPrice);
        }
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        if (mCouponType == TYPE_HISTORY) {
            mvpPresenter.getUserCoupons("History",mAreaId, user.UserId, mPrice);
        }
    }

    @Override
    protected void setListener() {
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                if(mType== CouponActivity.INTENT_TYPE_SELECTED&&mCouponType==TYPE_NOW)
                {
                    //选择优惠券
                    post(new Notice(ConstantValue.MSG_TYPE_SELECTED_COUPON,data.get(i)));
                    getActivity().finish();
                }
            }
        });
    }

    @Override
    protected CouponPresenter createPresenter() {
        return new CouponPresenter(this);
    }

    @Override
    public void onGetCouponsSuccess(List<Coupon> response) {
        adapter.addData(response);
    }
}
