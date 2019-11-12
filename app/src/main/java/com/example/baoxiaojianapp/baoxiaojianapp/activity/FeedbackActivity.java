package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.example.baoxiaojianapp.R;

public class FeedbackActivity extends AppCompatActivity implements TextWatcher {
    private RelativeLayout backLayout;
    private TextView feedbackLimitsText;
    private EditText feedbackedit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        bindView();

    }

    private void bindView(){
        backLayout=findViewById(R.id.back_layout);
        feedbackLimitsText=findViewById(R.id.feedback_limits);
        feedbackedit=findViewById(R.id.feedback_edit);
        feedbackedit.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        feedbackLimitsText.setText("昵称"+s.length()+"/300");
        if(s.length()>=300){
            feedbackLimitsText.setFocusable(false);
            feedbackLimitsText.setFocusableInTouchMode(false );
            KeyboardUtils.hideSoftInput(this);  //取消焦点后收起键盘，没有焦点就无法出发键盘，就无法输入
        }
        if(s.length()<300){
            feedbackLimitsText.setFocusable(true);
            feedbackLimitsText.setFocusableInTouchMode(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
