package com.baishan.mylibrary.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.baishan.mylibrary.LibApplication;

/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class PopupUtils {
    /**
     * 生成pop
     *
     * @param view
     * @return
     */
    public static PopupWindow generatePop(View view) {
        PopupWindow popupWindow = new PopupWindow();
        popupWindow.setContentView(view);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(LibApplication.getInstance().getResources().getDisplayMetrics().widthPixels);
        popupWindow.setFocusable(true);// 加上这个popupwindow中的ListView才可以接收点击事件

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color
                .parseColor("#000000")));// 设置背景图片，不能在布局中设置，要通过代码来设置
        popupWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
        return popupWindow;
    }
}
