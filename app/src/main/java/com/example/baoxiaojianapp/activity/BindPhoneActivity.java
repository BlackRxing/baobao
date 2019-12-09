package com.example.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.Utils.RegexUtils;

public class BindPhoneActivity extends AppCompatActivity implements View.OnClickListener{

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

    private void bindView(){
        backLayout=findViewById(R.id.back_layout);
        phoneNumEdit=findViewById(R.id.phoneNum_Edit);
        verifyCodeEdit=findViewById(R.id.vertifycode_edit);
        verifyCodeButton=findViewById(R.id.vertify_code);
        loginButton=findViewById(R.id.login_button);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.vertify_code:
                requestVertifyCode();
                break;
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

}
