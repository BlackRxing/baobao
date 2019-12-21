package com.example.baoxiaojianapp.Callback;

import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.baoxiaojianapp.Utils.Constants;
import com.example.baoxiaojianapp.Utils.MyApplication;
import com.example.baoxiaojianapp.Utils.NetInterface;
import com.example.baoxiaojianapp.Utils.OkHttpUtils;
import com.example.baoxiaojianapp.Utils.UserInfoCashUtils;
import com.example.baoxiaojianapp.activity.LoginActivity;
import com.example.baoxiaojianapp.adapter.AppraisalItemAdapter;
import com.example.baoxiaojianapp.classpakage.AppraisalResult;
import com.example.baoxiaojianapp.classpakage.User;
import com.example.baoxiaojianapp.fragment.FakeFragment;
import com.example.baoxiaojianapp.fragment.GenuineFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.fragment.app.FragmentActivity;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.baoxiaojianapp.fragment.GenuineFragment.appraisalItemAdapter;
import static com.example.baoxiaojianapp.fragment.GenuineFragment.appraisalResults;
import static com.example.baoxiaojianapp.fragment.GenuineFragment.hasMoreData;

public class Callback {
    public static int itemlength;
    public static int fakeitemlength;
    public static final String CHECK_NET_CONNECT="请检查网络连接";
    public static final String CONNECT_ERROR="网络连接出错";
    public static final String CONNECT_OVERTIME="网络请求超时";


    //toke续期
    public static void tokenRequest() {
        SharedPreferences sharedPreferences1 = MyApplication.getContext().getSharedPreferences("userinfo_cash", MODE_PRIVATE);
        if (System.currentTimeMillis() - sharedPreferences1.getLong("tokentime", 0) >= 7200000) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .build();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", UserInfoCashUtils.getUserInfoInt("id"));
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
            String url=AppUtils.isAppDebug()?NetInterface.DebugEnvironment+NetInterface.TSRenewalTokenRequest:NetInterface.ReleaseEnvironment+NetInterface.TSRenewalTokenRequest;
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            //第四步创建call回调对象
            final Call call = client.newCall(request);
            //第五步发起请求
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response = call.execute();
                        String responses = response.body().string();
                        JSONObject jsonObject = new JSONObject(responses);
                        if (jsonObject.getInt("code") == Constants.CODE_SUCCESS) {
                            UserInfoCashUtils.setUserInfo("turing_token", jsonObject.getString("newToken"));
                            UserInfoCashUtils.setUserInfo("tokentime", System.currentTimeMillis());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException j) {
                        j.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static OkHttpUtils.RealCallback LoginTestCallback = new OkHttpUtils.RealCallback() {
        @Override
        public void onResponse(Call call, Response response) {
            if (response.isSuccessful()) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getInt("code")==0){
                        User user = new Gson().fromJson(jsonObject.getJSONObject("user").toString(), User.class);
                        UserInfoCashUtils userInfoCashUtils = UserInfoCashUtils.getInstance();
                        userInfoCashUtils.saveUserInfoCash(user);
                        userInfoCashUtils.setLogin();
                        LoginActivity.afterLogin();
                    }else {
                        ToastUtils.showShort("所填信息不正确");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException j) {
                    ToastUtils.showShort("所填信息不正确");
                    j.printStackTrace();
                }
            } else {
            }
        }
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e("error", e.toString());
        }
    };


    public static void FakeloadData(final FragmentActivity fragmentActivity) {
        SharedPreferences sharedPreferences1 = MyApplication.getContext().getSharedPreferences("userinfo_cash", MODE_PRIVATE);
        String token = sharedPreferences1.getString("turing_token", "");
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        //第二步创建RequestBody
        RequestBody requestBody = RequestBody.create(null, new byte[]{});
        //第三步创建Rquestd
        MyOkhttp(requestBody, NetInterface.TSPersonCenterPageRequest, new OkhttpRun() {
            @Override
            public void run(JSONObject jsonObject) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("fakeList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        AppraisalResult appraisalResult = new AppraisalResult();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        appraisalResult.setAppraisalBrand(jsonObject1.getString("brandName"));
                        appraisalResult.setAppraisalData(jsonObject1.getString("timestamp"));
                        appraisalResult.setAppraisalId(jsonObject1.getString("modelNumber"));
                        appraisalResult.setAppraisalImage(jsonObject1.getString("imageUrl"));
                        appraisalResult.setType(jsonObject1.getInt("type"));
                        appraisalResult.setDetailModels(jsonObject1.getString("detailModels"));
                        FakeFragment.appraisalResults.add(appraisalResult);
                    }
                    if (jsonArray.length() == 0) {
                        FakeFragment.hasMoreData = false;
                    } else {
                        FakeFragment.hasMoreData = true;
                    }
                    FakeFragment.init();
                    fragmentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            FakeFragment.appraisalItemAdapter = new AppraisalItemAdapter(FakeFragment.appraisalResults);
                            FakeFragment.recyclerView.setAdapter(FakeFragment.appraisalItemAdapter);
                            appraisalItemAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException j) {
                    j.printStackTrace();
                }
            }
        },true);
    }

    public static void loadData(final FragmentActivity fragmentActivity) {

        RequestBody requestBody = RequestBody.create(null, new byte[]{});

        MyOkhttp(requestBody, NetInterface.TSPersonCenterPageRequest, new OkhttpRun() {
            @Override
            public void run(JSONObject jsonObject) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("realList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        AppraisalResult appraisalResult = new AppraisalResult();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        appraisalResult.setAppraisalBrand(jsonObject1.getString("brandName"));
                        appraisalResult.setAppraisalData(jsonObject1.getString("timestamp"));
                        appraisalResult.setAppraisalId(jsonObject1.getString("modelNumber"));
                        appraisalResult.setAppraisalImage(jsonObject1.getString("imageUrl"));
                        appraisalResult.setType(jsonObject1.getInt("type"));
                        appraisalResult.setDetailModels(jsonObject1.getString("detailModels"));
                        GenuineFragment.appraisalResults.add(appraisalResult);
                    }
                    if (jsonArray.length() == 0) {
                        GenuineFragment.hasMoreData = false;
                    } else {
                        GenuineFragment.hasMoreData = true;
                    }
                    GenuineFragment.init();
                    fragmentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            GenuineFragment.appraisalItemAdapter = new AppraisalItemAdapter(appraisalResults);
                            GenuineFragment.recyclerView.setAdapter(GenuineFragment.appraisalItemAdapter);
                            appraisalItemAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException j) {
                    j.printStackTrace();
                }
            }
        });
    }

    public static void refreshUserinfo(OkhttpRun okhttpRun) {
        RequestBody requestBody = RequestBody.create(null, new byte[]{});
        MyOkhttp(requestBody, NetInterface.TSPersonCenterPageRequest, okhttpRun);
    }

    public static void signin(OkhttpRun okhttpRun) {
        RequestBody requestBody = RequestBody.create(null, new byte[]{});
        MyOkhttp(requestBody, NetInterface.TSDailyPunchRequest, okhttpRun);
    }


    public static void MyOkhttp(RequestBody requestBody, String url, final OkhttpRun okhttpRun) {
        MyOkhttp(requestBody, url, okhttpRun, true);
    }

    public static void MyOkhttp(final RequestBody requestBody, String url, final OkhttpRun okhttpRun, boolean tokenRequired) {
        if(AppUtils.isAppDebug()){
            url=NetInterface.DebugEnvironment+url;
        }else{
            url=NetInterface.ReleaseEnvironment+url;
        }
        try{
            if (NetworkUtils.isConnected()){
                SharedPreferences sharedPreferences1 = MyApplication.getContext().getSharedPreferences("userinfo_cash", MODE_PRIVATE);
                final String murl = url;
                if (System.currentTimeMillis() - sharedPreferences1.getLong("tokentime", 0) >= 7200000 && tokenRequired) {
                    tokenRequest();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MyOkhttpThread(requestBody, murl, okhttpRun, true);
                        }
                    }, 2000);
                } else {
                    MyOkhttpThread(requestBody, url, okhttpRun, tokenRequired);
                }
            }else{
                ToastUtils.showShort(Callback.CHECK_NET_CONNECT);
            }
        }catch (Exception e){
            ToastUtils.showShort(Callback.CONNECT_ERROR);
        }

    }

    private static void MyOkhttpThread(RequestBody requestBody, String url, final OkhttpRun okhttpRun, boolean tokenNeed) {

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(40000, TimeUnit.MILLISECONDS)
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

        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(requestBody).
                addHeader("TS-DEVICE-I", DeviceUtils.getMacAddress()).
                addHeader("TS-VERSION","V1.0").
                addHeader("TS-MOBILE",DeviceUtils.getModel()).
                addHeader("TS-VERSION","TS-PLATFORM ANDROID");
        SharedPreferences sharedPreferences1 = MyApplication.getContext().getSharedPreferences("userinfo_cash", MODE_PRIVATE);
        if (tokenNeed) {
            String token = sharedPreferences1.getString("turing_token", "");
            builder.addHeader("Authorization", "Token " + token);
        }
        //有两个请求需要在头部加入sessionid
        if(url==NetInterface.TSThirdPartyBindPhoneRequest||url==NetInterface.TSloginRequest){
            SharedPreferences sharedPreferences2=MyApplication.getContext().getSharedPreferences("Session",MODE_PRIVATE);
            String sessionid= sharedPreferences2.getString("sessionid","");
            builder.addHeader("cookie",sessionid);
        }
        Request request = builder.build();
        //第四步创建call回调对象
        final Call call = client.newCall(request);
        //第五步发起请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                    call.enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            e.printStackTrace();
                            if (true){
                                ActivityUtils.getTopActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showShort(Callback.CONNECT_ERROR);
                                        JSONObject jsonObject=new JSONObject();
                                        try{
                                            jsonObject.put("err",Callback.CONNECT_ERROR);
                                        }catch (Exception e){

                                        }
                                        okhttpRun.run(jsonObject);
                                    }
                                });
                            }
                        }
                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            try{
                                String responses = response.body().string();
                                JSONObject jsonObject = new JSONObject(responses);
                                okhttpRun.run(jsonObject);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
            }
        }).start();
    }


    public interface OkhttpRun {
        void run(JSONObject jsonObject);
    }


    public static void FakeloadMore(final FragmentActivity fragmentActivity, int currentPage) {
        tokenRequest();
        SharedPreferences sharedPreferences1 = MyApplication.getContext().getSharedPreferences("userinfo_cash", MODE_PRIVATE);
        String token = sharedPreferences1.getString("turing_token", "");
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "personcenter_fake_key");
        jsonObject.addProperty("page", String.valueOf(currentPage));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        MyOkhttp(requestBody, NetInterface.TSPageBackwardRequest, new OkhttpRun() {
            @Override
            public void run(JSONObject jsonObject) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        AppraisalResult appraisalResult = new AppraisalResult();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        appraisalResult.setAppraisalBrand(jsonObject1.getString("brandName"));
                        appraisalResult.setAppraisalData(jsonObject1.getString("timestamp"));
                        appraisalResult.setAppraisalId(jsonObject1.getString("modelNumber"));
                        appraisalResult.setAppraisalImage(jsonObject1.getString("imageUrl"));
                        appraisalResult.setType(jsonObject1.getInt("type"));
                        appraisalResult.setDetailModels(jsonObject1.getString("detailModels"));
                        appraisalResults.add(appraisalResult);
                        fakeitemlength = jsonArray.length();
                    }
                    if (jsonArray.length() < 10) {
                        FakeFragment.hasMoreData = false;
                    }
                } catch (JSONException j) {
                    j.printStackTrace();
                }
            }
        },true);

    }


    public static void loadMore(final FragmentActivity fragmentActivity, int currentPage) {
        tokenRequest();
        SharedPreferences sharedPreferences1 = MyApplication.getContext().getSharedPreferences("userinfo_cash", MODE_PRIVATE);
        String token = sharedPreferences1.getString("turing_token", "");
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "personcenter_real_key");
        jsonObject.addProperty("page", String.valueOf(currentPage));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        MyOkhttp(requestBody, NetInterface.TSPageBackwardRequest, new OkhttpRun() {
            @Override
            public void run(JSONObject jsonObject) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        AppraisalResult appraisalResult = new AppraisalResult();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        appraisalResult.setAppraisalBrand(jsonObject1.getString("brandName"));
                        appraisalResult.setAppraisalData(jsonObject1.getString("timestamp"));
                        appraisalResult.setAppraisalId(jsonObject1.getString("modelNumber"));
                        appraisalResult.setAppraisalImage(jsonObject1.getString("imageUrl"));
                        appraisalResult.setType(jsonObject1.getInt("type"));
                        appraisalResult.setDetailModels(jsonObject1.getString("detailModels"));
                        appraisalResults.add(appraisalResult);
                        itemlength = jsonArray.length();
                    }
                    if (jsonArray.length() < 10) {
                        hasMoreData = false;
                    }
                } catch (JSONException j) {
                    j.printStackTrace();
                }
            }
        }, true);
    }

}
