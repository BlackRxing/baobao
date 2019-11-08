package com.example.baoxiaojianapp.baoxiaojianapp.Callback;

import com.blankj.utilcode.util.ToastUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.NetInterface;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetResquest {
    public static final String NICK_NAME="nickName";
    public static void RevisePersonInfo(String key,String value){
        final JSONObject json = new JSONObject();
        try {
            json.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        okHttpUtils.post(NetInterface.TSSetUserInfoRequest, requestBodyJson, new OkHttpUtils.RealCallback() {
            @Override
            public void onResponse(Call call, Response response) {
                try{
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    if(jsonObject.getInt("code")==0)
                        ToastUtils.showShort("修改成功");
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
