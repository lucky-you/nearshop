package com.baishan.nearshopclient.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.base.BaseMvpActivity;
import com.baishan.nearshopclient.presenter.WithdrawPresenter;
import com.baishan.nearshopclient.view.IWithdrawView;


public class WithdrawActivity extends BaseMvpActivity<WithdrawPresenter> implements IWithdrawView {
    private EditText etMoney;
    private EditText etBankName;
    private EditText etAccount;
    private TextView etAccountName;
    private Button btnSubmit;
    private TextView tvLog;
    private TextView tvCurrentMoney;
    private TextView etPassword;
    private int money;

    @Override
    protected WithdrawPresenter createPresenter() {
        return new WithdrawPresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_withdraw);
    }

    @Override
    protected void bindViews() {
        etMoney = (EditText) findViewById(R.id.etMoney);
        etBankName = (EditText) findViewById(R.id.etBankName);
        etAccount = (EditText) findViewById(R.id.etAccount);
        etAccountName = (TextView) findViewById(R.id.etAccountName);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        tvLog = (TextView) findViewById(R.id.tvLog);
        tvCurrentMoney = (TextView) findViewById(R.id.tvCurrentMoney);
        etPassword = (TextView) findViewById(R.id.etPassword);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initTitle("立即提现");
        tvCurrentMoney.setText(user.Surplus + "");
        etAccountName.setText(user.Contact);
    }

    @Override
    protected void setListener() {
        btnSubmit.setOnClickListener(this);
        tvLog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                String moneyStr = etMoney.getText().toString();
                String bankName = etBankName.getText().toString();
                String account = etAccount.getText().toString();
                String accountName = etAccountName.getText().toString();
                String password = etPassword.getText().toString();
                if (TextUtils.isEmpty(moneyStr)) {
                    showToast("请输入要提现的金额");
                    return;
                }
                if (TextUtils.isEmpty(bankName)) {
                    showToast("请输入提现的银行");
                    return;
                }
                if (TextUtils.isEmpty(account)) {
                    showToast("请输入提现账号");
                    return;
                }
                if (TextUtils.isEmpty(accountName)) {
                    showToast("请输入开户姓名");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showToast("请输入密码");
                    return;
                }
                try {
                    money = Integer.parseInt(moneyStr);
                    if (money < 100) {
                        showToast("提现金额要大于100元");
                        return;
                    }
                    if (money > user.Surplus) {
                        showToast("超过可提现金额");
                        return;
                    }

                    //提现
                    new AlertDialog.Builder(mContext).setTitle("是否确认提现")
                            .setMessage("请核对无误后点击确定进行提现")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mvpPresenter.submitWithdrawals(user, money, account, accountName, bankName, password);
                                }
                            })
                            .show();


                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tvLog:
                intent2Activity(WithdrawLogActivity.class);
                break;
        }
    }

    @Override
    public void submitWithdrawalsSuccess(String response) {
        showToast("提现成功！");
        user.Surplus -= money;
        finish();
    }
}
