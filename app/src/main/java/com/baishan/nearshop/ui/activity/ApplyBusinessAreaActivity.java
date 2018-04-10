package com.baishan.nearshop.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.mylibrary.utils.PhotoUtils;
import com.baishan.mylibrary.view.ActionSheet;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.presenter.ApplyBusinessAreaPresenter;
import com.baishan.nearshop.utils.AreaDialog;
import com.baishan.nearshop.view.IApplyBusinessAreaView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ApplyBusinessAreaActivity extends BaseMvpActivity<ApplyBusinessAreaPresenter> implements IApplyBusinessAreaView {


    @BindView(R.id.etBusinessName)
    EditText etBusinessName;
    @BindView(R.id.tvArea)
    TextView tvArea;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etIdCard)
    EditText etIdCard;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etFixPhone)
    EditText etFixPhone;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.ivCardFront)
    ImageView ivCardFront;
    @BindView(R.id.btnUploadFront)
    Button btnUploadFront;
    @BindView(R.id.ivCardBackground)
    ImageView ivCardBackground;
    @BindView(R.id.btnUploadBackground)
    Button btnUploadBackground;
    @BindView(R.id.ivLicense)
    ImageView ivLicense;
    @BindView(R.id.btnLicense)
    Button btnLicense;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    private AreaDialog areaDialog;
    private String mAreaString;
    /**
     * 选择营业执照
     */
    public static final int IMAGE_TYPE_LICENSE = 1;
    /**
     * 选择身份证正面
     */
    public static final int IMAGE_TYPE_ID_CARD_FRONT = 2;
    /**
     * 选择身份证反面
     */
    public static final int IMAGE_TYPE_ID_CARD_BACKGROUND = 3;
    private int mImageType;
    private String mCardFrontPath;
    private String mCardBackgroundPath;
    private String mLicencePath;
    private String adCode;
    private String cityCode;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_apply_business_area);
        ButterKnife.bind(this);
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initRedTitle("商区申请");

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected ApplyBusinessAreaPresenter createPresenter() {
        return new ApplyBusinessAreaPresenter(this);
    }


    @OnClick({R.id.tvArea, R.id.btnUploadFront, R.id.btnUploadBackground, R.id.btnLicense, R.id.btnSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvArea:
                showAreaDialog();
                break;
            case R.id.btnUploadFront:
                mImageType = IMAGE_TYPE_ID_CARD_FRONT;
                showActionSheet();
                break;
            case R.id.btnUploadBackground:
                mImageType = IMAGE_TYPE_ID_CARD_BACKGROUND;
                showActionSheet();
                break;
            case R.id.btnLicense:
                mImageType = IMAGE_TYPE_LICENSE;
                showActionSheet();
                break;
            case R.id.btnSubmit:
                String businessName = etBusinessName.getText().toString();
                String address = etAddress.getText().toString();
                String contact = etName.getText().toString();
                String idCard = etIdCard.getText().toString();
                String phone = etPhone.getText().toString();
                //String fixPhone = etFixPhone.getText().toString();
                String email = etEmail.getText().toString();
                if (TextUtils.isEmpty(businessName)) {
                    showToast(etBusinessName.getHint().toString());
                    return;
                }
                if (TextUtils.isEmpty(mAreaString)) {
                    showToast("请选择商区所属区域");
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    showToast(etAddress.getHint().toString());
                    return;
                }
                if (TextUtils.isEmpty(contact)) {
                    showToast(etName.getHint().toString());
                    return;
                }
                if (TextUtils.isEmpty(idCard)) {
                    showToast(etIdCard.getHint().toString());
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    showToast(etPhone.getHint().toString());
                    return;
                }
//                if(TextUtils.isEmpty(fixPhone))
//                {
//                    showToast(etFixPhone.getHint().toString());
//                    return;
//                }
                if (TextUtils.isEmpty(email)) {
                    showToast(etEmail.getHint().toString());
                    return;
                }
                if (TextUtils.isEmpty(mCardFrontPath)) {
                    showToast("请上传身份证正面");
                    return;
                }
                if (TextUtils.isEmpty(mCardBackgroundPath)) {
                    showToast("请上传身份证反面");
                    return;
                }
//                if(TextUtils.isEmpty(mLicencePath))
//                {
//                    showToast("请上传营业执照");
//                    return;
//                }
                mvpPresenter.applyBusinessArea(user.UserId + "", businessName, mAreaString, address, contact, idCard, phone, "", email, mCardFrontPath
                        , mCardBackgroundPath, mLicencePath, adCode, cityCode);
                break;
        }
    }

    private void showActionSheet() {
        new ActionSheet.Builder(mContext, getSupportFragmentManager())
                .setCancelableOnTouchOutside(true)
                .setCancelButtonTitle(getString(com.baishan.mylibrary.R.string.cancel))
                .setOtherButtonTitles(getResources().getStringArray(com.baishan.mylibrary.R.array.picture_select))
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        if (index == 1) {
                            PhotoUtils.pickImageFromAlbum(ApplyBusinessAreaActivity.this);
                        } else {
                            PhotoUtils.pickImageFromCamera(ApplyBusinessAreaActivity.this);
                        }
                    }
                })
                .show();
    }

    private void showAreaDialog() {
        if (areaDialog == null) {
            areaDialog = new AreaDialog(this);
            areaDialog.setOnSelectedAreaListener(new AreaDialog.OnSelectedAreaListener() {
                @Override
                public void onSelectedAreaSuccess(String[] code) {
                    mAreaString = code[1];
                    tvArea.setText(code[1]);
                    adCode = code[0];
                    cityCode = code[2];
                }
            });
        }
        areaDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mvpPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSelectAlbumFinish(Uri data) {
        showImage(PhotoUtils.getImageAbsolutePath(this, data));
    }

    @Override
    public void onCameraCancel() {
        PhotoUtils.deleteImageUri(this, PhotoUtils.imageUriFromCamera);
    }

    @Override
    public void onCameraFinish() {
        showImage(PhotoUtils.getImageAbsolutePath(this, PhotoUtils.imageUriFromCamera));
    }

    @Override
    public void onApplyPartnerSuccess() {
        showToast("申请成功");
        finish();
    }

    private void showImage(String path) {
        if (mImageType == IMAGE_TYPE_ID_CARD_FRONT) {
            ImageLoaderUtils.displayImage("file://" + path, ivCardFront);
            mCardFrontPath = path;
        } else if (mImageType == IMAGE_TYPE_ID_CARD_BACKGROUND) {
            ImageLoaderUtils.displayImage("file://" + path, ivCardBackground);
            mCardBackgroundPath = path;
        } else if (mImageType == IMAGE_TYPE_LICENSE) {
            ImageLoaderUtils.displayImage("file://" + path, ivLicense);
            mLicencePath = path;
        }
    }
}
