package com.baishan.nearshop.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baishan.mylibrary.utils.CommonUtil;
import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.mylibrary.utils.PhotoUtils;
import com.baishan.mylibrary.view.ActionSheet;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.Region;
import com.baishan.nearshop.presenter.ApplyPartnerPresenter;
import com.baishan.nearshop.utils.AreaDialog;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.IApplyPartnerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ApplyPartnerActivity extends BaseMvpActivity<ApplyPartnerPresenter> implements IApplyPartnerView {

    /**
     * 申请商户
     */
    public final static int APPlY_TYPE_COMMERCIAL_TENANT = 1;
    /**
     * 申请服务商
     */
    public final static int APPlY_TYPE_PROVIDER = 2;
    /**
     * 申请派送员
     */
    public final static int APPlY_TYPE_COURIER = 3;

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


    @BindView(R.id.tvArea)
    TextView tvArea;
    @BindView(R.id.tvBusinessName)
    TextView tvBusinessName;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.llDetailAddress)
    LinearLayout llDetailAddress;
    @BindView(R.id.ivCardFront)
    ImageView ivCardFront;
    @BindView(R.id.btnCardFront)
    Button btnCardFront;
    @BindView(R.id.llCardFront)
    LinearLayout llCardFront;
    @BindView(R.id.ivCardBackground)
    ImageView ivCardBackground;
    @BindView(R.id.btnCardBackground)
    Button btnCardBackground;
    @BindView(R.id.llCardBackground)
    LinearLayout llCardBackground;
    @BindView(R.id.ivLicense)
    ImageView ivLicense;
    @BindView(R.id.btnLicense)
    Button btnLicense;
    @BindView(R.id.llLicense)
    LinearLayout llLicense;
    private AreaDialog areaDialog;
    private List<Region> cities;
    private List<Region> provinces;
    private List<Region> areas;
    private int mType;
    private AlertDialog mBusinessDialog;
    private String mAreaString;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_apply_partner);
        ButterKnife.bind(this);
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mType = getIntent().getIntExtra(ConstantValue.TYPE, APPlY_TYPE_COMMERCIAL_TENANT);
        if (mType == APPlY_TYPE_COMMERCIAL_TENANT) {
            initRedTitle("商户申请");
            tvTitle.setText("供货范围");
            llLicense.setVisibility(View.VISIBLE);
        } else if (mType == APPlY_TYPE_PROVIDER) {
            initRedTitle("服务商申请");
            tvTitle.setText("服务范围");
            llLicense.setVisibility(View.VISIBLE);
        } else {
            initRedTitle("派送申请");
            llDetailAddress.setVisibility(View.GONE);
            llCardFront.setVisibility(View.VISIBLE);
            llCardBackground.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected ApplyPartnerPresenter createPresenter() {
        return new ApplyPartnerPresenter(this);
    }

    /**
     * 选择图片的类型
     */
    private int mImageType;
    private String mCardFrontPath;
    private String mCardBackgroundPath;
    private String mLicencePath;

    @OnClick({R.id.tvArea, R.id.tvBusinessName, R.id.btnCardFront, R.id.btnCardBackground, R.id.btnLicense, R.id.btnSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvArea:
                showAreaDialog();
                break;
            case R.id.tvBusinessName:
                if (TextUtils.isEmpty(mAreaCode)) {
                    showToast("请先选择城市");
                    return;
                }
                if (mAreaShop == null) {
                    showToast("商区还没加载出来，请稍后");
                    return;
                }
                if (mAreaShop.size() == 0) {
                    showToast("该地区还没有商区");
                    return;
                }
                showBusinessDialog();
                break;
            case R.id.btnCardFront:
                mImageType = IMAGE_TYPE_ID_CARD_FRONT;
                showActionSheet();
                break;
            case R.id.btnCardBackground:
                mImageType = IMAGE_TYPE_ID_CARD_BACKGROUND;
                showActionSheet();
                break;
            case R.id.btnLicense:
                mImageType = IMAGE_TYPE_LICENSE;
                showActionSheet();
                break;
            case R.id.btnSubmit:
                String address = etAddress.getText().toString();
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                String method="";
                if(TextUtils.isEmpty(mAreaString))
                {
                    showToast("请选择城市");
                    return;
                }
                if(TextUtils.isEmpty(mAreaCode))
                {
                    showToast("请选择城市");
                    return;
                }
                if(mSelectArea==null)
                {
                    showToast("请选择入驻商区名称");
                    return;
                }
                if(!CommonUtil.checkPhone(phone,true))
                {
                    return;
                }

                if(mType==APPlY_TYPE_COMMERCIAL_TENANT||mType==APPlY_TYPE_PROVIDER)
                {
                    if(TextUtils.isEmpty(address))
                    {
                        showToast("请输入详细联系地址");
                        return;
                    }
                    if(TextUtils.isEmpty(mLicencePath))
                    {
                        showToast("请上传营业执照");
                        return;
                    }

                    if(mType==APPlY_TYPE_COMMERCIAL_TENANT)
                    {
                        method="ApplyStores";
                    }else {
                        method="ApplyServiceProvider";
                    }

                }else if(mType==APPlY_TYPE_COURIER)
                {
                    if(TextUtils.isEmpty(mCardFrontPath))
                    {
                        showToast("请上传身份证正面");
                        return;
                    }
                    if(TextUtils.isEmpty(mCardBackgroundPath))
                    {
                        showToast("请上传身份证反面");
                        return;
                    }
                    method="ApplyCourier";
                }
                if(TextUtils.isEmpty(name))
                {
                    showToast("请输入姓名");
                    return;
                }
                if(TextUtils.isEmpty(phone))
                {
                    showToast("请输入手机号码");
                    return;
                }

                mvpPresenter.applyPartner(method,user.UserId+"",mAreaString, mSelectArea, address, name, phone, mCardFrontPath, mCardBackgroundPath, mLicencePath);



                break;
        }
    }

    private List<Area> mAreaShop;
    private Area mSelectArea;

    private void showBusinessDialog() {

        if (mBusinessDialog == null) {

            List<String> shops = new ArrayList<>();
            for (int i = 0; i < mAreaShop.size(); i++) {
                shops.add(mAreaShop.get(i).AreaName);
            }
            mBusinessDialog = new AlertDialog.Builder(this)
                    .setSingleChoiceItems(shops.toArray(new String[mAreaShop.size()]), 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mSelectArea = mAreaShop.get(which);
                            tvBusinessName.setText(mSelectArea.AreaName);
                            dialog.dismiss();
                        }
                    })
                    .create();
        }
        mBusinessDialog.show();
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
                            PhotoUtils.pickImageFromAlbum(ApplyPartnerActivity.this);
                        } else {
                            PhotoUtils.pickImageFromCamera(ApplyPartnerActivity.this);
                        }
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mvpPresenter.onActivityResult(requestCode, resultCode, data);
    }

    private String mAreaCode;

    private void showAreaDialog() {
        if (areaDialog == null) {
            areaDialog = new AreaDialog(this);
            areaDialog.setOnSelectedAreaListener(new AreaDialog.OnSelectedAreaListener() {
                @Override
                public void onSelectedAreaSuccess(String[] code) {
                    mAreaCode = code[0];
                    mAreaString = code[1];
                    tvArea.setText(code[1]);
                    mSelectArea = null;
                    tvBusinessName.setText("请选择您想入驻商区名称");
                    mvpPresenter.getShopAreaList(mAreaCode);
                }
            });
        }
        areaDialog.show();
    }

    private String getAddressText(int provinceIndex, int cityIndex, int areaIndex) {

        return provinces.get(provinceIndex).Name + " " + cities.get(cityIndex).Name + " " + areas.get(areaIndex).Name;
    }

    private List<String> getAddressTitle(List<Region> regions) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < regions.size(); i++) {
            list.add(regions.get(i).Name);
        }
        return list;
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
    public void getShopAreaListSuccess(List<Area> response) {
        mAreaShop = response;
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
