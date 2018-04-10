package com.baishan.nearshop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.CommonUtil;
import com.baishan.mylibrary.utils.DisplayUtils;
import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.mylibrary.utils.InputMethodUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.model.Image;
import com.baishan.nearshop.model.PostInfo;
import com.baishan.nearshop.model.PostReview;
import com.baishan.nearshop.presenter.CommentDetailPresenter;
import com.baishan.nearshop.ui.adapter.PostReviewAdapter;
import com.baishan.nearshop.ui.view.EmotionView;
import com.baishan.nearshop.ui.view.TitleBuilder;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.utils.DateUtils;
import com.baishan.nearshop.utils.SensitivewordFilter;
import com.baishan.nearshop.view.ICommentDetailView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostCommentDetailActivity extends BaseMvpActivity<CommentDetailPresenter> implements ICommentDetailView {

    private ScrollView scrollView;
    private ImageView ivAvator;
    private TextView tvName;
    private ImageView ivSex;
    private TextView tvFloor;
    private TextView tvTime;
    private TextView tvContent;
    private ImageView pic;
    private LinearLayout llComment;
    private RecyclerView rvComment;
    private TextView tvMore;

    private LinearLayout llBottom;
    private TextView tvSend;
    private EmotionView emotionView;
    private EditText etComment;
    private RelativeLayout rlPic;
    private ImageButton btnEmotion;
    private List<PostReview> mData = new ArrayList<>();

    private PostInfo comment;
    private int position;
    private PostReview buildReview = new PostReview();

    private PostReviewAdapter adapter;
    private Handler handler = new Handler();
    private TitleBuilder builder;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_post_comment_detail);
    }


    @Override
    protected void bindViews() {
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        ivAvator = (CircleImageView) findViewById(R.id.ivAvator);
        tvName = (TextView) findViewById(R.id.tvName);
        ivSex = (ImageView) findViewById(R.id.ivSex);
        tvFloor = (TextView) findViewById(R.id.tvFloor);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvContent = (TextView) findViewById(R.id.tvContent);
        pic = (ImageView) findViewById(R.id.pic);
        llComment = (LinearLayout) findViewById(R.id.llComment);
        rvComment = (RecyclerView) findViewById(R.id.rvComment);
        tvMore = (TextView) findViewById(R.id.tvMore);

        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        etComment = (EditText) findViewById(R.id.etComment);
        tvSend = (TextView) findViewById(R.id.tvSend);
        emotionView = (EmotionView) findViewById(R.id.emotionView);
        rlPic = (RelativeLayout) findViewById(R.id.rlPic);
        btnEmotion = (ImageButton) findViewById(R.id.btnEmotion);
        rlPic.setVisibility(View.GONE);
        emotionView.setEditText(etComment);
        tvFloor.setVisibility(View.GONE);

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.cancel(ConstantValue.PUSH_FLAG_POST_DETAIL);

        adapter = new PostReviewAdapter(mData, 2);
        initCommonRecyclerView(R.id.rvComment, adapter, null);

        position = getIntent().getIntExtra("position", 0);
//
        if (position == 0) {//其他地方跳转过来
            builder = initTitle("评论详情").setRightText("查看原帖");
            String commentId = getIntent().getStringExtra(ConstantValue.POST_ID);
            getCommentData(commentId);
        } else {
            initTitle(position + "F");
            comment = (PostInfo) getIntent().getSerializableExtra("comment");
            adapter.setCommentUserId(comment.UserId);
            buildReview.UserId = this.comment.UserId;
            buildReview.UserToken = this.comment.UserToken;
            buildReview.UserName = this.comment.UserName;
            Serializable serializable = getIntent().getSerializableExtra("review");
            if (serializable != null) {
                PostReview review = (PostReview) serializable;
                buildReview.UserId = review.UserId;
                buildReview.UserToken = review.UserToken;
                buildReview.UserName = review.UserName;
                etComment.setHint("回复@" + buildReview.UserName);
                InputMethodUtils.showKeyboard(this, etComment);
            }
            initComment();
            mvpPresenter.getReplyData(comment.Id + "");
        }

    }

    private void getCommentData(String commentId) {
        Map<String, String> params = new HashMap<>();
        params.put("forumid", commentId);
        params.put("userid", (user == null ? -1 : user.UserId) + "");

        mvpPresenter.getPostDetail(params);

    }


    /**
     * 初始化评论内容
     */
    private void initComment() {
        ImageLoaderUtils.displayImage(comment.UserPhoto, ivAvator);
        tvName.setText(comment.UserName);
        tvTime.setText(DateUtils.getShortTime(comment.CreateTime));
        if (TextUtils.isEmpty(comment.ForumContent)) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(comment.ForumContent);
        }
        List<Image> images = comment.Images;
        if (images != null && images.size() > 0) {
            pic.setVisibility(View.VISIBLE);
            Image image = images.get(0);
            int width = DisplayUtils.getScreenWidthPixels(mContext) - CommonUtil.dip2px(this.mContext, 61);
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
            ImageLoaderUtils.displayImage(mContext, image.OriginImage, width, height, pic);
        } else {
            pic.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setListener() {
        btnEmotion.setOnClickListener(v -> {
            InputMethodUtils.hideKeyboard(PostCommentDetailActivity.this);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    emotionView.show();
                }
            }, 50);
        });
        etComment.setOnTouchListener((v2, event) -> {
            emotionView.hide();
            return false;
        });
        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    tvSend.setTextColor(getResources().getColor(R.color.font_black));
                } else {
                    tvSend.setTextColor(getResources().getColor(R.color.font_grey_dark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvSend.setOnClickListener(v -> sendComment());
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                PostReview postReview = mData.get(i);
                buildReview.UserId = postReview.UserId;
                buildReview.UserToken = postReview.UserToken;
                buildReview.UserName = postReview.UserName;
                etComment.setHint("回复@" + postReview.UserName);
                InputMethodUtils.showKeyboard(PostCommentDetailActivity.this, etComment);
            }
        });

        scrollView.setOnTouchListener((v, event) -> {
            emotionView.hide();
            InputMethodUtils.hideKeyboard(PostCommentDetailActivity.this);
            return false;
        });

//        ivAvator.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (comment != null) {
//                    Intent intent = new Intent(mContext, ForumHomepageActivity.class);
//                    intent.putExtra(ConstantValue.USER_TOKEN, comment.getUserToken());
//                    mContext.startActivity(intent);
//                }
//            }
//        });
    }

    private void sendComment() {
        String content = etComment.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            showToast("回复内容不能为空");
            return;
        }
        if (!TextUtils.isEmpty(content) && content.length() > ConstantValue.POST_LENGTH) {
            showToast("内容长度最多" + ConstantValue.POST_LENGTH + "个字");
            return;
        }
        if (SensitivewordFilter.getInstance(mContext).check(content)) return;

        if (checkLogin()) {
            if (comment != null) {
                Map<String, String> params = new HashMap<>();
                params.put("Method", "CommentForum");
                params.put("forumId", comment.Id + "");
                params.put("forumToken", comment.ForumToken);
                params.put("userId", user.UserId + "");
                params.put("userToken", user.LoginToken);
                params.put("replyUserId", buildReview.UserId + "");
                params.put("replyUserToken", buildReview.UserToken);
                params.put("commentContent", content);
                int isPublisher = (user.UserId == comment.UserId ? 1 : 0);
                params.put("isPublisher", isPublisher + "");
                mvpPresenter.sendCommit(params, content);
                Logger.i(params.toString());

            } else {
                showToast("数据尚未加载出来，请稍候");
            }

        }
    }

    @Override
    public void onBackPressed() {
        if (emotionView.isVisible()) {
            emotionView.hide();
        } else {
            finish();
        }

    }

    @Override
    protected CommentDetailPresenter createPresenter() {
        return new CommentDetailPresenter(this);
    }

    @Override
    public void sendCommitSuccess(String content) {
        showToast("发送成功");
        etComment.setText("");
        emotionView.hide();
        //构建内容刷新
        PostReview review = new PostReview();
        review.commentId = comment.Id;
        review.ReplyUserId = buildReview.UserId;
        review.ReplyUserToken = buildReview.UserToken;
        review.ReplyUserName = buildReview.UserName;
        review.UserId = user.UserId;
        review.UserToken = user.UserToken;
        review.UserName = user.NickName;
        review.CommentContent = content;
        review.IsPublisher = user.UserId == comment.UserId ? 1 : 0;
        etComment.setHint("说点什么吧...");
        adapter.add(mData.size(), review);
        llComment.setVisibility(View.VISIBLE);
        buildReview.UserId = comment.UserId;
        buildReview.UserToken = comment.UserToken;
        buildReview.UserName = comment.UserName;
        EventBus.getDefault().post(new Notice(ConstantValue.MSG_TYPE_MESSAGE_POST_REPLY, review));
    }

    @Override
    public void getReplyDataSuccess(List<PostReview> response) {
        llComment.setVisibility(View.VISIBLE);
        adapter.addData(response);
    }

    @Override
    public void getPostDetailSuccess(List<PostInfo> response) {
        if (response.size() == 0) {
            showToast("帖子不存在");
            finish();
        } else {
            comment = response.get(0);
            adapter.setCommentUserId(comment.UserId);
            mvpPresenter.getReplyData(comment.Id + "");
            builder.setRightOnClickListener(v -> {
                Intent it = new Intent(mContext, PostDetailActivity.class);
                it.putExtra(ConstantValue.POST_ID, comment.ParentId + "");
                mContext.startActivity(it);
            });
            buildReview.UserId = comment.UserId;
            buildReview.UserToken = comment.UserToken;
            buildReview.UserName = comment.UserName;
            initComment();
        }
    }
}
