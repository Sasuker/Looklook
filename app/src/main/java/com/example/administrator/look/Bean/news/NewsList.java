package com.example.administrator.look.Bean.news;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/24.
 */

public class NewsList {
    @SerializedName("T1348647909107")
    ArrayList<NewsBean> newsList;
    public ArrayList<NewsBean> getNewsList() {
        return newsList;
    }
    public void setNewsList(ArrayList<NewsBean> newsList) {
        this.newsList = newsList;
    }
}
