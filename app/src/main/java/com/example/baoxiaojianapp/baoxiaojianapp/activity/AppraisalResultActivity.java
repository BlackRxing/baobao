package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.adapter.ModelDetailAdapter;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.DetailModels;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppraisalResultActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout back_layout;
    private TextView appraisal_number;
    private TextView brand_name;
    private ImageView appearanceImage;
    private ImageView sealImage;
    private RecyclerView recyclerView;
    private Button shareButton;
    private String brandName;
    private String modelNumber;
    private String timestamp;
    private int type;
    private String imageUrl;
    private List<DetailModels> detailModelsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraisal_result);
        bindView();
        bindData();
    }

    private void bindView() {
        back_layout = findViewById(R.id.back_layout);
        appraisal_number = findViewById(R.id.appraisal_number);
        brand_name = findViewById(R.id.brand_name);
        appearanceImage = findViewById(R.id.appearance_image);
        sealImage = findViewById(R.id.seal_image);
        recyclerView = findViewById(R.id.pointresult_recyclerview);
        shareButton = findViewById(R.id.share_button);
    }

    private void bindData() {
        Bundle bundle = getIntent().getExtras();
        brandName = bundle.getString("brandName");
        modelNumber = bundle.getString("modelNumber");
        timestamp = bundle.getString("timestamp");
        type = bundle.getInt("type");
        imageUrl = bundle.getString("imageUrl");
        brand_name.setText("鉴定号:" + brandName);
        appraisal_number.setText("品牌:" + modelNumber);

        Glide.with(this).load(imageUrl).centerCrop().into(appearanceImage);
        if (type == 0) {
            sealImage.setImageDrawable(getDrawable(R.drawable.appraisalreal));
        } else {
            sealImage.setImageDrawable(getDrawable(R.drawable.appraisalfake));
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        try {
            JSONArray jsonArray = new JSONArray(bundle.getString("detailModels"));
            detailModelsList=new ArrayList<>();
            for (int i=0;i<jsonArray.length();i++){
                DetailModels detailModels=new DetailModels();
//                detailModels.setFakeOrReal(jsonArray.getJSONObject(i).getInt("fakeOrReal"));
//                detailModels.setGrade(jsonArray.getJSONObject(i).getInt("grade"));
//                detailModels.setKey(jsonArray.getJSONObject(i).getString("key"));
//                detailModels.setImageUrl(jsonArray.getJSONObject(i).getString("imageUrl"));
//                detailModels.setTitle(jsonArray.getJSONObject(i).getString("title"));
                detailModels=new Gson().fromJson(jsonArray.get(i).toString(),DetailModels.class);
                detailModelsList.add(detailModels);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new ModelDetailAdapter(detailModelsList));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
        }
    }
}
