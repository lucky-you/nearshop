package com.baishan.nearshop.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.CommonUtil;
import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.mylibrary.utils.InputMethodUtils;
import com.baishan.mylibrary.utils.PhotoUtils;
import com.baishan.mylibrary.view.ActionSheet;
import com.baishan.mylibrary.view.LoadingDialog;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.model.Image;
import com.baishan.nearshop.model.PostCommentParser;
import com.baishan.nearshop.model.PostInfo;
import com.baishan.nearshop.model.PostParam;
import com.baishan.nearshop.model.PostReview;
import com.baishan.nearshop.model.UserInfo;
import com.baishan.nearshop.presenter.PostDetailPresenter;
import com.baishan.nearshop.ui.adapter.PostCommentAdapter;
import com.baishan.nearshop.ui.view.EmotionView;
import com.baishan.nearshop.ui.view.MultiLinearLayout;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.utils.DateUtils;
import com.baishan.nearshop.utils.RecyclerViewUtils;
import com.baishan.nearshop.utils.SensitivewordFilter;
import com.baishan.nearshop.view.IPostDetailView;
import com.baishan.permissionlibrary.PermissionUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;


/**
 * Created by Administrator on 2016/3/25.
 */

/**
 * 帖子详情
 */
public class PostDetailActivity extends BaseMvpActivity<PostDetailPresenter> implements IPostDetailView {

    private RelativeLayout rlTitle;
    private ImageView ivLeft;
    private RecyclerView recyclerView;

    private LinearLayout llBottom;
    private TextView tvSend;
    private EmotionView emotionView;
    private EditText etComment;
    private ImageView ivRedDot;

    private View header;
    private ImageView ivAvator;
    private ImageView ivSex;
    private LinearLayout llName;
    private TextView tvName;
    private TextView tvTime;
    private TextView tvAddress;
    private TextView tvTitle;
    private TextView tvContent;
    private LinearLayout llImgs;
    private TextView tvRewardTip;
    private MultiLinearLayout llReward;
    private TextView tvCommentCount;


    private View footer;

    private PostCommentAdapter adapter;
    private Handler handler = new Handler();
    private String imagePath;
    private File imageFile;
    private PostInfo detail;
    private String postId;
    private int curPage = 1;
    private int curLandlordPage = 1;
    private int curReversePage = 1;


    private List<PostInfo> comments = new ArrayList<>();
    public int totalComments, totalLandlordComments;

    private boolean isLike;

    //所有评论和楼主
    private String returnlist = "true";
    private PopupWindow popup;
    private int index;
    private boolean moderator = false;
    private String[] items;
    private boolean intenFromPostList = false;
    private ArrayList<String> mSelectPath = new ArrayList<>();
    private Dialog mDialog;
    private TextView tvRewardCount;
    private ImageView ivReward;
    private List<UserInfo> mRewardUsers = new ArrayList<>();
    private TextView tvReport;
    private String[] reasons;
    //是否置顶了
//    private boolean isTop = false;
    //是否设为精华了
//    private boolean isEssence = false;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_post_detail);
//        getWindow().setEnterTransition(new Fade().setDuration(500));
    }

    @Override
    protected void bindViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        rlTitle = (RelativeLayout) findViewById(R.id.rl_title);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tvSend = (TextView) findViewById(R.id.tvSend);
        emotionView = (EmotionView) findViewById(R.id.emotionView);
        ivRedDot = (ImageView) findViewById(R.id.ivRedDot);

        header = View.inflate(this, R.layout.header_post_detail, null);
        etComment = (EditText) findViewById(R.id.etComment);
        ivAvator = (ImageView) header.findViewById(R.id.iv_avator);
        ivSex = (ImageView) header.findViewById(R.id.ivSex);
        ivReward = (ImageView) header.findViewById(R.id.ivReward);
        llName = (LinearLayout) header.findViewById(R.id.ll_name);
        tvName = (TextView) header.findViewById(R.id.tvName);
        tvTime = (TextView) header.findViewById(R.id.tvTime);
        tvAddress = (TextView) header.findViewById(R.id.tvAddress);
        tvTitle = (TextView) header.findViewById(R.id.tvTitle);
        tvContent = (TextView) header.findViewById(R.id.tvContent);
        llImgs = (LinearLayout) header.findViewById(R.id.llImgs);
        tvRewardCount = (TextView) header.findViewById(R.id.tvRewardCount);
        tvReport = (TextView) header.findViewById(R.id.tvReport);

        tvRewardTip = (TextView) header.findViewById(R.id.tvRewardTip);
        tvCommentCount = (TextView) header.findViewById(R.id.tvCommentCount);

        llReward = (MultiLinearLayout) header.findViewById(R.id.ll_reward);
//        footer = View.inflate(this, R.layout.layout_loading, null);

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initTitle("帖子详情");
        reasons = getResources().getStringArray(R.array.report_reason);
//        imageAdapter = new ImageAdapter(mContext);
//        llImgs.setAdapter(imageAdapter);
//        rewardAdapter = new RewardAdapter(mContext);
//        rewardList.setAdapter(rewardAdapter);
        //系统消息的数据库Id
//        Serializable serializableExtra = getIntent().getSerializableExtra(ConstantValue.SYSTEM_NOTIFICATION_ID);
//        if (serializableExtra != null && serializableExtra instanceof Long) {
//            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            mNotificationManager.cancel(ConstantValue.PUSH_FLAG_ORDER_DETAIL);
//            //标示已看过这个系统推送- -
//            Long dataId = (Long) serializableExtra;
//            SystemNotification data = SystemNotification.load(SystemNotification.class, dataId);
//            if (data != null) {
//                data.isLook = true;
//                data.save();
//            }
//
//        }

        postId = getIntent().getStringExtra(ConstantValue.POST_ID);
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.cancel(ConstantValue.PUSH_FLAG_POST_DETAIL);
//        if (!TextUtils.isEmpty(postId))
//            mNotificationManager.cancel(Integer.parseInt(postId));
//        Serializable postSerializable = getIntent().getSerializableExtra("post");
//        if (postSerializable instanceof TopPostParser) {//靓贴推荐跳转
//            post = (TopPostParser) postSerializable;
//            tvExpClub.setText(post.getCategoryTitle());
//            getPostDetail(post.getId() + "");
//        } else if (TextUtils.isEmpty(postId)) {//帖子列表跳转
//            intenFromPostList = true;
//            rlClub.setVisibility(View.GONE);
//            detail = (PostDetailParser) postSerializable;
//            setPostInfo(detail);
//            getLikeCount(detail.getId());
//            getCommentData(1);
//            getRewardData(detail.getId());
//        } else {//其他页面跳转
//            getPostDetail(postId);
//        }
        getPostDetail(postId + "");
        emotionView.setEditText(etComment);

        RecyclerViewUtils.pauseOrResume(recyclerView);

        adapter = new PostCommentAdapter(comments);
        adapter.openLoadMore(10, true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, -2);
        header.setLayoutParams(layoutParams);
        adapter.addHeaderView(header);
//        footer.setLayoutParams(layoutParams);

        EventBus.getDefault().register(this);
    }


//    /**
//     * 获取打赏数据
//     */
//    private void getRewardData(int forumid) {
//
//        HttpUtils.get(this, URL.GETBBSDATA, params, new ResponseCallback<List<UserInfo>>(new TypeToken<List<UserInfo>>() {
//        }.getType()) {
//            @Override
//            protected void onSuccess(List<UserInfo> response) {
//                if (response != null && response.size() > 0) {
//                    //users.addAll(response);
//                    //setReward(users);
//                    tvRewardTip.setVisibility(View.GONE);
//                    tvRewardCount.setVisibility(View.VISIBLE);
//                    tvRewardCount.setText(String.format(getString(R.string.reward_count), response.size()));
//                    rewardList.setVisibility(View.VISIBLE);
//                    rewardAdapter.addDatas(response);
//                }
//            }
//        });
//    }

    @Override
    public void getRewardDataSuccess(List<UserInfo> response) {
        if (response.size() > 0) {
            mRewardUsers = response;
            setReward(response);
        } else {
            //没有打赏
            tvRewardTip.setVisibility(View.VISIBLE);
            llReward.removeAllViews();
            llReward.setVisibility(View.GONE);
        }
    }

    @Override
    public void onReportSuccess() {
        showToast("举报成功");
    }

    /**
     * 设置打赏信息
     *
     * @param response
     */
    private void setReward(List<UserInfo> response) {
        llReward.removeAllViews();
        tvRewardTip.setVisibility(View.GONE);
        tvRewardCount.setVisibility(View.VISIBLE);
        tvRewardCount.setText(String.format(getString(R.string.reward_count), response.size()));
        llReward.setVisibility(View.VISIBLE);
        for (UserInfo user : response) {
            View view = generateReward(user);
            llReward.addView(view);
        }
//        View view = View.inflate(mContext, R.layout.item_reward, null);
//        ((ImageView) view.findViewById(R.id.ivAvator)).setImageResource(R.drawable.btn_reward_more);
//        llReward.addView(view);
//        view.setOnClickListener(v -> {
//            Intent it = new Intent(mContext, TopActivity.class);
//            it.putExtra("users", (Serializable) response);
//            startActivity(it);
//        });
    }

    private View generateReward(UserInfo user) {
        View view = View.inflate(mContext, R.layout.item_reward, null);
        ImageLoaderUtils.displayImage(user.UserPhoto, (ImageView) view.findViewById(R.id.ivAvator));
        view.setOnClickListener(v -> {
           /* Intent it = new Intent(mContext,TopActivity.class);
            it.putExtra("users", (Serializable) response);
            startActivity(it);*/
        });
        return view;
    }

    /**
     * 获取评论数据
     */
    private void getCommentData() {
        if (detail != null) {
            Map<String, String> params = new HashMap<>();
            params.put("returnlist", returnlist);
            params.put("forumid", detail.Id + "");
            params.put("pagenow", curPage + "");
            params.put("orderby", "1");
            mvpPresenter.getCommentData(params);
//            HttpUtils.get(this, URL.GETBBSDATA, params, new ResponseCallback<PostCommentParser>(PostCommentParser.class) {
//                @Override
//                protected void onSuccess(PostCommentParser response) {
//                    changeList(response);
//                }
//            });
        }
    }

    @Override
    protected void setListener() {
        etComment.setOnTouchListener((v2, event) -> {
            emotionView.hide();
            return false;
        });
//        findViewById(R.id.btn_more).setOnClickListener(this);
//        ivLike.setOnClickListener((v) -> {
//
//            if (detail != null && checkLogin()) {
//                if (isLike) {
//                    showToast("您已经赞过了喔~");
//                    return;
//                }
//                go2Like();
//            }
//
//        });
        findViewById(R.id.btnImg).setOnClickListener(v1 -> {
            InputMethodUtils.hideKeyboard(PostDetailActivity.this);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //有图片
                    if (ivRedDot.getVisibility() == View.VISIBLE) {
                        emotionView.showImage(null);
                    } else {
                        emotionView.showPicSelect();
                    }
                }
            }, 50);
        });
        findViewById(R.id.btnEmotion).setOnClickListener(v1 -> {
            InputMethodUtils.hideKeyboard(PostDetailActivity.this);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    emotionView.showEmotion();
                }
            }, 50);
        });
        ivReward.setOnClickListener(v -> {
            if (detail != null && checkLogin()) {
                if (detail.UserId == user.UserId) {
                    showToast("不能打赏自己喔~");
                    return;
                }
                Intent it = new Intent(mContext, RechargeActivity.class);
                it.putExtra(ConstantValue.TYPE, RechargeActivity.TYPE_REWARD);
                it.putExtra(ConstantValue.DATA, detail);
                startActivity(it);
            }
        });
        emotionView.setOnItemClickListener(new EmotionView.OnItemClickListener() {
            @Override
            public void selectPic() {
                selectImage();
            }

            @Override
            public void takePhotos() {
                PermissionUtils.getInstance().checkSinglePermission(mContext, Manifest.permission.CAMERA, new PermissionUtils.PerPermissionCallback() {
                    @Override
                    public void onDeny() {
                        showToast("申请拍照权限失败");
                    }

                    @Override
                    public void onGuarantee() {
                        PhotoUtils.pickImageFromCamera(PostDetailActivity.this);
                    }
                });
            }

            @Override
            public void deletePic() {
                ivRedDot.setVisibility(View.GONE);
                imagePath = null;
            }
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

//        adapter.setOnLoadMoreListerner(() -> {
//            if (returnlist.equals("true")) {
//                getCommentData(orderby == 1 ? ++curPage : ++curReversePage);
//            } else {
//                getCommentData(++curLandlordPage);
//            }
//        });
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent it = new Intent(mContext, PostCommentDetailActivity.class);
                it.putExtra("comment", comments.get(i));
                it.putExtra("position", i + 1);
                startActivity(it);
            }
        });

//        adapter.setOnItemClickListener((position, data) -> {
//            Intent it = new Intent(mContext, PostCommentDetailActivity.class);
//            it.putExtra("comment", (PostDetailParser) data);
//            it.putExtra("position", position + 1);
//            startActivity(it);
//        });
//        llImgs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent it = new Intent(mContext, ImageBrowserActivity.class);
//                it.putExtra("images", detail.getImages());
//                it.putExtra("position", position);
//                startActivity(it);
//            }
//        });
//        landlord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    returnlist = "isPublisher";
//                    if (landlordComments.size() == 0) {
//                        adapter.removeAll();
//                        getCommentData(curLandlordPage);
//                    } else {
//                        setPostCount(totalLandlordComments);
//                        adapter.removeAll();
//                        adapter.addDatas(landlordComments);
//                    }
//                } else {
//                    setPostCount(totalComments);
//                    returnlist = "true";
//                    adapter.removeAll();
//                    adapter.addDatas(comments);
//                }
//            }
//        });
//        rlClub.setOnClickListener(v -> {
//            if (detail != null) {
//                Intent it = new Intent(mContext, ForumListActivity.class);
//                it.putExtra(ConstantValue.CLUB_INFO, new ExperienceClubParser(detail.getCategoryId()));
//                startActivity(it);
//            }
//        });
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (emotionView.isVisible()) {
                    InputMethodUtils.hideKeyboard(PostDetailActivity.this);
                    emotionView.hide();
                }
                return false;
            }
        });

//        btnMore.setOnClickListener(v -> {
//            if (detail != null) {
//                showMoreWindow();
//            }
//        });
//        ivChat.setOnClickListener(v -> {
//            if (detail != null) {
//                if (checkLogin()) {
//                    if (user.UserToken.equals(detail.getUserToken())) {
//                        showToast("是你自己是你自己是你自己喔~");
//                    } else {
//                        UserDao.saveUser(new UserInfo(detail.getUserToken(), detail.getUserName(), detail.getUserPhoto(), detail.getIsAnchor()));
//                        RongIM.getInstance().startPrivateChat(mContext, detail.getUserToken(), detail.getUserName());
//                    }
//                }
//            }
//        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                curPage++;
                getCommentData();
            }
        });
        tvReport.setOnClickListener(v -> {
            if (checkLogin()) {
                if (detail.UserId == user.UserId) {
                    showToast("不能举报自己喔~");
                    return;
                }
                report();
            }
        });
    }

    /**
     * 举报
     */
    private void report() {
        ActionSheet.createBuilder(mContext, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles(reasons)

                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        Map<String, String> params = new HashMap<>();
                        params.put("Method", "ReportForum");
                        params.put("userId", user.UserId + "");
                        params.put("userToken", user.UserToken);
                        params.put("forumId", detail.UserId + "");
                        params.put("forumToken", detail.ForumToken);
                        params.put("message", reasons[index]);
                        mvpPresenter.report(params);
                    }
                }).show();
    }
//    private void showMoreWindow() {
//        setBackgroundAlpha(0.7f);
//        View content = View.inflate(mContext, R.layout.popup_forum_review_menu, null);
//        LinearLayout relMenuDescReview = (LinearLayout) content.findViewById(R.id.relMenuDescReview);
//        TextView tvMenuDescReview = (TextView) content.findViewById(R.id.tvMenuDescReview);
//        LinearLayout relMenuInform = (LinearLayout) content.findViewById(R.id.relMenuInform);
//        LinearLayout relMenuShare = (LinearLayout) content.findViewById(R.id.relMenuShare);
//        LinearLayout llDelete = (LinearLayout) content.findViewById(R.id.llDelete);
//        LinearLayout llTransform = (LinearLayout) content.findViewById(R.id.llTransform);
//        LinearLayout llTop = (LinearLayout) content.findViewById(R.id.llTop);
//        LinearLayout llRecommend = (LinearLayout) content.findViewById(R.id.llRecommend);
//        LinearLayout llEssence = (LinearLayout) content.findViewById(R.id.llEssence);
//        TextView tvTop = (TextView) content.findViewById(R.id.tvTop);
//        TextView tvRecommend = (TextView) content.findViewById(R.id.tvRecommend);
//        TextView tvEssence = (TextView) content.findViewById(R.id.tvEssence);
//        if (moderator) {
//            llDelete.setVisibility(View.VISIBLE);
//            llTransform.setVisibility(View.VISIBLE);
//            llTop.setVisibility(View.VISIBLE);
//            llEssence.setVisibility(View.VISIBLE);
//            tvTop.setText(isTop ? "取消置顶" : "置顶");
//            tvEssence.setText(isEssence ? "取消精华" : "精华");
//        }
//        if (orderby == 1) {
//            tvMenuDescReview.setText("倒序评论");
//        } else {
//            tvMenuDescReview.setText("恢复正序");
//        }
//        popup = new PopupWindow(content, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        popup.showAsDropDown(btnMore);
//        popup.setOnDismissListener(() -> setBackgroundAlpha(1f));
//        relMenuInform.setOnClickListener(v -> {
//            if (checkLogin()) {
//                if (detail.getUserId() == user.Id) {
//                    showToast("不能举报自己喔~");
//                    return;
//                }
//                report();
//            }
//        });
//        relMenuDescReview.setOnClickListener(v -> {
//            dismiss();
//            landlord.setChecked(false);
//            returnlist = "true";
//            if (orderby == 1) {
//                orderby = 2;
//                if (reverseComments.size() == 0) {
//                    adapter.removeAll();
//                    getCommentData(curReversePage);
//                } else {
//                    setPostCount(totalComments);
//                    adapter.removeAll();
//                    adapter.addDatas(reverseComments);
//                }
//            } else {
//                orderby = 1;
//                setPostCount(totalComments);
//                adapter.removeAll();
//                adapter.addDatas(comments);
//            }
//        });
//        relMenuShare.setOnClickListener(v -> {
//            dismiss();
//            share();
//        });
//        llDelete.setOnClickListener(v -> {
//            dismiss();
//            deletePost();
//        });
//        llTransform.setOnClickListener(v -> {
//            dismiss();
//            if (clubs.size() == 0) {
//                initAllClub();
//            } else {
//                transformPost();
//            }
//        });
//        llTop.setOnClickListener(v -> {
//            dismiss();
//            StickPost();
//        });
//        llRecommend.setOnClickListener(v -> {
//            dismiss();
//            RecommendPost();
//        });
//        llEssence.setOnClickListener(v -> {
//            dismiss();
//            EssencePost();
//        });
//    }


//
//    /**
//     * 关闭弹出窗口
//     */
//    private void dismiss() {
//        popup.dismiss();
//        setBackgroundAlpha(1f);
//    }


    /**
     * 发布评论
     */
    private void sendComment() {
        String comment = etComment.getText().toString().trim();
        if (TextUtils.isEmpty(comment) && imagePath == null) {
            showToast("评论内容不能为空");
            return;
        }
        if (!TextUtils.isEmpty(comment) && comment.length() > ConstantValue.POST_LENGTH) {
            showToast("内容长度最多" + ConstantValue.POST_LENGTH + "个字");
            return;
        }
        if (SensitivewordFilter.getInstance(mContext).check(comment)) return;
        if (checkLogin()) {
            if (detail == null) {
                showToast("帖子正在加载中");
                return;
            }

            PostParam param = new PostParam();
            param.title = detail.ForumTitle;
            param.content = comment;
            param.pics = mSelectPath;
            param.userId = user.UserId;
            param.userToken = user.UserToken;
            param.categoryId = detail.CategoryId;
            param.isPublisher = user.UserId == detail.UserId ? 1 : 0;
            param.location = currentArea.AreaName;
            param.cityCode = currentArea.CityCode;
            param.areaId = currentArea.AreaId;
            param.parentId = detail.Id;
            param.parentToken = detail.ForumToken;
            mvpPresenter.send(param);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantValue.REQUEST_IMAGE && resultCode == RESULT_OK) {
            ArrayList<String> list = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            imagePath = list.get(0);
            mSelectPath.clear();
            mSelectPath.add(imagePath);
            emotionView.showImage(list.get(0));
            ivRedDot.setVisibility(View.VISIBLE);
        } else if (requestCode == PhotoUtils.REQUEST_CODE_FROM_CAMERA) {
            if (resultCode == RESULT_CANCELED) {
                PhotoUtils.deleteImageUri(this, PhotoUtils.imageUriFromCamera);
            } else {
                imagePath = PhotoUtils.getImageAbsolutePath(mContext, PhotoUtils.imageUriFromCamera);
                mSelectPath.clear();
                mSelectPath.add(imagePath);
                emotionView.showImage(imagePath);
                ivRedDot.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 打开图片选择页面
     */
    private void selectImage() {
        Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        startActivityForResult(intent, ConstantValue.REQUEST_IMAGE);
    }

    @Override
    public void onBackPressed() {
        if (emotionView.isVisible()) {
            emotionView.hide();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 获取帖子详情
     */
    public void getPostDetail(String pid) {
        Map<String, String> params = new HashMap<>();
        params.put("forumid", pid);
        params.put("userid", (user == null ? -1 : user.UserId) + "");

        mvpPresenter.getPostDetail(params);


//        HttpUtils.get(this, ApiService.URL_GET_BBS_DATA, params, new ResponseCallback<List<PostDetailParser>>(new TypeToken<List<PostDetailParser>>() {
//        }.getType()) {
//
//            @Override
//            protected void onSuccess(List<PostDetailParser> response) {
//                if (response.size() == 0) {
//                    showToast("帖子不存在");
//                    finish();
//                } else {
//                    detail = response.get(0);
//                    setPostInfo(detail);
//                    getCommentData(1);
//                    getRewardData(detail.getId());
//                }
//            }
//        });
    }


    private void setPostInfo(PostInfo response) {
//        isTop = response.getIsTop() == 1;
//        isEssence = response.getIsEssence() == 1;
        ivSex.setImageResource(response.UserSex == 1 ? R.drawable.ic_man : R.drawable.ic_woman);
        ImageLoader.getInstance().displayImage(response.UserPhoto, ivAvator);
        ivAvator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, ForumHomepageActivity.class);
//                intent.putExtra(ConstantValue.USER_TOKEN, response.getUserToken());
//                mContext.startActivity(intent);
            }
        });
        tvName.setText(response.UserName);
        tvTime.setText(DateUtils.getFormatTime(response.CreateTime));
        tvAddress.setText(response.Location);
        tvTitle.setText(response.ForumTitle);
        tvContent.setText(response.ForumContent);
        if (response.Images != null) {
//            imageAdapter.addDatas(images);
            generateImages(response.Images);
        }
    }

    private void generateImages(List<Image> images) {
        llImgs.removeAllViews();
        for (int i = 0; i < images.size(); i++) {
            Image image = images.get(i);
            ImageView imageView = new ImageView(this);
            int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
            int width = screenWidth - CommonUtil.dip2px(this.mContext, 20);
            int height = 0;
            if (image.OriginHeight == 0 || image.OriginWidth == 0) {
                height = width;
            } else {
                height = width * image.OriginHeight / image.OriginWidth;
            }
            LinearLayout.MarginLayoutParams params = new LinearLayout.MarginLayoutParams(width, height);
            params.topMargin = CommonUtil.dip2px(mContext, 10);
            imageView.setLayoutParams(params);
            ImageLoaderUtils.displayImage(this.mContext, image.OriginImage, width, height, imageView);
            llImgs.addView(imageView);
        }

    }


    private void setPostCount(int count) {
        tvCommentCount.setText("全部回帖(" + count + ")");
    }


    @Subscribe
    public void onEvent(Notice msg) {
        if (msg.type == ConstantValue.MSG_TYPE_UPDATE_REWARD) {
            llReward.setVisibility(View.VISIBLE);
            tvRewardCount.setVisibility(View.VISIBLE);
            UserInfo info = new UserInfo();
            info.UserId = user.UserId;
            info.NickName = user.NickName;
            info.UserPhoto = user.UserPhoto;
            info.UserSex = user.UserSex;
            info.UserAge = user.UserAge;
//            info.Coins = (int) msg.content;
            List<UserInfo> users = mRewardUsers;
            if (!users.contains(info)) {
                users.add(info);
                View view = generateReward(info);
                llReward.addView(view);
                tvRewardCount.setText(String.format(getString(R.string.reward_count), users.size()));
            } else {
//                users.get(users.indexOf(info)).Coins += (int) msg.content;
            }
        } else if (msg.type == ConstantValue.MSG_TYPE_MESSAGE_POST_REPLY) {
            PostReview review = (PostReview) msg.content;
            for (PostInfo parser : comments) {
                if (parser.Id == review.commentId) {
                    if (parser.Comments == null) {
                        parser.Comments = new ArrayList<>();
                    }
                    parser.Comments.add(review);
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    @Override
    protected PostDetailPresenter createPresenter() {
        return new PostDetailPresenter(this);
    }

    @Override
    public void showLoading() {
        mDialog = LoadingDialog.show(this);
    }

    @Override
    public void hideLoading() {
        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
    }

    @Override
    public void getPostDetailSuccess(List<PostInfo> response) {
        if (response.size() <= 0) {
            showToast("帖子不存在");
            finish();
            return;
        }
        detail = response.get(0);
        setPostInfo(detail);
        curPage = 1;
        getCommentData();
        mvpPresenter.getRewardData(detail.Id);
    }

    @Override
    public void getCommentDataSuccess(PostCommentParser response) {
        if (curPage == 1) {
            adapter.getData().clear();
            adapter.notifyDataSetChanged();
            setPostCount(response.TotalCounts);

        }
        if (response != null && response.Table.size() > 0) {
            adapter.notifyDataChangedAfterLoadMore(response.Table, curPage < response.PageCount);
        }
    }

    @Override
    public void onSendCommentSuccess(String response) {
        showToast("发帖成功");
        getCommentData();
        showToast("发送成功");
        etComment.setText("");
        ivRedDot.setVisibility(View.GONE);
        emotionView.hide();
        imagePath = null;
        if (imageFile != null) {
            imageFile.delete();
        }
    }


}
