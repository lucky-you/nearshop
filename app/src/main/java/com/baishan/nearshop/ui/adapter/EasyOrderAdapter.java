package com.baishan.nearshop.ui.adapter;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseApplication;
import com.baishan.nearshop.model.Address;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.EasyService;
import com.baishan.nearshop.ui.activity.AddrManageActivity;
import com.baishan.nearshop.ui.activity.LoginActivity;
import com.baishan.nearshop.utils.ConstantValue;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import static com.baishan.mylibrary.utils.ToastUtils.showToast;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class EasyOrderAdapter extends BaseQuickAdapter<EasyService> {

    private Area mDefaultArea;
    private Address mSelectedAddress;

    public EasyOrderAdapter(List<EasyService> data) {
        super(R.layout.item_easy_order, data);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, EasyService easyOrder) {
        ImageLoaderUtils.displayGoods(easyOrder.getImage(), baseViewHolder.getView(R.id.ivImg));
        baseViewHolder.setText(R.id.tvCompany, easyOrder.Name)
                .setText(R.id.tvDesc, easyOrder.Title)
                .setText(R.id.tvPrice, easyOrder.Price)
                .setOnClickListener(R.id.btnOrder, v -> {
                    order(easyOrder);
                })
                .setVisible(R.id.tvLicense, easyOrder.HasLicense == 1)
                .setVisible(R.id.tvIdentity, easyOrder.HasIdentity == 1)
                .setVisible(R.id.tvDeposit, easyOrder.Deposit > 0)
                .setText(R.id.tvDeposit, String.format(mContext.getString(R.string.deposit),easyOrder.Deposit));

    }

    @Subscribe
    public void onEvent(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_SELECTED_ADDRESS) {
            if (EasyOrderAdapter.class.getSimpleName().equals(notice.content1)) {
                Area area = (Area) notice.content;
                mDefaultArea = area;
                mSelectedAddress = area.AddressInfo.get(0);
                tvAreaName.setText(area.AreaName);
                tvAreaName.setOnClickListener(null);
                tvAddress.setText(mSelectedAddress.Address + " (" + mSelectedAddress.Contact + " 收" + " " + mSelectedAddress.Phone);
//            showOrderDialog(area);
            }
        }
    }

    private void order(EasyService easyOrder) {
        if (!checkLogin()) return;
        mDefaultArea = BaseApplication.getInstance().getCurrentArea();
        mSelectedAddress = mDefaultArea.defaultAddress;
        showOrderDialog(easyOrder);
    }

    private TextView tvAreaName;
    private TextView tvAddress;
    private RelativeLayout relAddress;

    private void showOrderDialog(EasyService easyOrder) {


        View content = View.inflate(mContext, R.layout.dialog_order, null);
        AlertDialog dialog = new AlertDialog.Builder(mContext).setView(content).show();
        relAddress = (RelativeLayout) content.findViewById(R.id.relAddress);
        tvAreaName = (TextView) content.findViewById(R.id.tvAreaName);
        tvAddress = (TextView) content.findViewById(R.id.tvAddress);
        TextView tvFinish = (TextView) content.findViewById(R.id.tvFinish);
        Button btnCommit = (Button) content.findViewById(R.id.btnCommit);
        EditText etRemark = (EditText) content.findViewById(R.id.etRemark);

        relAddress.setOnClickListener(v -> {
            Intent it = new Intent(mContext, AddrManageActivity.class);
            it.putExtra(ConstantValue.CLASSNAME, EasyOrderAdapter.class.getSimpleName());
            it.putExtra(ConstantValue.TYPE, AddrManageActivity.INTENT_SELECT);
            mContext.startActivity(it);
        });
        if (mSelectedAddress == null) {
            tvAreaName.setText("请选择地址");
        } else {
            tvAreaName.setText(mDefaultArea.AreaName);
            tvAddress.setText(mSelectedAddress.Address + " (" + mSelectedAddress.Contact + " 收" + " " + mSelectedAddress.Phone);
        }


        btnCommit.setOnClickListener(v -> {
            if (mSelectedAddress == null) {
                showToast("请选择地址");
                return;
            }
            dialog.dismiss();
            String remark = etRemark.getText().toString();
            if (onReservationServiceListener != null)
                onReservationServiceListener.onReservationService(mDefaultArea, mSelectedAddress, remark, easyOrder.Id);
        });
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    private onReservationServiceListener onReservationServiceListener;

    public void setOnReservationServiceListener(EasyOrderAdapter.onReservationServiceListener onReservationServiceListener) {
        this.onReservationServiceListener = onReservationServiceListener;
    }

    public interface onReservationServiceListener {
        void onReservationService(Area area, Address address, String remark, int serviceId);
    }

    public boolean checkLogin() {
        if (BaseApplication.getInstance().getUserInfo() == null) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(intent);
            return false;
        }
        return true;
    }
}
