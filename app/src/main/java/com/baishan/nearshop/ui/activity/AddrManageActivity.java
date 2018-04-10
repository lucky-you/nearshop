package com.baishan.nearshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.view.EmptyView;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.model.Address;
import com.baishan.nearshop.model.AddressItem;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.presenter.AddrManagePresenter;
import com.baishan.nearshop.ui.adapter.AddrManageAdapter;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.IAddrManageView;

import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddrManageActivity extends BaseMvpActivity<AddrManagePresenter> implements IAddrManageView {

    private AddrManageAdapter adapter;
    private List<AddressItem> data = new ArrayList<>();
    private Button btnAdd;
    private AddressItem curItem;
    private int position;

    public static final int INTENT_DEFAULT = 1;
    public static final int INTENT_SELECT = 2;
    private int type;
    private String className;
    private Area area;


    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_addr_manage);
    }

    @Override
    protected void bindViews() {
        btnAdd = get(R.id.btnAdd);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initTitle("收货地址");
        if (!checkLogin()) {
            finish();
            return;
        }
        type = getIntent().getIntExtra(ConstantValue.TYPE, INTENT_DEFAULT);
        className = getIntent().getStringExtra(ConstantValue.CLASSNAME);
        Serializable extra = getIntent().getSerializableExtra(ConstantValue.AREA);
        if (extra != null) area = (Area) extra;
        registerEvent();
        adapter = new AddrManageAdapter(data);
        adapter.setEmptyView(new EmptyView(mContext));
        initCommonRecyclerView(adapter, null);
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (type == INTENT_SELECT) {
            showToast("请选择一个地址");
        }
    }

    @Override
    protected void setListener() {
        btnAdd.setOnClickListener(v -> {
            if (type == INTENT_DEFAULT) {
                intent2Activity(AddAddrActivity.class);
            } else {
                Intent it = new Intent(mContext, AddAddrActivity.class);
                it.putExtra(ConstantValue.AREA, area == null ? currentArea : area);
                it.putExtra(ConstantValue.TYPE, AddAddrActivity.INTENT_ADD);
                startActivity(it);
            }
        });
        adapter.setOnRecyclerViewItemChildClickListener((baseQuickAdapter, view, i) -> {
            curItem = data.get(i);
            position = i;
            switch (view.getId()) {
                case R.id.tvAdd:
                    Intent it = new Intent(mContext, AddAddrActivity.class);
                    it.putExtra(ConstantValue.AREA, data.get(i).area);
                    it.putExtra(ConstantValue.TYPE, AddAddrActivity.INTENT_ADD);
                    startActivity(it);
                    break;
                case R.id.tvEdit:
                    Intent it1 = new Intent(mContext, AddAddrActivity.class);
                    it1.putExtra(ConstantValue.ADDRESS, curItem.address);
                    it1.putExtra(ConstantValue.TYPE, AddAddrActivity.INTENT_EDIT);
                    startActivity(it1);
                    break;
                case R.id.tvDelete:
                    new AlertDialog.Builder(mContext)
                            .setMessage("确定删除这个地址吗？")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", (dialog, which) -> {
                                dialog.dismiss();
                                Map<String, Object> params = new HashMap();
                                params.put("Method", "DeleteAddress");
                                params.put("UserId", user.UserId);
                                params.put("AreaId", curItem.address.area.AreaId);
                                params.put("AddressId", curItem.address.AddressId);
                                mvpPresenter.doAddress(params);
                            }).show();
                    break;
                case R.id.cbDefault:
                    Map<String, Object> params1 = new HashMap();
                    params1.put("Method", "SetDefaultAddress");
                    params1.put("UserId", user.UserId);
                    params1.put("AreaId", curItem.address.area.AreaId);
                    params1.put("AddressId", curItem.address.AddressId);
                    mvpPresenter.doAddress(params1);
                    break;
            }

        });
        if (type == INTENT_SELECT) {
            adapter.setOnRecyclerViewItemClickListener((view, i) -> {
                AddressItem item = data.get(i);
                if (item.getItemType() == AddressItem.ADDRESS) {
                    Area area = new Area();
                    Area a = item.address.area;
                    area.City = a.City;
                    area.County = a.County;
                    area.AreaName = a.AreaName;
                    area.AreaId = a.AreaId;
                    List<Address> addresses = new ArrayList<>();
                    addresses.add(item.address);
                    area.AddressInfo = addresses;
                    post(new Notice(ConstantValue.MSG_TYPE_SELECTED_ADDRESS, area, className));
                    finish();
                }
            });
        }
    }

    @Override
    protected AddrManagePresenter createPresenter() {
        return new AddrManagePresenter(this);
    }

    @Subscribe
    public void onEvent(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_UPDATE_ADDRESS) {
            if (notice.content == null) {
                getData();
            } else {
                curItem.address = (Address) notice.content;
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void getData() {
        mvpPresenter.getAddressList(user.UserId, type == INTENT_SELECT ? (area != null ? area.AreaId : currentArea.AreaId) : -1);
    }

    @Override
    public void deleteSuccess() {
        if (curItem.address.isDefault) {
            if (curItem.address.area.AreaId == currentArea.AreaId) {
                currentArea.defaultAddress = null;
            }
            getData();
        }
        if (curItem.address.area.AddressInfo.size() == 1) {
            data.remove(position - 1);
            data.remove(position - 1);
        } else {
            curItem.address.area.AddressInfo.remove(curItem.address);
            data.remove(position);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setDefaultSuccess() {
        for (Address a : curItem.address.area.AddressInfo) {
            a.isDefault = false;
        }
        curItem.address.isDefault = true;
        adapter.notifyDataSetChanged();
        //修改的是当前商区
        if (curItem.address.area.AreaId == currentArea.AreaId) {
            currentArea.defaultAddress = curItem.address;
        }
    }

    @Override
    public void getAddressListSuccess(List<Area> response) {
        adapter.getData().clear();
        for (Area area : response) {
            data.add(new AddressItem(area));
            for (int i = 0; i < area.AddressInfo.size(); i++) {
                Address a = area.AddressInfo.get(i);
                if (i == 0) {
                    a.isDefault = true;
                    if (area.AreaId == currentArea.AreaId) {
                        currentArea.defaultAddress = a;
                    }
                }
                a.area = area;
                data.add(new AddressItem(a));
            }
            data.add(new AddressItem());
        }
        adapter.notifyDataSetChanged();
    }
}
