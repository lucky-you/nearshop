package com.baishan.nearshop.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.view.EmptyView;
import com.baishan.mylibrary.view.IosDialog;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpFragment;
import com.baishan.nearshop.model.Address;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.CartGoods;
import com.baishan.nearshop.model.Shopcar;
import com.baishan.nearshop.model.ShopcarItem;
import com.baishan.nearshop.presenter.ShopCarPresenter;
import com.baishan.nearshop.ui.activity.AddAddrActivity;
import com.baishan.nearshop.ui.activity.AddrManageActivity;
import com.baishan.nearshop.ui.activity.ConfirmOrdersActivity;
import com.baishan.nearshop.ui.activity.GoodsDetailActivity;
import com.baishan.nearshop.ui.activity.MainActivity;
import com.baishan.nearshop.ui.adapter.ShopcarAdapter;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.IShopCarView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class ShopCarFragment extends BaseMvpFragment<ShopCarPresenter> implements IShopCarView {

    private static final String NAME = ShopCarFragment.class.getSimpleName();
    private ImageView ivMenu;
    private SwipeRefreshLayout srl;
    private RecyclerView recyclerView;
    private CheckBox cbAll;
    private TextView tvMoney;
    private Button btnBuy;
    private LinearLayout llResult;

    private ShopcarAdapter adapter;
    private List<ShopcarItem> data = new ArrayList<>();
    private double totalPrice = 0;
    private int position;
    /**
     * 变化的数量
     */
    private int goodsNum;


    @Override
    protected ShopCarPresenter createPresenter() {
        return new ShopCarPresenter(this);
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_shopcar, container, false);
    }

    @Override
    protected void bindViews(View view) {
        ivMenu = (ImageView) view.findViewById(R.id.iv_menu);
        cbAll = (CheckBox) view.findViewById(R.id.cbAll);
        tvMoney = (TextView) view.findViewById(R.id.tvMoney);
        btnBuy = (Button) view.findViewById(R.id.btnBuy);
        llResult = (LinearLayout) view.findViewById(R.id.llResult);
        srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
    }

    @Override
    protected void processLogic() {
        registerEvent();
        adapter = new ShopcarAdapter(data);
        adapter.setEmptyView(new EmptyView(mContext));
        recyclerView = initCommonRecyclerView(adapter, null);
        getData();
        setMessageNum(MainActivity.getInstance().getShopCarNum());
    }

    private void getData() {
        getCommonData();
        if (user != null) {
            mvpPresenter.getMyShopCart(user.UserId);
        } else {
            llResult.setVisibility(View.GONE);
            cbAll.setChecked(false);
            totalPrice = 0;
            setMoney();
            data.clear();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void setListener() {
        ivMenu.setOnClickListener(v -> showMoreWindow(ivMenu));
        cbAll.setOnClickListener(v -> selectAll());
        adapter.setOnRecyclerViewItemClickListener((view, i) -> {
            if (data.get(i).getItemType() == ShopcarItem.GOODS) {
                CartGoods goods = data.get(i).goods;
                goods.Id = goods.AreaProductId;
                Intent it = new Intent(mContext, GoodsDetailActivity.class);
                it.putExtra(ConstantValue.DATA, goods.Id);
                startActivity(it);
            }
        });
        adapter.setOnRecyclerViewItemChildClickListener((baseQuickAdapter, view, i) -> itemClick(view, i));
        btnBuy.setOnClickListener(v -> gotoBuy());
        srl.setOnRefreshListener(() -> getData());
    }

    /**
     * 去结算
     */
    private void gotoBuy() {
        if (totalPrice > 0) {
//            List<Shopcar> cars = new ArrayList<>();
            StringBuffer sb = new StringBuffer();
            for (ShopcarItem item : data) {
                if (item.getItemType() == ShopcarItem.ADDRESS) {
                    Shopcar shopcar = item.address;
                    for (CartGoods goods : shopcar.CartItem) {
                        if (goods.isChecked) {
                            sb.append("|" + goods.CartToken);
                        }
                    }
//                    List<CartGoods> cgs = new ArrayList<>();
//                    Shopcar shopcar = item.address;
//                    for (CartGoods goods : shopcar.CartItem) {
//                        if (goods.isChecked) cgs.add(goods);
//                    }
//                    if (cgs.size() > 0) {
//                        Shopcar car = new Shopcar();
//                        car.AreaId = shopcar.AreaId;
//                        car.Province = shopcar.Province;
//                        car.City = shopcar.City;
//                        car.County = shopcar.County;
//                        car.AreaName = shopcar.AreaName;
//                        car.AddressInfo = shopcar.AddressInfo;
//                        car.CartItem = cgs;
//                        cars.add(car);
//                    }
                }
            }
            Intent it = new Intent(mContext, ConfirmOrdersActivity.class);
            it.putExtra(ConstantValue.DATA, sb.substring(1, sb.length()));
            startActivity(it);
        }
    }

    /**
     * Item各处点击的逻辑
     *
     * @param view
     * @param i
     */
    private void itemClick(View view, int i) {
        switch (view.getId()) {
            case R.id.cbArea:
                CheckBox box = (CheckBox) view;
                data.get(i).isChecked = box.isChecked();
                for (CartGoods goods : data.get(i).address.CartItem) {
                    if (box.isChecked()) {
                        if (!goods.isChecked)
                            totalPrice += goods.totalPrice();
                    } else {
                        if (goods.isChecked)
                            totalPrice -= goods.totalPrice();
                    }
                    goods.isChecked = box.isChecked();
                }
                setMoney();
                adapter.notifyDataSetChanged();
                break;
            case R.id.tvUpdate:
                position = i;
                Address address = data.get(i).address.AddressInfo.get(0);
                address.area = data.get(i).address;
                Intent it1 = new Intent(mContext, AddAddrActivity.class);
                it1.putExtra(ConstantValue.ADDRESS, address);
                it1.putExtra(ConstantValue.TYPE, AddAddrActivity.INTENT_EDIT);
                it1.putExtra(ConstantValue.CLASSNAME, NAME);
                startActivity(it1);
                break;
            case R.id.tvSelectOther:
                position = i;
                Intent it = new Intent(mContext, AddrManageActivity.class);
                it.putExtra(ConstantValue.CLASSNAME, NAME);
                it.putExtra(ConstantValue.TYPE, AddrManageActivity.INTENT_SELECT);
                it.putExtra(ConstantValue.AREA, data.get(i).address);
                startActivity(it);
                break;
            case R.id.cbGoods:
                CheckBox box1 = (CheckBox) view;
                CartGoods goods = data.get(i).goods;
                goods.isChecked = box1.isChecked();
                if (box1.isChecked()) {
                    totalPrice += goods.totalPrice();
                } else {
                    totalPrice -= goods.totalPrice();
                }
                setMoney();
                adapter.notifyItemChanged(i);
                break;
            case R.id.ivDelete:
                position = i;
                new AlertDialog.Builder(mContext)
                        .setTitle("提示")
                        .setMessage("您确定要删除这件商品吗？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mvpPresenter.deleteGoods(data.get(i).goods.CartToken, user.UserId);
                            }
                        }).show();
                break;
            case R.id.tvNum:
                position = i;
                IosDialog dialog = new IosDialog(mContext)
                        .builder();
                dialog.setTitle("设置数量")
                        .setEditHint("可输入1~999")
                        .setEditInputType(InputType.TYPE_CLASS_NUMBER)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String result = dialog.getResult();
                                int num = Integer.parseInt(result);
                                if (num == 0 || num > 999) {
                                    showToast("请输入合适的数量");
                                    return;
                                }
                                CartGoods cg = data.get(i).goods;
                                if (cg.Num != num) {
                                    goodsNum = num - cg.Num;
                                    mvpPresenter.changeNum(user.UserId, cg.CartToken, num);
                                }
                            }
                        }).show();
                break;
            case R.id.btnMinus:
                position = i;
                CartGoods cg = data.get(i).goods;
                if (cg.Num > 1) {
                    int num = cg.Num;
                    goodsNum = -1;
                    mvpPresenter.changeNum(user.UserId, cg.CartToken, --num);
                }
                break;
            case R.id.btnAdd:
                position = i;
                CartGoods cg1 = data.get(i).goods;
                int num = cg1.Num;
                goodsNum = 1;
                mvpPresenter.changeNum(user.UserId, cg1.CartToken, ++num);
                break;
        }

    }

    private void setMoney() {
        tvMoney.setText(String.format("%.2f", totalPrice));
    }

    private void selectAll() {
        totalPrice = 0;
        for (ShopcarItem item : data) {
            item.isChecked = cbAll.isChecked();
            if (item.goods != null)
                item.goods.isChecked = cbAll.isChecked();
            if (cbAll.isChecked() && item.getItemType() == ShopcarItem.GOODS) {
                totalPrice += item.goods.totalPrice();
            }
        }
        setMoney();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getMyShopCartSuccess(List<Shopcar> response) {
        srl.setRefreshing(false);
        llResult.setVisibility(response.size() > 0 ? View.VISIBLE : View.GONE);
        cbAll.setChecked(false);
        totalPrice = 0;
        setMoney();
        data.clear();
        int num = 0;
        for (Shopcar s : response) {
            data.add(new ShopcarItem(s));
            for (CartGoods g : s.CartItem) {
                num += g.Num;
                data.add(new ShopcarItem(g));
            }
        }
        MainActivity.getInstance().setShopCarNum(num);
        post(new Notice(ConstantValue.MSG_TYPE_SHOPCAR_UPDATE_FINISH));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void changeAddressSuccess() {

    }

    @Override
    public void deleteGoodsSuccess() {
        showToast("删除商品成功");
        getData();
    }

    @Override
    public void changeNumSuccess() {
        CartGoods cg = data.get(position).goods;
        cg.Num += goodsNum;
        if (cg.isChecked) {
            totalPrice += cg.Price * goodsNum;
            setMoney();
        }
        adapter.notifyItemChanged(position);
    }

    @Subscribe
    public void onEvent(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_UPDATE_USER) {
            getData();
        } else if (notice.type == ConstantValue.MSG_TYPE_SHOPCAR_UPDATE) {
            getData();
        } else if (notice.type == ConstantValue.MSG_TYPE_SELECTED_ADDRESS) {
            if (NAME.equals(notice.content1)) {
                Address a = ((Area) notice.content).AddressInfo.get(0);
                Address address = data.get(position).address.AddressInfo.get(0);
                if (address.AddressId != a.AddressId) {
                    address.AddressId = a.AddressId;
                    address.Address = a.Address;
                    address.Phone = a.Phone;
                    address.Contact = a.Contact;
                    adapter.notifyItemChanged(position);
                    mvpPresenter.changeAddress(user.UserId, data.get(position).address.AreaId, a.AddressId);
                }
            }
        } else if (notice.type == ConstantValue.MSG_TYPE_UPDATE_ADDRESS) {
            if (NAME.equals(notice.content1)) {
                Address a = (Address) notice.content;
                Address address = data.get(position).address.AddressInfo.get(0);
                address.Contact = a.Contact;
                address.Phone = a.Phone;
                address.Address = a.Address;
                adapter.notifyItemChanged(position);
            }
        }
    }

    @Override
    public void stopRefresh() {
        srl.setRefreshing(false);
    }
}
