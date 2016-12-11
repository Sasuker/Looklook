package com.example.administrator.look;

import android.app.Application;

/**
 * Created by Administrator on 2016/11/23.
 */

public class MyApplication extends Application {
    private static MyApplication myApplication;
    public static Application getContext(){
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }
}
