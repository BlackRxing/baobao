package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.MyApplication;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.UserInfoCashUtils;

public class SplashActivity extends AppCompatActivity {
    private Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext=this;
        setContentView(R.layout.activity_splash);
        process();
    }
    private void process(){
        if(UserInfoCashUtils.getUserLoginState()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(mcontext,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(mcontext,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }

    }
}
