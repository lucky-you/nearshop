package com.baishan.nearshop.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.baishan.mylibrary.utils.InputMethodUtils;
import com.baishan.mylibrary.utils.PhotoUtils;
import com.baishan.mylibrary.view.ActionSheet;
import com.baishan.mylibrary.view.LoadingDialog;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.model.ForumCategory;
import com.baishan.nearshop.model.PostParam;
import com.baishan.nearshop.presenter.PostingPresenter;
import com.baishan.nearshop.ui.adapter.PostImageAdapter;
import com.baishan.nearshop.ui.view.EmotionView;
import com.baishan.nearshop.ui.view.VerticalItemDecoration;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.IPostingView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class PostingActivity extends BaseMvpActivity<PostingPresenter> implements IPostingView {


    @BindView(R.id.iv_left)
    ImageButton ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_titlebar)
    RelativeLayout rlTitlebar;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.iv_emotion)
    ImageView ivEmotion;
    @BindView(R.id.emotionView)
    EmotionView emotionView;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.picList)
    RecyclerView picList;
    @BindView(R.id.scrollView)
    ScrollView scrollView;


    //选择图片最大数目
    public static final int MAX_NUM = 9;
    public static final String ADD_FLAG = "add";

    //图片集合
    private ArrayList<String> mSelectPath = new ArrayList<>();
    private PostImageAdapter imageAdapter;
    private List<ForumCategory> categories;
    //private ForumCategory curCategory;
    private Dialog dialog;

    private int categoryId;

    @Override
    protected PostingPresenter createPresenter() {
        return new PostingPresenter(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_post);
        //getWindow().setEnterTransition(new Explode().setDuration(500));
    }

    @Override
    protected void bindViews() {
        ButterKnife.bind(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        categories = (List<ForumCategory>) getIntent().getSerializableExtra(ConstantValue.DATA_CATEGORY);
        //int position = getIntent().getIntExtra(ConstantValue.TABPOSITION,0);
        //curCategory = categories.get(position);
        //tvTitle.setText(curCategory.Title);
        String[] array = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            array[i] = categories.get(i).Title;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        emotionView.setEditText(etContent);
        imageAdapter = new PostImageAdapter(mSelectPath);
        picList.setLayoutManager(new GridLayoutManager(mContext, 3));
        picList.addItemDecoration(new VerticalItemDecoration(mContext, Color.TRANSPARENT, 8));
        picList.setAdapter(imageAdapter);
    }

    @Override
    protected void setListener() {
        ivLeft.setOnClickListener(v2 -> finish());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryId = categories.get(position).Id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        etContent.setOnTouchListener((v1, event) -> {
            //changeState(true);
            emotionView.hide();
            return false;
        });
        etTitle.setOnTouchListener((v1, event) -> {
            //changeState(false);
            emotionView.hide();
            return false;
        });
        ivCamera.setOnClickListener(v -> select(1));
        ivImage.setOnClickListener(v -> select(2));
        ivEmotion.setOnClickListener(v -> {
            if (emotionView.isVisible()) {
                InputMethodUtils.showKeyboard(this, etContent);
                emotionView.hide();
            } else {
                InputMethodUtils.hideKeyboard(this);
                new Handler().postDelayed(() -> emotionView.show(), 200);
            }
        });
        imageAdapter.setOnRecyclerViewItemChildClickListener((baseQuickAdapter, view, i) -> {
            ActionSheet.createBuilder(mContext, getSupportFragmentManager())
                    .setCancelButtonTitle("取消")
                    .setOtherButtonTitles("从相册选择", "拍照")
                    .setCancelableOnTouchOutside(true)
                    .setListener(new ActionSheet.ActionSheetListener() {
                        @Override
                        public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                        }

                        @Override
                        public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                            select(index);
                        }
                    }).show();
        });
        tvRight.setOnClickListener(v -> {
            if (checkLogin()) {
                mSelectPath.remove(ADD_FLAG);
                PostParam param = new PostParam();
                param.title = etTitle.getText().toString().trim();
                param.content = etContent.getText().toString().trim();
                param.pics = mSelectPath;
                param.userId = user.UserId;
                param.userToken = user.UserToken;
                param.categoryId = categoryId;
                param.isPublisher = 1;
                param.location = currentArea.AreaName;
                param.cityCode = currentArea.CityCode;
                param.areaId = currentArea.AreaId;
                mvpPresenter.send(param);
            }
        });
    }

    /**
     * 选择图片
     *
     * @param flag 1 拍照 2 图片
     */
    private void select(int flag) {
        if (!mSelectPath.contains(ADD_FLAG) && mSelectPath.size() == MAX_NUM) {
            showToast("最多上传" + MAX_NUM + "张图片");
        } else {
            if (flag == 1) {
                PhotoUtils.pickImageFromCamera(this);
            } else {
                selectPic();
            }
        }
    }

    /**
     * 打开图片选择页面
     */
    private void selectPic() {
        mSelectPath.remove(ADD_FLAG);
        Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);
        // whether show camera
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
        // max select image amount
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, MAX_NUM);
        // default select images (support array list)
        intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        startActivityForResult(intent, ConstantValue.REQUEST_IMAGE);
    }

    @Override
    public void showLoading() {
        dialog = LoadingDialog.show(mContext);
    }

    @Override
    public void hideLoading() {
        dialog.dismiss();
    }

    @Override
    public void onCameraCancel() {
        PhotoUtils.deleteImageUri(this, PhotoUtils.imageUriFromCamera);
    }

    @Override
    public void onCameraFinish() {
        String path = PhotoUtils.getImageAbsolutePath(this, PhotoUtils.imageUriFromCamera);
        if (mSelectPath.contains(ADD_FLAG))
            mSelectPath.remove(ADD_FLAG);
        mSelectPath.add(path);
        if (mSelectPath.size() < MAX_NUM) {
            mSelectPath.add(ADD_FLAG);
        }
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onImageFinish(ArrayList<String> list) {
        mSelectPath.clear();
        mSelectPath.addAll(list);
        if (mSelectPath.size() < MAX_NUM) {
            mSelectPath.add(ADD_FLAG);
        }
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mvpPresenter.onActivityResult(requestCode, resultCode, data);
    }
}
