package com.example.administrator.look.API;


import com.example.administrator.look.Bean.zhihu.ZhihuDaily;
import com.example.administrator.look.Bean.zhihu.ZhihuStory;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


public interface ZhihuApi {

    @GET("/api/4/news/latest")
    Observable<ZhihuDaily> getLastDaily();

    @GET("/api/4/news/before/{date}")
    Observable<ZhihuDaily> getTheDaily(@Path("date") String date);

    @GET("/api/4/news/{id}")
    Observable<ZhihuStory> getZhihuStory(@Path("id") String id);



}
