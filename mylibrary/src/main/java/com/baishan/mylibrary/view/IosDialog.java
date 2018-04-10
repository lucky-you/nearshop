package com.baishan.mylibrary.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.baishan.mylibrary.R;

/**
 * 仿ios的dialog对话框
 */
public class IosDialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView txt_title;
    private TextView txt_msg;
    private EditText edittxt_result;
    private LinearLayout dialog_Group;
    private ImageView dialog_marBottom;
    private Button btn_neg;
    private Button btn_pos;
    private ImageView img_line;
    private Display display;
    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showEditText = false;
    private boolean showLayout = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;

    public IosDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public IosDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.toast_view_alertdialog, null);

        // 获取自定义Dialog布局中的控件
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setVisibility(View.GONE);
        txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        txt_msg.setVisibility(View.GONE);
        edittxt_result = (EditText) view.findViewById(R.id.edittxt_result);
        edittxt_result.setVisibility(View.GONE);
        dialog_Group = (LinearLayout) view.findViewById(R.id.dialog_Group);
        dialog_Group.setVisibility(View.GONE);
        dialog_marBottom = (ImageView) view.findViewById(R.id.dialog_marBottom);
        btn_neg = (Button) view.findViewById(R.id.btn_neg);
        btn_neg.setVisibility(View.GONE);
        btn_pos = (Button) view.findViewById(R.id.btn_pos);
        btn_pos.setVisibility(View.GONE);
        img_line = (ImageView) view.findViewById(R.id.img_line);
        img_line.setVisibility(View.GONE);

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LayoutParams.WRAP_CONTENT));

        return this;
    }


    public IosDialog setTitle(String title) {
        showTitle = true;
        if ("".equals(title)) {
            txt_title.setText("标题");
        } else {
            txt_title.setText(title);
        }
        return this;
    }

    public IosDialog setEditHint(String hint) {
        showEditText = true;
        edittxt_result.setHint(hint);
        return this;
    }

    public IosDialog setEditHeight(int height) {
        showEditText = true;
        edittxt_result.setHeight(height);
        return this;
    }

    public IosDialog setEditMinLine(int minLine) {
        showEditText = true;
        edittxt_result.setMinLines(minLine);
        return this;
    }

    public IosDialog setEditMaxLine(int maxLine) {
        showEditText = true;
        edittxt_result.setMaxLines(maxLine);
        return this;
    }

    public IosDialog setEditGravity(int gravity) {
        showEditText = true;
        edittxt_result.setGravity(gravity);
        return this;
    }

    public IosDialog setEditTextSize(int spSize) {
        showEditText = true;
        edittxt_result.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize);
        return this;
    }

    public IosDialog setEditText(String msg) {
        showEditText = true;
        edittxt_result.setText(msg);
        edittxt_result.setSelection(msg.length());
        return this;
    }

    public IosDialog setEditInputType(int type) {
        showEditText = true;
        edittxt_result.setInputType(type);
        return this;
    }

    public IosDialog setEditSelection(int index) {
        showEditText = true;
        edittxt_result.setSelection(index);
        return this;
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

    public String getResult() {
        return edittxt_result.getText().toString();
    }

    public EditText getEditText() {
        return edittxt_result;
    }

    public IosDialog setMsg(String msg) {
        showMsg = true;
        if ("".equals(msg)) {
            txt_msg.setText("内容");
        } else {
            txt_msg.setText(msg);
        }
        return this;
    }

    public IosDialog setMsgTextColor(int color) {
        txt_msg.setTextColor(color);
        return this;
    }

    public IosDialog setMsgOnClickListener(OnClickListener listener) {
        txt_msg.setOnClickListener(listener);
        return this;
    }

    public IosDialog setView(View view) {
        showLayout = true;
        if (view == null) {
            showLayout = false;
        } else
            dialog_Group.addView(view,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        return this;
    }

    public IosDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public IosDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }


    public IosDialog setPositiveButton(String text,
                                       final OnClickListener listener) {
        showPosBtn = true;
        if (TextUtils.isEmpty(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dismiss();
            }
        });
        return this;
    }

    public IosDialog setPositiveButtonColor(int color) {
        showPosBtn = true;
        btn_pos.setTextColor(color);
        return this;
    }

    public IosDialog setNegativeButton(String text,
                                       final OnClickListener listener) {
        showNegBtn = true;
        if (TextUtils.isEmpty(text)) {
            btn_neg.setText("取消");
        } else {
            btn_neg.setText(text);
        }
        btn_neg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    private void setLayout() {
//        if (!showTitle && !showMsg) {
//            txt_title.setText("提示");
//            txt_title.setVisibility(View.VISIBLE);
//        }

        if (showTitle) {
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showEditText) {
            edittxt_result.setVisibility(View.VISIBLE);
        }

        if (showMsg) {
            txt_msg.setVisibility(View.VISIBLE);
        }

        if (showLayout) {
            dialog_Group.setVisibility(View.VISIBLE);
            dialog_marBottom.setVisibility(View.GONE);
        }

        if (!showPosBtn && !showNegBtn) {
            btn_pos.setText("确定");
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
            btn_pos.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        if (showPosBtn && showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
            img_line.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && !showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }

        if (!showPosBtn && showNegBtn) {
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }
    }

    public void show() {
        setLayout();
        dialog.show();
    }
}
