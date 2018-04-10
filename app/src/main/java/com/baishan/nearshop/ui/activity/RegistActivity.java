package com.baishan.nearshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.ApiService;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.presenter.CodePresenter;
import com.baishan.nearshop.presenter.RegistPresenter;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.ICodeView;
import com.baishan.nearshop.view.IRegistView;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistActivity extends BaseMvpActivity<RegistPresenter> implements IRegistView, ICodeView {
    public CodePresenter codePrensenter;

    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.llProtocol)
    LinearLayout llProtocol;
    @BindView(R.id.tvProtocol)
    TextView tvProtocol;
    @BindView(R.id.cbProtocol)
    CheckBox cbProtocol;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnCode)
    Button btnCode;
    private int second = 60;
    private CountDownTimer timer;

    public final static int TYPE_REGIST = 1;
    public final static int TYPE_FORGET = 2;
    public final static int TYPE_UPDATE = 3;
    private int mType;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (codePrensenter != null) {
            codePrensenter.detachView();
        }
    }

    @Subscribe
    public void onEventMainThread(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_UPDATE_USER) {
            if (mType == TYPE_REGIST) {
                intent2Activity(PersonInfoActivity.class);
                finish();
            }
        }
    }


    @Override
    protected void processLogic(Bundle savedInstanceState) {
        codePrensenter = new CodePresenter(this);
        registerEvent();
        mType = getIntent().getIntExtra(ConstantValue.TYPE, TYPE_REGIST);
        if (mType == TYPE_REGIST) {
            //z注册
            llProtocol.setVisibility(View.VISIBLE);
            etPassword.setHint("请输入密码");
            initTitle("注册").setRightText("登陆").setRightOnClickListener(v -> {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            });
        } else if (mType == TYPE_FORGET) {
            initTitle("找回密码").setRightText("登陆").setRightOnClickListener(v -> {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            });
            btnLogin.setText("保存");
        } else {
            initTitle("修改密码");
            btnLogin.setText("修改");
        }
    }


    @Override
    protected void setListener() {
        btnLogin.setOnClickListener(v -> {
            if (mType == TYPE_REGIST) {
                if (!cbProtocol.isChecked()) {
                    showToast("请先同意注册协议");
                    return;
                }
                mvpPresenter.regist(etPhone.getText().toString(), etPassword.getText().toString(), etCode.getText().toString());
            } else {
                mvpPresenter.changePwd(etPhone.getText().toString(), etPassword.getText().toString(), etCode.getText().toString());
            }


        });
        btnCode.setOnClickListener(v -> {
            codePrensenter.getCode(etPhone.getText().toString());
        });
        tvProtocol.setOnClickListener(v -> {
            Intent it1 = new Intent(mContext, WebActivity.class);
            it1.putExtra(ConstantValue.URL, ApiService.REGISTER_PROTOCOL);
            it1.putExtra(ConstantValue.TITLE, "注册协议");
            startActivity(it1);
        });
    }

    public void countDown() {
        btnCode.setEnabled(false);
        timer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                btnCode.setText("  " + second + "S" + "  ");
                second--;
            }

            @Override
            public void onFinish() {
                second = 60;
                btnCode.setEnabled(true);
                btnCode.setText("重新发送");
            }
        }.start();
    }

    @Override
    protected RegistPresenter createPresenter() {
        return new RegistPresenter(this);
    }

    @Override
    public void onPhoneError() {
        showToast("手机号输入错误");
    }

    @Override
    public void onPassWordError() {
        showToast("请输入大于6位数的密码");
    }

    @Override
    public void startCountDown() {
        countDown();
    }

    @Override
    public void onChangeSuccess() {
        showToast("修改成功,请重新登陆");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
