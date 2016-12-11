package com.example.administrator.look.Presents.Presents;

/**
 * Created by Administrator on 2016/11/24.
 */

public interface IZhihuPresent extends BasePresent {
    void getLastZhihuNews();

    void getTheDaily(String date);

    void getLastFromCache();
}
