package com.example.administrator.look.Bean.zhihu;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/24.
 */

public class ZhihuDaily {
    @SerializedName("date")
    private String date;
    @SerializedName("top_stories")
    private ArrayList<ZhihuDailyItem> mZhihuDailyItems;
    @SerializedName("stories")
    private ArrayList<ZhihuDailyItem> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<ZhihuDailyItem> getZhihuDailyItems() {
        return mZhihuDailyItems;
    }

    public void setZhihuDailyItems(ArrayList<ZhihuDailyItem> zhihuDailyItems) {
        this.mZhihuDailyItems = zhihuDailyItems;
    }

    public ArrayList<ZhihuDailyItem> getStories() {
        return stories;
    }

    public void setStories(ArrayList<ZhihuDailyItem> stories) {
        this.stories = stories;
    }
}
