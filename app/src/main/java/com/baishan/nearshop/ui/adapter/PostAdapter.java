package com.baishan.nearshop.ui.adapter;

import android.text.SpannableString;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baishan.mylibrary.utils.CommonUtil;
import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.mylibrary.utils.StringUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.model.Image;
import com.baishan.nearshop.model.PostInfo;
import com.baishan.nearshop.utils.DateUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/3/25.
 */
public class PostAdapter extends BaseQuickAdapter<PostInfo> {

    public PostAdapter(List<PostInfo> data) {
        super(R.layout.item_post, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, PostInfo data) {
        ImageLoaderUtils.displayImage(data.UserPhoto, holder.getView(R.id.iv_avator));
        holder.setText(R.id.tv_name, data.UserName);
        holder.setText(R.id.tv_post_title, data.ForumTitle);
//        holder.tv_name.setText(data.getUserName());
//        VipUtils.showVip(data.IsVip,holder.ivVip);
//        holder.tv_post_title.setText(data.getForumTitle());
//        holder.tvStateRec.setVisibility(data.getIsRecommend() == 1 ? View.VISIBLE : View.GONE);
//        holder.tvStateEss.setVisibility(data.getIsEssence() == 1 ? View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(data.ForumContent)) {
            holder.setVisible(R.id.tv_post_des, true);
            holder.setText(R.id.tv_post_des, data.ForumContent);
        } else {
            holder.setVisible(R.id.tv_post_des, false);
        }



        if (data.HasPic == 1) {
            holder.setVisible(R.id.llPic,true);
            ImageView image1 = holder.getView(R.id.iv_image1);
            ImageView image2 = holder.getView(R.id.iv_image2);
            ImageView image3 = holder.getView(R.id.iv_image3);

            image1.setVisibility(View.INVISIBLE);
            image2.setVisibility(View.INVISIBLE);
            image3.setVisibility(View.INVISIBLE);
            holder.setVisible(R.id.tv_image_count, false);
            //有图片
            List<Image> images = data.Images;
            switch (images.size()) {
                case 1:
                    image1.setVisibility(View.VISIBLE);
                    ImageLoaderUtils.displayImage(images.get(0).thumbImage, image1);
                    break;
                case 2:
                    image1.setVisibility(View.VISIBLE);
                    image2.setVisibility(View.VISIBLE);
                    ImageLoaderUtils.displayImage(images.get(0).thumbImage, image1);
                    ImageLoaderUtils.displayImage(images.get(1).thumbImage, image2);
                    break;
                case 3:
                    image1.setVisibility(View.VISIBLE);
                    image2.setVisibility(View.VISIBLE);
                    image3.setVisibility(View.VISIBLE);
                    ImageLoaderUtils.displayImage(images.get(0).thumbImage, image1);
                    ImageLoaderUtils.displayImage(images.get(1).thumbImage, image2);
                    ImageLoaderUtils.displayImage(images.get(2).thumbImage, image3);
                    break;
                default:
                    holder.setVisible(R.id.iv_image1, true)
                            .setVisible(R.id.iv_image2, true)
                            .setVisible(R.id.iv_image3, true)
                            .setVisible(R.id.tv_image_count, true)
                            .setText(R.id.tv_image_count, "共" + images.size() + "张");
                    ImageLoaderUtils.displayImage(images.get(0).thumbImage, image1);
                    ImageLoaderUtils.displayImage(images.get(1).thumbImage, image2);
                    ImageLoaderUtils.displayImage(images.get(2).thumbImage, image3);
                    break;
            }
        }else {
            holder.setVisible(R.id.llPic,false);
        }
        holder.setText(R.id.tv_time, DateUtils.getShortTime(data.CreateTime))
                .setText(R.id.tv_comment, data.ReturnCount + "")
                .setImageResource(R.id.iv_sex,data.UserSex== 1 ? R.drawable.ic_man : R.drawable.ic_woman)
                .setOnClickListener(R.id.iv_avator, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });


        LinearLayout ll_new_comment = holder.getView(R.id.ll_new_comment);
        if (data.ReturnList != null && data.ReturnList.size() > 0) {
            ll_new_comment.setVisibility(View.VISIBLE);
            ll_new_comment.removeAllViews();
            for (int i = 0; i < data.ReturnList.size(); i++) {
                PostInfo post = data.ReturnList.get(i);
                TextView textView = new TextView(mContext);
                textView.setMaxLines(1);
                textView.setEllipsize(TextUtils.TruncateAt.END);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, CommonUtil.dip2px(mContext, 5), 0, 0);
                textView.setLayoutParams(params);
//                SpannableString ss = new SpannableString(post.getUserName()+":"+post.getForumContent());
//                PostClickableSpan clickableSpan = new PostClickableSpan(mContext, post.getUserId()+"");
//                ss.setSpan(clickableSpan, 0, post.getUserName().length(),
//                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString userNameText = StringUtils.getClickText(textView, post.UserName, mContext.getResources().getColor(R.color.btn_yellow), () -> {
//                    Intent intent = new Intent(mContext, ForumHomepageActivity.class);
//                    intent.putExtra(ConstantValue.USER_TOKEN, post.getUserToken());
//                    mContext.startActivity(intent);
                });

//                SpannableString content = StringUtils.getFormatContent(mContext, textView, post.getForumContent());
                textView.append(userNameText);
                textView.append(" : ");
                textView.append(post.ForumContent);
                ll_new_comment.addView(textView);
            }
        } else {
            ll_new_comment.setVisibility(View.GONE);
        }
//
//        if (type == TYPE_USER_HOME) {
//            //用户主页的发表
//            holder.review_status.setVisibility(View.GONE);
//            holder.tv_source.setVisibility(View.VISIBLE);
//            holder.tv_source.setText(data.getCategoryTitle());
//        } else if (type == TYPE_USER_POST) {
//            //我的发帖
//            holder.tv_source.setVisibility(View.VISIBLE);
//            holder.review_status.setVisibility(View.VISIBLE);
//            holder.tv_source.setText(data.getCategoryTitle());
//            if (data.getIsActive() == 0) {
//                //未通过审核
//                holder.review_status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_audit_fail, 0, 0, 0);
//                holder.review_status.setText("未通过审核");
//            } else if (data.getIsActive() == 1) {
//                //已通过审核
//                holder.review_status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_audit_success, 0, 0, 0);
//                holder.review_status.setText("审核通过");
//            } else if (data.getIsActive() == 2) {
//                //审核中
//                holder.review_status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_audit_in, 0, 0, 0);
//                holder.review_status.setText("审核中");
//            }
//        } else if (type == TYPE_POST_LIST || type == TYPE_NEAR_PEOPLE) {
//            holder.iv_sex.setImageResource(data.getUserSex() == 1 ? R.drawable.ic_man : R.drawable.ic_woman);
//            //帖子列表
//            holder.review_status.setVisibility(View.GONE);
//            holder.tv_source.setVisibility(View.GONE);
//            holder.iv_avator.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, ForumHomepageActivity.class);
//                    intent.putExtra(ConstantValue.USER_TOKEN, data.getUserToken());
//                    mContext.startActivity(intent);
//                }
//            });
//            if (type == TYPE_NEAR_PEOPLE) {
//                holder.tvCity.setVisibility(View.VISIBLE);
//                holder.tvCity.setText(data.getLocation());
//            }
//        } else if (type == TYPE_TRAIL_HALL) {
//            holder.tv_post_des.setVisibility(View.GONE);
//        }
    }


//    static class PostClickableSpan extends ClickableSpan {
//
//        private final String mUserId;
//        private Context context;
//
//        public PostClickableSpan(Context context, String userId) {
//            this.context = context;
//            this.mUserId = userId;
//        }
//
//
//        @Override
//        public void onClick(View widget) {
//            Intent intent = new Intent(context, ForumHomepageActivity.class);
//            intent.putExtra(ConstantValue.USER_TOKEN, mUserId);
//            context.startActivity(intent);
//        }
//
//        @Override
//        public void updateDrawState(TextPaint ds) {
//            ds.setColor(context.getResources().getColor(R.color.bg_yellow_4));
//            ds.setUnderlineText(false);
////            ds.bgColor = mIsPressed ? context.getResources().getColor(R.color.bg_grey) : context.getResources().getColor(R.color.trans);
//        }
//    }

}
