package com.example.baoxiaojianapp.Callback;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.baoxiaojianapp.Utils.NetInterface;
import com.example.baoxiaojianapp.Utils.OkHttpUtils;
import com.example.baoxiaojianapp.Utils.UserInfoCashUtils;
import com.example.baoxiaojianapp.activity.InfoSettingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetResquest {
    public static final String NICK_NAME="nickName";
    public static final String LOCATION="location";
    public static final String SEX="sex";
    public static final String AVATAR="avatar";
    public static void RevisePersonInfo(final String key, final String value){
        final JSONObject json = new JSONObject();
        try {
            json.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        String url=AppUtils.isAppDebug()?NetInterface.DebugEnvironment+NetInterface.TSSetUserInfoRequest:NetInterface.ReleaseEnvironment+NetInterface.TSSetUserInfoRequest;
        okHttpUtils.post(url, requestBodyJson, new OkHttpUtils.RealCallback() {
            @Override
            public void onResponse(Call call, Response response) {
                try{
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    if(jsonObject.getInt("code")==0){
                        ToastUtils.showShort("修改成功");
                        switch (key){
                            case NICK_NAME:
                                UserInfoCashUtils.setUserInfo("nick_name",value);
                                InfoSettingActivity.changeUI(NICK_NAME);
                                break;
                            case LOCATION:
                                UserInfoCashUtils.setUserInfo("location",value);
                                InfoSettingActivity.changeUI(LOCATION);
                                break;
                            case SEX:
                                UserInfoCashUtils.setUserInfo("sex",value);
                                InfoSettingActivity.changeUI(SEX);
                                break;
                            case AVATAR:
                                InfoSettingActivity.changeUI(AVATAR);
                                break;
                        }
                    }
                    else {
                        ToastUtils.showShort("网络错误");
                    }
                } catch (IOException i){
                    i.printStackTrace();
                }catch (JSONException j){
                    j.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
            }
        },true);
    }
    public static void UserAdvice(final String value){
        final JSONObject json = new JSONObject();
        try {
            json.put("advice",value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        String url=AppUtils.isAppDebug()?NetInterface.DebugEnvironment+NetInterface.TSUserAdviceRequest:NetInterface.ReleaseEnvironment+NetInterface.TSUserAdviceRequest;
        okHttpUtils.post(url, requestBodyJson, new OkHttpUtils.RealCallback() {
            @Override
            public void onResponse(Call call, Response response) {
                try{
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    if(jsonObject.getInt("code")==0){
                        ToastUtils.showShort("提交成功");
                    }
                    else {
                        ToastUtils.showShort("网络错误");
                    }
                } catch (IOException i){
                    i.printStackTrace();
                }catch (JSONException j){
                    j.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
            }
        },true);
    }
}
