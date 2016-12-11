package com.example.administrator.look.Presents.Presents.implPresents;

import com.example.administrator.look.Bean.news.NewsDetailBean;
import com.example.administrator.look.Presents.Presents.INewTopDescriblePresent;
import com.example.administrator.look.Presents.Presents.implView.ITopNewsDesFragment;
import com.example.administrator.look.Util.NewsJsonUtils;
import com.example.administrator.look.Util.OkHttpUtils;
import com.example.administrator.look.Util.Urls;

/**
 * Created by Administrator on 2016/11/24.
 */

public class TopNewsDesPresenterImpl extends BasePresenterImpl implements INewTopDescriblePresent{

    private ITopNewsDesFragment mITopNewsFragment;

    public TopNewsDesPresenterImpl(ITopNewsDesFragment topNewsFragment) {
        if (topNewsFragment == null)
            throw new IllegalArgumentException(" must not be null");
        mITopNewsFragment = topNewsFragment;
    }
    private String getDetailUrl(String docID){
        StringBuffer sb = new StringBuffer(Urls.NEW_DETAIL);
        sb.append(docID).append(Urls.END_DETAIL_URL);
        return sb.toString();
    }

    @Override
    public void getDescrible(final String docid) {
        mITopNewsFragment.showProgressDialog();
        String url = getDetailUrl(docid);
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {
            public void onSuccess(String response) {
                NewsDetailBean newsDetailBean = NewsJsonUtils.readJsonNewsDetailBeans(response, docid);
                mITopNewsFragment.upListItem(newsDetailBean);
            }

            public void onFailure(Exception e) {
                mITopNewsFragment.showError(e.toString());
            }
        };
        OkHttpUtils.get(url, loadNewsCallback);

    }
    }

