package com.baishan.nearshop.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baishan.mylibrary.utils.CommonUtil;
import com.baishan.mylibrary.utils.ImageLoaderUtils;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseMvpFragment;
import com.baishan.nearshop.model.ChatRoom;
import com.baishan.nearshop.model.ForumCategory;
import com.baishan.nearshop.presenter.ForumPresenter;
import com.baishan.nearshop.ui.activity.ChatActivity;
import com.baishan.nearshop.ui.activity.PostingActivity;
import com.baishan.nearshop.ui.adapter.TitlePagerAdapter;
import com.baishan.nearshop.utils.ConstantValue;
import com.baishan.nearshop.view.IForumView;
import com.hyphenate.chat.EMChatRoom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/22 0022.
 */

public class ForumFragment extends BaseMvpFragment<ForumPresenter> implements IForumView {

    @BindView(R.id.relChatRoom)
    LinearLayout relChatRoom;
    @BindView(R.id.ivPic)
    ImageView ivPic;
    @BindView(R.id.tvChatRoom)
    TextView tvChatRoom;
    @BindView(R.id.tvNum)
    TextView tvNum;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.llTabs)
    TabLayout llTabs;
    @BindView(R.id.fab)
    ImageView fab;

    private View mLastSelectedBtn;
    private int curPosition;

    //    private String[] titiles = new String[]{"热门", "跳蚤市场", "失物寻找", "相亲交友"};
    private int mWidthPixels;

    private List<ForumCategory> categories;
    private EMChatRoom chatRoom;
    private ChatRoom curChatRoom;

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_forum, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void bindViews(View view) {

    }

    @Override
    protected void processLogic() {
        String type = getArguments().getString(ConstantValue.DATA_TYPE);
        if ("area".equals(type)) {
            relChatRoom.setVisibility(View.VISIBLE);
            mvpPresenter.getChatRoom(user.UserId, mCurrentArea.ChatroomId, mCurrentArea.AreaId);
        }
        mWidthPixels = getResources().getDisplayMetrics().widthPixels;
//        createTabAndFragments();
        mvpPresenter.getCategoryForum();
    }

    /**
     * 创建顶部tabs和fragments
     *
     * @param response
     */
    private void createTabAndFragments(List<ForumCategory> response) {
        //llTabs.removeAllViews();
        List<Fragment> fragments = new ArrayList<>();
        String[] titiles = new String[response.size()];
        for (int i = 0; i < response.size(); i++) {
            ForumCategory forumCategory = response.get(i);
            //添加fragment
            ForumListFragment fragment = new ForumListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ConstantValue.DATA_CATEGORY, forumCategory.Id);
            bundle.putString(ConstantValue.DATA_TYPE, getArguments().getString(ConstantValue.DATA_TYPE));
            fragment.setArguments(bundle);
            fragments.add(fragment);
//            Button tab = createTab(forumCategory.Title, i);
//            llTabs.addView(tab);
            titiles[i] = forumCategory.Title;
        }
        vp.setAdapter(new TitlePagerAdapter(getChildFragmentManager(), fragments, titiles));
        llTabs.setupWithViewPager(vp);
    }

    /**
     * 创建顶部tab
     *
     * @param position
     */
    private Button createTab(String text, int position) {

        Button tab = new Button(getActivity());
        tab.setBackgroundResource(R.drawable.selector_tab_yellow);
        tab.setText(text);
        tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLastSelectedBtn.setSelected(false);
                v.setSelected(true);
                vp.setCurrentItem(position);
                mLastSelectedBtn = v;
            }
        });
        tab.setLayoutParams(new ViewGroup.LayoutParams(mWidthPixels / 4, CommonUtil.dip2px(getActivity(), 40)));
        tab.setGravity(Gravity.CENTER);
        //默认选中第一个
        if (position == 0) {
            tab.setSelected(true);
            mLastSelectedBtn = tab;
        }
        return tab;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void setListener() {
//        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                curPosition = position;
//                mLastSelectedBtn.setSelected(false);
//                View child = llTabs.getChildAt(position);
//                child.setSelected(true);
//                mLastSelectedBtn = child;
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        fab.setOnClickListener(v -> {
            if (categories != null && categories.size() > 0) {
                Intent it = new Intent(mContext, PostingActivity.class);
                it.putExtra(ConstantValue.DATA_CATEGORY, (Serializable) categories);
                //it.putExtra(ConstantValue.TABPOSITION,vp.getCurrentItem());
                startActivity(it);
            }
        });
        //进入聊天室
        relChatRoom.setOnClickListener(v -> {
            if (curChatRoom == null) {
                return;
            }
            if (curChatRoom.IsGag == 1) {
                showToast("您已被禁言，无法进入聊天室");
                return;
            }
            Intent it = new Intent(mContext, ChatActivity.class);
            it.putExtra("chatType", 3);
            it.putExtra("userId", mCurrentArea.ChatroomId);
            if (chatRoom != null) {
                it.putExtra("title", chatRoom.getName());
            }
            startActivity(it);
        });
    }

    @Override
    protected ForumPresenter createPresenter() {
        return new ForumPresenter(this);
    }


    @Override
    public void getAllForumCategorySuccess(List<ForumCategory> response) {
        categories = response;
        createTabAndFragments(response);
    }

    @Override
    public void getChatRoomSuccess(ChatRoom chatRoom) {
        curChatRoom = chatRoom;
        ImageLoaderUtils.displayImage(chatRoom.ImageUrl, ivPic);
        tvChatRoom.setText(chatRoom.Name + "聊天室");
        tvNum.setText(chatRoom.AffiliationsCount + "人");
    }
}
