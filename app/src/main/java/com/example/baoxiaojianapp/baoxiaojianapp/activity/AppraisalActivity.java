package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.ToastUtils;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.view.CameraFocusView;
import com.example.baoxiaojianapp.baoxiaojianapp.view.CameraSurfaceView;
import com.smarttop.library.utils.LogUtil;

import java.io.IOException;
import java.util.List;

import static java.util.Collections.sort;
@SuppressWarnings("deprecation")
public class AppraisalActivity extends AppCompatActivity implements CameraFocusView.IAutoFocus{

    private CameraSurfaceView cameraSurfaceView;
    private CameraFocusView cameraFocusView;
    private Camera camera;
    private String TAG="info";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_appraisal);

        bindView();
        initView();
    }
    private void bindView(){
        cameraSurfaceView=findViewById(R.id.cameraSurfaceView);
        cameraFocusView=findViewById(R.id.cameraFocusView);


        cameraFocusView.setmIAutoFocus(this);
    }
    private void initView(){

    }

    @Override
    public void autoFocus(float x, float y) {
        cameraSurfaceView.setAutoFocus((int)x,(int)y);
    }




}
