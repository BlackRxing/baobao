package com.example.baoxiaojianapp.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.Utils.UserInfoCashUtils;

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
        initView();
    }
    private void initView(){
        if (!UserInfoCashUtils.getUserLoginState()){
            offlineButton.setBackground(getResources().getDrawable(R.drawable.login_button_background));
            offlineButton.setText(getText(R.string.click_login));
        }else {
            offlineButton.setBackground(getResources().getDrawable(R.drawable.offline_button_background));
            offlineButton.setText(getText(R.string.offline));
        }
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
                if (UserInfoCashUtils.getUserLoginState()){
                    offLineDialog();
                }else {
                    startActivity(new Intent(this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                }
                break;
            case R.id.about_layout:
                startActivity(new Intent(this,AboutActivity.class));
                break;
            case R.id.feedback_layout:
                startActivity(new Intent(this,FeedbackActivity.class));
                break;
            case R.id.contact_layout:
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setMessage(getText(R.string.contactus_toast)).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();
                alertDialog.show();
                break;
            case R.id.back_layout:
                finish();
                break;
        }
    }

    private void offLineDialog(){
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setMessage("确定要退出吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            offLine();
                            initView();
                            dialogInterface.dismiss();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create();
            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
    }
    private void offLine(){
        UserInfoCashUtils.clearUserInfoCash();
    }
}
