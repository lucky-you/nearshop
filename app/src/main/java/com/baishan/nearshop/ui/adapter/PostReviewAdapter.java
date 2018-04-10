package com.baishan.nearshop.ui.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.baishan.mylibrary.utils.StringUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.model.PostReview;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


/**
 * Created by Administrator on 2016/4/27.
 */
public class PostReviewAdapter extends BaseQuickAdapter<PostReview> {

    public int type;

    private int commentUserId;

    public PostReviewAdapter(List<PostReview> data) {
        this(data, 1);
    }

    public PostReviewAdapter(List<PostReview> data, int type) {
        super(R.layout.item_post_review, data);
        this.type = type;
    }


    public void setCommentUserId(int commentUserId) {
        this.commentUserId = commentUserId;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, PostReview postReview) {
        TextView tvReview = baseViewHolder.getView(R.id.tvReview);
        if (type == 1) {
            tvReview.setMaxLines(1);
            tvReview.setEllipsize(TextUtils.TruncateAt.END);
        }
        tvReview.setText(StringUtils.getColorText(postReview.UserName, mContext.getResources().getColor(R.color.font_black)));
        int color = mContext.getResources().getColor(R.color.yellow_dark);
        if (commentUserId != postReview.ReplyUserId) {
            tvReview.append(StringUtils.getColorText("回复@" + postReview.ReplyUserName, color));
        }
        tvReview.append(StringUtils.getColorText(" : ", color));
        tvReview.append(postReview.CommentContent);
    }
}
