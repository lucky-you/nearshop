package com.baishan.nearshopclient.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baishan.mylibrary.utils.SPUtils;
import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.base.BaseMvpActivity;
import com.baishan.nearshopclient.dao.AccountHelper;
import com.baishan.nearshopclient.dao.UserDao;
import com.baishan.nearshopclient.model.Account;
import com.baishan.nearshopclient.model.UserInfo;
import com.baishan.nearshopclient.presenter.LoginPresenter;
import com.baishan.nearshopclient.ui.adapter.AccountAdapter;
import com.baishan.nearshopclient.utils.ConstantValue;
import com.baishan.nearshopclient.view.ILoginView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements ILoginView {

    private EditText etPhone;
    private EditText etPassword;
    private Button btnLogin;
    private AccountAdapter mAccountAdapter;
    private List<Account> mAccounts;

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void bindViews() {
        etPhone = (EditText) findViewById(R.id.etPhone);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initTitle("账户登录");
        mAccounts = getAllAccount();
        mAccountAdapter = new AccountAdapter(mAccounts);
        initCommonRecyclerView(mAccountAdapter, null);
        if (mAccounts.size() > 0) {
            Account account = mAccounts.get(0);
            etPhone.setText(account.Phone);
            etPassword.setText(account.Password);
            if ((boolean) SPUtils.get(ConstantValue.SP_IS_LOGIN, false))
                mvpPresenter.Login(etPhone.getText().toString(), etPassword.getText().toString());
        }
    }

    private List<Account> getAllAccount() {
        return AccountHelper.getAccountList();
    }

    @Override
    protected void setListener() {
        btnLogin.setOnClickListener(v -> mvpPresenter.Login(etPhone.getText().toString(), etPassword.getText().toString()));
        mAccountAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Account account = mAccounts.get(i);
                mvpPresenter.Login(account.Phone, account.Password);
            }
        });
    }

    @Override
    public void onLoginSuccess(List<UserInfo> response, String phone, String pwd) {
        if (response.size() == 0) return;

        UserInfo userInfo = response.get(0);
        if (userInfo.IdentityFlag == UserInfo.SENDER || userInfo.IdentityFlag == UserInfo.BUSINESS || userInfo.IdentityFlag == UserInfo.ADMIN || userInfo.IdentityFlag == UserInfo.SERVICE_PROVIDER) {
            UserDao.login(userInfo, phone, pwd);
            intent2Activity(MainActivity.class);
            finish();
        } else {
            showToast("该用户无法登陆");
        }
    }
}
