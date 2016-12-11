package com.example.administrator.look.API;


import com.example.administrator.look.Bean.news.NewsList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


public interface TopNews {


    @GET("http://c.m.163.com/nc/article/headline/T1348647909107/{id}-20.html")
    Observable<NewsList> getNews(@Path("id") int id);

    @GET("http://c.m.163.com/nc/article/{id}/full.html")
    Observable<String> getNewsDetail(@Path("id") String id);

}
