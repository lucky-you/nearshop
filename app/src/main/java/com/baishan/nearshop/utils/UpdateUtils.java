/**
 *
 */
package com.baishan.nearshop.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.baishan.mylibrary.utils.FileUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.AppClient;
import com.baishan.nearshop.model.UpdateInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 版本更新工具
 */
public class UpdateUtils {

    private static final String DOWNLOAD_ACTION = "com.baishan.notification.download";
    private static final String INSTALL_ACTION = "com.baishan.notification.install";
    private static final int NOTIFICATION_ID = 100;

    private static UpdateInfo updateInfo;

    /**
     * 检查是否需要更新
     *
     * @param context
     * @param callback
     */
    public static void checkUpdate(final Activity context, final UpdateCallback callback) {

        AppClient.getApiService().getUpdateInfo().
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UpdateInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.result(false);
                    }

                    @Override
                    public void onNext(UpdateInfo info) {
                        if (info != null) {
                            if (info.version.compareTo(getVersionName(context)) > 0) {
                                updateInfo = info;
                                callback.result(true);
                            } else {
                                callback.result(false);
                            }
                        } else {
                            callback.result(false);
                        }
                    }
                });

    }

    /***
     * 执行更新
     *
     * @param context
     */
    public static void update(Activity context) {
        showUpdateDialog(context);
    }


    /*
     * 获得应用版本名称
     */
    private static String getVersionName(Activity context) {
        PackageManager pm = context.getPackageManager();
        try {
            // 获得清单文件的信息
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static void showUpdateDialog(final Activity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("升级提示")
                .setMessage(updateInfo.description)
                .setPositiveButton("立刻升级", (dialog1, which) -> {
                    // 下载APK并替换安装
                    if (Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        getApk(context);
                    } else {
                        Toast.makeText(context, "未检测到SD卡，请插入SD卡再运行",
                                Toast.LENGTH_SHORT).show();
                    }
                    dialog1.dismiss();
                });
        if (!updateInfo.forceUpdate) builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(!updateInfo.forceUpdate);
        dialog.setCanceledOnTouchOutside(!updateInfo.forceUpdate);
        dialog.show();
    }

    private static void getApk(final Activity context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle("正在下载")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowWhen(false)
                .setTicker("更新文件下载中")
                .setProgress(100, 0, false);
        manager.notify(NOTIFICATION_ID, builder.build());

//        IntentFilter filter = new IntentFilter();
//        filter.addAction(DOWNLOAD_ACTION);
//        filter.addAction(INSTALL_ACTION);
//        context.registerReceiver(new NotificationReceiver(), filter);

        final int[] downloadCount = {0};
        AppClient.getApiService()
                .downloadApk(updateInfo.apkurl)
                .map(new FileCallBack() {
                    @Override
                    public void inProgress(float progress, long total) {
                        int pro = (int) (progress * 100);
                        //为了防止频繁的通知导致应用吃紧，百分比增加5才通知一次
                        if (pro - 5 >= downloadCount[0]) {
                            downloadCount[0] += 5;
                            builder.setContentTitle("已下载" + downloadCount[0] + "%");
                            builder.setProgress(100, downloadCount[0], false);
                            manager.notify(NOTIFICATION_ID, builder.build());
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        builder.setContentTitle("下载失败");
//                Intent it = new Intent(DOWNLOAD_ACTION);
//                PendingIntent intent = PendingIntent.getBroadcast(context, REQUEST_CODE, it, 0);
//                builder.setContentIntent(intent);
                        manager.notify(NOTIFICATION_ID, builder.build());
                    }

                    @Override
                    public void onNext(File file) {
                        if (file != null) {
                            builder.setContentTitle("下载完成");
//                Intent it = new Intent(INSTALL_ACTION);
//                PendingIntent intent = PendingIntent.getBroadcast(context, REQUEST_CODE, it, 0);
//                builder.setContentIntent(intent);
                            manager.notify(NOTIFICATION_ID, builder.build());
                            // 安装APK
                            installApk(file, context);
                        }
                    }
                });

    }


    // 安装APK
    private static void installApk(File apk, Activity context) {
        Intent it = new Intent();
        it.setAction("android.intent.action.VIEW");
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        it.setDataAndType(Uri.fromFile(apk),
                "application/vnd.android.package-archive");
        context.startActivity(it);
    }

    /**
     * 检查更新结果回调
     */
    public interface UpdateCallback {
        /**
         * 更新结果
         *
         * @param update 是否需要更新
         */
        void result(boolean update);

    }


    private static class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.getAction().equals(DOWNLOAD_ACTION)) {

                } else {

                }
            }
        }
    }


    /***
     * 文件保存及进度回调
     */
    private static class FileCallBack implements Func1<ResponseBody, File> {

        @Override
        public File call(ResponseBody responseBody) {
            File file = null;
            try {
                file = saveFile(responseBody);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        }

        public void inProgress(float progress, long total) {

        }

        private File saveFile(ResponseBody response) throws IOException {
            String destFileDir = FileUtils.getDirFilePath(FileUtils.CACHE_FILE_PATH);
            String destFileName = updateInfo.apkurl.substring(updateInfo.apkurl.lastIndexOf("/") + 1);
            InputStream is = null;
            byte[] buf = new byte[2048];
            int len = 0;
            FileOutputStream fos = null;
            try {
                is = response.byteStream();
                final long total = response.contentLength();
                long sum = 0;

                File dir = new File(destFileDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, destFileName);
                fos = new FileOutputStream(file);
                while ((len = is.read(buf)) != -1) {
                    sum += len;
                    fos.write(buf, 0, len);
                    inProgress(sum * 1.0f / total, total);
                }
                fos.flush();

                return file;

            } finally {
                try {
                    if (is != null) is.close();
                } catch (IOException e) {
                }
                try {
                    if (fos != null) fos.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
