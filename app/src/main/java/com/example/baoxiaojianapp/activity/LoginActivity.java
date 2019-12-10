package com.example.baoxiaojianapp.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.Callback.Callback;
import com.example.baoxiaojianapp.Utils.Constants;
import com.example.baoxiaojianapp.Utils.MyApplication;
import com.example.baoxiaojianapp.Utils.NetInterface;
import com.example.baoxiaojianapp.Utils.OkHttpUtils;
import com.example.baoxiaojianapp.Utils.RegexUtils;
import com.example.baoxiaojianapp.Utils.UserInfoCashUtils;
import com.example.baoxiaojianapp.classpakage.LoginRequest;
import com.example.baoxiaojianapp.classpakage.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

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
    private ImageView weixinButton;
    private ImageView weiboButton;
    private CheckBox userprotocalCheckBox;
    private CheckBox privacycheckBox;
    public static boolean isSuccess = false;

    private long mLastClickTime = 0;
    public static final long TIME_INTERVAL = 300L;

    private Context mcontext;

    //weixin
    private IWXAPI iwxapi;

    private BroadcastReceiver broadcastReceiver;

    //weibo
    private Oauth2AccessToken oauth2AccessToken;
    private SsoHandler ssoHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mcontext = this;
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
        weixinButton.setOnClickListener(this);
        weiboButton.setOnClickListener(this);
        userprotocalCheckBox.setOnCheckedChangeListener(this);
        privacycheckBox.setOnCheckedChangeListener(this);
    }

    public void bindView() {
        person_login_button = findViewById(R.id.person_login);
        enterprise_button = findViewById(R.id.enterprise);
        firstEdit = findViewById(R.id.login_first_edit);
        linearLayout_enterprise = findViewById(R.id.linearlayout_enterprise);
        linearLayout_person = findViewById(R.id.linearlayout_person);
        get_verfitycode_button = findViewById(R.id.vertify_code);
        vertify_code_Edit = findViewById(R.id.vertify_edit);
        skipButton = findViewById(R.id.skip_button);
        account_password_Edit = findViewById(R.id.enterprise_password_edit);
        loginButton = findViewById(R.id.login_button);
        weixinButton = findViewById(R.id.weixin_button);
        weiboButton = findViewById(R.id.weibo_button);
        userprotocalCheckBox = findViewById(R.id.userprotocal_checkbox);
        privacycheckBox = findViewById(R.id.privacypolicy_checkbox);
    }


    @Override
    public void onClick(View v) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > TIME_INTERVAL) {
            mLastClickTime = nowTime;
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
                    if (userprotocalCheckBox.isChecked() && privacycheckBox.isChecked()) {
                        login();
                    } else {
                        ToastUtils.showShort(getText(R.string.tickcheckbox_tip));
                    }
                    break;
                case R.id.skip_button:
                    if (userprotocalCheckBox.isChecked() && privacycheckBox.isChecked()) {
                        skip();
                    } else {
                        ToastUtils.showShort(getText(R.string.tickcheckbox_tip));
                    }
                    break;
                case R.id.weibo_button:
                    weiboLogin();
                    break;
                case R.id.weixin_button:
                    weixinLogin();
                    break;
            }
        } else {
        }
    }


    private void weiboLogin() {
        WbSdk.install(this,new AuthInfo(this, Constants.APP_KEY,Constants.REDIRECT_URL,Constants.SCOPE));
        ssoHandler=new SsoHandler(this);
        ssoHandler.authorize(new SelfWbAthListener());
    }


    private void weixinLogin() {
        regToWx();
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        iwxapi.sendReq(req);
    }

    private void regToWx() {
        iwxapi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        iwxapi.registerApp(Constants.APP_ID);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                iwxapi.registerApp(Constants.APP_ID);
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));
    }

    private void skip() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
//        if (broadcastReceiver!=null){
//            unregisterReceiver(broadcastReceiver);
//
//        }
        super.onPause();
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
                json.put("phoneNum", firstEdit.getText().toString());
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
            if (!RegexUtils.checkPhoneNumber(firstEdit.getText().toString())) {
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
            okHttpUtils.post(NetInterface.TSloginRequest, requestBodyJson, Callback.LoginTestCallback);
        } else {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setLoginType(0);
            loginRequest.setUsername(firstEdit.getText().toString());
            loginRequest.setPassword(EncryptUtils.encryptDES2HexString(account_password_Edit.getText().toString().getBytes(), "BaoBaoV2".getBytes(), "DES", null));
            Gson gson = new Gson();
            String json = gson.toJson(loginRequest);
            OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
            final RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
            okHttpUtils.post(NetInterface.TSloginRequest, requestBodyJson, Callback.LoginTestCallback);
        }
    }

    public static void afterLogin() {
        ActivityUtils.finishOtherActivities(LoginActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MyApplication.getContext(), MainActivity.class);
                intent.putExtra("success", "登录成功");
                ActivityUtils.startActivity(intent);
                ActivityUtils.finishActivity(LoginActivity.class);
            }
        }, 500);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.userprotocal_checkbox:
                if (isChecked) {
                    startActivity(new Intent(this, UserProtacalActivity.class));
                }
                break;
            case R.id.privacypolicy_checkbox:
                if (isChecked) {
                    startActivity(new Intent(this, PrivatePolicyActivity.class));
                }
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final String wxopenId = intent.getStringExtra("openId");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("thirdParty", Constants.WEIXIN_ID);
        jsonObject.addProperty("weiXin_id", wxopenId);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        Callback.MyOkhttp(requestBody, NetInterface.TSThirdPartyloginRequest, new Callback.OkhttpRun() {
            @Override
            public void run(JSONObject jsonObject) {
                try {
                    if (jsonObject.getInt("code") == Constants.CODE_SUCCESS) {
                        final int state = jsonObject.getInt("state");
                        if (state == 0 || state == 1) {
                            Intent intent = new Intent(mcontext, BindPhoneActivity.class);
                            intent.putExtra("wxopenId", wxopenId);
                            intent.putExtra("thirdParty", Constants.WEIXIN_ID);
                            startActivity(intent);
                        } else {
                            User user = new Gson().fromJson(jsonObject.getJSONObject("user").toString(), User.class);
                            UserInfoCashUtils userInfoCashUtils = UserInfoCashUtils.getInstance();
                            userInfoCashUtils.saveUserInfoCash(user);
                            userInfoCashUtils.setLogin();
                            ActivityUtils.finishOtherActivities(LoginActivity.class);
                            Intent intent = new Intent(MyApplication.getContext(), MainActivity.class);
                            intent.putExtra("success", "登录成功");
                            ActivityUtils.startActivity(intent);
                            finish();
                        }
                    } else {
                        ToastUtils.showShort(getString(R.string.thirdparty_failure));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showShort(getString(R.string.thirdparty_failure));

                }
            }
        }, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(ssoHandler!=null){
            ssoHandler.authorizeCallBack(requestCode,resultCode,data);
        }

    }

    private class SelfWbAthListener implements WbAuthListener{

        @Override
        public void onSuccess(final Oauth2AccessToken oauth2AccessToken) {
            if(oauth2AccessToken.isSessionValid()){
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("thirdParty", Constants.WEIBO_ID);
                jsonObject.addProperty("weiBo_id", oauth2AccessToken.getUid());
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
                Callback.MyOkhttp(requestBody, NetInterface.TSThirdPartyloginRequest, new Callback.OkhttpRun() {
                    @Override
                    public void run(JSONObject jsonObject) {
                        try {
                            if (jsonObject.getInt("code") == Constants.CODE_SUCCESS) {
                                final int state = jsonObject.getInt("state");
                                if (state == 0 || state == 1) {
                                    Intent intent = new Intent(mcontext, BindPhoneActivity.class);
                                    intent.putExtra("weiboId", oauth2AccessToken.getUid());
                                    intent.putExtra("thirdParty", Constants.WEIBO_ID);
                                    startActivity(intent);
                                } else {
                                    User user = new Gson().fromJson(jsonObject.getJSONObject("user").toString(), User.class);
                                    UserInfoCashUtils userInfoCashUtils = UserInfoCashUtils.getInstance();
                                    userInfoCashUtils.saveUserInfoCash(user);
                                    userInfoCashUtils.setLogin();
                                    ActivityUtils.finishOtherActivities(LoginActivity.class);
                                    Intent intent = new Intent(MyApplication.getContext(), MainActivity.class);
                                    intent.putExtra("success", "登录成功");
                                    ActivityUtils.startActivity(intent);
                                    finish();
                                }
                            } else {
                                ToastUtils.showShort(getString(R.string.thirdparty_failure));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showShort(getString(R.string.thirdparty_failure));

                        }
                    }
                }, false);
            }
        }

        @Override
        public void cancel() {
            ToastUtils.showShort(getText(R.string.weibocancel));
        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
            ToastUtils.showShort(wbConnectErrorMessage.getErrorMessage());
        }
    }
}
