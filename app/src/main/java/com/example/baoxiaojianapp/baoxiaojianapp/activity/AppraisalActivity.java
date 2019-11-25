

package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.Callback.Callback;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.BitmapUtil;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.CameraModel;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.Constants;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.FileUtil;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.NetInterface;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.OkHttpUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.PicProcessUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.adapter.StickFigureAdapter;
import com.example.baoxiaojianapp.baoxiaojianapp.classpakage.AppraisalPointItem;
import com.example.baoxiaojianapp.baoxiaojianapp.view.AppraisalPointDialog;
import com.example.baoxiaojianapp.baoxiaojianapp.view.CameraFocusView;
import com.example.baoxiaojianapp.baoxiaojianapp.view.CameraSurfaceView;
import com.google.gson.JsonObject;
import com.smarttop.library.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.sort;

@SuppressWarnings("deprecation")
public class AppraisalActivity extends AppCompatActivity implements CameraFocusView.IAutoFocus, View.OnClickListener, StickFigureAdapter.StickFigureAdaterClick, ViewTreeObserver.OnGlobalLayoutListener {

    private CameraSurfaceView cameraSurfaceView;
    private CameraFocusView cameraFocusView;
    private Camera camera;
    private String TAG = "info";
    private RelativeLayout backLayout;
    private TextView gotoAppraisalText;
    private RecyclerView pointsRecyclerView;
    private Button seeCaseButton;
    private Button usePhotoButton;
    private Button takePhotoButton;
    private List<AppraisalPointItem> appraisalPointItemList;
    private String brandName;
    private ImageView bigStickFigureImage;
    private ImageView pointImage;
    private int currentPoint = -1;
    private int lastPoint;
    private byte[] photodata;
    private CameraModel mCameraModel;
    private int[] pointsstates;
    private RelativeLayout pointResultDialog;
    private Button clicktorestartButton;
    private TextView resultText;
    private String[] imagePaths;


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

    private void bindView() {
        cameraSurfaceView = findViewById(R.id.cameraSurfaceView);
        cameraFocusView = findViewById(R.id.cameraFocusView);
        backLayout = findViewById(R.id.back_layout);
        gotoAppraisalText = findViewById(R.id.gotoAppraisal);
        pointsRecyclerView = findViewById(R.id.point_recyclerview);
        seeCaseButton = findViewById(R.id.seecase_button);
        usePhotoButton = findViewById(R.id.usephoto_button);
        bigStickFigureImage = findViewById(R.id.bigstickfigure_image);
        pointImage = findViewById(R.id.point_image);
        takePhotoButton = findViewById(R.id.takephoto_button);
        clicktorestartButton = findViewById(R.id.restart_button);
        pointResultDialog = findViewById(R.id.result_dialog);
        resultText = findViewById(R.id.resulttext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        pointsRecyclerView.setLayoutManager(linearLayoutManager);
        cameraFocusView.setmIAutoFocus(this);
        backLayout.setOnClickListener(this);
        seeCaseButton.setOnClickListener(this);
        takePhotoButton.setOnClickListener(this);
        clicktorestartButton.setOnClickListener(this);
        usePhotoButton.setOnClickListener(this);
    }

    private void initView() {

    }

    private void bindData() {
        mCameraModel = new CameraModel(this);
        Bundle bundle = getIntent().getExtras();
        brandName = bundle.getString("brandName");
        appraisalPointItemList = (List<AppraisalPointItem>) bundle.getSerializable("points");
        pointsstates = new int[appraisalPointItemList.size()];
        imagePaths = new String[appraisalPointItemList.size()];
        StickFigureAdapter stickFigureAdapter = new StickFigureAdapter(appraisalPointItemList);
        stickFigureAdapter.setItemClick(this);
        pointsRecyclerView.setAdapter(stickFigureAdapter);
        pointsRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this);

    }


    @Override
    public void autoFocus(float x, float y) {
        if(pointsstates[currentPoint]==Constants.WAITING){
            cameraSurfaceView.setAutoFocus((int) x, (int) y);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.seecase_button:
                seeCase();
                break;
            case R.id.takephoto_button:
                savePhoto();
                break;
            case R.id.restart_button:
                restartPoint();
                break;
            case R.id.usephoto_button:
                ToastUtils.showShort("yes");
                break;
        }
    }

    private void restartPoint() {
        pointsstates[currentPoint] = Constants.WAITING;
        stateChange(currentPoint);
    }

    private void singlepointAppraisal(final String path, final int threadpoint) {
        String base64 = PicProcessUtils.convertIconToString(PicProcessUtils.getCompressBm(path));
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("point_key", appraisalPointItemList.get(currentPoint).getKey());
        jsonObject.addProperty("img_base64", base64);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        Callback.MyOkhttp(requestBody, NetInterface.TSSingleAppraisalRequestV2, new Callback.OkhttpRun() {
            @Override
            public void run(JSONObject jsonObject) {
                try {
                    if (jsonObject.getJSONObject("err").getInt("code") == 0) {
                        pointsstates[threadpoint] = Constants.SUCCESS;
                    } else {
                        pointsstates[threadpoint] = Constants.FAILURE;
                    }
                    if (currentPoint == threadpoint) {
                        ToastUtils.showShort("current" + currentPoint + "threadpoint" + threadpoint);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                stateChange(threadpoint);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                changeOnlystate(threadpoint);
                            }
                        });
                    }
                } catch (JSONException j) {
                    j.printStackTrace();
                }

            }
        });
    }

    private void savePhoto() {
        final int threadpoint;
        threadpoint = currentPoint;
        pointsstates[currentPoint] = Constants.DETECTING;
        stateChange(currentPoint);
        cameraSurfaceView.takePicture(new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                camera.startPreview();
                photodata = data;
                Observable.just(data)
                        .map(new Func1<byte[], String>() {
                            @Override
                            public String call(byte[] bytes) {
                                String path = mCameraModel.handlePhoto(bytes, cameraSurfaceView.getCameraId());
                                return path;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String path) {
                                try {
                                    imagePaths[threadpoint] = path;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            cameraFocusView.setBackground(new BitmapDrawable(BitmapUtil.getBitmap(imagePaths[threadpoint])));
                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                singlepointAppraisal(path, threadpoint);
                            }
                        });
            }
        });
    }

    private void seeCase() {
        final AppraisalPointDialog appraisalPointDialog = new AppraisalPointDialog(this);
        TextView pointName = appraisalPointDialog.findViewById(R.id.pointName);
        TextView pointcontent = appraisalPointDialog.findViewById(R.id.pointcontent);
        ImageView pointImage = appraisalPointDialog.findViewById(R.id.pointImage);
        Button button = appraisalPointDialog.findViewById(R.id.confirm_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appraisalPointDialog.dismiss();
            }
        });
        pointName.setText(appraisalPointItemList.get(currentPoint).getPointtext());
        pointcontent.setText(appraisalPointItemList.get(currentPoint).getPointcontent());
        Glide.with(this).load(appraisalPointItemList.get(currentPoint).getPointimageUrl()).fitCenter().into(pointImage);
        appraisalPointDialog.show();
    }

    @Override
    public void onItemClick(int position, StickFigureAdapter.ViewHolder viewHolder) {
        lastPoint = currentPoint;
        currentPoint = position;
        if (lastPoint >= 0) {
            getRecyclerItemView(lastPoint, R.id.selectstate).setVisibility(View.INVISIBLE);
        }
        getRecyclerItemView(currentPoint, R.id.selectstate).setVisibility(View.VISIBLE);
        Glide.with(this).load(appraisalPointItemList.get(position).getBigStickFigureURL()).fitCenter().into(bigStickFigureImage);
        Glide.with(this).load(appraisalPointItemList.get(position).getPointimageUrl()).fitCenter().into(pointImage);
        stateChange(currentPoint);
    }

    private void stateChange(int position) {
        getRecyclerItemView(position, R.id.stickFigure).setVisibility(View.INVISIBLE);
        getRecyclerItemView(position, R.id.stateImage).setVisibility(View.VISIBLE);
        cameraFocusView.setBackground(new BitmapDrawable(BitmapUtil.getBitmap(imagePaths[position])));
        setButtonUnclickable();
        switch (pointsstates[position]) {
            case Constants.WAITING:
                pointResultDialog.setVisibility(View.GONE);
                clicktorestartButton.setVisibility(View.GONE);
                getRecyclerItemView(position, R.id.stickFigure).setVisibility(View.VISIBLE);
                getRecyclerItemView(position, R.id.stateImage).setVisibility(View.INVISIBLE);
                setButtonClickable();
                cameraFocusView.setBackground(null);
                break;
            case Constants.DETECTING:
                pointResultDialog.setVisibility(View.VISIBLE);
                clicktorestartButton.setVisibility(View.GONE);
                resultText.setText(getText(R.string.isdetecting));
                Glide.with(this).load(R.drawable.waiting).fitCenter().into((ImageView) getRecyclerItemView(position, R.id.stateImage));
                break;
            case Constants.FAILURE:
                pointResultDialog.setVisibility(View.VISIBLE);
                clicktorestartButton.setVisibility(View.VISIBLE);
                resultText.setText(getText(R.string.pointinvalid));
                Glide.with(this).load(R.drawable.refresh).fitCenter().into((ImageView) getRecyclerItemView(position, R.id.stateImage));
                break;
            case Constants.SUCCESS:
                pointResultDialog.setVisibility(View.VISIBLE);
                clicktorestartButton.setVisibility(View.VISIBLE);
                resultText.setText(getText(R.string.pointsuccess));
                Glide.with(this).load(R.drawable.tick).fitCenter().into((ImageView) getRecyclerItemView(position, R.id.stateImage));
                break;
        }
    }

    private void changeOnlystate(int position) {
        switch (pointsstates[position]) {
            case Constants.FAILURE:
                Glide.with(this).load(R.drawable.refresh).fitCenter().into((ImageView) getRecyclerItemView(position, R.id.stateImage));
                break;
            case Constants.SUCCESS:
                Glide.with(this).load(R.drawable.tick).fitCenter().into((ImageView) getRecyclerItemView(position, R.id.stateImage));
                break;
        }
    }

    private void setButtonUnclickable(){
        usePhotoButton.setTextColor(getColor(R.color.grey));
        usePhotoButton.setClickable(false);
    }
    private void setButtonClickable(){
        usePhotoButton.setTextColor(getColor(R.color.white));
        usePhotoButton.setClickable(true);
    }

    private View getRecyclerItemView(int position, int id) {
        return pointsRecyclerView.getChildAt(position).findViewById(id);
    }

    @Override
    public void onGlobalLayout() {
        if (currentPoint == -1)
            pointsRecyclerView.getChildAt(0).performClick();
    }
}
