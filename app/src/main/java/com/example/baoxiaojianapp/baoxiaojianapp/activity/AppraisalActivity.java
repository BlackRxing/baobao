package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.adapter.StickFigureAdapter;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.AppraisalPointItem;
import com.example.baoxiaojianapp.baoxiaojianapp.view.CameraFocusView;
import com.example.baoxiaojianapp.baoxiaojianapp.view.CameraSurfaceView;
import com.smarttop.library.utils.LogUtil;

import java.io.IOException;
import java.util.List;

import static java.util.Collections.sort;
@SuppressWarnings("deprecation")
public class AppraisalActivity extends AppCompatActivity implements CameraFocusView.IAutoFocus, View.OnClickListener{

    private CameraSurfaceView cameraSurfaceView;
    private CameraFocusView cameraFocusView;
    private Camera camera;
    private String TAG="info";
    private RelativeLayout backLayout;
    private TextView gotoAppraisalText;
    private RecyclerView pointsRecyclerView;
    private Button seeCaseButton;
    private Button usePhotoButton;
    private List<AppraisalPointItem> appraisalPointItemList;
    private String brandName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_appraisal);
        bindView();
        bindData();
        initView();
    }
    private void bindView(){
        cameraSurfaceView=findViewById(R.id.cameraSurfaceView);
        cameraFocusView=findViewById(R.id.cameraFocusView);
        backLayout=findViewById(R.id.back_layout);
        gotoAppraisalText=findViewById(R.id.gotoAppraisal);
        pointsRecyclerView=findViewById(R.id.point_recyclerview);
        seeCaseButton=findViewById(R.id.seecase_button);
        usePhotoButton=findViewById(R.id.usephoto_button);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        pointsRecyclerView.setLayoutManager(linearLayoutManager);
        cameraFocusView.setmIAutoFocus(this);
        backLayout.setOnClickListener(this);
    }
    private void initView(){

    }

    private void bindData(){
        Bundle bundle=getIntent().getExtras();
        brandName=bundle.getString("brandName");
        appraisalPointItemList=(List<AppraisalPointItem>) bundle.getSerializable("points");
        pointsRecyclerView.setAdapter(new StickFigureAdapter(appraisalPointItemList));


    }

    @Override
    public void autoFocus(float x, float y) {
        cameraSurfaceView.setAutoFocus((int)x,(int)y);
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
