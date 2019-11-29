package com.example.baoxiaojianapp.baoxiaojianapp.Callback;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.inspector.StaticInspectionCompanionProvider;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.Constants;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.MyApplication;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.NetInterface;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.OkHttpUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.UserInfoCashUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.activity.LoginActivity;
import com.example.baoxiaojianapp.baoxiaojianapp.adapter.AppraisalItemAdapter;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.AppraisalResult;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.User;
import com.example.baoxiaojianapp.baoxiaojianapp.fragment.FakeFragment;
import com.example.baoxiaojianapp.baoxiaojianapp.fragment.GenuineFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.baoxiaojianapp.baoxiaojianapp.fragment.GenuineFragment.appraisalItemAdapter;
import static com.example.baoxiaojianapp.baoxiaojianapp.fragment.GenuineFragment.appraisalResults;
import static com.example.baoxiaojianapp.baoxiaojianapp.fragment.GenuineFragment.hasMoreData;

public class Callback {
    public static int itemlength;
    public static int fakeitemlength;

    //toke续期
    public static void tokenRequest(){
        SharedPreferences sharedPreferences1 = MyApplication.getContext().getSharedPreferences("userinfo_cash", MODE_PRIVATE);
         if (System.currentTimeMillis()-sharedPreferences1.getLong("tokentime",0)>=7200000){
             OkHttpClient client = new OkHttpClient.Builder()
                     .build();
             JsonObject jsonObject = new JsonObject();
             jsonObject.addProperty("id", UserInfoCashUtils.getUserInfoInt("id"));
             RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
             Request request = new Request.Builder()
                     .url(NetInterface.TSRenewalTokenRequest)
                     .post(requestBody)
                     .build();
             //第四步创建call回调对象
             final Call call = client.newCall(request);
             //第五步发起请求
             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     try{
                         Response response = call.execute();
                         String responses=response.body().string();
                         JSONObject jsonObject = new JSONObject(responses);
                         if (jsonObject.getInt("code")== Constants.CODE_SUCCESS){
                             UserInfoCashUtils.setUserInfo("turing_token",jsonObject.getString("newToken"));
                             UserInfoCashUtils.setUserInfo("tokentime",System.currentTimeMillis());
                         }
                     }catch (IOException e) {
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
                    User user = new Gson().fromJson(jsonObject.getJSONObject("user").toString(), User.class);
                    UserInfoCashUtils userInfoCashUtils = UserInfoCashUtils.getInstance();
                    userInfoCashUtils.saveUserInfoCash(user);
                    userInfoCashUtils.setLogin();
                    LoginActivity.afterLogin();
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
        tokenRequest();
        SharedPreferences sharedPreferences1 = MyApplication.getContext().getSharedPreferences("userinfo_cash", MODE_PRIVATE);
        String token = sharedPreferences1.getString("turing_token", "");
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        //第二步创建RequestBody
        RequestBody requestBody = RequestBody.create(null, new byte[]{});
        //第三步创建Rquest
        Request request = new Request.Builder()
                .url(NetInterface.TSPersonCenterPageRequest)
                .post(requestBody).addHeader("Authorization", "Token " + token)
                .build();
        //第四步创建call回调对象
        final Call call = client.newCall(request);
        //第五步发起请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    String result = response.body().string();
                    Log.i("response", result);
                    JSONObject jsonObject = new JSONObject(result);
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
                        appraisalResults.add(appraisalResult);
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
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException j) {
                    j.printStackTrace();
                }
            }
        }).start();
    }

    public static void loadData(final FragmentActivity fragmentActivity) {

        RequestBody requestBody = RequestBody.create(null, new byte[]{});

        MyOkhttp(requestBody, NetInterface.TSPersonCenterPageRequest, new OkhttpRun() {
            @Override
            public void run(JSONObject jsonObject) {
                try{
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
                        appraisalResults.add(appraisalResult);
                    }
                    if (jsonArray.length() == 0) {
                        GenuineFragment.hasMoreData = false;
                    } else {
                        hasMoreData = true;
                    }
                    GenuineFragment.init();
                    fragmentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            GenuineFragment.appraisalItemAdapter = new AppraisalItemAdapter(appraisalResults);
                            GenuineFragment.recyclerView.setAdapter(appraisalItemAdapter);
                        }
                    });
                }  catch (JSONException j) {
                    j.printStackTrace();
                }
            }
        });
    }

    public static void refreshUserinfo(OkhttpRun okhttpRun){
        RequestBody requestBody=RequestBody.create(null,new byte[]{});
        MyOkhttp(requestBody, NetInterface.TSPersonCenterPageRequest,okhttpRun);
    }

    public static void signin(OkhttpRun okhttpRun){
        RequestBody requestBody=RequestBody.create(null,new byte[]{});
        MyOkhttp(requestBody, NetInterface.TSDailyPunchRequest,okhttpRun);
    }


    public static void MyOkhttp(RequestBody requestBody, String url, final OkhttpRun okhttpRun) {
        tokenRequest();
        SharedPreferences sharedPreferences1 = MyApplication.getContext().getSharedPreferences("userinfo_cash", MODE_PRIVATE);
        String token = sharedPreferences1.getString("turing_token", "");
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody).addHeader("Authorization", "Token " + token)
                .build();
        //第四步创建call回调对象
        final Call call = client.newCall(request);
        //第五步发起请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Response response = call.execute();
                    String responses=response.body().string();
                    JSONObject jsonObject = new JSONObject(responses);
                    okhttpRun.run(jsonObject);
                }catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException j) {
                    j.printStackTrace();
                }
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

        Request request = new Request.Builder()
                .url(NetInterface.TSPageBackwardRequest)
                .post(requestBody).addHeader("Authorization", "Token " + token)
                .build();
        //第四步创建call回调对象
        final Call call = client.newCall(request);
        //第五步发起请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    String result = response.body().string();
//                    Log.i("userresponse", result);
                    JSONObject jsonObject = new JSONObject(result);
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
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException j) {
                    j.printStackTrace();
                }
            }
        }).start();
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

        Request request = new Request.Builder()
                .url(NetInterface.TSPageBackwardRequest)
                .post(requestBody).addHeader("Authorization", "Token " + token)
                .build();
        //第四步创建call回调对象
        final Call call = client.newCall(request);
        //第五步发起请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    String result = response.body().string();
//                    Log.i("userresponse", result);
                    JSONObject jsonObject = new JSONObject(result);
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
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException j) {
                    j.printStackTrace();
                }
            }
        }).start();
    }


}
