package com.baishan.nearshop.ui.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baishan.mylibrary.bean.Notice;
import com.baishan.nearshop.R;
import com.baishan.nearshop.base.BaseFragment;
import com.baishan.nearshop.model.GridType;
import com.baishan.nearshop.ui.adapter.GridTypeAdapter;
import com.baishan.nearshop.utils.ConstantValue;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by Administrator on 2016/10/14 0014.
 */
public class GridTypeFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private GridTypeAdapter adapter;
    private List<GridType> list;

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_grid_type, null);
    }

    @Override
    protected void bindViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    }

    @Override
    protected void processLogic() {
        Logger.i("GridTypeFragment.processLogic");
        list = (List<GridType>) getArguments().getSerializable(ConstantValue.LIST);
        adapter = new GridTypeAdapter(list);
//        NothingAdapter adapter = (NothingAdapter) getArguments().getSerializable(ConstantValue.ADAPTER);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        recyclerView.setAdapter(adapter);
        post(new Notice(ConstantValue.MSG_TYPE_MEASURE_VIEW_PAGER));
    }

    @Override
    protected void setListener() {
        adapter.setOnRecyclerViewItemChildClickListener((baseQuickAdapter, view, i) -> ((EasyOrderFragment) getParentFragment()).showTypeList(list.get(i)));
    }
}
