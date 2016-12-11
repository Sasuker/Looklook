package com.example.administrator.look.Presents.Presents.implPresents;

import com.example.administrator.look.API.ApiManage;
import com.example.administrator.look.Bean.news.NewsList;
import com.example.administrator.look.Presents.Presents.INewTopPresent;
import com.example.administrator.look.Presents.Presents.implView.ITopNewsFragment;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/25.
 */

public class TopNewsPrensenterImpl extends BasePresenterImpl implements INewTopPresent{
    ITopNewsFragment mITopNewsFragment;
    public TopNewsPrensenterImpl(ITopNewsFragment iTopNewsFragment){
        mITopNewsFragment=iTopNewsFragment;
    }
    @Override
    public void getNewsList(int t) {
        mITopNewsFragment.showProgressDialog();
        Subscription subscription = ApiManage.getInstence().getTopNewsService().getNews(t)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mITopNewsFragment.hidProgressDialog();
                        mITopNewsFragment.showError(e.toString());
                    }

                    @Override
                    public void onNext(NewsList newsList) {
                        mITopNewsFragment.hidProgressDialog();
                        mITopNewsFragment.upListItem(newsList);
                    }
                });
        addSubscription(subscription);
        }
    }

