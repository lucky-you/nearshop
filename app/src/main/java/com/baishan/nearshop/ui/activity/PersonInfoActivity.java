package com.baishan.nearshop.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.mylibrary.utils.CommonUtil;
import com.baishan.mylibrary.utils.FileUtils;
import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.mylibrary.utils.PhotoUtils;
import com.baishan.mylibrary.view.ActionSheet;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpActivity;
import com.baishan.nearshop.presenter.PersonInfoPresenter;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.IPersonInfoView;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonInfoActivity extends BaseMvpActivity<PersonInfoPresenter> implements IPersonInfoView {


    @BindView(R.id.ivAvatar)
    CircleImageView ivAvatar;
    @BindView(R.id.relAvatar)
    RelativeLayout relAvatar;
    @BindView(R.id.tvNickName)
    TextView tvNickName;
    @BindView(R.id.relNickName)
    RelativeLayout relNickName;
    @BindView(R.id.tvSex)
    TextView tvSex;
    @BindView(R.id.relSex)
    RelativeLayout relSex;
    @BindView(R.id.tvAge)
    TextView tvAge;
    @BindView(R.id.relAge)
    RelativeLayout relAge;
    private Uri imageUri;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);
    }

    @Override
    protected void bindViews() {
        initRedTitle("个人信息");
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        registerEvent();
        setUserInfo();
    }
    @Subscribe
    public void onEventMainThread(Notice notice) {
        if (notice.type == ConstantValue.MSG_TYPE_EDIT_INFO) {
            int type= (int) notice.content;
            String value= (String) notice.content1;
            if(type==EditInfoActivity.EDIT_TYPE_NICK_NAME)
            {
                tvNickName.setText(value);
                mvpPresenter.editUserInfo(user.LoginToken,"NickName",value);
            }else if(type==EditInfoActivity.EDIT_TYPE_AGE)
            {
                tvAge.setText(value);
                mvpPresenter.editUserInfo(user.LoginToken, "UserAge", value);
            }
        }
    }
    private void setUserInfo() {
        if (user == null)
            return;
        tvNickName.setText(user.NickName);
        tvAge.setText(user.UserAge + "岁");
        tvSex.setText(user.UserSex == 1 ? "男" : "女");
        ImageLoaderUtils.displayAvatar(user.UserPhoto, ivAvatar);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected PersonInfoPresenter createPresenter() {
        return new PersonInfoPresenter(this);
    }

    @OnClick({R.id.relAvatar, R.id.relNickName, R.id.relSex, R.id.relAge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relAvatar:
                showActionSheet();
                break;
            case R.id.relNickName:
                Intent editNickIntent = new Intent(this, EditInfoActivity.class);
                editNickIntent.putExtra(ConstantValue.TYPE, EditInfoActivity.EDIT_TYPE_NICK_NAME);
                startActivity(editNickIntent);
                break;
            case R.id.relSex:
                relSex.setOnClickListener(v -> {
                    new AlertDialog.Builder(mContext)
                            .setTitle("选择性别")
                            .setSingleChoiceItems(new String[]{"男", "女"}, user.UserSex - 1, (dialog, which) -> {
//                                editUser.Sex = which + 1;
                                tvSex.setText(which == 0 ? "男" : "女");
                                dialog.dismiss();
                                mvpPresenter.editUserInfo(user.LoginToken, "UserSex", (which + 1) + "");
                            })

                            .show();
                });
                break;
            case R.id.relAge:
                Intent editAgeIntent = new Intent(this, EditInfoActivity.class);
                editAgeIntent.putExtra(ConstantValue.TYPE, EditInfoActivity.EDIT_TYPE_AGE);
                startActivity(editAgeIntent);
                break;
        }
    }

    private void showActionSheet() {
        new ActionSheet.Builder(mContext, getSupportFragmentManager())
                .setCancelableOnTouchOutside(true)
                .setCancelButtonTitle(getString(com.baishan.mylibrary.R.string.cancel))
                .setOtherButtonTitles(getResources().getStringArray(com.baishan.mylibrary.R.array.picture_select))
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        if (index == 1) {
                            PhotoUtils.pickImageFromAlbum(PersonInfoActivity.this);
                        } else {
                            PhotoUtils.pickImageFromCamera(PersonInfoActivity.this);
                        }
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mvpPresenter.onActivityResult(requestCode, resultCode, data);
    }
//    /**
//     *  * 裁剪图片方法实现
//     *  * @param uri
//     *  
//     */
//    public void startPhotoZoom(Uri sourceUri) {
//        int size = CommonUtil.dip2px(this, 48 * 5);
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(sourceUri, "image/*");
//        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", size);
//        intent.putExtra("outputY", size);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("return-data", false);
//        intent.putExtra("noFaceDetection", true);
//        //加上下面的这两句之后，系统就会把图片给我们拉伸了
//        intent.putExtra("scale", true);
//        intent.putExtra("scaleUpIfNeeded", true);
//        startActivityForResult(intent, PhotoUtils.REQUEST_CODE_CROP_PICTURE);
//    }

    /**
     *  * 裁剪图片方法实现
     *  * @param uri
     *  
     */
    public void startPhotoZoom(Uri sourceUri) {
        int size = CommonUtil.dip2px(this, 38 * 5);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(sourceUri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        imageUri = Uri.fromFile(FileUtils.getCropFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, PhotoUtils.REQUEST_CODE_CROP_PICTURE);
    }

    @Override
    public void onSelectAlbumFinish(Uri data) {
        String path = PhotoUtils.getImageAbsolutePath(this, data);
        startPhotoZoom(Uri.fromFile(new File(path)));
    }

    @Override
    public void onCameraFinish() {
        startPhotoZoom(PhotoUtils.imageUriFromCamera);
    }

    @Override
    public void onCropSuccess() {
        String path = PhotoUtils.getImageAbsolutePath(this, imageUri);
        if (!TextUtils.isEmpty(path)) {
            ImageLoaderUtils.displayImage("file://" + path, ivAvatar);
            mvpPresenter.uploadAvatar(user.LoginToken, path);
        }
    }

    @Override
    public void onCameraCancel() {
        PhotoUtils.deleteImageUri(this, PhotoUtils.imageUriFromCamera);
    }

    @Override
    public void onUploadSuccess(String imageUrl) {
        user.UserPhoto = imageUrl;
        showToast("上传成功");
    }

    @Override
    public void onEditSexSuccess( String value) {
        user.UserSex=Integer.parseInt(value);
        showToast("修改成功");
    }

    @Override
    public void onEditNickNameSuccess(String value) {
        user.NickName=value;
        showToast("修改成功");
    }

    @Override
    public void onEditAgeSuccess(String value) {
        user.UserAge= Integer.parseInt(value);
        showToast("修改成功");
    }
}
