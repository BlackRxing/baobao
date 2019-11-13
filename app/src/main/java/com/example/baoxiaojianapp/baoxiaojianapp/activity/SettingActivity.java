package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ToastUtils;
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
                offLine();
                break;
            case R.id.about_layout:
                startActivity(new Intent(this,AboutActivity.class));
                break;
            case R.id.feedback_layout:
                startActivity(new Intent(this,FeedbackActivity.class));
                break;
            case R.id.contact_layout:
                ToastUtils.showShort(getText(R.string.contactus_toast));
                break;
            case R.id.back_layout:
                finish();
                break;
        }
    }

    private void offLine(){
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setMessage("确定要退出吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ToastUtils.showShort("yes");
                            dialogInterface.dismiss();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ToastUtils.showShort("no");
                            dialogInterface.dismiss();
                        }
                    }).create();
            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
    }
}
