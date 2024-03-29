package com.example.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.baoxiaojianapp.Callback.Callback;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.Utils.NetInterface;
import com.example.baoxiaojianapp.Utils.OkHttpUtils;
import com.example.baoxiaojianapp.Utils.UserInfoCashUtils;
import com.example.baoxiaojianapp.adapter.AppraisalPointAdapter;
import com.example.baoxiaojianapp.classpakage.AppraisalPointItem;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AppraisalNoticeActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout backLayout;
    private ImageView brandImage;
    private TextView brandText;
    private TextView classText;
    private RecyclerView recyclerView;
    private Button appraisalButton;
    private LinearLayout greenhandlinearLayout;
    private List<AppraisalPointItem> appraisalPointItemList;
    private String brandName;
    private long mLastClickTime=0;
    public static final long TIME_INTERVAL = 500L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraisal_notice);
        bindView();
        Bundle bundle = getIntent().getExtras();
        initView(bundle.getString("brandName"), bundle.getString("imageUrl"), bundle.getString("kindKey"));
        pointcheck();
    }

    private void pointcheck(){
        if(UserInfoCashUtils.getUserInfoInt("point")<10){
            final AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage(getText(R.string.pointwarning));
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.show();
        }
    }

    private void bindView() {
        backLayout = findViewById(R.id.back_layout);
        brandImage = findViewById(R.id.brandImage);
        brandText = findViewById(R.id.brandName);
        classText = findViewById(R.id.className);
        recyclerView = findViewById(R.id.appraisal_point_recyclerView);
        appraisalButton = findViewById(R.id.appraisal_button);
        backLayout=findViewById(R.id.back_layout);
        greenhandlinearLayout=findViewById(R.id.greenhand_layout);
        greenhandlinearLayout.setOnClickListener(this);
        backLayout.setOnClickListener(this);
        appraisalButton.setOnClickListener(this);
    }

    public void initView(final String brandName, String imageUrl, String kindKey) {
        this.brandName=brandName;
        RequestOptions options = new RequestOptions().bitmapTransform(new RoundedCorners(5));
        Glide.with(this).load(imageUrl).apply(options).fitCenter().into(brandImage);
 //       Glide.with(this).load(imageUrl).apply(options).into();
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
        Callback.MyOkhttp(requestBody, NetInterface.TSAppraisalProcessV2Request, new Callback.OkhttpRun() {
            @Override
            public void run(JSONObject jsonObject) {
                try {
                    final JSONArray jsonArray = jsonObject.getJSONArray("pointList");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bindData(jsonArray);
                        }
                    });
                } catch (Exception e) {
                }
            }
        },true);

    }

    private void bindData(JSONArray jsonArray) {
        appraisalPointItemList = new ArrayList<>();
        int points = jsonArray.length();
        Log.i("this",jsonArray.toString());
        String image_Url="";
        String title="";
        String content="";
        String stickFigure="";
        String bigStickFigure="";
        String key="";
        int type=0;
        for (int i=0;i < points; i++) {
            try {
                image_Url=jsonArray.getJSONObject(i).getString("image_url");
                title=jsonArray.getJSONObject(i).getString("title");
                content=jsonArray.getJSONObject(i).getString("content");
                stickFigure=jsonArray.getJSONObject(i).getString("stickFigureURL");
                bigStickFigure=jsonArray.getJSONObject(i).getString("bigStickFigureURL");
                type=jsonArray.getJSONObject(i).getInt("type");
                key=jsonArray.getJSONObject(i).getString("key");
                Log.d("j",bigStickFigure);
            } catch (JSONException j) {
                j.printStackTrace();
            }
            AppraisalPointItem appraisalPointItem=new AppraisalPointItem();
            appraisalPointItem.setPointtext(title);
            appraisalPointItem.setPointimageUrl(image_Url);
            appraisalPointItem.setPointcontent(content);
            appraisalPointItem.setStickFigureURL(stickFigure);
            appraisalPointItem.setBigStickFigureURL(bigStickFigure);
            appraisalPointItem.setType(type);
            appraisalPointItem.setKey(key);
            appraisalPointItemList.add(appraisalPointItem);
        }

        AppraisalPointAdapter appraisalPointAdapter = new AppraisalPointAdapter(appraisalPointItemList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(appraisalPointAdapter);
    }

    private void gotoGreenHand(){
        Intent intent=new Intent(this,GreenhandNoticeActivity.class);
        intent.putExtra("brandName",brandName);
        intent.putExtra("points",(Serializable)appraisalPointItemList);
        startActivity(intent);
    }

    private void gotoAppraisal(){
        Intent intent=new Intent(this,AppraisalActivity.class);
        intent.putExtra("brandName",brandName);
        intent.putExtra("points",(Serializable)appraisalPointItemList);
        startActivity(intent);
    }



    @Override
    public void onClick(View v) {
        long nowTime=System.currentTimeMillis();
        if (nowTime-mLastClickTime>TIME_INTERVAL){
            mLastClickTime=nowTime;
            switch (v.getId()) {
                case R.id.back_layout:
                    finish();
                    break;
                case R.id.appraisal_button:
                    gotoAppraisal();
                    break;
                case R.id.greenhand_layout:
                    gotoGreenHand();
                    break;
            }
        }
    }
}
