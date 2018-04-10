package com.baishan.nearshop.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseActivity;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class BarcodeScannerActivity extends BaseActivity {

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_barcode_scanner);
    }

    @Override
    protected void bindViews() {
        barcodeScannerView = get(R.id.zxing_barcode_scanner);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
//        new IntentIntegrator(this).initiateScan();
//        barcodeScannerView = initializeContent();
        initTitle("条码扫描");
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }

    @Override
    protected void setListener() {

    }

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;


    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
