package com.baishan.mylibrary.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.baishan.mylibrary.LibApplication;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/7/14 0014.
 */
public class StringUtils {
    /**
     * 把集合里面的字符串用空格连起来
     * @param list
     * @return
     */
    public static String getMiddleSpaceString(List<String> list)
    {
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            sb.append(str+" ");
        }
        return sb.toString();
    }

    public static SpannableString getColorText(String text, int color) {
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new ForegroundColorSpan(color), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
    public static SpannableString getColorSizeText(String text, int color,int sp) {
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new ForegroundColorSpan(color), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(CommonUtil.sp2px(LibApplication.getInstance(),sp)), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
    public static SpannableString getSizeText(Context context,String text, int sp) {
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new AbsoluteSizeSpan(CommonUtil.sp2px(context,sp)), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static SpannableString getClickText(TextView tv, String text, int color, OnClickListener listener) {
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new MyClickableSpan(listener, color), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }


    public static SpannableString getBigNumber(String text) {
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#b584ff")), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(20, true), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }


    /**
     * 关键字高亮显示
     *
     * @param target
     *            需要高亮的关键字
     * @param text
     *            需要显示的文字
     * @return spannable 处理完后的结果，记得不要toString()，否则没有效果
     */
    public static SpannableStringBuilder highlight(String text, String target,int color) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span;
        Pattern p = Pattern.compile(target);
        Matcher m = p.matcher(text);
        while (m.find()) {
            span = new ForegroundColorSpan(color);
            spannable.setSpan(span, m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    private static class MyClickableSpan extends ClickableSpan {

        private OnClickListener listener;
        private int color;

        public MyClickableSpan(OnClickListener listener, int color) {
            this.listener = listener;
            this.color = color;
        }

        @Override
        public void onClick(View widget) {
            listener.onClick();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(color);
            ds.setUnderlineText(false);
        }
    }

    public interface OnClickListener {
        void onClick();
    }

}
