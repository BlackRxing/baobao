package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.OkHttpUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    Button person_login_button;
    Button enterprise_button;
    Button get_verfitycode_button;
    EditText firstEdit;
    EditText vertify_code_Edit;
    EditText account_password_Edit;
    LinearLayout linearLayout_person;
    LinearLayout linearLayout_enterprise;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindView();
        init();
    }

    public void init(){
        personLogin();
        enterprise_button.setOnClickListener(this);
        person_login_button.setOnClickListener(this);
        get_verfitycode_button.setOnClickListener(this);
    }

    public void bindView(){
        person_login_button=findViewById(R.id.person_login);
        enterprise_button=findViewById(R.id.enterprise);
        firstEdit=findViewById(R.id.login_first_edit);
        linearLayout_enterprise=findViewById(R.id.linearlayout_enterprise);
        linearLayout_person=findViewById(R.id.linearlayout_person);
        get_verfitycode_button=findViewById(R.id.vertify_code);
        vertify_code_Edit=findViewById(R.id.vertify_edit);
        account_password_Edit=findViewById(R.id.enterprise_password_edit);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.person_login:
                personLogin();
                break;
            case R.id.enterprise:
                enterpriseLogin();
                break;
            case R.id.vertify_code:
                requestVertifyCode();
                break;
        }
    }

    public boolean checkPhoneNumber(){
            String phoneNum=firstEdit.getText().toString();
            if(phoneNum.length()==0||phoneNum==null){
                ToastUtils.showShort("请输入手机号");
                return false;
            }else{
                String regex = "(1[0-9][0-9]|15[0-9]|18[0-9])\\d{8}";
                Pattern p = Pattern.compile(regex);
                if (p.matches(regex, phoneNum)){
                    return true;
                }else {
                    ToastUtils.showShort("请输入1位手机号");
                    return false;
                }
            }
    }
    public void requestVertifyCode(){
        if(checkPhoneNumber()){
            vertifyCodeCountDown();
            //第一步创建OKHttpClient
            OkHttpClient client = new OkHttpClient.Builder()
                    .build();
            //第二步创建RequestBody（Form表达）
            //POST方式提交JSON：传递JSON同时设置son类型头（接口找一下）
            JSONObject json=new JSONObject();
            try
            {
                json.put("phoneNum","18252458715");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));

            OkHttpUtils okHttpUtils=OkHttpUtils.getInstance();
            okHttpUtils.post("https://dev2.turingsenseai.com/account/sendSMS", requestBodyJson, new OkHttpUtils.RealCallback() {
                @Override
                public void onResponse(Call call, Response response) {

                }

                @Override
                public void onFailure(Call call, IOException e) {

                }
            });
        }
    }
    public void personLogin(){
        enterprise_button.setBackgroundColor(getResources().getColor(R.color.transparent));
        person_login_button.setBackground(getResources().getDrawable(R.drawable.login_selector_selected));
        firstEdit.setHint(R.string.enter_phonenumber);
        linearLayout_person.setVisibility(View.VISIBLE);
        linearLayout_enterprise.setVisibility(View.INVISIBLE);
    }
    public void enterpriseLogin(){
        person_login_button.setBackgroundColor(getResources().getColor(R.color.transparent));
        enterprise_button.setBackground(getResources().getDrawable(R.drawable.login_selector_selected));
        firstEdit.setHint(R.string.enterprise_login_new);
        linearLayout_person.setVisibility(View.INVISIBLE);
        linearLayout_enterprise.setVisibility(View.VISIBLE);
    }
    public void vertifyCodeCountDown() {
        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                get_verfitycode_button.setClickable(false);
                get_verfitycode_button.setText(millisUntilFinished/1000+"秒");
            }

            @Override
            public void onFinish() {
                get_verfitycode_button.setText("重新获取");
                get_verfitycode_button.setClickable(true);
            }
        };
        countDownTimer.start();
    }




    @Override
    public int getLayoutResId() {
        return R.layout.activity_login;
    }


}
