package com.baishan.nearshop.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.presenter.FeedBackPresenter;
import com.baishan.nearshop.view.IFeedBackView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户反馈
 */
public class FeedBackActivity extends BaseMvpActivity<FeedBackPresenter> implements IFeedBackView {


    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.wordNumber)
    TextView wordNumber;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initRedTitle("用户反馈");
    }

    @Override
    protected void setListener() {
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                wordNumber.setText("300字以内");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int number = 300 - s.length();
                wordNumber.setText("还能输入" + number + "个字");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @OnClick(R.id.btnSubmit)
    public void onClick() {
        String txt = etContent.getText().toString();
        if (TextUtils.isEmpty(txt)) {
            showToast("请输入您的建议");
            return;
        } else {
            mvpPresenter.submitFeedback(user.UserId, txt);
        }

    }

    @Override
    protected FeedBackPresenter createPresenter() {
        return new FeedBackPresenter(this);
    }

    @Override
    public void onSuccess() {
        showToast("提交成功");
        finish();
    }

}
