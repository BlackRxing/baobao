package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import android.os.Bundle;

import com.example.baoxiaojianapp.R;

import androidx.annotation.Nullable;

public class MainActivity extends BaseActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }
}
