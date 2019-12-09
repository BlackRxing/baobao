package com.example.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.Callback.NetResquest;

public class FeedbackActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener{
    private RelativeLayout backLayout;
    private TextView feedbackLimitsText;
    private EditText feedbackedit;
    private TextView sendText;
    private  static Context mcontext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mcontext=this;
        bindView();

    }

    private void bindView(){
        backLayout=findViewById(R.id.back_layout);
        feedbackLimitsText=findViewById(R.id.feedback_limits);
        feedbackedit=findViewById(R.id.feedback_edit);
        sendText=findViewById(R.id.save_text);
        sendText.setOnClickListener(this);
        backLayout.setOnClickListener(this);
        feedbackedit.addTextChangedListener(this);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        feedbackLimitsText.setText("昵称"+s.length()+"/400");
        if(s.length()>=400){
//            ToastUtils.showShort("最多400字哦");
//            feedbackedit.setFocusable(false);
//            feedbackedit.setFocusableInTouchMode(false );
//            KeyboardUtils.hideSoftInput(this);  //取消焦点后收起键盘，没有焦点就无法出发键盘，就无法输入
        }
        if(s.length()<400){
            feedbackedit.setFocusable(true);
            feedbackedit.setFocusableInTouchMode(true);
        }
    }
    private void sendAdvice(){
        String advice=feedbackedit.getText().toString();
        if (advice.length()==0||advice==null){
            ToastUtils.showShort("对不起，您没有输入任何内容");
            return;
        }
        NetResquest.UserAdvice(feedbackedit.getText().toString());
        finish();
    }



    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_text:
                sendAdvice();
                break;
            case R.id.back_layout:
                finish();
                break;
        }
    }
}
