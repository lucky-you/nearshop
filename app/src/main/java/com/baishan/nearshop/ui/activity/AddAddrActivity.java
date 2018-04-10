package com.baishan.nearshop.ui.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.VerificationUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.model.Address;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.presenter.AddAddrPresenter;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.IAddAddrView;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/3/24.
 */
public class AddAddrActivity extends BaseMvpActivity<AddAddrPresenter> implements IAddAddrView {


    @BindView(R.id.tvCurrent)
    TextView tvCurrent;
    @BindView(R.id.tvChange)
    TextView tvChange;
    @BindView(R.id.consigneeEt)
    EditText consigneeEt;
    @BindView(R.id.telEt)
    EditText telEt;
    @BindView(R.id.tvCurrentAddress)
    TextView tvCurrentAddress;
    @BindView(R.id.etDetail)
    EditText etDetail;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    @BindView(R.id.ll_business)
    LinearLayout llBusiness;

    //在当前商区上添加
    public static final int INTENT_DEFAULT = 1;
    //在传过来的商区上添加
    public static final int INTENT_ADD = 2;
    public static final int INTENT_EDIT = 3;
    private int type;

    private Area selectedArea = new Area();
    private Address address;
    private String className;


    @Override
    protected AddAddrPresenter createPresenter() {
        return new AddAddrPresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_addr_add);
    }

    @Override
    protected void bindViews() {
        ButterKnife.bind(this);
        initTitle("新增地址");
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        registerEvent();
        type = getIntent().getIntExtra(ConstantValue.TYPE, 1);
        className = getIntent().getStringExtra(ConstantValue.CLASSNAME);
        switch (type) {
            case INTENT_DEFAULT:
                selectedArea.AreaId = currentArea.AreaId;
                setAreaInfo(currentArea);
                break;
            case INTENT_ADD:
                llBusiness.setVisibility(View.GONE);
                selectedArea = (Area) getIntent().getSerializableExtra(ConstantValue.AREA);
                setAreaInfo(selectedArea);
                break;
            case INTENT_EDIT:
                initTitle("编辑地址");
                llBusiness.setVisibility(View.GONE);
                address = (Address) getIntent().getSerializableExtra(ConstantValue.ADDRESS);
                selectedArea.AreaId = address.area.AreaId;
                consigneeEt.setText(address.Contact);
                telEt.setText(address.Phone);
                etDetail.setText(address.Address);
                tvCurrentAddress.setText(address.area.concatAddress());
                break;
        }
    }

    private void setAreaInfo(Area area) {
        if (area != null) {
            tvCurrent.setText(area.AreaName);
            tvCurrentAddress.setText(area.concatAddress());
        }
    }


    @Override
    protected void setListener() {

    }

    @OnClick({R.id.btnAdd, R.id.tvChange})
    void click(View v) {
        if (v.getId() == R.id.tvChange) {
            intent2Activity(SelectAddrActivity.class);
        } else {
            add();
        }
    }

    private void add() {
        String consignee = consigneeEt.getText().toString();
        String phone = telEt.getText().toString();
        String detail = etDetail.getText().toString();
        if (TextUtils.isEmpty(consignee)) {
            showToast(consigneeEt.getHint().toString());
            return;
        }
        if (!VerificationUtils.checkPhone(phone)) {
            showToast("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(detail)) {
            showToast(etDetail.getHint().toString());
            return;
        }
        Map<String, Object> params = new HashMap();
        if (type == INTENT_EDIT) {
            params.put("Method", "EditAddress");
            params.put("AddressId", address.AddressId);
            address.Contact = consignee;
            address.Phone = phone;
            address.Address = detail;
        } else {
            params.put("Method", "AddAddress");
        }
        params.put("UserId", user.UserId);
        params.put("AreaId", selectedArea.AreaId);
        params.put("Contact", consignee);
        params.put("Phone", phone);
        params.put("Address", detail);
        mvpPresenter.doAddress(params);
    }

    @Override
    public void addSuccess() {
        showToast("添加地址成功");
        post(new Notice(ConstantValue.MSG_TYPE_UPDATE_ADDRESS));
        finish();
    }

    @Override
    public void editSuccess() {
        showToast("修改地址成功");
        post(new Notice(ConstantValue.MSG_TYPE_UPDATE_ADDRESS, address, className));
        finish();
    }

    @Subscribe
    public void onEvent(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_UPDATE_AREA) {
            getCommonData();
            selectedArea.AreaId = currentArea.AreaId;
            setAreaInfo(currentArea);
        }
    }
}
