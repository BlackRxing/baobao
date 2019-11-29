package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.PicProcessUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.adapter.ModelDetailAdapter;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.DetailModels;
import com.example.baoxiaojianapp.baoxiaojianapp.view.AppraisalPointDialog;
import com.example.baoxiaojianapp.baoxiaojianapp.view.AppraisalReportDialog;
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
    private String brandName;
    private String modelNumber;
    private String timestamp;
    private int type;
    private String imageUrl;
    private Button generateReportButton;
    private List<DetailModels> detailModelsList;
    private Context mcontext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraisal_result);
        mcontext=this;
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
        generateReportButton = findViewById(R.id.generarereport_button);
        back_layout.setOnClickListener(this);
        generateReportButton.setOnClickListener(this);

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


        Glide.with(this).load(imageUrl).into(appearanceImage);
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
                detailModels=new Gson().fromJson(jsonArray.get(i).toString(),DetailModels.class);
                detailModelsList.add(detailModels);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new ModelDetailAdapter(detailModelsList));

    }

    private void generateReport(){
        final AppraisalReportDialog appraisalReportDialog=new AppraisalReportDialog(this);
        appraisalReportDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        Glide.with(this).load(imageUrl).centerCrop().into((ImageView) appraisalReportDialog.findViewById(R.id.dialogbrand_image));
        ((TextView)appraisalReportDialog.findViewById(R.id.dialogid_text)).setText("鉴定号: "+modelNumber);
        ((TextView)appraisalReportDialog.findViewById(R.id.dialogdata_text)).setText("鉴定日期: "+timestamp);
        ((TextView)appraisalReportDialog.findViewById(R.id.dialogtype_text)).setText("鉴定结果: "+((type==0)?"正品":"赝品"));
        ((TextView)appraisalReportDialog.findViewById(R.id.dialogbrandname_text)).setText("品牌型号: "+brandName);
        appraisalReportDialog.findViewById(R.id.savetoGallary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view=appraisalReportDialog.findViewById(R.id.report_layout);
                Bitmap bitmap=ConvertUtils.view2Bitmap(view);
                PicProcessUtils.saveBmp2Gallery(mcontext,bitmap,modelNumber+timestamp);
                appraisalReportDialog.dismiss();
            }
        });
        appraisalReportDialog.show();

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.generarereport_button:
                generateReport();
                break;

        }
    }
}
