package com.baishan.nearshop.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.baishan.mylibrary.view.ProgressWebView;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseActivity;
import com.baishan.nearshop.base.BaseApplication;
import com.baishan.nearshop.model.Area;
import com.baishan.nearshop.model.UserInfo;
import com.baishan.nearshop.ui.view.TitleBuilder;
import com.baishan.nearshop.utils.ConstantValue;

import org.json.JSONException;
import org.json.JSONObject;


public class WebActivity extends BaseActivity {

    private ProgressWebView web;
    private String url;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_web);
    }

    @Override
    protected void bindViews() {
        web = (ProgressWebView) findViewById(R.id.web);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        TitleBuilder titleBuilder = new TitleBuilder(this);
        titleBuilder.setRightImage(R.drawable.ic_refresh)
                .setRightOnClickListener(v -> refresh());
        String title = getIntent().getStringExtra(ConstantValue.TITLE);
        titleBuilder.setTitleText(TextUtils.isEmpty(title) ? "网页" : title);
        web.addJavascriptInterface(new AndroidJavaScript(this), "Android");
        url = getIntent().getStringExtra(ConstantValue.URL);
        web.loadUrl(url);
    }

    private void refresh() {
        web.clearCache(true);
        web.loadUrl(url);
    }

    @Override
    protected void setListener() {

    }

    class AndroidJavaScript {

        private Activity activity;

        public AndroidJavaScript(Activity activity) {
            this.activity = activity;
        }

        @JavascriptInterface
        public String getInfo() {
            UserInfo userInfo = BaseApplication.getInstance().getUserInfo();
            Area area = BaseApplication.getInstance().getCurrentArea();
            JSONObject json = new JSONObject();
            try {
                json.put("UserId", userInfo == null ? "-1" : userInfo.UserId);
                json.put("AreaId", area.AreaId);
                json.put("AreaName", area.AreaName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json.toString();
        }
    }

}
