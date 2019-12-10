package com.example.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.baoxiaojianapp.Callback.Callback;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.Utils.Constants;
import com.example.baoxiaojianapp.Utils.MyApplication;
import com.example.baoxiaojianapp.Utils.NetInterface;
import com.example.baoxiaojianapp.Utils.OkHttpUtils;
import com.example.baoxiaojianapp.Utils.RegexUtils;
import com.example.baoxiaojianapp.Utils.UserInfoCashUtils;
import com.example.baoxiaojianapp.classpakage.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class BindPhoneActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout backLayout;
    private EditText phoneNumEdit;
    private EditText verifyCodeEdit;
    private Button verifyCodeButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        bindView();
    }

    private void bindView() {
        backLayout = findViewById(R.id.back_layout);
        phoneNumEdit = findViewById(R.id.phoneNum_Edit);
        verifyCodeEdit = findViewById(R.id.vertifycode_edit);
        verifyCodeButton = findViewById(R.id.vertify_code);
        loginButton = findViewById(R.id.login_button);
        verifyCodeButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        backLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vertify_code:
                requestVertifyCode();
                break;
            case R.id.login_button:
                bindphone();
                break;
            case R.id.back_layout:
                finish();
                break;
        }
    }

    private void bindphone(){
        if (!RegexUtils.checkPhoneNumber(phoneNumEdit.getText().toString())) {
            ToastUtils.showShort("所填信息不正确");
            return;
        }
        try {
            final JSONObject json = new JSONObject();
            json.put("phoneNum", phoneNumEdit.getText().toString());
            json.put("sms",verifyCodeEdit.getText().toString());
            json.put("weiXin_id",getIntent().getStringExtra("wxopenId"));
            json.put("thirdParty",getIntent().getIntExtra("thirdParty",-1));//微博是1，微信是2
            RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
            Callback.MyOkhttp(requestBodyJson, NetInterface.TSThirdPartyBindPhoneRequest, new Callback.OkhttpRun() {
                @Override
                public void run(JSONObject jsonObject) {
                    try{
                        if(jsonObject.getInt("code")==Constants.CODE_SUCCESS){
                            Log.d("ff",jsonObject.toString());
                            ActivityUtils.finishOtherActivities(BindPhoneActivity.class);
                            User user = new Gson().fromJson(jsonObject.getJSONObject("user").toString(), User.class);
                            UserInfoCashUtils userInfoCashUtils = UserInfoCashUtils.getInstance();
                            userInfoCashUtils.saveUserInfoCash(user);
                            userInfoCashUtils.setLogin();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("success", "登录成功");
                            ActivityUtils.startActivity(intent);
                            finish();
                        }else{
                            final int code=jsonObject.getJSONObject("err").getInt("code");
                            if(code==30003||code==30004){
                                ToastUtils.showShort(jsonObject.getJSONObject("err").getString("title"));
                                finish();
                            }else{
                                ToastUtils.showShort(getString(R.string.wronginfo));
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            },false);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void vertifyCodeCountDown() {
        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                verifyCodeButton.setClickable(false);
                verifyCodeButton.setText(millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                verifyCodeButton.setText("重新获取");
                verifyCodeButton.setClickable(true);
            }
        };
        countDownTimer.start();
    }

    public void requestVertifyCode() {
        if (checkPhoneNumber()) {
            vertifyCodeCountDown();
            //第一步创建OKHttpClient
            OkHttpClient client = new OkHttpClient.Builder()
                    .build();
            //第二步创建RequestBody（Form表达）
            //POST方式提交JSON：传递JSON同时设置son类型头（接口找一下）
            try {
                JSONObject json = new JSONObject();
                json.put("phoneNum", phoneNumEdit.getText().toString());
                RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
                OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
                okHttpUtils.post(NetInterface.TSloginSMSRequest, requestBodyJson, new OkHttpUtils.RealCallback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        Headers headers = response.headers();
                        List cookies = headers.values("Set-Cookie");
                        String session = cookies.get(0).toString();
                        String sessionid = session.substring(0, session.indexOf(";"));
                        SharedPreferences share = getApplicationContext().getSharedPreferences("Session", MODE_PRIVATE);
                        SharedPreferences.Editor edit = share.edit();//编辑文件
                        edit.putString("sessionid", sessionid);
                        Log.i("sessionid_in", session);
                        edit.commit();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public boolean checkPhoneNumber() {
        String phoneNum = phoneNumEdit.getText().toString();
        if (phoneNum.length() == 0 || phoneNum == null) {
            ToastUtils.showShort("请输入手机号");
            return false;
        } else {
            if (RegexUtils.checkPhoneNumber(phoneNum)) {
                return true;
            } else {
                ToastUtils.showShort("请输入11位手机号");
                return false;
            }
        }
    }

}
