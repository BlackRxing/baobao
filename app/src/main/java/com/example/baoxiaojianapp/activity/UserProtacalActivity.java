package com.example.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.example.baoxiaojianapp.R;

import java.io.IOException;
import java.io.InputStream;

public class UserProtacalActivity extends AppCompatActivity {


    private RelativeLayout backLayout;
    private LinearLayout linearLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_protacal);
        bindView();
        initPic();
    }

    private void initPic(){
        for (int i=1;i<=4;i++){
            ImageView imageView=(ImageView) linearLayout.getChildAt(i-1);
            imageView.setAdjustViewBounds(true);
            String picName="pic/yonghuxieyi"+i+".png";
            InputStream inputStream;
            try {
                inputStream=getAssets().open(picName);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
                ToastUtils.showShort("图片加载错误");
            }
        }
    }

    private void bindView(){
        linearLayout=findViewById(R.id.scrollLinear);
        backLayout=findViewById(R.id.back_layout);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
