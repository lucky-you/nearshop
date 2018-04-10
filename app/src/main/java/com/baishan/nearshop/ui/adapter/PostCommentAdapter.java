package com.baishan.nearshop.ui.adapter;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baishan.mylibrary.utils.CommonUtil;
import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.model.Image;
import com.baishan.nearshop.model.PostInfo;
import com.baishan.nearshop.ui.activity.PostCommentDetailActivity;
import com.baishan.nearshop.utils.DateUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/3/25.
 */
public class PostCommentAdapter extends BaseQuickAdapter<PostInfo> {


    public PostCommentAdapter(List<PostInfo> data) {
        super(R.layout.item_comment, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, PostInfo postInfo) {
//        MyViewHolder holder = (MyViewHolder) viewHolder;
        ImageLoaderUtils.displayImage(postInfo.UserPhoto, baseViewHolder.getView(R.id.ivAvator));
        baseViewHolder.setText(R.id.tvName, postInfo.UserName)
                .setImageResource(R.id.ivSex, postInfo.UserSex== 1 ? R.drawable.ic_man : R.drawable.ic_woman)
                .setText(R.id.tvTime, DateUtils.getShortTime(postInfo.CreateTime))
                .setText(R.id.tvFloor, baseViewHolder.getAdapterPosition() + "F");

        if (TextUtils.isEmpty(postInfo.ForumContent)) {
            baseViewHolder.setVisible(R.id.tvContent, false);
        } else {
            baseViewHolder.setVisible(R.id.tvContent, true)
                    .setText(R.id.tvContent, postInfo.ForumContent);
        }


//
//        holder.ivAvator.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, ForumHomepageActivity.class);
//                intent.putExtra(ConstantValue.USER_TOKEN, data.getUserToken());
//                mContext.startActivity(intent);
//            }
//        });
//
//


//        ArrayList<Image> images = data.getImages();
        ImageView pic = baseViewHolder.getView(R.id.pic);
        if (postInfo.Images != null && postInfo.Images.size() > 0) {
            baseViewHolder.setVisible(R.id.pic,true)
                    .setOnClickListener(R.id.pic, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
            Image image = postInfo.Images.get(0);
            int screenWidth  = mContext.getResources().getDisplayMetrics().widthPixels;
            int width = screenWidth - CommonUtil.dip2px(this.mContext, 61);
            int height = 0;
            if (image.OriginHeight == 0 || image.OriginWidth == 0) {
                height = width;
            } else {
                height = width * image.OriginHeight / image.OriginWidth;
            }
            ViewGroup.LayoutParams params = pic.getLayoutParams();
            params.width = width;
            params.height = height;
            pic.setLayoutParams(params);
            ImageLoaderUtils.displayImage(this.mContext, image.OriginImage, width, height, pic);
        }else {
            baseViewHolder.setVisible(R.id.pic,false);
        }
        RecyclerView rvComment =baseViewHolder.getView(R.id.rvComment);
        if (postInfo.Comments != null) {
             baseViewHolder.setVisible(R.id.llComment, true);
            PostReviewAdapter adapter = new PostReviewAdapter(postInfo.Comments);
            adapter.setCommentUserId(postInfo.UserId);
            rvComment.setAdapter(adapter);
            rvComment.setLayoutManager(new LinearLayoutManager(mContext));
            adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    Intent it = new Intent(mContext, PostCommentDetailActivity.class);
                    it.putExtra("comment", postInfo);
                    it.putExtra("review", postInfo.Comments.get(i));
                    it.putExtra("position", baseViewHolder.getAdapterPosition());
                    mContext.startActivity(it);
                }
            });
//            int counts = postInfo.Comments.get(0).TotalCounts;
//            if (counts > 2) {
//                baseViewHolder.setVisible(R.id.tvMore,true)
//                        .setText(R.id.tvMore,"查看更多");
//                holder.tvMore.setText(String.format(this.mContext.getString(R.string.comment_more), counts - 2));
//            } else {
//                holder.tvMore.setVisibility(View.GONE);
//            }
        } else {
            rvComment.setVisibility(View.GONE);
        }
    }
}
