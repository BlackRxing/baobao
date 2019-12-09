package com.example.baoxiaojianapp.Utils;

import android.app.Application;
import android.content.Context;

import com.example.baoxiaojianapp.activity.AboutActivity;

public class CameraApplication extends AboutActivity {
    private static Application application;

    public static boolean DEBUG = true;

    /**
     * Camera初始化（主要用于工具类）debug:是否打印日志 true：打印 false：不打印
     * @param app
     * @param debug
     */
    public static void init(Application app, boolean debug){
        application = app;
        DEBUG = debug;
    }

    public static Context getCommonLibContext(){
        return application.getApplicationContext();
    }

    public static Application getCommonLibApplication(){
        return application;
    }
}
