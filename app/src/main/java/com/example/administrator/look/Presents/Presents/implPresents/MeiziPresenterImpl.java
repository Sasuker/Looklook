package com.example.administrator.look.Presents.Presents.implPresents;

import android.content.Context;

import com.example.administrator.look.API.ApiManage;
import com.example.administrator.look.Bean.Meizi.MeiziData;
import com.example.administrator.look.Bean.Meizi.VedioData;
import com.example.administrator.look.Presents.Presents.IMeiziPresent;
import com.example.administrator.look.Presents.Presents.implView.IMeiziFragment;
import com.example.administrator.look.Util.CacheUtil;
import com.example.administrator.look.config.Config;
import com.google.gson.Gson;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/24.
 */

public class MeiziPresenterImpl extends BasePresenterImpl implements IMeiziPresent {
    private IMeiziFragment mMeiziFragment;
    private CacheUtil mCacheUtil;
    private Gson gson = new Gson();
    public MeiziPresenterImpl(Context context, IMeiziFragment mMeiziFragment) {

        this.mMeiziFragment = mMeiziFragment;
        mCacheUtil = CacheUtil.get(context);
    }
    @Override
    public void getVedioData(int t) {
        Subscription subscription = ApiManage.getInstence().getGankService().getVedioData(t)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VedioData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mMeiziFragment.hidProgressDialog();
                        mMeiziFragment.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(VedioData vedioData) {
                        mMeiziFragment.hidProgressDialog();
                        mMeiziFragment.updateVedioData(vedioData.getResults());
                    }
                });
        addSubscription(subscription);
    }


    @Override
    public void getMeiziData(int t) {
        mMeiziFragment.showProgressDialog();
        Subscription subscription = ApiManage.getInstence().getGankService().getMeizhiData(t)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MeiziData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mMeiziFragment.hidProgressDialog();
                        mMeiziFragment.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(MeiziData meiziData) {
                        mMeiziFragment.hidProgressDialog();
                        mCacheUtil.put(Config.ZHIHU, gson.toJson(meiziData));
                        mMeiziFragment.updateMeiziData(meiziData.getResults());
                    }
                });
        addSubscription(subscription);
    }
}
