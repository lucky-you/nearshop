package com.baishan.nearshopclient.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.nearshopclient.R;
import com.baishan.nearshopclient.base.BaseApplication;
import com.baishan.nearshopclient.db.DBManager;
import com.baishan.nearshopclient.model.PushParser;
import com.baishan.nearshopclient.model.UserInfo;
import com.baishan.nearshopclient.ui.activity.GoodsOrdersDetailActivity;
import com.baishan.nearshopclient.ui.activity.LoginActivity;
import com.baishan.nearshopclient.ui.activity.MainActivity;
import com.baishan.nearshopclient.ui.activity.MyMessageActivity;
import com.baishan.nearshopclient.ui.activity.OrdersActivity;
import com.baishan.nearshopclient.utils.ConstantValue;
import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.FeedbackCmdMessage;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.igexin.sdk.message.SetTagCmdMessage;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;


/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class PushIntentService extends GTIntentService {

    //系统消息
    public static final String COMMON = "Common";

    /****************便民****************/

    /**
     * 用户取消订单
     */
    public static final String USER_CANCEL_ORDER = "UserCancelOrder";
    /**
     * 确认无误
     */
    public static final String CONFIRM_OK = "ConfirmOk";
    /**
     * 申请退款
     */
    public static final String APPLY_REFUND = "ApplyRefund";
    /**
     * 来订单了
     */
    public static final String RESERVATION_SERVICE = "ReservationService";

    /****************便民****************/

    /****************商品****************/

    /**
     * 确认收货
     */
    public static final String CONFIRM_OK_SHOP_ORDER = "ConfirmOkShopOrder";
    /**
     * 取消订单
     */
    public static final String CANCEL_SHOP_ORDER = "CancelShopOrder";
    /**
     * 申请退款
     */
    public static final String APPLY_SHOP_REFUND = "ApplyShopRefund";
    /**
     * 超市订单
     */
    public static final String SHOP_ORDER = "ShopOrder";
    /* ****************商品*/


    private static final String TAG = "PushIntentService";

    private Context context;

    /**
     * 为了观察透传数据变化.
     */
    private static int cnt;

    public PushIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        Log.d(TAG, "onReceiveServicePid -> " + pid);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        this.context = context;

        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();

        // 第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
        Log.d(TAG, "call sendFeedbackMessage = " + (result ? "success" : "failed"));

        Log.d(TAG, "onReceiveMessageData -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nmessageid = " + messageid + "\npkg = " + pkg
                + "\ncid = " + cid);

        if (payload == null) {
            Log.e(TAG, "receiver payload = null");
        } else {
            String data = new String(payload);
            Log.d(TAG, "receiver payload = " + data);

            // 测试消息为了观察数据变化
            if (data.equals("收到一条透传测试消息")) {
                data = data + "-" + cnt;
                cnt++;
            }
            Logger.json(data);
            try {
                processData(data);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        Log.d(TAG, "----------------------------------------------------------------------------------------------");
    }


    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);


    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        Log.d(TAG, "onReceiveOnlineState -> " + (online ? "online" : "offline"));
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Log.d(TAG, "onReceiveCommandResult -> " + cmdMessage);

        int action = cmdMessage.getAction();

        if (action == PushConsts.SET_TAG_RESULT) {
            setTagResult((SetTagCmdMessage) cmdMessage);
        } else if ((action == PushConsts.THIRDPART_FEEDBACK)) {
            feedbackResult((FeedbackCmdMessage) cmdMessage);
        }
    }

    private void setTagResult(SetTagCmdMessage setTagCmdMsg) {
        String sn = setTagCmdMsg.getSn();
        String code = setTagCmdMsg.getCode();

        String text = "设置标签失败, 未知异常";
        switch (Integer.valueOf(code)) {
            case PushConsts.SETTAG_SUCCESS:
                text = "设置标签成功";
                break;

            case PushConsts.SETTAG_ERROR_COUNT:
                text = "设置标签失败, tag数量过大, 最大不能超过200个";
                break;

            case PushConsts.SETTAG_ERROR_FREQUENCY:
                text = "设置标签失败, 频率过快, 两次间隔应大于1s且一天只能成功调用一次";
                break;

            case PushConsts.SETTAG_ERROR_REPEAT:
                text = "设置标签失败, 标签重复";
                break;

            case PushConsts.SETTAG_ERROR_UNBIND:
                text = "设置标签失败, 服务未初始化成功";
                break;

            case PushConsts.SETTAG_ERROR_EXCEPTION:
                text = "设置标签失败, 未知异常";
                break;

            case PushConsts.SETTAG_ERROR_NULL:
                text = "设置标签失败, tag 为空";
                break;

            case PushConsts.SETTAG_NOTONLINE:
                text = "还未登陆成功";
                break;

            case PushConsts.SETTAG_IN_BLACKLIST:
                text = "该应用已经在黑名单中,请联系售后支持!";
                break;

            case PushConsts.SETTAG_NUM_EXCEED:
                text = "已存 tag 超过限制";
                break;

            default:
                break;
        }

        Log.d(TAG, "settag result sn = " + sn + ", code = " + code + ", text = " + text);
    }

    private void feedbackResult(FeedbackCmdMessage feedbackCmdMsg) {
        String appid = feedbackCmdMsg.getAppid();
        String taskid = feedbackCmdMsg.getTaskId();
        String actionid = feedbackCmdMsg.getActionId();
        String result = feedbackCmdMsg.getResult();
        long timestamp = feedbackCmdMsg.getTimeStamp();
        String cid = feedbackCmdMsg.getClientId();

        Log.d(TAG, "onReceiveCommandResult -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nactionid = " + actionid + "\nresult = " + result
                + "\ncid = " + cid + "\ntimestamp = " + timestamp);
    }

    private void processData(String data) {
        PushParser parser = null;
        parser = new Gson().fromJson(data, PushParser.class);

        if (parser == null) return;
//        String userToken = (String) SPUtils.get(ConstantValue.SP_USER_TOKEN, "");
        if (!parser.getPushType().equals(COMMON)) {
//            parser.setLoginToken(userToken);
            UserInfo user = BaseApplication.getInstance().getUserInfo();
            if (TextUtils.isEmpty(parser.PushValue.getUserId()) && user != null) {
                parser.setUserId(user.IdentityFlag + "_" + user.Id);
            } else {
                parser.setUserId(parser.PushValue.getUserId());
            }
            long id = DBManager.getInstance().getPushValueDao().insert(parser.PushValue);
            parser.setValueId(id);
        }
        parser.setIsRead(false);
        long dbId = DBManager.getInstance().getPushDao().insert(parser);
        Intent goIntent = null;
        switch (parser.getPushType()) {
            case COMMON:
                goIntent = new Intent(context, MyMessageActivity.class);
                initStartActivityNotification(parser, goIntent, 0);
                break;
            case USER_CANCEL_ORDER://用户取消订单
            case CONFIRM_OK://确认无误
            case APPLY_REFUND://申请退款
            case RESERVATION_SERVICE:
                int position = -1;
                if (parser.getPushType().equals(USER_CANCEL_ORDER)) {//取消订单
                    position = 4;//已完成
                } else if (parser.getPushType().equals(CONFIRM_OK)) {//确认无误
                    position = 4;//已完成
                } else if (parser.getPushType().equals(APPLY_REFUND)) {//申请退款
                    position = 3;//工作中
                } else if (parser.getPushType().equals(RESERVATION_SERVICE)) {//来订单
                    position = 1;//抢单
                    post(new Notice(ConstantValue.MSG_TYPE_UPDATE_ORDER, 0));
                } else {
                    post(new Notice(ConstantValue.MSG_TYPE_UPDATE_ORDER, 3));
                }
                goIntent = new Intent(context, OrdersActivity.class);
                goIntent.putExtra(ConstantValue.TYPE, OrdersActivity.ORDER_Service);
                goIntent.putExtra(ConstantValue.POSITION, position);
                initStartActivityNotification(parser, goIntent, 0);
                break;
            case CONFIRM_OK_SHOP_ORDER://确认收货
            case CANCEL_SHOP_ORDER://取消订单
            case APPLY_SHOP_REFUND://申请退款
                post(new Notice(ConstantValue.MSG_TYPE_UPDATE_ORDER));
                goIntent = new Intent(context, GoodsOrdersDetailActivity.class);
                goIntent.putExtra(ConstantValue.ORDER_NO, parser.getPushValue().getOrderNo());
                initStartActivityNotification(parser, goIntent, 0);
                break;
            case SHOP_ORDER://商品来订单了
                post(new Notice(ConstantValue.MSG_TYPE_UPDATE_ORDER, 0));
                goIntent = new Intent(context, OrdersActivity.class);
                goIntent.putExtra(ConstantValue.TYPE, OrdersActivity.ORDER_GOODS);
                goIntent.putExtra(ConstantValue.POSITION, 1);
                initStartActivityNotification(parser, goIntent, 0);
                break;
        }
    }

    private void post(Notice notice) {
        EventBus.getDefault().post(notice);
    }


    private void initStartActivityNotification(PushParser parser, Intent goIntent, int notifyFlag) {
        PendingIntent pendingIntent = null;
        int requestCode = (int) SystemClock.uptimeMillis();
        if (MainActivity.getInstance() == null) {
//            //app未运行
            Intent splashIntent = new Intent(context, LoginActivity.class);
//            splashIntent.putExtra(ConstantValue.EXTRA_INTENT, goIntent);
            pendingIntent = PendingIntent.getActivity(context, requestCode, splashIntent, 0);
        } else {
            //正在运行app 直接跳到指定界面
            pendingIntent = PendingIntent.getActivity(context, requestCode, goIntent, 0);
        }
        notification(parser, pendingIntent, notifyFlag);
    }

    private void notification(PushParser parser, PendingIntent intent, int notifyFlag) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(parser.getPushTitle())
                .setContentTitle(parser.getPushTitle())
                .setContentText(parser.getPushContent())
                .setAutoCancel(true)
//                .setFullScreenIntent(intent,true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        if (intent != null) {
            builder.setContentIntent(intent);
        }
        if (notifyFlag == 0) {
            notifyFlag = (int) SystemClock.uptimeMillis();
        }
        manager.notify(notifyFlag, builder.build());
    }

}
