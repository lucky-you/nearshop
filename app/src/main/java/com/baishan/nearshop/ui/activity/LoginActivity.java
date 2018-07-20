package com.baishan.nearshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.base.ResultResponse;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.presenter.LoginPresenter;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.ILoginView;

import org.greenrobot.eventbus.Subscribe;

public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements ILoginView {
    private EditText etPhone;
    private EditText etPassword;
    private TextView tvForget;
    private Button btnLogin;
    private TextView tvRegist;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void bindViews() {
        etPhone = (EditText) findViewById(R.id.etPhone);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvForget = (TextView) findViewById(R.id.tvForget);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvRegist = (TextView) findViewById(R.id.tvRegist);
    }

    @Subscribe
    public void onEventMainThread(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_UPDATE_USER) {
            finish();
        }
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initTitle("账户登陆")
                .setRightText("注册")
                .setRightOnClickListener(v -> intent2Activity(RegistActivity.class));
        registerEvent();
    }

    @Override
    protected void setListener() {
        tvRegist.setOnClickListener(v -> intent2Activity(RegistActivity.class));
        tvForget.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistActivity.class);
            intent.putExtra(ConstantValue.TYPE, RegistActivity.TYPE_FORGET);
            startActivity(intent);
        });
        btnLogin.setOnClickListener(v -> {
            mvpPresenter.login(etPhone.getText().toString(), etPassword.getText().toString());
        });
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void onPhoneError() {
        showToast("手机号输入错误");
    }

    @Override
    public void onPassWordError() {
        showToast("请输入大于6位数的密码");
    }


}
