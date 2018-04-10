package com.baishan.nearshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.SPUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseApplication;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.dao.RegionDao;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.City;
import com.baishan.nearshop.model.Region;
import com.baishan.nearshop.presenter.LocationPresenter;
import com.baishan.nearshop.presenter.SelectAddrPresenter;
import com.baishan.nearshop.ui.adapter.AreaAdapter;
import com.baishan.nearshop.ui.adapter.CityAdapter;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.ILocationView;
import com.baishan.nearshop.view.ISelectAddrView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectAddrActivity extends BaseMvpActivity<SelectAddrPresenter> implements ISelectAddrView, ILocationView {

    @BindView(R.id.tvCity)
    TextView tvCity;
    @BindView(R.id.tvChange)
    TextView tvChange;
    @BindView(R.id.ivChange)
    ImageView ivChange;
    @BindView(R.id.areaList)
    RecyclerView areaList;
    @BindView(R.id.tvCityCounty)
    TextView tvCityCounty;
    @BindView(R.id.tvArea)
    TextView tvArea;
    @BindView(R.id.tvAreaHint)
    TextView tvAreaHint;
    @BindView(R.id.generalAreaList)
    RecyclerView generalAreaList;
    @BindView(R.id.flContainer)
    FrameLayout flContainer;
    @BindView(R.id.layoutCurrent)
    LinearLayout layoutCurrent;

    private RecyclerView cityList;
    private RecyclerView shopAreaList;
    private ImageView ivBack;
    private EditText editText;
    private TextView tvSure;
    private View header;

    public static final int INTENT_SPLASH = 1;
    private int type;
    private CityAdapter cityAdapter;
    private List<City> cities = new ArrayList<>();
    private AreaAdapter areaAdapter;
    private List<Area> areas = new ArrayList<>();
    private List<Area> counties = new ArrayList<>();
    private AreaAdapter searchAreaAdapter;
    private List<Area> searchAreas = new ArrayList<>();
    private AreaAdapter commonAreaAdapter;
    private List<Area> commonAreas = new ArrayList<>();
    private LocationPresenter locationPresenter;
    private Area selArea = new Area();

    private boolean isSearch = false;
    private String province;
    private boolean changeCity = true;


    @Override
    protected SelectAddrPresenter createPresenter() {
        locationPresenter = new LocationPresenter(this);
        return new SelectAddrPresenter(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_select_addr);
    }

    @Override
    protected void bindViews() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        editText = (EditText) findViewById(R.id.editText);
        tvSure = (TextView) findViewById(R.id.tvSure);
        header = View.inflate(mContext, R.layout.header_address_select, null);
        header.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ButterKnife.bind(this, header);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        type = getIntent().getIntExtra(ConstantValue.TYPE, 0);
        locationPresenter.startLocation(mContext);
        mvpPresenter.getHotCities();
        if (user != null) locationPresenter.getCommonArea(user.UserId);
        cityAdapter = new CityAdapter(cities);
        cityList = initCommonRecyclerView(R.id.cityList, cityAdapter, null);
        cityAdapter.addHeaderView(header);
        areaAdapter = new AreaAdapter(areas, 1);
        areaList.setLayoutManager(new GridLayoutManager(mContext, 3));
        areaList.setAdapter(areaAdapter);
        searchAreaAdapter = new AreaAdapter(searchAreas, 2);
        shopAreaList = initGridRecyclerView(R.id.shopAreaList, searchAreaAdapter, null, 3);
        commonAreaAdapter = new AreaAdapter(commonAreas, 3);
        generalAreaList.setLayoutManager(new GridLayoutManager(mContext, 3));
        generalAreaList.setAdapter(commonAreaAdapter);
        if (currentArea != null) {
            layoutCurrent.setVisibility(View.VISIBLE);
            tvCityCounty.setVisibility(View.VISIBLE);
            tvCityCounty.setText(currentArea.concatCC());
            tvArea.setText(currentArea.AreaName);
        }
    }

    @Override
    protected void setListener() {
        ivBack.setOnClickListener(v -> back());
        tvSure.setOnClickListener(v -> search());
        layoutCurrent.setOnClickListener(v -> finish());
        cityAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
            City city = cities.get(i);
            selectCity(city.CityCode, city.Province, city.Name);
        });
        areaAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
            Area area = areas.get(i);
            if (area.AreaId == 0) {
                selArea.County = area.County;
                selArea.AdCode = area.AdCode;
                tvCity.setText(selArea.City + area.County);
                tvChange.setText("返回区县");
                changeCity = false;
                mvpPresenter.getShopAreaList(area.AdCode);
            } else {
                selArea.AreaId = area.AreaId;
                selArea.AreaName = area.AreaName;
                selArea.ChatroomId = area.ChatroomId;
                selectArea(selArea);
            }
        });
        searchAreaAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
            selectArea(searchAreas.get(i));
        });
        commonAreaAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
            selectArea(commonAreas.get(i));
        });
    }


    /**
     * 选择了商区
     *
     * @param area
     */
    private void selectArea(Area area) {
        if (type == INTENT_SPLASH) {
            SPUtils.set(ConstantValue.SP_AREAID, area.AreaId);
            BaseApplication.getInstance().setCurrentArea(area);
            gotoMain();
        } else if (currentArea.AreaId != area.AreaId) {
            SPUtils.set(ConstantValue.SP_AREAID, area.AreaId);
            BaseApplication.getInstance().setCurrentArea(area);
            post(new Notice(ConstantValue.MSG_TYPE_UPDATE_AREA));
        }
        finish();
    }

    private void selectCity(String cityCode, String province, String city) {
        areas.clear();
        areaAdapter.notifyDataSetChanged();
        selArea.CityCode = cityCode;
        mvpPresenter.getAreaList(selArea.CityCode);
        selArea.Province = province;
        selArea.City = city;
        tvCity.setText(selArea.City);
        tvChange.setText("切换城市");
        changeCity = true;
    }

    private void back() {
        if (isSearch) {
            isSearch = false;
            searchAreas.clear();
            searchAreaAdapter.notifyDataSetChanged();
            cityList.setVisibility(View.VISIBLE);
            editText.setText("");
            return;
        }
        if (type == INTENT_SPLASH) {
            if (currentArea == null) {
                new AlertDialog.Builder(mContext)
                        .setMessage("没有选择商区将无法使用该应用")
                        .setNegativeButton("退出应用", (dialog, which) -> {
                            finish();
                        })
                        .setPositiveButton("继续选择", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            } else {
                gotoMain();
            }
        } else {
            finish();
        }

    }

    private void gotoMain() {
        Parcelable parcelableExtra = getIntent().getParcelableExtra(ConstantValue.EXTRA_INTENT);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ConstantValue.EXTRA_INTENT, parcelableExtra);
        startActivity(intent);
    }

    private void search() {
        String key = editText.getText().toString();
        if (TextUtils.isEmpty(key)) {
            return;
        }
        isSearch = true;
        cityList.setVisibility(View.GONE);
        mvpPresenter.searchArea(key);
    }

    @OnClick({R.id.tvChange})
    void click(View v) {
        switch (v.getId()) {
            case R.id.tvChange:
                if(changeCity){
                    showProvinceDialog();
                }else{
                    tvChange.setText("切换城市");
                    changeCity = true;
                    tvCity.setText(selArea.City);
                    areaAdapter.getData().clear();
                    areaAdapter.addData(counties);
                }
//                if (flContainer.getVisibility() == View.VISIBLE) {
//                    flContainer.setVisibility(View.GONE);
//                    ivChange.setImageResource(R.drawable.ic_triangle_grey_down);
//                } else {
//                    flContainer.setVisibility(View.VISIBLE);
//                    ivChange.setImageResource(R.drawable.ic_triangle_grey_up);
//                }
                break;
        }
    }

    private void showProvinceDialog() {
        List<Region> provinces = RegionDao.getProvinces();
        String[] pArray = new String[provinces.size()];
        for (int i = 0; i < provinces.size(); i++) {
            pArray[i] = provinces.get(i).Name;
        }
        new AlertDialog.Builder(mContext)
                .setTitle("请选择省份")
                .setItems(pArray, (dialog, which) -> {
                    Region region = provinces.get(which);
                    province = region.Name;
                    showCityDialog(region.AdCode);
                }).show();
    }

    private void showCityDialog(String adCode) {
        List<Region> cities = RegionDao.queryCities(adCode);
        String[] cArray = new String[cities.size()];
        for (int i = 0; i < cities.size(); i++) {
            cArray[i] = cities.get(i).Name;
        }
        new AlertDialog.Builder(mContext)
                .setTitle("请选择城市")
                .setItems(cArray, (dialog, which) -> {
                    Region region = cities.get(which);
                    selectCity(region.CityCode, province, region.Name);
                }).show();
    }

    @Override
    public void getAreaListSuccess(List<Area> response) {
        if (response.size() == 0) {
            areaList.setVisibility(View.GONE);
            tvAreaHint.setVisibility(View.VISIBLE);
        } else {
            tvAreaHint.setVisibility(View.GONE);
            areaList.setVisibility(View.VISIBLE);
            counties = response;
            areaAdapter.getData().clear();
            areaAdapter.addData(response);
        }
    }

    @Override
    public void getShopAreaListSuccess(List<Area> response) {
        areaAdapter.getData().clear();
        areaAdapter.addData(response);
    }

    @Override
    public void locationSuccess(AMapLocation aMapLocation) {
        locationPresenter.stopLocation();
        selectCity(aMapLocation.getCityCode(), aMapLocation.getProvince(), aMapLocation.getCity());
    }

    @Override
    public void getHotCitiesSuccess(List<City> response) {
        cityAdapter.addData(response);
    }

    @Override
    public void searchAreaSuccess(List<Area> response) {
        searchAreas.clear();
        searchAreaAdapter.addData(response);
    }

    @Override
    public void locationFailure() {
        tvCity.setText("定位失败");
    }

    @Override
    public void getCommonAreaSuccess(List<Area> response) {
        commonAreaAdapter.addData(response);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationPresenter != null) {
            locationPresenter.detachView();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }
}
