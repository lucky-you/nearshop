package com.baishan.nearshop.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.CommonUtil;
import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.ApiService;
import com.baishan.nearshop.base.BaseApplication;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.model.Address;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.Banner;
import com.baishan.nearshop.model.EasyService;
import com.baishan.nearshop.model.ImageInfo;
import com.baishan.nearshop.presenter.ServiceDetailPresenter;
import com.baishan.nearshop.ui.adapter.ImageHolderView;
import com.baishan.nearshop.ui.view.CustWebView;
import com.baishan.nearshop.ui.view.DragLayout;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.IServiceDetalView;
import com.baishan.permissionlibrary.PermissionUtils;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceDetailActivity extends BaseMvpActivity<ServiceDetailPresenter> implements IServiceDetalView {


    @BindView(R.id.banner)
    ConvenientBanner banner;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDesc)
    TextView tvDesc;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.btnOrder)
    Button btnOrder;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvLicense)
    TextView tvLicense;
    @BindView(R.id.tvIdentity)
    TextView tvIdentity;
    @BindView(R.id.tvIntroTip)
    TextView tvIntroTip;
    @BindView(R.id.tvStoreDesc)
    TextView tvStoreDesc;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.llPicContainer)
    LinearLayout llPicContainer;
    @BindView(R.id.llDesc)
    LinearLayout llDesc;
    @BindView(R.id.tvServiceDesc)
    TextView tvServiceDesc;
    @BindView(R.id.tvDeposit)
    TextView tvDeposit;
    @BindView(R.id.webView)
    CustWebView webView;
    @BindView(R.id.dragLayout)
    DragLayout dragLayout;

    private EasyService service;
    private int serviceId;

    private TextView tvAreaName;
    private TextView tvAddress;
    private Area mDefaultArea;

    @Override
    protected ServiceDetailPresenter createPresenter() {
        return new ServiceDetailPresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_service_detail);
    }

    @Override
    protected void bindViews() {
        ButterKnife.bind(this);
        initTitle("服务详情");
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        registerEvent();
        serviceId = getIntent().getIntExtra(ConstantValue.DATA, 0);
        if (serviceId == 0) {
            service = (EasyService) getIntent().getSerializableExtra(ConstantValue.SERVICE);
            initTitle(service.Title);
            initInfo();
        } else {
            mvpPresenter.getServiceDetail(serviceId);
        }
    }

    private void initInfo() {
        List<Banner> list = new ArrayList<>();
        for (String s : service.ImageUrl.split("\\|")) {
            list.add(new Banner(s));
        }
        banner.setPages(
                new CBViewHolderCreator<ImageHolderView>() {
                    @Override
                    public ImageHolderView createHolder() {
                        return new ImageHolderView();
                    }
                }, list)
                .setPageIndicator(new int[]{R.drawable.shape_page_indicator_n, R.drawable.shape_page_indicator_f})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        tvTitle.setText(service.Title);
        if(!TextUtils.isEmpty(service.ServiceIntro)){
            tvDesc.setVisibility(View.VISIBLE);
            tvDesc.setText(service.ServiceIntro);
        }
        tvPrice.setText(service.Price);
        tvName.setText(service.Name);
        if (service.HasLicense == 0) {
            tvLicense.setBackgroundColor(getResources().getColor(R.color.font_grey_light));
        }
        if (service.HasIdentity == 0) {
            tvIdentity.setBackgroundColor(getResources().getColor(R.color.font_grey_light));
        }
        if (service.Deposit > 0) {
            tvDeposit.setText(String.format(getString(R.string.deposit),service.Deposit));
        }
        tvStoreDesc.setText(service.Intro);
        tvPhone.setText(service.Phone);
        if(!TextUtils.isEmpty(service.Description)){
            llDesc.setVisibility(View.VISIBLE);
            tvServiceDesc.setText(service.Description);
        }
        webView.loadUrl(ApiService.SERVICE_DETAIL_URL + service.ServiceId + ".html");
        List<ImageInfo> images = service.getProviderImage();
        for (ImageInfo url : images) {
            ImageView iv = new ImageView(mContext);
            int size = CommonUtil.dip2px(mContext, 80);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.rightMargin = CommonUtil.dip2px(mContext, 10);
            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoaderUtils.displayImage(url.ImageUrl, iv);
            llPicContainer.addView(iv);
        }
    }

    @Override
    protected void setListener() {
        tvPhone.setOnClickListener(v -> {
            if(service!=null){
                PermissionUtils.getInstance().checkSinglePermission(mContext, Manifest.permission.CALL_PHONE, new PermissionUtils.PerPermissionCallback() {
                    @Override
                    public void onDeny() {
                        showToast("申请拨打电话权限失败");
                    }

                    @Override
                    public void onGuarantee() {
                        CommonUtil.callPhone(mContext,service.Phone);
                    }
                });
            }
        });
    }

    @Subscribe
    public void onEvent(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_SELECTED_ADDRESS) {
            if (ServiceDetailActivity.class.getSimpleName().equals(notice.content1)) {
                Area area = (Area) notice.content;
                mDefaultArea = area;
                mSelectedAddress = area.AddressInfo.get(0);
                tvAreaName.setText(area.AreaName);
                tvAreaName.setOnClickListener(null);
                tvAddress.setText(mSelectedAddress.Address + " (" + mSelectedAddress.Contact + " 收" + " " + mSelectedAddress.Phone);
//            showOrderDialog(area);
            }

        }
    }

    private Address mSelectedAddress;

    @OnClick({R.id.btnOrder})
    void click(View v) {
        if (!checkLogin()) return;
        mDefaultArea = BaseApplication.getInstance().getCurrentArea();
        mSelectedAddress = mDefaultArea.defaultAddress;
        showOrderDialog();
    }

    private void showOrderDialog() {


        View content = View.inflate(mContext, R.layout.dialog_order, null);
        AlertDialog dialog = new AlertDialog.Builder(mContext).setView(content).show();
        RelativeLayout relAddress = (RelativeLayout) content.findViewById(R.id.relAddress);
        tvAreaName = (TextView) content.findViewById(R.id.tvAreaName);
        tvAddress = (TextView) content.findViewById(R.id.tvAddress);
        TextView tvFinish = (TextView) content.findViewById(R.id.tvFinish);
        Button btnCommit = (Button) content.findViewById(R.id.btnCommit);
        EditText etRemark = (EditText) content.findViewById(R.id.etRemark);
        relAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, AddrManageActivity.class);
                it.putExtra(ConstantValue.CLASSNAME, ServiceDetailActivity.class.getSimpleName());
                it.putExtra(ConstantValue.TYPE, AddrManageActivity.INTENT_SELECT);
                startActivity(it);
            }
        });
        if (mSelectedAddress == null) {
            tvAreaName.setText("请选择地址");

        } else {
            tvAreaName.setText(mDefaultArea.AreaName);
            tvAddress.setText(mSelectedAddress.Address + " (" + mSelectedAddress.Contact + " 收" + " " + mSelectedAddress.Phone);
        }


        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedAddress == null) {
                    showToast("请选择地址");
                    return;
                }
                dialog.dismiss();
                String remark = etRemark.getText().toString();
                mvpPresenter.reservationService(mDefaultArea, mSelectedAddress, remark, user.UserId, service.Id);
            }
        });
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    @Override
    public void reservationServiceSuccess(String response) {
        showToast("预约成功");
    }

    @Override
    public void getServiceDetailSuccess(EasyService easyService) {
        this.service = easyService;
        initInfo();
    }
}


