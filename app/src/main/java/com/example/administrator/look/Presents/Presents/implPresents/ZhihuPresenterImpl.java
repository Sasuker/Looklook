package com.example.administrator.look.Presents.Presents.implPresents;

import android.content.Context;

import com.example.administrator.look.API.ApiManage;
import com.example.administrator.look.Bean.zhihu.ZhihuDaily;
import com.example.administrator.look.Bean.zhihu.ZhihuDailyItem;
import com.example.administrator.look.Presents.Presents.IZhihuPresent;
import com.example.administrator.look.Presents.Presents.implView.IZhihuFragment;
import com.example.administrator.look.Util.CacheUtil;
import com.example.administrator.look.config.Config;
import com.google.gson.Gson;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/25.
 */

public class ZhihuPresenterImpl extends BasePresenterImpl implements IZhihuPresent{
    private IZhihuFragment mZhihuFragment;
    private CacheUtil mCacheUtil;
    private Gson gson = new Gson();

    public ZhihuPresenterImpl(Context context, IZhihuFragment zhihuFragment) {

        mZhihuFragment = zhihuFragment;
        mCacheUtil = CacheUtil.get(context);
    }
    @Override
    public void getLastZhihuNews() {
        mZhihuFragment.showProgressDialog();
        Subscription subscription = ApiManage.getInstence().getZhihuApiService().getLastDaily()
                .map(new Func1<ZhihuDaily, ZhihuDaily>() {
                    @Override
                    public ZhihuDaily call(ZhihuDaily zhihuDaily) {
                        String date = zhihuDaily.getDate();
                        for(ZhihuDailyItem zhihuDailyItem : zhihuDaily.getStories()){
                            zhihuDailyItem.setDate(date);
                        }
                        return zhihuDaily;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhihuDaily>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mZhihuFragment.hidProgressDialog();
                        mZhihuFragment.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ZhihuDaily zhihuDaily) {
                        mZhihuFragment.hidProgressDialog();
                        mCacheUtil.put(Config.ZHIHU, gson.toJson(zhihuDaily));
                        mZhihuFragment.updateList(zhihuDaily);
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void getTheDaily(String date) {
        Subscription subscription = ApiManage.getInstence().getZhihuApiService().getTheDaily(date)
                .map(new Func1<ZhihuDaily, ZhihuDaily>() {
                    @Override
                    public ZhihuDaily call(ZhihuDaily zhihuDaily) {
                        String date = zhihuDaily.getDate();
                        for (ZhihuDailyItem zhihuDailyItem : zhihuDaily.getStories()){
                            zhihuDailyItem.setDate(date);
                        }
                        return zhihuDaily;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhihuDaily>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mZhihuFragment.hidProgressDialog();
                        mZhihuFragment.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ZhihuDaily zhihuDaily) {
                        mZhihuFragment.hidProgressDialog();
                        mZhihuFragment.updateList(zhihuDaily);
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void getLastFromCache() {
        if (mCacheUtil.getAsJSONObject(Config.ZHIHU) != null) {
            ZhihuDaily zhihuDaily = gson.fromJson(mCacheUtil.getAsJSONObject(Config.ZHIHU).toString(), ZhihuDaily.class);
            mZhihuFragment.updateList(zhihuDaily);
        }
    }
}
