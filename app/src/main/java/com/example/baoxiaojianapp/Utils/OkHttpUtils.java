package com.example.baoxiaojianapp.Utils;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class OkHttpUtils {
    private static OkHttpUtils okHttpUtils;
    private static OkHttpClient okHttpClient;
    private static Handler mHandler;

    /**
     * 构造初始化
     */
    private OkHttpUtils(){
        /**
         * 构建OkHttpClient
         */
        okHttpClient = new OkHttpClient.Builder()
                /**
                 * 请求的超时时间
                 */
                .readTimeout(5000,TimeUnit.MILLISECONDS)
                /**
                 * 设置响应的超时时间
                 */
                .writeTimeout(5000,TimeUnit.MILLISECONDS)
                /**
                 * 设置连接的超时时间
                 */
                .connectTimeout(5000,TimeUnit.MILLISECONDS)
                /**
                 * 构建
                 */
                .build();


        /**
         * 获取主线程的handler
         */
        mHandler = new Handler(Looper.getMainLooper());
    }


    /**
     * 通过单例模式构造对象
     * @return
     */
    public static OkHttpUtils getInstance(){
        if (okHttpUtils == null){
            synchronized (OkHttpUtils.class){
                if (okHttpUtils == null){
                    okHttpUtils = new OkHttpUtils();
                }
            }
        }
        return okHttpUtils;
    }

    /**
     * 构造Get请求，封装对用的Request请求，实现方法
     * @param url  访问路径
     * @param realCallback  接口回调
     */
    private void getRequest(String url, final RealCallback realCallback){

        Request request = new Request.Builder().url(url).get().build();
        deliveryResult(realCallback, okHttpClient.newCall(request));
    }

    /**
     * 构造post 请求，封装对用的Request请求，实现方法
     * @param url 请求的url
     * @param requestBody 请求参数
     * @param realCallback 结果回调的方法
     */
    private void postRequest(String url, RequestBody requestBody, final RealCallback realCallback, boolean needToken){
        SharedPreferences sharedPreferences=MyApplication.getContext().getSharedPreferences("Session",MODE_PRIVATE);
        SharedPreferences sharedPreferences1=MyApplication.getContext().getSharedPreferences("userinfo_cash",MODE_PRIVATE);
        String sessionid= sharedPreferences.getString("sessionid","");
        String token=sharedPreferences1.getString("turing_token","");
        Request.Builder builder;
        builder= new Request.Builder().url(url).post(requestBody).
                addHeader("TS-DEVICE-I", DeviceUtils.getMacAddress()).
                addHeader("TS-VERSION","V1.0").
                addHeader("TS-MOBILE",DeviceUtils.getModel()).
                addHeader("TS-VERSION","TS-PLATFORM ANDROID");
        if (url.contains("/account/login")){
             builder.addHeader("cookie",sessionid);
        }else{
            if (needToken){
                builder.addHeader("Authorization","Token "+token);
            }
        }
        Request request=builder.build();
        deliveryResult(realCallback, okHttpClient.newCall(request));
    }

    /**
     * 处理请求结果的回调：主线程切换
     * @param realCallback
     * @param call
     */
    private void deliveryResult(final RealCallback realCallback, Call call) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                sendFailureThread(call, e, realCallback);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                sendSuccessThread(call, response, realCallback);
            }
        });
    }

    /**
     * 发送成功的回调
     * @param call
     * @param response
     * @param realCallback
     */
    private void sendSuccessThread(final Call call, final Response response, final RealCallback
            realCallback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                realCallback.onResponse(call,response);
            }
        });
    }

    /**
     * 发送失败的回调
     * @param call
     * @param e
     * @param realCallback
     */
    private void sendFailureThread(final Call call, final IOException e, final RealCallback realCallback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                realCallback.onFailure(call,e);
            }
        });
    }

    //-----------对外公共访问的get/post方法-----------
    /**
     * get请求
     * @param url  请求url
     * @param realCallback  请求回调
     */
    public void get(String url, final RealCallback realCallback){
        getRequest(url,realCallback);
    }

    /**
     * post请求
     * @param url       请求url
     * @param realCallback  请求回调
     * @param requestBody    请求参数
     */
    public void post(String url, RequestBody requestBody, final RealCallback realCallback,boolean needToken){
        try{
            if (NetworkUtils.isConnected()){
                postRequest(url,requestBody,realCallback,needToken);
            }else{
                ToastUtils.showShort(com.example.baoxiaojianapp.Callback.Callback.CHECK_NET_CONNECT);
            }
        }catch (Exception e){
            ToastUtils.showShort(com.example.baoxiaojianapp.Callback.Callback.CONNECT_ERROR);
            e.printStackTrace();
        }

    }

    public void post(String url, RequestBody requestBody, final RealCallback realCallback){
        url= AppUtils.isAppDebug()?NetInterface.DebugEnvironment+url:NetInterface.ReleaseEnvironment+url;
        post(url,requestBody,realCallback,false);
    }
    /**
     * http请求回调类,回调方法在UI线程中执行
     */
    public static abstract class RealCallback {
        /**
         * 请求成功回调
         * @param response
         */
        public abstract void onResponse(Call call,Response response);
        /**
         * 请求失败回调
         * @param e
         */
        public abstract void onFailure(Call call,IOException e);
    }
}
