package com.baishan.nearshop.ui.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.mylibrary.utils.StringUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.ApiService;
import com.baishan.nearshop.base.BaseMvpFragment;
import com.baishan.nearshop.presenter.MePresenter;
import com.baishan.nearshop.ui.activity.AddrManageActivity;
import com.baishan.nearshop.ui.activity.CoinsStoreActivity;
import com.baishan.nearshop.ui.activity.ConsumeRecordActivity;
import com.baishan.nearshop.ui.activity.CouponActivity;
import com.baishan.nearshop.ui.activity.FeedBackActivity;
import com.baishan.nearshop.ui.activity.ForumActivity;
import com.baishan.nearshop.ui.activity.MyMessageActivity;
import com.baishan.nearshop.ui.activity.OrdersActivity;
import com.baishan.nearshop.ui.activity.PartnerActivity;
import com.baishan.nearshop.ui.activity.PersonInfoActivity;
import com.baishan.nearshop.ui.activity.RechargeActivity;
import com.baishan.nearshop.ui.activity.SettingActivity;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.IMeView;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public class MeFragment extends BaseMvpFragment<MePresenter> implements IMeView {
    private RelativeLayout llPersonalInfo;
    private CircleImageView ivAvatar;
    private TextView tvName;
    private TextView tvMoney;
    private TextView tvMoneyDesc;
    private TextView tvCoins;
    private TextView tvLocation;
    private RelativeLayout relMessage;
    private RelativeLayout relOrder;
    private RelativeLayout relEasyOrder;
    private RelativeLayout rlCoinsStore;
    private RelativeLayout relMyCoupon;
    private RelativeLayout relMyAddress;
    private RelativeLayout rlPartner;
    private RelativeLayout relFeedback;
    private RelativeLayout relForum;
    private RelativeLayout relSetting;
    private RelativeLayout relShare;

    @Override
    protected MePresenter createPresenter() {
        return new MePresenter(this);
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_me, null);
    }

    @Override
    protected void bindViews(View view) {
        llPersonalInfo = (RelativeLayout) view.findViewById(R.id.llPersonalInfo);
        ivAvatar = (CircleImageView) view.findViewById(R.id.ivAvatar);
        relMessage = (RelativeLayout) view.findViewById(R.id.relMessage);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvMoney = (TextView) view.findViewById(R.id.tvMoney);
        tvMoneyDesc = (TextView) view.findViewById(R.id.tvMoneyDesc);
        tvCoins = (TextView) view.findViewById(R.id.tvCoins);
        tvLocation = (TextView) view.findViewById(R.id.tvLocation);
        relOrder = (RelativeLayout) view.findViewById(R.id.relOrder);
        relEasyOrder = (RelativeLayout) view.findViewById(R.id.relEasyOrder);
        rlCoinsStore = (RelativeLayout) view.findViewById(R.id.rlCoinsStore);
        relMyCoupon = (RelativeLayout) view.findViewById(R.id.relMyCoupon);
        relMyAddress = (RelativeLayout) view.findViewById(R.id.relMyAddress);
        rlPartner = (RelativeLayout) view.findViewById(R.id.rlPartner);
        relFeedback = (RelativeLayout) view.findViewById(R.id.relFeedback);
        relForum = (RelativeLayout) view.findViewById(R.id.relForum);
        relSetting = (RelativeLayout) view.findViewById(R.id.relSetting);
        relShare = (RelativeLayout) view.findViewById(R.id.relShare);
    }

    @Override
    protected void processLogic() {
        registerEvent();
        tvLocation.setText(mCurrentArea.AreaName);
    }

    @Subscribe
    public void onEventMainThread(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_UPDATE_USER) {
            setUserInfo();
        } else if (notice.type == ConstantValue.MSG_TYPE_UPDATE_AREA) {
            getCommonData();
            tvLocation.setText(mCurrentArea.AreaName);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserInfo();
    }

    private void setUserInfo() {
        getCommonData();
        if (user == null) {
            //未登录
            tvName.setText("未登录");
            ivAvatar.setImageResource(R.drawable.ic_default_photo);
            tvMoney.setText("0");
            tvCoins.setText("0S币");
        } else {
            //已登录
            tvName.setText(user.NickName);
            ImageLoaderUtils.displayAvatar(user.UserPhoto, ivAvatar);
            tvMoney.setText("￥" + user.Balance);
            tvCoins.setText(user.SCoins + "S币");
            tvMoneyDesc.setText("");
            tvMoneyDesc.append("我的余额 ");
            tvMoneyDesc.append(StringUtils.getClickText(tvMoneyDesc, "充值", getResources().getColor(R.color.font_green), () -> {
                if (checkLogin()) intent2Activity(RechargeActivity.class);
            }));
            if (user.IdentityFlag != 0) {
                rlPartner.setVisibility(View.GONE);
            } else {
                rlPartner.setVisibility(View.VISIBLE);
            }
            mvpPresenter.getLastCoins(mCurrentArea == null ? 1 : mCurrentArea.AreaId, user.LoginToken);
            mvpPresenter.getLastBalance(user.LoginToken);
        }

    }

    @Override
    protected void setListener() {
        relMessage.setOnClickListener(v -> intent2Activity(MyMessageActivity.class));
        rlCoinsStore.setOnClickListener(v -> intent2Activity(CoinsStoreActivity.class));
        llPersonalInfo.setOnClickListener(v -> {
            if (checkLogin())
                intent2Activity(PersonInfoActivity.class);
        });
        relMyCoupon.setOnClickListener(v -> {
            if (checkLogin()) intent2Activity(CouponActivity.class);
        });
        rlPartner.setOnClickListener(v -> {
            if (checkLogin())
                intent2Activity(PartnerActivity.class);
        });
        relFeedback.setOnClickListener(v -> {
            if (checkLogin())
                intent2Activity(FeedBackActivity.class);
        });
        relForum.setOnClickListener(v -> {
            if (checkLogin()) intent2Activity(ForumActivity.class);
        });
        relSetting.setOnClickListener(v -> intent2Activity(SettingActivity.class));
//        relSetting.setOnClickListener(v -> intent2Activity(ForumActivity.class));
        relMyAddress.setOnClickListener(v -> {
                    if (checkLogin())
                        intent2Activity(AddrManageActivity.class);
                }
        );
        relOrder.setOnClickListener(v -> {
            if (checkLogin())
                intent2Activity(OrdersActivity.class);
        });
        relEasyOrder.setOnClickListener(v -> {
            if (!checkLogin()) return;
            Intent intent = new Intent(getActivity(), OrdersActivity.class);
            intent.putExtra(ConstantValue.TYPE, OrdersActivity.TYPE_EASY_ORDERS);
            startActivity(intent);
        });
        relShare.setOnClickListener(v -> {
            if (checkLogin())
                showShare();
        });
        tvMoney.setOnClickListener(v -> {
            if (checkLogin()) intent2Activity(ConsumeRecordActivity.class);
        });
    }

    @Override
    public void getLastCoinsSuccess(String response) {
        user.SCoins = response;
        tvCoins.setText(user.SCoins + "S币");
    }

    @Override
    public void getLastBalanceSuccess(String response) {
        user.Balance = response;
        tvMoney.setText("￥" + user.Balance);
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("来身边够吧");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(ApiService.SHARE_URL + "user=" + user.UserId + "&area=" + mCurrentArea.AreaId);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("真正身边的超市——身边够" + "\n" + ApiService.SBG_OFFICIAL);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://www.sbgo.cc//sbgoimg/chaoshi.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://www.sbgo.cc");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("身边够创造全新的商业时代");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("sbgo.cc");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.sbgo.cc");
        oks.setCallback(new ShareListener());
        // 启动分享GUI
        oks.show(mContext);
    }


    private class ShareListener implements PlatformActionListener {

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            showLog("onComplete---" + platform.getName());
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            showLog("onError---" + platform.getName() + "---" + throwable.getMessage());
        }

        @Override
        public void onCancel(Platform platform, int i) {
            showLog("onCancel---" + platform.getName());
        }
    }

}
