package com.baishan.nearshop.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.SPUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.db.DBManager;
import com.baishan.nearshop.model.PushParser;
import com.baishan.nearshop.ui.activity.ConfirmOrdersActivity;
import com.baishan.nearshop.ui.activity.MainActivity;
import com.baishan.nearshop.ui.activity.MyMessageActivity;
import com.baishan.nearshop.ui.activity.PostCommentDetailActivity;
import com.baishan.nearshop.ui.activity.PostDetailActivity;
import com.baishan.nearshop.ui.activity.ServiceOrderDetailActivity;
import com.baishan.nearshop.ui.activity.SplashActivity;
import com.baishan.nearshop.utils.ConstantValue;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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

    /****************服务****************/

    /**
     * 接单
     */
    public static final String ACCEPTORDER = "AcceptOrder";
    /**
     * 开始工作
     */
    public static final String STARTWORK = "StartWork";
    /**
     * 取消订单
     */
    public static final String CANCELORDER = "CancelOrder";
    /**
     * 完成工作
     */
    public static final String FINISHWORK = "FinishWork";
    /**
     * 拒绝退款
     */
    public static final String REFUSEREFUND = "RefuseRefund";
    /**
     * 同意退款
     */
    public static final String AGREEREFUND = "AgreeRefund";

    /****************服务****************/

    /****************商品****************/

    /**
     * 派送此单
     */
    public static final String ACCEPTSHOPORDER = "AcceptShopOrder";
    /**
     * 我已送达
     */
    public static final String ARRIVESHOPORDER = "ArriveShopOrder";
    /**
     * 拒绝退款
     */
    public static final String REFUSESHOPREFUND = "RefuseShopRefund";
    /**
     * 同意退款
     */
    public static final String AGREESHOPREFUND = "AgreeShopRefund";

   /****************商品****************/

   /****************社区****************/

    /**
     * 被回帖
     */
    public static final String BERETURNFORUM = "BeReturnForum";
    /**
     * 回复
     */
    public static final String REPLYCOMMENT = "ReplyComment";


   /****************社区****************/


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
        try {
            parser = new Gson().fromJson(data, PushParser.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        if (parser == null) return;
        EventBus.getDefault().post(new Notice(ConstantValue.MSG_TYPE_NEW_MESSAGE));
        Intent goIntent;
        int type = 0 ;
        switch (parser.getPushType()) {
            case COMMON:
                type =1 ;
                goIntent = new Intent(context, MyMessageActivity.class);
                initStartActivityNotification(parser, goIntent, 0);
                break;
            case ACCEPTORDER:
            case STARTWORK:
            case CANCELORDER:
            case FINISHWORK:
            case REFUSEREFUND:
            case AGREEREFUND:
                type =3 ;
                goIntent = new Intent(context, ServiceOrderDetailActivity.class);
                goIntent.putExtra(ConstantValue.ORDERNO, parser.PushValue.getOrderNo());
                initStartActivityNotification(parser, goIntent, parser.PushValue.getOrderNo().hashCode());
                break;
            case ACCEPTSHOPORDER:
            case ARRIVESHOPORDER:
            case REFUSESHOPREFUND:
            case AGREESHOPREFUND:
                type =2 ;
                goIntent = new Intent(context, ConfirmOrdersActivity.class);
                goIntent.putExtra(ConstantValue.ORDERNO, parser.PushValue.getOrderNo());
                goIntent.putExtra(ConstantValue.TYPE, ConfirmOrdersActivity.INTENT_ORDERS_LIST);
                initStartActivityNotification(parser, goIntent, parser.PushValue.getOrderNo().hashCode());
                break;
            case BERETURNFORUM:
                type = 4;
                goIntent = new Intent(context, PostDetailActivity.class);
                goIntent.putExtra(ConstantValue.POST_ID, parser.PushValue.getLinkValue());
                initStartActivityNotification(parser, goIntent, parser.PushValue.getLinkValue().hashCode());
                break;
            case REPLYCOMMENT:
                type = 4;
                goIntent = new Intent(context, PostCommentDetailActivity.class);
                goIntent.putExtra(ConstantValue.POST_ID, parser.PushValue.getLinkValue());
                initStartActivityNotification(parser, goIntent, parser.PushValue.getLinkValue().hashCode());
                break;
        }
        parser.setType(type);
        String userToken = (String) SPUtils.get(ConstantValue.SP_USER_TOKEN, "");
        if (!parser.getPushType().equals(COMMON)) {
            parser.setLoginToken(userToken);
            long id = DBManager.getInstance().getPushValueDao().insert(parser.PushValue);
            parser.setValueId(id);
        }
        DBManager.getInstance().getPushDao().insert(parser);
    }

    private void initStartActivityNotification(PushParser parser, Intent goIntent, int notifyFlag) {
        PendingIntent pendingIntent;
        int requestCode = (int) SystemClock.uptimeMillis();
        if (MainActivity.getInstance() == null) {
            //app未运行
            Intent splashIntent = new Intent(context, SplashActivity.class);
            splashIntent.putExtra(ConstantValue.EXTRA_INTENT, goIntent);
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
                .setSmallIcon(R.mipmap.ic_notification)
                .setTicker(parser.getPushTitle())
                .setContentTitle(parser.getPushTitle())
                .setContentText(parser.getPushContent())
                .setAutoCancel(true)
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
