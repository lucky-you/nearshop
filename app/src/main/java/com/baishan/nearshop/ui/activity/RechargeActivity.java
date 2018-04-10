package com.baishan.nearshop.ui.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.model.PostInfo;
import com.baishan.nearshop.presenter.PayPresenter;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.IPayView;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RechargeActivity extends BaseMvpActivity<PayPresenter> implements IPayView {


    @BindView(R.id.tvCurrentBalance)
    TextView tvCurrentBalance;
    @BindView(R.id.tvMoneyTitle)
    TextView tvMoneyTitle;
    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.rlAlipay)
    RelativeLayout rlAlipay;
    @BindView(R.id.rlWXPay)
    RelativeLayout rlWXPay;
    @BindView(R.id.cb_pay_alipay)
    CheckBox cbPayAlipay;
    @BindView(R.id.cb_pay_wx)
    CheckBox cbPayWx;
    @BindView(R.id.btnRecharge)
    Button btnRecharge;
    @BindView(R.id.llBalance)
    LinearLayout llBalance;
    /**
     * 正常充值
     */
    public static final int TYPE_NORMAL = 1;
    /**
     * 帖子献爱心
     */
    public static final int TYPE_REWARD = 2;
    private int mType;
    private PostInfo mPostInfo;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

        mType = getIntent().getIntExtra(ConstantValue.TYPE, TYPE_NORMAL);
        registerEvent();
        if (mType == TYPE_NORMAL) {
            initTitle("充值");
            llBalance.setVisibility(View.VISIBLE);
            tvCurrentBalance.setText("￥" + user.Balance);
        } else {
            mPostInfo = (PostInfo) getIntent().getSerializableExtra(ConstantValue.DATA);
            initTitle("献爱心");
            tvMoneyTitle.setText("爱心金额");
            llBalance.setVisibility(View.GONE);
            btnRecharge.setText("奉献爱心");
            etMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            //限制只能小数点2位
            etMoney.setFilters(new InputFilter[]{new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    if (source.equals(".") && dest.toString().length() == 0) {
                        return "0.";
                    }
                    if (dest.toString().contains(".")) {
                        int index = dest.toString().indexOf(".");
                        int mlength = dest.toString().substring(index).length();
                        if (mlength == 3) {
                            return "";
                        }
                    }
                    return null;
                }
            }});
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected PayPresenter createPresenter() {
        return new PayPresenter(this);
    }

    private String mPayWay = "支付宝";

    @OnClick({R.id.rlAlipay, R.id.rlWXPay, R.id.btnRecharge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlAlipay:
                cbPayAlipay.setChecked(true);
                cbPayWx.setChecked(false);
                mPayWay = "支付宝";
                break;
            case R.id.rlWXPay:
                cbPayAlipay.setChecked(false);
                cbPayWx.setChecked(true);
                mPayWay = "微信";
                break;
            case R.id.btnRecharge:
                if (mType == TYPE_NORMAL)
                    mvpPresenter.recharge(user.UserId, etMoney.getText().toString(), mPayWay);
                else {
                    reward();
                }
                break;
        }
    }

    /**
     * 打赏
     */
    private void reward() {
        String money = etMoney.getText().toString();
        if (mPostInfo == null) return;
        if (TextUtils.isEmpty(money)) {
            showToast("请输入金额");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("forumId", mPostInfo.Id + "");
        params.put("forumToken", mPostInfo.ForumToken);
        params.put("userId", mPostInfo.UserId + "");
        params.put("userToken", mPostInfo.UserToken);
        params.put("coins", money);
        params.put("PayType", mPayWay);
        params.put("method", "RewardForum");
        mvpPresenter.getPayInfo(params);
    }

    @Override
    public void getPayInfoSuccess() {

    }

    @Override
    public void balancePaySuccess() {

    }

    @Subscribe
    public void onEvent(Integer msg) {
        if (msg == 8888) {
            if (mType == TYPE_REWARD)
                post(new Notice(ConstantValue.MSG_TYPE_UPDATE_REWARD));
            finish();
        }
    }
}
