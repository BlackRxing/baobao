package com.example.baoxiaojianapp.baoxiaojianapp.activity;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.Callback.Callback;
import com.example.baoxiaojianapp.baoxiaojianapp.Callback.NetResquest;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.NetInterface;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.OkHttpUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.UserInfoCashUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.LoginRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class EditNickNameActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private TextView backTextView;
    private TextView saveTextView;
    private TextView nicknameText;
    private EditText nicknameedit;
    private Button   deleteButton;
    private Button   backButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nick_name);
        bindView();
         LoginTest();
    }

    private void bindView(){
        backTextView=findViewById(R.id.back_text);
        saveTextView=findViewById(R.id.save_text);
        nicknameedit=findViewById(R.id.nickname_edit);
        nicknameText=findViewById(R.id.nickname_text);
        deleteButton=findViewById(R.id.delete_button);
        backButton=findViewById(R.id.back_button);
        nicknameedit.setOnClickListener(this);
        nicknameText.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        nicknameedit.addTextChangedListener(this);
        saveTextView.setOnClickListener(this);
        backTextView.setOnClickListener(this);
        backButton.setOnClickListener(this);
        if (UserInfoCashUtils.getUserInfo("nick_name")!=""){
            nicknameedit.setText(UserInfoCashUtils.getUserInfo("nick_name"));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete_button:
                nicknameedit.setText("");
                break;
            case R.id.save_text:
                saveNickName();
                break;
            case R.id.back_text|R.id.back_button:
                finish();
                break;
        }
    }

    public void saveNickName(){
        String nickName=nicknameedit.getText().toString();
        if (nickName==null||nickName.length()==0){
            ToastUtils.showShort("昵称为空");
            return;
        }
        if(nickName.equals(UserInfoCashUtils.getUserInfo("nick_name"))){
            ToastUtils.showShort("未进行任何修改");
            return;
        }
        NetResquest.RevisePersonInfo(NetResquest.NICK_NAME,nicknameedit.getText().toString());
        finish();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        nicknameText.setText("昵称"+s.length()+"/20");
        if(s.length()>=20){
            nicknameedit.setFocusable(false);
            nicknameedit.setFocusableInTouchMode(false );
            KeyboardUtils.hideSoftInput(this);  //取消焦点后收起键盘，没有焦点就无法出发键盘，就无法输入
        }
        if(s.length()<20){
            nicknameedit.setFocusable(true);
            nicknameedit.setFocusableInTouchMode(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

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
        okHttpUtils.post(NetInterface.TSloginRequest, requestBodyJson, Callback.LoginTestCallback);
    }
}
