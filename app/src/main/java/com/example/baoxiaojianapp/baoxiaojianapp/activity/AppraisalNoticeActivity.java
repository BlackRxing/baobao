package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.baoxiaojianapp.baoxiaojianapp.adapter.AppraisalPointAdapter;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.AppraisalPointItem;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.LoginRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        Bundle bundle = getIntent().getExtras();
        initView(bundle.getString("brandName"), bundle.getString("imageUrl"), bundle.getString("kindKey"));
    }

    private void bindView() {
        backLayout = findViewById(R.id.back_layout);
        brandImage = findViewById(R.id.brandImage);
        brandText = findViewById(R.id.brandName);
        classText = findViewById(R.id.className);
        recyclerView = findViewById(R.id.appraisal_point_recyclerView);
        appraisalButton = findViewById(R.id.appraisal_button);
        backLayout=findViewById(R.id.back_layout);
        backLayout.setOnClickListener(this);
        appraisalButton.setOnClickListener(this);
    }

    public void initView(String brandName, String imageUrl, String kindKey) {
        Glide.with(this).load(imageUrl).centerCrop().into(brandImage);
        brandText.setText(brandName);
        switch (kindKey) {
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

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("brand", brandName);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        okHttpUtils.post(NetInterface.TSAppraisalProcessRequest, requestBody, new OkHttpUtils.RealCallback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonObject.getJSONArray("pointList");
                    bindData(jsonArray);
                } catch (IOException e) {

                } catch (JSONException j) {

                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        }, true);
    }

    private void bindData(JSONArray jsonArray) {
        List<AppraisalPointItem> appraisalPointItemList = new ArrayList<>();
        int points = jsonArray.length();
        Log.i("this",jsonArray.toString());
        String image_Url="";
        String title="";
        for (int i=0;i < points; i++) {
            try {
                image_Url=jsonArray.getJSONObject(i).getString("image_url");
                title=jsonArray.getJSONObject(i).getString("title");
            } catch (JSONException j) {
                j.printStackTrace();
            }
            AppraisalPointItem appraisalPointItem=new AppraisalPointItem();
            appraisalPointItem.setPointtext(title);
            appraisalPointItem.setPointimageUrl(image_Url);
            appraisalPointItemList.add(appraisalPointItem);
        }

        AppraisalPointAdapter appraisalPointAdapter = new AppraisalPointAdapter(appraisalPointItemList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(appraisalPointAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.appraisal_button:
                ToastUtils.showShort("goto appraisal");
                break;
        }
    }
}
