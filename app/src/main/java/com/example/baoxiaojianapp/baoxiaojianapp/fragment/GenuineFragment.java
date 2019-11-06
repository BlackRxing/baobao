package com.example.baoxiaojianapp.baoxiaojianapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.NetInterface;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.OkHttpUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.UserInfoCashUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.LoginRequest;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.User;
import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class GenuineFragment extends Fragment {

    private SuperSwipeRefreshLayout swipeRefreshLayout;
    public GenuineFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginTest();
    }

    public void loadData(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_genuine, container, false);
        swipeRefreshLayout=view.findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnPushLoadMoreListener(new SuperSwipeRefreshLayout.OnPushLoadMoreListener(){

            @Override
            public void onLoadMore() {
                ToastUtils.showShort("is loading more");
            }

            @Override
            public void onPushDistance(int i) {

            }

            @Override
            public void onPushEnable(boolean b) {

            }
        });
        return view;
    }


    public void LoginTest(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginType(1);
        loginRequest.setSms("1116");
        loginRequest.setPhoneNum("18051982306");
        Gson gson = new Gson();
        String json = gson.toJson(loginRequest);
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        final RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        okHttpUtils.post(NetInterface.TSloginRequest, requestBodyJson, new OkHttpUtils.RealCallback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        User user = new Gson().fromJson(jsonObject.getJSONObject("user").toString(), User.class);
                        UserInfoCashUtils userInfoCashUtils = UserInfoCashUtils.getInstance();
                        userInfoCashUtils.clearUserInfoCash();
                        userInfoCashUtils.saveUserInfoCash(user);
                        Log.i("return info", user.getPhone_num());
                        Log.i("return info", user.getTuring_token());
                        ToastUtils.showShort("sucesslogin");
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
        });
    }



}
