package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.actionsheetdialog.ActionSheetDialog;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.Callback.NetResquest;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.UserInfoCashUtils;
import com.smarttop.library.bean.City;
import com.smarttop.library.bean.County;
import com.smarttop.library.bean.Province;
import com.smarttop.library.bean.Street;
import com.smarttop.library.utils.LogUtil;
import com.smarttop.library.widget.AddressSelector;
import com.smarttop.library.widget.BottomDialog;
import com.smarttop.library.widget.OnAddressSelectedListener;


public class InfoSettingActivity extends BaseActivity implements View.OnClickListener,OnAddressSelectedListener,AddressSelector.OnDialogCloseListener,AddressSelector.onSelectorAreaPositionListener {

    private RelativeLayout backLayout;
    private RelativeLayout profileImageLayout;
    private RelativeLayout nickNameLayout;
    private RelativeLayout regionLayout;
    private RelativeLayout sexLayout;
    private CircleImageView profileImage;
    private static TextView nicknameText;
    private TextView regionText;
    private TextView sexText;
    private BottomDialog bottomDialog;
    private LinearLayout mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_setting);
        mContent=findViewById(R.id.container_layout);
        bindView();
        initData();
    }

    public static void changeNickName(){
        nicknameText.setText(UserInfoCashUtils.getUserInfo("nick_name"));
    }

    private void initData(){
        SharedPreferences sharedPreferences=this.getSharedPreferences("userinfo_cash",MODE_PRIVATE);
        String nickname=sharedPreferences.getString("nick_name","");
        String location=sharedPreferences.getString("location","");
        String sex=sharedPreferences.getString("sex","");
        nicknameText.setText(nickname);
        Log.i("locaiton",location);
        if(!location.equals("null"))
            regionText.setText(location);
        if(sex=="m")
            sexText.setText("男");
        if(sex=="f")
            sexText.setText("女");
        Glide.with(this).load(sharedPreferences.getString("avatar_url","")).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                profileImage.setBackground(resource);
            }
        });


    }

    private void bindView(){
        backLayout=findViewById(R.id.back_layout);
        profileImageLayout=findViewById(R.id.profile_image_layout);
        nickNameLayout=findViewById(R.id.nickname_layout);
        regionLayout=findViewById(R.id.region_layout);
        sexLayout=findViewById(R.id.sex_layout);
        profileImage=findViewById(R.id.profile_image);
        nicknameText=findViewById(R.id.nickname_textview);
        regionText=findViewById(R.id.region_textview);
        sexText=findViewById(R.id.sex_textview);
        nickNameLayout.setOnClickListener(this);
        profileImageLayout.setOnClickListener(this);
        regionLayout.setOnClickListener(this);

    }

    @Override
    public int getLayoutResId() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                break;
            case R.id.profile_image_layout:
                chooseProfileImage();
                break;
            case R.id.nickname_layout:
                startActivity(new Intent(this,EditNickNameActivity.class));
                break;
            case R.id.region_layout:
                selectRegion();
                break;
        }
    }

    private void selectRegion(){
        bottomDialog= new BottomDialog(this);
        bottomDialog.setOnAddressSelectedListener(this);
        bottomDialog.setDialogDismisListener(this);
        bottomDialog.setSelectorAreaPositionListener(this);
        bottomDialog.setTextSelectedColor(R.color.black);
        bottomDialog.setTextUnSelectedColor(R.color.black);
        bottomDialog.show();
    }

    private void chooseProfileImage(){
        ActionSheetDialog actionSheetDialog = new ActionSheetDialog.ActionSheetBuilder(this,R.style.ActionSheetDialogBase_SampleStyle)
                .setItems(new CharSequence[]{"从相册选取","拍照"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true)
                .create();
        actionSheetDialog.show();
    }

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        String region = (province == null ? "" : province.name) + (city == null ? "" : city.name) + (county == null ? "" : county.name) +
                (street == null ? "" : street.name);
        regionText.setText(region);
        if (bottomDialog!=null){
            bottomDialog.dismiss();
        }
        NetResquest.RevisePersonInfo("location",region);
    }

    @Override
    public void dialogclose() {
        if(bottomDialog!=null){
            bottomDialog.dismiss();
        }
    }

    @Override
    public void selectorAreaPosition(int provincePosition, int cityPosition, int countyPosition, int streetPosition) {

    }
}
