package com.baishan.nearshopclient.ui.adapter;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.base.BaseApplication;
import com.baishan.nearshopclient.model.SelectedStore;
import com.baishan.nearshopclient.model.SenderOrdersDetail;
import com.baishan.nearshopclient.model.StoreListBean;
import com.baishan.nearshopclient.model.UserInfo;
import com.baishan.nearshopclient.utils.OrdersConstants;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/2 0002.
 */

public class GoodsOrderDetailAdapter extends BaseQuickAdapter<SenderOrdersDetail.ProductItemBean> {


    private Map<String, SelectedStore> mSelectedStore;
    private int mOrderState;

    public GoodsOrderDetailAdapter(List<SenderOrdersDetail.ProductItemBean> data, Map<String, SelectedStore> selectedStore) {
        super(R.layout.item_detail_goods, data);
        mSelectedStore = selectedStore;
    }

    public void setOrderState(int mOrderState) {
        this.mOrderState = mOrderState;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SenderOrdersDetail.ProductItemBean productItem) {
        ImageLoaderUtils.displayImage(productItem.getImage(), baseViewHolder.getView(R.id.ivPic));
        baseViewHolder
                .setText(R.id.goods_name, productItem.Title)
                .setText(R.id.tvPrice, "￥" + productItem.Price + " X " + productItem.Num);
        if (!TextUtils.isEmpty(productItem.Spec)) {
            baseViewHolder.setText(R.id.tvSpec, "规格：" + productItem.Spec);
        } else {
            baseViewHolder.setText(R.id.tvSpec, "");
        }
        if (BaseApplication.getInstance().getUserInfo().getIdentity() == UserInfo.SENDER) {


            baseViewHolder.setVisible(R.id.tvSelect, mOrderState == OrdersConstants.ORDER_STATE_WAIT_SENDER && TextUtils.isEmpty(productItem.StoreName))//待操作
                    .setVisible(R.id.tvStoreName, !TextUtils.isEmpty(productItem.StoreName))
                    .setText(R.id.tvStoreName, productItem.StoreName)
                    .setOnClickListener(R.id.tvSelect, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //选择供货商
                            showSelectStore(productItem, baseViewHolder);
                        }
                    })
                    .setOnClickListener(R.id.tvStoreName, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOrderState == OrdersConstants.ORDER_STATE_WAIT_SENDER) {
                                //选择供货商
                                showSelectStore(productItem, baseViewHolder);
                            }
                        }
                    });
        }
    }

    private void showSelectStore(SenderOrdersDetail.ProductItemBean item, BaseViewHolder baseViewHolder) {
        List<StoreListBean> storeList = item.StoreList;
        Dialog dialog = new Dialog(mContext);
        View dialogView = View.inflate(mContext, R.layout.dialog_selecte_store, null);
        RadioGroup rg = (RadioGroup) dialogView.findViewById(R.id.rg);
//        TextView tvEnter = (TextView) dialogView.findViewById(R.id.tvEnter);
//        TextView tvCancel = (TextView) dialogView.findViewById(R.id.tvCancel);
        rg.removeAllViews();
        for (int i = 0; i < storeList.size(); i++) {

            StoreListBean storeListBean = storeList.get(i);

            RadioButton rb = new RadioButton(mContext);
            rb.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            rb.setText(storeListBean.StoreName);
            rg.addView(rb);
            if (i == storeList.size() - 1) {
                //最后一个 ，手动加一个`无货`
                RadioButton lastRb = new RadioButton(mContext);
                lastRb.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                lastRb.setText("无货");
                rg.addView(lastRb);
            }

        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                int i = group.indexOfChild(rb);
                if (rb.isChecked()) {
                    String storeName = "";
                    int viewHolderPosition = getViewHolderPosition(baseViewHolder);
                    SenderOrdersDetail.ProductItemBean productItemBean = mData.get(viewHolderPosition);
                    if (i == storeList.size()) {
                        //选择了`无货`
                        storeName = "无货";
                        mSelectedStore.put(productItemBean.CartToken, new SelectedStore(productItemBean.CartToken, 0));
                    } else {

                        storeName = storeList.get(i).StoreName;
                        mSelectedStore.put(productItemBean.CartToken, new SelectedStore(productItemBean.CartToken, storeList.get(i).StoreId));
                    }
                    dialog.dismiss();
                    item.StoreName = storeName;
                    baseViewHolder.setText(R.id.tvStoreName, storeName)
                            .setVisible(R.id.tvStoreName, true)
                            .setVisible(R.id.tvSelect, false);
                }


//                boolean isChecked = false;
//                String storeName = "";
//                for (int i = 0; i < rg.getChildCount(); i++) {
//                    RadioButton child = (RadioButton) rg.getChildAt(i);
//                    if (child.isChecked()) {
//                        isChecked = true;
//                        SenderOrdersDetail.ProductItemBean productItemBean = mData.get(viewHolderPosition);
//                        if (i == storeList.size()) {
//                            //选择了`无货`
//                            storeName = "无货";
//                            mSelectedStore.put(productItemBean.CartToken, new SelectedStore(productItemBean.CartToken, 0));
//                        } else {
//                            storeName = storeList.get(i).StoreName;
//                            mSelectedStore.put(productItemBean.CartToken, new SelectedStore(productItemBean.CartToken, storeList.get(i).StoreId));
//                        }
////                        productItemBean.StoreName = storeName;
//
//                        break;
//                    }
//                }
//                if (!isChecked) {
//                    ToastUtils.showToast("请选择供货商");
//                } else {
//                    dialog.dismiss();
////                    notifyItemChanged(baseViewHolder.getAdapterPosition());
//                    baseViewHolder.setText(R.id.tvStoreName, storeName)
//                            .setVisible(R.id.tvStoreName, true)
//                            .setVisible(R.id.tvSelect, false);
//                }
                Logger.i(mSelectedStore.toString());


            }
        });
//        tvEnter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        tvCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        Window win = dialog.getWindow();
        win.setWindowAnimations(android.R.style.Animation_InputMethod);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = mContext.getResources().getDisplayMetrics().widthPixels;
        win.setBackgroundDrawableResource(android.R.color.transparent);
        win.setAttributes(lp);
        win.setGravity(Gravity.CENTER);
        dialog.show();
    }

}
