package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;

import com.example.baoxiaojianapp.R;

public class LoginActivity extends BaseActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_login;
    }
}
