package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.Callback.Callback;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.MyApplication;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.NetInterface;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.OkHttpUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.RegexUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.UserInfoCashUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.LoginRequest;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button person_login_button;
    Button enterprise_button;
    Button get_verfitycode_button;
    Button loginButton;
    Button skipButton;
    EditText firstEdit;
    EditText vertify_code_Edit;
    EditText account_password_Edit;
    RelativeLayout linearLayout_person;
    LinearLayout linearLayout_enterprise;
    public static boolean isSuccess=false;

    private long mLastClickTime=0;
    public static final long TIME_INTERVAL = 1000L;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindView();
        init();
    }

    public void init() {
        personLogin();
        enterprise_button.setOnClickListener(this);
        person_login_button.setOnClickListener(this);
        get_verfitycode_button.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        skipButton.setOnClickListener(this);

    }

    public void bindView() {
        person_login_button = findViewById(R.id.person_login);
        enterprise_button = findViewById(R.id.enterprise);
        firstEdit = findViewById(R.id.login_first_edit);
        linearLayout_enterprise = findViewById(R.id.linearlayout_enterprise);
        linearLayout_person = findViewById(R.id.linearlayout_person);
        get_verfitycode_button = findViewById(R.id.vertify_code);
        vertify_code_Edit = findViewById(R.id.vertify_edit);
        skipButton=findViewById(R.id.skip_button);
        account_password_Edit = findViewById(R.id.enterprise_password_edit);
        loginButton = findViewById(R.id.login_button);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.person_login:
                personLogin();
                break;
            case R.id.enterprise:
                enterpriseLogin();
                break;
            case R.id.vertify_code:
                requestVertifyCode();
                break;
            case R.id.login_button:
                long nowTime=System.currentTimeMillis();
                if(nowTime-mLastClickTime>TIME_INTERVAL){
                    login();
                    mLastClickTime=nowTime;
                }else{
                    mLastClickTime=nowTime;
                }
                break;
            case R.id.skip_button:
                skip();
                break;
        }
    }

    private void skip(){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    public boolean checkPhoneNumber() {
        String phoneNum = firstEdit.getText().toString();
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

    public void requestVertifyCode() {
        if (checkPhoneNumber()) {
            vertifyCodeCountDown();
            //第一步创建OKHttpClient
            OkHttpClient client = new OkHttpClient.Builder()
                    .build();
            //第二步创建RequestBody（Form表达）
            //POST方式提交JSON：传递JSON同时设置son类型头（接口找一下）
            JSONObject json = new JSONObject();
            try {
                json.put("phoneNum", "18252458715");
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        }
    }

    public void personLogin() {
        enterprise_button.setBackgroundColor(getResources().getColor(R.color.transparent));
        person_login_button.setBackground(getResources().getDrawable(R.drawable.login_selector_selected));
        firstEdit.setHint(R.string.enter_phonenumber);
        firstEdit.setText("");
        firstEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        linearLayout_person.setVisibility(View.VISIBLE);
        linearLayout_enterprise.setVisibility(View.INVISIBLE);
    }

    public void enterpriseLogin() {
        person_login_button.setBackgroundColor(getResources().getColor(R.color.transparent));
        enterprise_button.setBackground(getResources().getDrawable(R.drawable.login_selector_selected));
        firstEdit.setInputType(InputType.TYPE_CLASS_TEXT);
        firstEdit.setHint(R.string.enterprise_login_new);
        firstEdit.setText("");
        linearLayout_person.setVisibility(View.INVISIBLE);
        linearLayout_enterprise.setVisibility(View.VISIBLE);
    }

    private void vertifyCodeCountDown() {
        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                get_verfitycode_button.setClickable(false);
                get_verfitycode_button.setText(millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                get_verfitycode_button.setText("重新获取");
                get_verfitycode_button.setClickable(true);
            }
        };
        countDownTimer.start();
    }




    private void login() {
        if (linearLayout_person.getVisibility() == View.VISIBLE) {
            if (!RegexUtils.checkVertifyCode(vertify_code_Edit.getText().toString())) {
                ToastUtils.showShort("所填信息不正确");
                return;
            }
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setLoginType(1);
            loginRequest.setSms(vertify_code_Edit.getText().toString());
            loginRequest.setPhoneNum(firstEdit.getText().toString());
            Gson gson = new Gson();
            String json = gson.toJson(loginRequest);
            OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
            final RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            okHttpUtils.post(NetInterface.TSloginRequest, requestBodyJson,Callback.LoginTestCallback);
        } else {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setLoginType(0);
            loginRequest.setUsername(firstEdit.getText().toString());
            loginRequest.setPassword(EncryptUtils.encryptDES2HexString(account_password_Edit.getText().toString().getBytes(), "BaoBaoV2".getBytes(), "DES", null));
            Gson gson = new Gson();
            String json = gson.toJson(loginRequest);
            OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
            final RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            okHttpUtils.post(NetInterface.TSloginRequest, requestBodyJson,Callback.LoginTestCallback);
        }
    }

    public static void afterLogin(){
        ActivityUtils.finishOtherActivities(LoginActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(MyApplication.getContext(),MainActivity.class);
                intent.putExtra("success","登录成功");
                ActivityUtils.startActivity(intent);
                ActivityUtils.finishActivity(LoginActivity.class);
            }
        },500);
    }





}
