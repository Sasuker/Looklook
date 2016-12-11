package com.example.administrator.look.Presents.Presents.implPresents;

import com.example.administrator.look.API.ApiManage;
import com.example.administrator.look.Bean.zhihu.ZhihuStory;
import com.example.administrator.look.Presents.Presents.IZhihuStoryPresent;
import com.example.administrator.look.Presents.Presents.implView.IZhihuStory;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/25.
 */

public class ZhihuStoryPresenterImpl extends BasePresenterImpl implements IZhihuStoryPresent{
    private IZhihuStory mIZhihuStory;

    public ZhihuStoryPresenterImpl(IZhihuStory zhihuStory) {
        if (zhihuStory == null)
            throw new IllegalArgumentException("zhihuStory must not be null");
        mIZhihuStory = zhihuStory;
    }
    @Override
    public void getZhihuStory(String id) {
        Subscription subscription = ApiManage.getInstence().getZhihuApiService().getZhihuStory(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhihuStory>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mIZhihuStory.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ZhihuStory zhihuStory) {
                        mIZhihuStory.showZhihuStory(zhihuStory);
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void getGuokrArticle(String id) {

    }
}
