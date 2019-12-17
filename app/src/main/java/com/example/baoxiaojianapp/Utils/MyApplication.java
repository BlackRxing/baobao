package com.example.baoxiaojianapp.Utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

public class MyApplication extends Application {
    private static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();
        //获取context
        mContext = getApplicationContext();
        CameraApplication.init(this, true);

//       xingeInit();
    }

//    public void xingeInit() {
//        //xinge
//        XGPushConfig.enableDebug(this, true);
//        XGPushConfig.enableOtherPush(getApplicationContext(),true);
//        XGPushConfig.setMiPushAppId(getApplicationContext(), "2882303761518276156");
//        XGPushConfig.setMiPushAppKey(getApplicationContext(), "5631827618156");
//        XGPushManager.registerPush(this, new XGIOperateCallback() {
//            @Override
//            public void onSuccess(Object data, int flag) {
////token在设备卸载重装的时候有可能会变
//                Log.d("TPush", "注册成功，设备token为：" + data);
//            }
//
//            @Override
//            public void onFail(Object data, int errCode, String msg) {
//                Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
//            }
//        });
//        XGPushManager.bindAccount(getApplicationContext(), "XINGE");
//        XGPushManager.setTag(this,"XINGE");
//
//    }


    //创建一个静态的方法，以便获取context对象
    public static Context getContext() {
        return mContext;
    }
}
