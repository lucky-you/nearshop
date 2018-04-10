package com.baishan.nearshopclient.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.view.EmptyView;
import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.base.BaseApplication;
import com.baishan.nearshopclient.base.BaseMvpFragment;
import com.baishan.nearshopclient.dao.MessageDao;
import com.baishan.nearshopclient.listener.OnStateListener;
import com.baishan.nearshopclient.model.BasePageCount;
import com.baishan.nearshopclient.model.Orders;
import com.baishan.nearshopclient.model.SenderGoodsOrders;
import com.baishan.nearshopclient.model.ServiceOrders;
import com.baishan.nearshopclient.model.ShopOrder;
import com.baishan.nearshopclient.model.UserInfo;
import com.baishan.nearshopclient.presenter.OrdersPresenter;
import com.baishan.nearshopclient.ui.activity.GoodsOrdersDetailActivity;
import com.baishan.nearshopclient.ui.activity.OrdersActivity;
import com.baishan.nearshopclient.ui.adapter.EasyOrdersAdapter;
import com.baishan.nearshopclient.ui.adapter.SenderGoodsOrdersAdpater;
import com.baishan.nearshopclient.ui.adapter.ServiceOrdersAdaper;
import com.baishan.nearshopclient.ui.adapter.ShopOrdersAdapter;
import com.baishan.nearshopclient.utils.ConstantValue;
import com.baishan.nearshopclient.view.IOrdersView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.baishan.nearshopclient.ui.activity.OrdersActivity.ORDER_GOODS;

/**
 * Created by RayYeung on 2016/9/25.
 */
public class OrdersFragment extends BaseMvpFragment<OrdersPresenter> implements IOrdersView, OnStateListener {

    private RecyclerView recyclerView;
    private int type;
    private int index;

    private BaseQuickAdapter adapter;
    private List<SenderGoodsOrders> mDatas1 = new ArrayList<>();
    private List<ShopOrder> mDatas2 = new ArrayList<>();
    private List<ServiceOrders> mDatas3 = new ArrayList<>();
    private List<Orders> mDatas4 = new ArrayList<>();
    private SwipeRefreshLayout srl;

    @Override
    protected OrdersPresenter createPresenter() {
        return new OrdersPresenter(this);
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_refresh_recyclerview, container, false);
    }

    @Override
    protected void bindViews(View view) {
        srl = get(R.id.srl);
    }

    private String getType() {
        switch (index) {
            case 0:
                if (user.getIdentity() == UserInfo.ADMIN) {
                    return "JXZ";
                } else if (user.getIdentity() == UserInfo.SENDER) { //派送员
                    return "QD";//抢单
                }
                return "DCL";
            case 1:
                if (user.getIdentity() == UserInfo.ADMIN) {
                    return "YWC";
                }
                if (user.getIdentity() == UserInfo.SENDER) {
                    return "DCL";  //待处理
                }
                if (type == ORDER_GOODS) {
                    return "YQR";
                } else {
                    return "YJD";  //已接单
                }

            case 2:
                if (type == ORDER_GOODS) {
                    if (user.getIdentity() == UserInfo.SENDER) {
                        return "JXZ";
                    } else if (user.getIdentity() == UserInfo.BUSINESS) {

                        return "TK"; //退款
                    } else {
                        return "YQR";
                    }
                } else {
                    if (user.getIdentity() == UserInfo.SENDER) {
                        return "YJD";  //已接单
                    } else {
                        return "GZZ";
                    }
                }

            case 3:
                if (type == OrdersActivity.ORDER_GOODS) {
                    if (user.getIdentity() == UserInfo.BUSINESS) {
                        return "TKQR";//退款确认
                    } else {
                        return "YWC";
                    }
                } else {
                    if (user.getIdentity() == UserInfo.SENDER) {
                        return "GZZ";
                    } else {
                        return "YWC";
                    }
                }
            case 4:
                if (type == ORDER_GOODS) {
                    if (user.getIdentity() == UserInfo.SENDER) {
                        return "YQX"; //已取消
                    }
                } else {
                    return "YWC"; //已完成
                }
                break;
        }
        return "";
    }

    private int mPageNow = 1;

    @Override
    protected void processLogic() {
        registerEvent();
        initData();
        if (type == ORDER_GOODS) {
            if (user.getIdentity() == UserInfo.SENDER) {
                //派送员
                adapter = new SenderGoodsOrdersAdpater(mDatas1);
                ((SenderGoodsOrdersAdpater) adapter).setOnStateListener(this);
            } else {
                //商家店铺
                adapter = new ShopOrdersAdapter(mDatas2, user.getIdentity(), index);
            }
        } else {
            if (user.getIdentity() == UserInfo.SENDER) {
                //服务订单
                adapter = new ServiceOrdersAdaper(mDatas3, index);
                ((ServiceOrdersAdaper) adapter).setOnStateListener(this);
            } else {
                adapter = new EasyOrdersAdapter(mDatas4);
            }
        }
        recyclerView = initCommonRecyclerView(adapter, null);
        adapter.openLoadMore(10, true);
        adapter.setEmptyView(new EmptyView(mContext));
    }

    @Subscribe
    public void onEvent(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_UPDATE_ORDER || notice.type == ConstantValue.MSG_UPDATE_SHOP_ORDERS) {
            mPageNow = 1;
            getData();
        }
    }

    @Override
    protected void setListener() {
//        adapter.setOnRecyclerViewItemClickListener((view, i) -> {
//            if (type == OrdersActivity.ORDER_GOODS) {
//                if (user.getIdentity() == UserInfo.SENDER) {
//                    //派送员商品订单详情
//                    Intent intent = new Intent(mContext, GoodsOrdersDetailActivity.class);
//                    intent.putExtra(ConstantValue.ORDER_NO, ((BasePageCount) adapter.getData().get(i)).OrderNo);
//                    startActivity(intent);
//                }
//            }
//        });
        adapter.setOnRecyclerViewItemChildClickListener((baseQuickAdapter, view, i) -> {
            if (user.getIdentity() == UserInfo.ADMIN) {
                //重新分配派送员
                mvpPresenter.reDistribute(mDatas2.get(i).OrderNo, 1);
            } else {
                //确认
                if (index == 0) {
                    mvpPresenter.confirmSupply(mDatas2.get(i).OrderNo, user.Id);
                } else if (index == 2) {
                    mvpPresenter.ConfirmSupplyTk(mDatas2.get(i).OrderNo, user.Id);
                }
            }
        });
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNow = 1;
                getData();
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPageNow++;
                getData();
            }
        });
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        initData();
        getData();
    }

    private void getData() {
        user = BaseApplication.getInstance().getUserInfo();
        if (type == ORDER_GOODS) {
            if (user.getIdentity() == UserInfo.SENDER) {
                mvpPresenter.getSenderShopOrderList(getType(), user.Id, mPageNow);
            } else if (user.getIdentity() == UserInfo.ADMIN) {//管理员
                mvpPresenter.getAdminShopOrderList(getType(), user.Id, mPageNow);
            } else {//商家
                mvpPresenter.getOrderList(getType(), user.Id, mPageNow);
            }
        } else {
            if (user.getIdentity() == UserInfo.SENDER) {
                mvpPresenter.getServiceOrderList(getType(), user.Id, mPageNow);
            } else {
                mvpPresenter.getAdminServiceOrderList(getType(), user.Id, mPageNow);
            }
        }
    }

    private void initData() {
        type = getArguments().getInt(ConstantValue.TYPE);
        index = getArguments().getInt(ConstantValue.INDEX);
    }

    @Override
    public void getServiceOrderListSuccess(List<ServiceOrders> response) {
        onOrderSuccess(response);

    }

    private void onOrderSuccess(List<? extends BasePageCount> response) {
        srl.setRefreshing(false);
        if (mPageNow == 1) {
            adapter.getData().clear();
            adapter.notifyDataSetChanged();
        }
        if (response.size() > 0) {
            adapter.notifyDataChangedAfterLoadMore(response, mPageNow < response.get(0).PageCount);
        }
    }

    @Override
    public void changeStateSuccess(String method, String orderNo) {
        showToast("操作成功");
        post(new Notice(ConstantValue.MSG_TYPE_UPDATE_ORDER));
        if (method.equals("RobOrder") && type == ORDER_GOODS) {
            //派单
            //跳转派送员商品订单详情
            Intent intent = new Intent(mContext, GoodsOrdersDetailActivity.class);
            intent.putExtra(ConstantValue.ORDER_NO, orderNo);
            startActivity(intent);
        }
    }

    @Override
    public void getSenderShopOrderListSuccess(List<SenderGoodsOrders> response) {
        onOrderSuccess(response);
    }

    @Override
    public void getShopOrderListSuccess(List<ShopOrder> response) {
        srl.setRefreshing(false);
        if (mPageNow == 1) {
            adapter.getData().clear();
            adapter.notifyDataSetChanged();
        }
        if (response.size() > 0) {
            generateOrderItem(response);
            adapter.notifyDataChangedAfterLoadMore(mPageNow < response.get(0).PageCount);
        }
    }

    @Override
    public void reDistributeSuccess() {
        showToast("操作成功");
        mPageNow = 1;
        getData();
    }

    @Override
    public void getAdminServiceOrderListSuccess(List<Orders> response) {
        srl.setRefreshing(false);
        if (mPageNow == 1) {
            adapter.getData().clear();
            adapter.notifyDataSetChanged();
        }
        if (response.size() > 0) {
            adapter.notifyDataChangedAfterLoadMore(response, mPageNow < response.get(0).PageCount);
        }
    }

    @Override
    public void confirmSupplySuccess() {
        showToast("操作成功");
        mPageNow = 1;
        getData();
    }

    private void generateOrderItem(List<ShopOrder> response) {
        for (int i = 0; i < response.size(); i++) {
            ShopOrder order = response.get(i);
            //地址
            ShopOrder areaItem = new ShopOrder(order);
            //当前商区下的索引
            areaItem.setItemType(ShopOrder.AREA);
            mDatas2.add(areaItem);
            for (int j = 0; j < order.ProductItem.size(); j++) {
                //商品
                ShopOrder shopOrder = new ShopOrder(order.ProductItem.get(j));
                shopOrder.OrderNo = order.OrderNo;
                mDatas2.add(shopOrder);
            }
            //商品下面的信息
            ShopOrder infoItem = new ShopOrder(order);
            infoItem.setItemType(ShopOrder.INFO);
            mDatas2.add(infoItem);
        }
    }

    @Override
    public void onState(String method, String orderNo) {
        MessageDao.read(orderNo);
        if ("RobOrder".equals(method)) {
            //为什么要单独判断 ，罗凯 搞事情！！
            mvpPresenter.changeState(method, orderNo, user.Id, type);
        } else {
            mvpPresenter.changeState(method, orderNo, user.Id);
        }
    }

    @Override
    public void onState(String method, String orderNo, String remark) {
        //派送端完成工作
        mvpPresenter.finishWork(method, orderNo, user.Id, remark);
    }
}
