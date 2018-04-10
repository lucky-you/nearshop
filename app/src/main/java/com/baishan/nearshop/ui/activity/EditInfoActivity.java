package com.baishan.nearshop.ui.activity;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseActivity;
import com.baishan.nearshop.utils.ConstantValue;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditInfoActivity extends BaseActivity {

    @BindView(R.id.tvKey)
    TextView tvKey;
    @BindView(R.id.etValue)
    EditText etValue;

    public final static int EDIT_TYPE_NICK_NAME = 1;
    public final static int EDIT_TYPE_AGE = 2;
    private int mType;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initRedTitle("修改").setRightText("保存").setRightOnClickListener(v -> {
            String value = etValue.getText().toString();
            if (TextUtils.isEmpty(value)) {
                showToast("请输入信息");
                return;
            }
            post(new Notice(ConstantValue.MSG_TYPE_EDIT_INFO, mType, value));
            finish();
        });
        mType = getIntent().getIntExtra(ConstantValue.TYPE, EDIT_TYPE_NICK_NAME);
        if (mType == EDIT_TYPE_NICK_NAME) {
            tvKey.setText("昵称");
            etValue.setHint("请输入昵称");
        } else if (mType == EDIT_TYPE_AGE) {
            tvKey.setText("年龄");
            etValue.setHint("请输入年龄");
            etValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }

    @Override
    protected void setListener() {

    }
}
