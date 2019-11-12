package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.fragment.PersonFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rateLayout;
    private RelativeLayout corporateInfoLayout;
    private Button shareButton;
    private RelativeLayout backLayout;
    private ImageView iconImageview;
    private TextView userProtocalText;
    private TextView privacyPolicyText;
    private BottomSheetDialog bottomSheetDialog;
    private LinearLayout weiboLayout;
    private LinearLayout weixinLayout;
    private LinearLayout friendCircleLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        bindView();
        initView();
    }
    private void bindView(){
        rateLayout=findViewById(R.id.RateLayout);
        shareButton=findViewById(R.id.share_button);
        backLayout=findViewById(R.id.back_layout);
        corporateInfoLayout=findViewById(R.id.corporateInfoLayout);
        iconImageview=findViewById(R.id.icon);
        userProtocalText=findViewById(R.id.userprotocal_textview);
        privacyPolicyText=findViewById(R.id.privacypolicy_textview);
        rateLayout.setOnClickListener(this);
        shareButton.setOnClickListener(this);
        corporateInfoLayout.setOnClickListener(this);
        backLayout.setOnClickListener(this);
        userProtocalText.setOnClickListener(this);
        privacyPolicyText.setOnClickListener(this);
    }

    private void initView(){
        View bottomview=View.inflate(this,R.layout.share_bottomsheet_layout,null);
        bottomSheetDialog=new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomview);
        weiboLayout=bottomview.findViewById(R.id.weibo_button);
        weixinLayout=bottomview.findViewById(R.id.weixin_button);
        friendCircleLayout=bottomview.findViewById(R.id.friendcircle_button);
        weiboLayout.setOnClickListener(this);
        weixinLayout.setOnClickListener(this);
        friendCircleLayout.setOnClickListener(this);

        RequestOptions options = new RequestOptions().bitmapTransform(new RoundedCorners(30));//图片圆角为30
        Glide.with(this).load(R.drawable.icon).apply(options).into(iconImageview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                finish();
                break;
            case R.id.share_button:
                showShareBottom();
                break;
            case R.id.RateLayout:
                break;
            case R.id.corporateInfoLayout:
                break;
            case R.id.userprotocal_textview:
                break;
            case R.id.privacypolicy_textview:
                break;
            case R.id.weixin_button:
                ToastUtils.showShort("weixin");
                break;
            case R.id.weibo_button:
                ToastUtils.showShort("wwebo");
                break;
            case R.id.friendcircle_button:
                ToastUtils.showShort("friend");
                break;
        }
    }

    private void showShareBottom(){
        bottomSheetDialog.show();
    }
}
