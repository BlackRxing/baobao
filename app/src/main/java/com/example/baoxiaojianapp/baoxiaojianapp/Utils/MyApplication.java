package com.example.baoxiaojianapp.baoxiaojianapp.Utils;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();
        //获取context
        mContext = getApplicationContext();
        CameraApplication.init(this,true);
    }
    //创建一个静态的方法，以便获取context对象
    public static Context getContext(){
        return mContext;
    }
}
