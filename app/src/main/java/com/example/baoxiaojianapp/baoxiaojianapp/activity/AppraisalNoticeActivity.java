package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.NetInterface;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.OkHttpUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.LoginRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

public class AppraisalNoticeActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout backLayout;
    private ImageView brandImage;
    private TextView brandText;
    private TextView classText;
    private RecyclerView recyclerView;
    private Button appraisalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraisal_notice);
        bindView();
        Bundle bundle=getIntent().getExtras();
        initView(bundle.getString("brandName"),bundle.getString("imageUrl"),bundle.getString("kindKey"));
    }

    private void bindView() {
        backLayout = findViewById(R.id.back_layout);
        brandImage = findViewById(R.id.brandImage);
        brandText = findViewById(R.id.brandName);
        classText = findViewById(R.id.className);
        recyclerView = findViewById(R.id.recycler_view);
        appraisalButton = findViewById(R.id.appraisal_button);
    }

    public void initView(String brandName,String imageUrl,String kindKey){
        Glide.with(this).load(imageUrl).centerCrop().into(brandImage);
        brandText.setText(brandName);
        switch (kindKey){
            case "shoe":
                classText.setText("#鞋履");
                break;
            case "watch":
                classText.setText("#名表");
                break;
            case "bag":
                classText.setText("#箱包");
                break;
        }

        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("brand",brandName);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonObject.toString());
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        okHttpUtils.post(NetInterface.TSAppraisalProcessRequest, requestBody, new OkHttpUtils.RealCallback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    bindData(response.body().string());
                }catch(IOException e){
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {

            }
        }, true);
    }

    private void bindData(String json){

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                finish();
                break;
        }
    }
}
