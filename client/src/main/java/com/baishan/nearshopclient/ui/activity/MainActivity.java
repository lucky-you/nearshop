package com.baishan.nearshopclient.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.mylibrary.utils.SPUtils;
import com.baishan.mylibrary.utils.StringUtils;
import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.base.BaseApplication;
import com.baishan.nearshopclient.base.BaseMvpActivity;
import com.baishan.nearshopclient.dao.AccountHelper;
import com.baishan.nearshopclient.dao.UserDao;
import com.baishan.nearshopclient.model.Account;
import com.baishan.nearshopclient.model.OrderCount;
import com.baishan.nearshopclient.model.UserInfo;
import com.baishan.nearshopclient.presenter.MainPresenter;
import com.baishan.nearshopclient.utils.ConstantValue;
import com.baishan.nearshopclient.view.IMainView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.baishan.nearshopclient.ui.activity.OrdersActivity.ORDER_GOODS;
import static com.baishan.nearshopclient.ui.activity.OrdersActivity.ORDER_Service;

public class MainActivity extends BaseMvpActivity<MainPresenter> implements IMainView {
    private static final String TAG = "MainActivity";
    private SwipeRefreshLayout srl;
    private TextView tvTitle;
    private TextView tvIdentity;
    private TextView tvPhone;
    private TextView tvArea;
    private TextView tvMyBalance;
    private TextView tvWithdraw;
    private TextView tvBalance;
    private RelativeLayout relFirst;
    private ImageView ivIcon1;
    private TextView tvFirstItem;
    private TextView tvNum;
    private RelativeLayout relSecond;
    private ImageView ivIcon2;
    private TextView tvSecondItem;
    private Button btnLoginoff;
    private LinearLayout llIdentity;
    private RelativeLayout relMessage;
    private TextView tvState;
    private CircleImageView ivAvatar;

    private TextView tvShopQD;
    private TextView tvShopDCL;
    private TextView tvShopJXZ;
    private TextView tvShopYWC;
    private TextView tvShopYQX;
    private TextView tvServiceQD;
    private TextView tvServiceDCL;
    private TextView tvServiceYJD;
    private TextView tvServiceGZZ;
    private TextView tvServiceYWC;
    private LinearLayout llServiceTitle;
    private LinearLayout llShopTitle;
    private TextView tvSecondDesc;
    private TextView tvFirstDesc;
    private LinearLayout Clearing_balance;
    private List<Account> mAccounts;
    private Handler handler = new Handler();
    private static MainActivity instance;

    public static MainActivity getInstance() {
        return instance;
    }

    /**
     * 正常点击上下班
     */
    public static final int CHANGE_TYPE_NORMALL = 1;
    /**
     * 点击切换账号
     */
    public static final int CHANGE_TYPE_CHANGE_ACCOUNT = 2;
    /**
     * 点击退出app
     */
    public static final int CHANGE_TYPE_FINISH = 3;

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void bindViews() {
        srl = (SwipeRefreshLayout) findViewById(R.id.srl);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        relMessage = (RelativeLayout) findViewById(R.id.relMessage);
//        llIdentity = (LinearLayout) findViewById(R.id.llIdentity);
        tvIdentity = (TextView) findViewById(R.id.tvIdentity);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvArea = (TextView) findViewById(R.id.tvArea);
        ivAvatar = (CircleImageView) findViewById(R.id.ivAvatar);
        tvMyBalance = (TextView) findViewById(R.id.tvMyBalance);
        tvWithdraw = (TextView) findViewById(R.id.tvWithdraw);
        tvBalance = (TextView) findViewById(R.id.tvBalance);
        tvNum = (TextView) findViewById(R.id.tvNum);
        relFirst = (RelativeLayout) findViewById(R.id.relFirst);
        ivIcon1 = (ImageView) findViewById(R.id.ivIcon1);
        tvFirstItem = (TextView) findViewById(R.id.tvFirstItem);
        relSecond = (RelativeLayout) findViewById(R.id.relSecond);
        ivIcon2 = (ImageView) findViewById(R.id.ivIcon2);
        tvSecondItem = (TextView) findViewById(R.id.tvSecondItem);
        tvState = (TextView) findViewById(R.id.tvState);
        btnLoginoff = (Button) findViewById(R.id.btnLoginoff);

        tvShopQD = (TextView) findViewById(R.id.tvShopQD);
        tvShopDCL = (TextView) findViewById(R.id.tvShopDCL);
        tvShopJXZ = (TextView) findViewById(R.id.tvShopJXZ);
        tvShopYWC = (TextView) findViewById(R.id.tvShopYWC);
        tvShopYQX = (TextView) findViewById(R.id.tvShopYQX);
        tvServiceQD = (TextView) findViewById(R.id.tvServiceQD);
        tvServiceDCL = (TextView) findViewById(R.id.tvServiceDCL);
        tvServiceYJD = (TextView) findViewById(R.id.tvServiceYJD);
        tvServiceGZZ = (TextView) findViewById(R.id.tvServiceGZZ);
        tvServiceYWC = (TextView) findViewById(R.id.tvServiceYWC);
        tvSecondDesc = (TextView) findViewById(R.id.tvSecondDesc);
        tvFirstDesc = (TextView) findViewById(R.id.tvFirstDesc);
        llShopTitle = (LinearLayout) findViewById(R.id.llShopTitle);
        llServiceTitle = (LinearLayout) findViewById(R.id.llServiceTitle);
        Clearing_balance = (LinearLayout) findViewById(R.id.Clearing_balance);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        registerEvent();
        instance = this;
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 10000);
        switch (user.getIdentity()) {

            case UserInfo.SENDER://派送员
                ImageLoaderUtils.displayAvatar(user.UserPhoto, ivAvatar);
                tvState.setVisibility(View.VISIBLE);
                setState();
                tvState.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(mContext).setTitle("提示")
                                .setMessage("是否更改状态为" + (user.IsActive == 1 ? "下班" : "上班"))
                                .setNegativeButton("取消", null)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mvpPresenter.changeWorkState(user.Id, user.IsActive == 1 ? 0 : 1, CHANGE_TYPE_NORMALL);
                                    }
                                })
                                .show();
                    }
                });
                tvTitle.setText("派送中心");
                tvIdentity.setText("(派送员)");
                relSecond.setOnClickListener(v -> gotoOrders(ORDER_Service));
                mvpPresenter.getOrderCount(user.Id + "", user.AreaId + "");
                break;
            case UserInfo.BUSINESS://2商家
                ImageLoaderUtils.displayAvatar(user.UserPhoto, ivAvatar);
                tvTitle.setText("商家中心");
//                llIdentity.setBackgroundResource(R.drawable.shape_bg_oval_red);
                tvIdentity.setText("(商  家)");
                tvFirstItem.setText("订单中心");
                tvSecondItem.setText("我的商品");
                relSecond.setOnClickListener(v -> intent2Activity(MyGoodsActivity.class));
                break;
            case UserInfo.ADMIN://管理员
                tvTitle.setText("管理中心");
                ImageLoaderUtils.displayAvatar(user.UserPhoto, ivAvatar);
//                llIdentity.setBackgroundResource(R.drawable.shape_bg_oval_red);
                tvIdentity.setText("(管理员)");
                relSecond.setOnClickListener(v -> gotoOrders(ORDER_Service));
                break;

            case UserInfo.SERVICE_PROVIDER://服务商
                ImageLoaderUtils.displayAvatar(user.UserPhoto, ivAvatar);
                tvTitle.setText("服务中心");
//                llIdentity.setBackgroundResource(R.drawable.shape_bg_oval_red);
                tvIdentity.setText("(服务商)");
                relFirst.setVisibility(View.GONE);
                relSecond.setVisibility(View.GONE);
        }

        setUserInfo();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_UPDATE_ORDER) {
            refreshUnReadCount();
            if (user.getIdentity() == UserInfo.SENDER)
                mvpPresenter.getOrderCount(user.Id + "", user.AreaId + "");
        }
    }

//    private void generateTextView() {
//        TextView textView = new TextView(this);
//        textView.setGravity(Gravity.CENTER);
//        textView.setPadding(5, 5, 5, 5);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams.weight = 1;
//        textView.setLayoutParams(layoutParams);
//        textView.setTextSize(getResources().getDimensionPixelSize(R.dimen.txtsize_caption));
//        textView.setTextColor(getResources().getColor(R.color.font_red));
//    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUnReadCount();
        setUserInfo();
        if (user.getIdentity() == UserInfo.SENDER)
            mvpPresenter.getOrderCount(user.Id + "", user.AreaId + "");
    }

    private void refreshUnReadCount() {
        //刷新消息数
        int messageCount = mvpPresenter.getMessageCount(user.IdentityFlag + "_" + user.Id);
        if (messageCount == 0) {
            tvNum.setVisibility(View.GONE);
        } else {
            tvNum.setVisibility(View.VISIBLE);
            tvNum.setText(messageCount + "");
        }
    }

    private void setState() {
        tvState.setText(user.IsActive == 1 ? "正在上班" : "已下班");
    }

    /**
     * 设置用户的信息
     */
    private void setUserInfo() {
        UserInfo user = BaseApplication.getInstance().getUserInfo();
        tvPhone.setText(user.Phone);
        tvArea.setText(user.AreaName);
        ImageLoaderUtils.displayAvatar(user.UserPhoto, ivAvatar);
        tvMyBalance.setText("￥" + user.Surplus);
        tvBalance.setText("￥" + user.Balance);
    }

    private void gotoOrders(int type) {
        Intent it = new Intent(mContext, OrdersActivity.class);
        it.putExtra(ConstantValue.TYPE, type);
        startActivity(it);
    }

    private List<Account> getAllAccount() {
        return AccountHelper.getAccountList();
    }

    //定时刷新数据
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            upDatas();
            handler.postDelayed(this, 10000);

        }
    };

    //刷新数据
    private void upDatas() {
        mAccounts = getAllAccount();
        for (int i = 0; i < mAccounts.size(); i++) {
            Account account = mAccounts.get(i);
            //比对用户名
            if (user.Phone.equals(mAccounts.get(i).Phone)) {
                mvpPresenter.getUserInfo("LoginOther", account.Phone, account.Password);
            }
        }
        if (user.getIdentity() == UserInfo.SENDER) {
            mvpPresenter.getOrderCount(user.Id + "", user.AreaId + "");
        }
    }

    @Override
    protected void setListener() {
        //下拉刷新数据
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                upDatas();
                srl.setRefreshing(false);
            }
        });

        //结算余额的监听
        Clearing_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent2Activity(ClearingBalanceActivity.class);
            }
        });
        relMessage.setOnClickListener(v1 -> intent2Activity(MyMessageActivity.class));
        tvWithdraw.setOnClickListener(v -> intent2Activity(WithdrawActivity.class));
        relFirst.setOnClickListener(v -> gotoOrders(OrdersActivity.ORDER_GOODS));
        //切换帐号
        btnLoginoff.setOnClickListener(v -> {

            if (user.getIdentity() == UserInfo.SENDER) {
                mvpPresenter.changeWorkState(user.Id, user.IsActive == 1 ? 0 : 1, CHANGE_TYPE_CHANGE_ACCOUNT);
            } else {
                logout();
            }

        });
    }

    @Override
    public void onBackPressed() {
        if (user.getIdentity() == UserInfo.SENDER) {
            mvpPresenter.changeWorkState(user.Id, user.IsActive == 1 ? 0 : 1, CHANGE_TYPE_FINISH);
        } else {
            super.onBackPressed();
        }
    }

    private void logout() {
        SPUtils.set(ConstantValue.SP_IS_LOGIN, false);
//        UserDao.logout();
        intent2Activity(LoginActivity.class);
        finish();
    }

    @Override
    public void changeWorkState(int state, int type) {
        if (type == CHANGE_TYPE_NORMALL) { //正常切换上下班
            user.IsActive = state;
            setState();
            refreshUnReadCount();
            setUserInfo();
        } else if (type == CHANGE_TYPE_CHANGE_ACCOUNT) {//点击切换帐号
            logout();
        } else {
            finish();
        }
    }

    @Override
    public void onLoginSuccess(List<UserInfo> response, String phone, String pwd) {
        UserInfo user = response.get(0);
        BaseApplication.getInstance().setUserInfo(user);
        setUserInfo();
        setState();
        refreshUnReadCount();
    }

    @Override
    public void getOrderCountSuccess(OrderCount response) {
        llShopTitle.setVisibility(View.VISIBLE);
        llServiceTitle.setVisibility(View.VISIBLE);
        tvShopQD.setText(TextUtils.concat("抢单", StringUtils.getColorText("(" + response.ShopQD + ")", getResources().getColor(R.color.font_red))));
        tvShopDCL.setText(TextUtils.concat("待处理", StringUtils.getColorText("(" + response.ShopDCL + ")", getResources().getColor(R.color.font_red))));
        tvShopJXZ.setText(TextUtils.concat("进行中", StringUtils.getColorText("(" + response.ShopJXZ + ")", getResources().getColor(R.color.font_red))));
        tvShopYWC.setText(TextUtils.concat("已完成", StringUtils.getColorText("(" + response.ShopYWC + ")", getResources().getColor(R.color.font_red))));
        tvShopYQX.setText(TextUtils.concat("已取消", StringUtils.getColorText("(" + response.ShopYQX + ")", getResources().getColor(R.color.font_red))));

        tvShopQD.setOnClickListener(new GotoOrdersListener(true, 0));
        tvShopDCL.setOnClickListener(new GotoOrdersListener(true, 1));
        tvShopJXZ.setOnClickListener(new GotoOrdersListener(true, 2));
        tvShopYWC.setOnClickListener(new GotoOrdersListener(true, 3));
        tvShopYQX.setOnClickListener(new GotoOrdersListener(true, 4));

        tvServiceQD.setText(TextUtils.concat("抢单", StringUtils.getColorText("(" + response.ServiceQD + ")", getResources().getColor(R.color.font_red))));
        tvServiceDCL.setText(TextUtils.concat("待处理", StringUtils.getColorText("(" + response.ServiceDCL + ")", getResources().getColor(R.color.font_red))));
        tvServiceYJD.setText(TextUtils.concat("已接单", StringUtils.getColorText("(" + response.ServiceYJD + ")", getResources().getColor(R.color.font_red))));
        tvServiceGZZ.setText(TextUtils.concat("工作中", StringUtils.getColorText("(" + response.ServiceGZZ + ")", getResources().getColor(R.color.font_red))));
        tvServiceYWC.setText(TextUtils.concat("已完成", StringUtils.getColorText("(" + response.ServiceYWC + ")", getResources().getColor(R.color.font_red))));

        tvServiceQD.setOnClickListener(new GotoOrdersListener(false, 0));
        tvServiceDCL.setOnClickListener(new GotoOrdersListener(false, 1));
        tvServiceYJD.setOnClickListener(new GotoOrdersListener(false, 2));
        tvServiceGZZ.setOnClickListener(new GotoOrdersListener(false, 3));
        tvServiceYWC.setOnClickListener(new GotoOrdersListener(false, 4));

        tvFirstDesc.setText(String.format("(超时%d次,严重超时%d次,忽略%d次)", response.ShopOverOrderCount, response.ShopOver2OrderCount, response.ShopIgnoreOrderCount));
        tvSecondDesc.setText(String.format("(超时%d次,严重超时%d次,忽略%d次)", response.ServiceOverOrderCount, response.ServiceOver2OrderCount, response.ServiceIgnoreOrderCount));
    }


    class GotoOrdersListener implements View.OnClickListener {
        private boolean isShop;
        private int mPosition;

        public GotoOrdersListener(boolean isShop, int position) {
            this.isShop = isShop;
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            Intent it = new Intent(mContext, OrdersActivity.class);
            it.putExtra(ConstantValue.TYPE, isShop ? ORDER_GOODS : ORDER_Service);
            it.putExtra(ConstantValue.POSITION, mPosition);
            startActivity(it);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
        handler.removeCallbacks(runnable);
        UserDao.logout();
    }
}
