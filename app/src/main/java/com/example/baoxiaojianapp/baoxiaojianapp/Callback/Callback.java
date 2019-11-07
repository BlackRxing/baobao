package com.example.baoxiaojianapp.baoxiaojianapp.Callback;

import android.content.SharedPreferences;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.MyApplication;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.NetInterface;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.OkHttpUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.UserInfoCashUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.adapter.AppraisalItemAdapter;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.AppraisalResult;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.User;
import com.example.baoxiaojianapp.baoxiaojianapp.fragment.GenuineFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class Callback {
    public static OkHttpUtils.RealCallback LoginTestCallback=new OkHttpUtils.RealCallback() {
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
    };






}
