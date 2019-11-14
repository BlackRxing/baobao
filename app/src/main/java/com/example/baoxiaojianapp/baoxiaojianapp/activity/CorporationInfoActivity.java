package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.example.baoxiaojianapp.R;

import java.io.IOException;
import java.io.InputStream;

public class CorporationInfoActivity extends AppCompatActivity {

    private ImageView corporateImage;
    private RelativeLayout backLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporation_info);
        corporateImage=findViewById(R.id.corporate_info);
        backLayout=findViewById(R.id.back_layout);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initPic();
    }

    private void initPic(){
        InputStream inputStream;
        try {
            inputStream=getAssets().open("pic/yingyezhizhao.png");
            Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
            corporateImage.setImageBitmap(bitmap);
        }catch (IOException e){
            e.printStackTrace();
            ToastUtils.showShort("图片加载识别");
        }

    }


}
