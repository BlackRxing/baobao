package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.baoxiaojianapp.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private Button offlineButton;
    private RelativeLayout backLayout;
    private RelativeLayout aboutLayout;
    private RelativeLayout feedbackLayout;
    private RelativeLayout contactLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        bindView();
    }
    private void bindView(){
        backLayout=findViewById(R.id.back_layout);
        offlineButton=findViewById(R.id.offline_button);
        aboutLayout=findViewById(R.id.about_layout);
        feedbackLayout=findViewById(R.id.feedback_layout);
        contactLayout=findViewById(R.id.contact_layout);
        backLayout.setOnClickListener(this);
        offlineButton.setOnClickListener(this);
        feedbackLayout.setOnClickListener(this);
        contactLayout.setOnClickListener(this);
        aboutLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.offline_button:
                break;
            case R.id.about_layout:
                break;
            case R.id.feedback_layout:
                break;
            case R.id.contact_layout:
                break;
            case R.id.back_layout:
                break;
        }
    }
}
