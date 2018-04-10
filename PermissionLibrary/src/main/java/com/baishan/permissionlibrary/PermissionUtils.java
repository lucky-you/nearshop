package com.baishan.permissionlibrary;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by RayYeung on 2016/9/9.
 */
public class PermissionUtils {

    private String[] permissions = {
            WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION, CAMERA};
    private static PermissionUtils instance;
    private PermissionCallback permissionCallback;
    private PerPermissionCallback perPermissionCallback;

    private boolean isOpen;

    public static PermissionUtils getInstance() {
        if (instance == null) {
            instance = new PermissionUtils();
        }
        return instance;
    }

    /**
     * 检查是否需要权限申请(多个权限)
     *
     * @param context
     * @return false  不需要 true 需要
     */
    public boolean check(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        for (String p : permissions) {
            if (!checkPermission(context, p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查多个权限
     *
     * @param context
     * @param callback
     */
    public void checkMutiPermission(Context context, PermissionCallback callback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callback.onFinish();
            return;
        }
        boolean flag = false;
        for (String p : permissions) {
            if (!checkPermission(context, p)) {
                flag = true;
                break;
            }
        }
        if (flag) {
            permissionCallback = callback;
            context.startActivity(new Intent(context, PermissionActivity.class));
        } else {
            callback.onFinish();
        }
    }

    /**
     * 检查单个权限
     *
     * @param context
     * @param permission
     * @param callback
     */
    public void checkSinglePermission(Context context, String permission, PerPermissionCallback callback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callback.onGuarantee();
            return;
        }
        if (checkPermission(context, permission)) {
            callback.onGuarantee();
        } else {
            perPermissionCallback = callback;
            Intent it = new Intent(context, PermissionActivity.class);
            it.putExtra("type", PermissionActivity.PERMISSION_SINGLE);
            it.putExtra("name", permission);
            context.startActivity(it);
        }
    }

    private static boolean checkPermission(Context context, String permission) {
        int checkPermission = ContextCompat.checkSelfPermission(context, permission);
        if (checkPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    /**
     * 请求权限
     *
     * @param context
     * @param callback
     */
    public void request(Context context, PermissionCallback callback) {
        permissionCallback = callback;
        context.startActivity(new Intent(context, PermissionActivity.class));
    }

    public void openActivity() {
        isOpen = true;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void onDeny() {
        perPermissionCallback.onDeny();
    }

    public void onGuarantee() {
        perPermissionCallback.onGuarantee();
    }

    public void close() {
        isOpen = false;
        permissionCallback.onClose();
    }

    public void finish() {
        isOpen = false;
        permissionCallback.onFinish();
    }

    public interface PermissionCallback {
        void onClose();

        void onFinish();
    }

    public interface PerPermissionCallback {
        void onDeny();

        void onGuarantee();

    }


}
