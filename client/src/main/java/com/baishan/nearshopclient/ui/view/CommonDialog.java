package com.baishan.nearshopclient.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baishan.nearshopclient.R;


/**
 * Created by Administrator on 2016/11/1 0001.
 */
public class CommonDialog extends Dialog {

    private final FrameLayout flCus;
    private TextView tvFinish;
    private Button btnCommit;
    private TextView tvTitle;
    private final Window win;

    public CommonDialog(Context context) {
        super(context);
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_common, null);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvFinish = (TextView) view.findViewById(R.id.tvFinish);
        btnCommit = (Button) view.findViewById(R.id.btnCommit);
        flCus = (FrameLayout) view.findViewById(R.id.flContent);
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
        win = getWindow();
        win.setWindowAnimations(android.R.style.Animation_InputMethod);
        setWidth((int) (display.getWidth() * 0.95));

    }

    public CommonDialog setWidth(int width) {
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = width;
        win.setAttributes(lp);
        return this;
    }


    public CommonDialog setCustomView(View view) {
        flCus.setVisibility(View.VISIBLE);
        flCus.addView(view);
        return this;
    }


    public CommonDialog setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }


    public CommonDialog setButton(String text,
                                  final View.OnClickListener listener) {
        btnCommit.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(text)) {
            btnCommit.setText("确定");
        } else {
            btnCommit.setText(text);
        }
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dismiss();
            }
        });
        return this;
    }


}
