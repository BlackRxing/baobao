package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.baoxiaojianapp.R;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    Button person_login_button;
    Button enterprise_button;
    EditText firstEdit;
    EditText secondEdit;
    Button loginButton;
    ImageView weixinImage;
    ImageView weiboImage;
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
    }

    public void bindView(){
        person_login_button=findViewById(R.id.person_login);
        enterprise_button=findViewById(R.id.enterprise);
        firstEdit=findViewById(R.id.login_first_edit);
        secondEdit=findViewById(R.id.login_second_edit);
        loginButton=findViewById(R.id.login_button);
        weixinImage=findViewById(R.id.weixin_button);
        weiboImage=findViewById(R.id.weibo_button);
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
        }
    }
    public void personLogin(){
        enterprise_button.setBackgroundColor(getResources().getColor(R.color.transparent));
        person_login_button.setBackground(getResources().getDrawable(R.drawable.login_selector_selected));
        firstEdit.setHint(R.string.enter_phonenumber);
        secondEdit.setHint(R.string.vertify_code);
    }
    public void enterpriseLogin(){
        person_login_button.setBackgroundColor(getResources().getColor(R.color.transparent));
        enterprise_button.setBackground(getResources().getDrawable(R.drawable.login_selector_selected));
        firstEdit.setHint(R.string.enterprise_login_new);
        secondEdit.setHint(R.string.account_password);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_login;
    }
}
