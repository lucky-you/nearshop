package com.baishan.mylibrary.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.baishan.mylibrary.R;

/**
 * Created by RayYeung on 2016/8/17.
 */
public class LoadingDialog {


    public static Dialog show(Context context, String text) {
        Dialog dialog = new Dialog(context, R.style.LoadingDialog);
        View view = View.inflate(context, R.layout.view_dialog, null);
        TextView tvInfo = (TextView) view.findViewById(R.id.tvInfo);
        if (!TextUtils.isEmpty(text)) {
            tvInfo.setText(text);
        }
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    public static Dialog show(Context context) {
        return show(context, null);
    }


}
