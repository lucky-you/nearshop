package com.baishan.nearshop.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.baishan.nearshop.R;
import com.baishan.nearshop.dao.RegionDao;
import com.baishan.nearshop.model.Region;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/13 0013.
 */
public class AreaDialog {

    private List<Region> provinces;
    private List<Region> cities;
    private List<Region> areas;
    private Dialog dialog;

    public AreaDialog(Context context) {
        createAreaDialog(context);
    }

    private  void createAreaDialog(Context context)
    {
        dialog = new Dialog(context);
        dialog.show();
        WindowManager windowManager = dialog.getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Window win = dialog.getWindow();
        win.setWindowAnimations(android.R.style.Animation_Translucent);
        WindowManager.LayoutParams lp = win.getAttributes();
        win.setBackgroundDrawableResource(android.R.color.transparent);
        lp.width = (display.getWidth()); //设置宽度
        win.setAttributes(lp);
        View areaView = View.inflate(context, R.layout.dialog_select_area, null);
        WheelView wvProvince = (WheelView) areaView.findViewById(R.id.wvProvince);
        WheelView wvCity = (WheelView) areaView.findViewById(R.id.wvCity);
        WheelView wvArea = (WheelView) areaView.findViewById(R.id.wvArea);
        TextView tvEnter = (TextView) areaView.findViewById(R.id.tvEnter);

        provinces = RegionDao.getProvinces();
        cities = RegionDao.queryCities(provinces.get(0).AdCode);
        areas = RegionDao.queryAreas(cities.get(0).CityCode);
        ArrayWheelAdapter provinceAdapter = new ArrayWheelAdapter(context);
        ArrayWheelAdapter cityAdapter = new ArrayWheelAdapter(context);
        ArrayWheelAdapter areaAdapter = new ArrayWheelAdapter(context);
//            设置省市区初始数据
        wvProvince.setWheelAdapter(provinceAdapter);
        wvProvince.setWheelData(getAddressTitle(provinces));
        wvCity.setWheelAdapter(cityAdapter);
        wvCity.setWheelData(getAddressTitle(cities));
        wvArea.setWheelAdapter(areaAdapter);
        wvArea.setWheelData(getAddressTitle(areas));
        //区code和省市区文字
        final String[] code = {areas.get(0).AdCode, getAddressText(0, 0, 0),provinces.get(0).CityCode};
        wvProvince.setSkin(WheelView.Skin.Holo);
        wvCity.setSkin(WheelView.Skin.Holo);
        wvArea.setSkin(WheelView.Skin.Holo);

        wvProvince.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                cities.clear();
                List<Region> cityList = RegionDao.queryCities(provinces.get(position).AdCode);
                AreaDialog.this.cities.addAll(cityList);
                cityAdapter.setData(getAddressTitle(AreaDialog.this.cities));


                areas.clear();
                try {
                    areas.addAll(RegionDao.queryAreas(AreaDialog.this.cities.get(0).CityCode));
                    code[0] = areas.get(0).AdCode;
                    code[1] = getAddressText(position, 0, 0);
                    code[2] = cityList.get(0).CityCode;
                } catch (Exception e) {
                    //没有城市和区
                    code[0] = provinces.get(position).AdCode;
                    code[1] = provinces.get(position).Name;
                    code[2] = cityList.get(0).CityCode;
                }
                areaAdapter.setData(getAddressTitle(areas));

            }
        });
        wvCity.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                areas.clear();
                try {
                    areas.addAll(RegionDao.queryAreas(cities.get(position).CityCode));
                    code[0] = areas.get(0).AdCode;
                    code[1] = getAddressText(wvProvince.getCurrentPosition(), position, 0);
                    code[2] = cities.get(position).CityCode;
                } catch (Exception e) {
                    //没有城市和区
                    code[0] = provinces.get(position).AdCode;
                    code[1] = provinces.get(position).Name;
                }
                areaAdapter.setData(getAddressTitle(areas));

            }
        });
        wvArea.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                code[0] = areas.get(position).AdCode;
                code[1] = getAddressText(wvProvince.getCurrentPosition(), wvCity.getCurrentPosition(), position);
            }
        });
        tvEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                if(onSelectedAreaListener!=null)
                {
                    onSelectedAreaListener.onSelectedAreaSuccess(code);
                }



            }
        });
        dialog.setContentView(areaView);
    }
    public void show()
    {
        dialog.show();
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
    private OnSelectedAreaListener onSelectedAreaListener;

    public void setOnSelectedAreaListener(AreaDialog.OnSelectedAreaListener onSelectedAreaListener) {
        this.onSelectedAreaListener = onSelectedAreaListener;
    }

    public interface OnSelectedAreaListener{
        void onSelectedAreaSuccess(String[] code);
    }
}
