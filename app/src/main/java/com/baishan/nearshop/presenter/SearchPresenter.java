package com.baishan.nearshop.presenter;

import com.baishan.mylibrary.mvp.BasePresenter;
import com.baishan.nearshop.dao.SearchHistoryDao;
import com.baishan.nearshop.db.DBManager;
import com.baishan.nearshop.model.SearchHistory;
import com.baishan.nearshop.view.ISearchView;

import java.util.List;

/**
 * Created by RayYeung on 2016/11/25.
 */

public class SearchPresenter extends BasePresenter<ISearchView> {

    public SearchPresenter(ISearchView mvpView) {
        super(mvpView);
    }

    public List<SearchHistory> getHistoryData() {
        return DBManager.getInstance().getHistoryDao().queryBuilder().orderDesc(SearchHistoryDao.Properties.Id).list();
    }

    public void insert(String key) {
        SearchHistory history = new SearchHistory(key);
        SearchHistoryDao dao = DBManager.getInstance().getHistoryDao();
        List<SearchHistory> list = dao.queryBuilder().where(SearchHistoryDao.Properties.Key.eq(key)).list();
        if (list.size() > 0) {
            dao.delete(list.get(0));
        }
        long count = dao.queryBuilder().count();
        if (count == 12) {
            List<SearchHistory> list1 = dao.queryBuilder().limit(1).list();
            dao.delete(list1.get(0));
        }
        dao.insert(history);
    }
}
