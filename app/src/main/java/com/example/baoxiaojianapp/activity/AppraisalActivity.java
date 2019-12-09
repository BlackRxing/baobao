
package com.example.baoxiaojianapp.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.Callback.Callback;
import com.example.baoxiaojianapp.Utils.BitmapUtil;
import com.example.baoxiaojianapp.Utils.CameraModel;
import com.example.baoxiaojianapp.Utils.Constants;
import com.example.baoxiaojianapp.Utils.FileUtil;
import com.example.baoxiaojianapp.Utils.MyApplication;
import com.example.baoxiaojianapp.Utils.NetInterface;
import com.example.baoxiaojianapp.Utils.PathUtils;
import com.example.baoxiaojianapp.Utils.PicProcessUtils;
import com.example.baoxiaojianapp.adapter.StickFigureAdapter;
import com.example.baoxiaojianapp.classpakage.AppraisalPointItem;
import com.example.baoxiaojianapp.view.AppraisalPointDialog;
import com.example.baoxiaojianapp.view.CameraFocusView;
import com.example.baoxiaojianapp.view.CameraSurfaceView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import static java.util.Collections.sort;

@SuppressWarnings("deprecation")
public class AppraisalActivity extends TakePhotoActivity implements CameraFocusView.IAutoFocus, View.OnClickListener, StickFigureAdapter.StickFigureAdaterClick, ViewTreeObserver.OnGlobalLayoutListener {

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
    private ImageView staticImage;
    private String[] uuids;
    private StickFigureAdapter stickFigureAdapter;

    private long mLastClickTime=0;
    public static final long TIME_INTERVAL = 2000L;
    public static Context mcontext;


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
        staticImage = findViewById(R.id.staticImage);
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
        gotoAppraisalText.setOnClickListener(this);

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
        uuids = new String[appraisalPointItemList.size()];
        stickFigureAdapter= new StickFigureAdapter(appraisalPointItemList);
        stickFigureAdapter.setItemClick(this);
        pointsRecyclerView.setAdapter(stickFigureAdapter);
        pointsRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }


    @Override
    public void autoFocus(float x, float y) {
        if (pointsstates[currentPoint] == Constants.WAITING) {
            cameraSurfaceView.setAutoFocus((int) x, (int) y);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
                FileUtil.deleteFolderFile(PathUtils.getFilePath(this, "appraisal"),false);
                finish();
                break;
            case R.id.seecase_button:
                seeCase();
                break;
            case R.id.takephoto_button:
                if(NetworkUtils.isConnected()){
                    savePhoto();
                }else {
                    ToastUtils.showShort(Callback.CHECK_NET_CONNECT);
                }
                break;
            case R.id.restart_button:
                restartPoint();
                break;
            case R.id.usephoto_button:
                if(NetworkUtils.isConnected()){
                    choosePhotoFormGallary();
                }else{
                    ToastUtils.showShort(Callback.CHECK_NET_CONNECT);
                }
                break;
            case R.id.gotoAppraisal:
                long nowTime=System.currentTimeMillis();
                if (nowTime-mLastClickTime>TIME_INTERVAL){
                    gotoAppraisal();
                    mLastClickTime=nowTime;
                }else{
                    mLastClickTime=nowTime;
                }
                break;

        }
    }

    private void gotoAppraisal() {
        int i = 0;
        JsonArray jsonArray = new JsonArray();
        for (int state : pointsstates) {
            if (state == Constants.DETECTING) {
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setMessage(getText(R.string.appraisalerrorcase_2)).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();
                alertDialog.show();
                return;
            }

            if (appraisalPointItemList.get(i).getType() == 1 && state == Constants.FAILURE) {
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setMessage(getText(R.string.appraisalerrorcase_3)).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();
                alertDialog.show();
                return;
            }
            if (appraisalPointItemList.get(i).getType() == 1 && state != Constants.SUCCESS) {
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setMessage(getText(R.string.appraisalerrorcase_1)).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();
                alertDialog.show();
                return;
            }
            if (state == Constants.SUCCESS) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("appraisalUUID", uuids[i]);
                jsonObject.addProperty("pointKey", appraisalPointItemList.get(i).getKey());
                jsonArray.add(jsonObject);
            }
            i++;
        }
        AppraisalAll(jsonArray);
    }

    private void AppraisalAll(final JsonArray jsonArray) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("points", jsonArray);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        Callback.MyOkhttp(requestBody, NetInterface.TSAppraisalResultV4Request, new Callback.OkhttpRun() {
            @Override
            public void run(JSONObject jsonObject) {
                try {
                    JSONObject jsonObject1= jsonObject.getJSONObject("result");
                    Log.d("here",jsonObject.toString());
                    Intent intent=new Intent(MyApplication.getContext(),AppraisalResultActivity.class);
                    intent.putExtra("imageUrl",jsonObject1.getString("imageUrl"));
                    intent.putExtra("brandName",jsonObject1.getString("brandName"));
                    intent.putExtra("modelNumber",jsonObject1.getString("modelNumber"));
                    intent.putExtra("timestamp",jsonObject1.getString("timestamp"));
                    intent.putExtra("type",jsonObject1.getInt("type"));
                    intent.putExtra("detailModels",jsonObject1.getJSONArray("detailModels").toString());
                    startActivity(intent);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            ToastUtils.showShort(result + "");
//                        }
//                    });
                } catch (JSONException j) {
                    j.printStackTrace();
                }
            }
        });
    }



    private void choosePhotoFormGallary() {
        TakePhoto takePhoto = getTakePhoto();
        File file = new File(PathUtils.getFilePath(this, "appraisal"), System.currentTimeMillis() + "_gallary" + ".jpg");
     //   configCompress(takePhoto);
        takePhoto.onPickFromGalleryWithCrop(Uri.fromFile(file), new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create());
    }

    private void configCompress(TakePhoto takePhoto) {//压缩配置
        int maxSize = Integer.parseInt("3000000");//最大 压缩B
        int width = Integer.parseInt("500");//宽
        int height = Integer.parseInt("500");//高
        CompressConfig config;
        config = new CompressConfig.Builder().setMaxSize(maxSize)
         //       .setMaxPixel(width >= height ? width : height)
                .create();
        takePhoto.onEnableCompress(config, true);//是否显示进度
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2){
            //判断安卓版本
            if (resultCode == RESULT_OK&&data!=null){
                if (Build.VERSION.SDK_INT>=19)
                    handImage(data);
            }
        }

    }

    private void handImage(Intent data){
        String path =null;
        Uri uri = data.getData();
        //根据不同的uri进行不同的解析
        if (DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+"="+id;
                path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                path = getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            path = getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            path = uri.getPath();
        }
        //展示图片
        pointsstates[currentPoint] = Constants.DETECTING;
        stateChange(currentPoint);
        String filePath = path;
       // filePath = BitmapUtil.myrotate(filePath);  //修正个别（小米三星）系统竖排图片旋转
        imagePaths[currentPoint] = filePath;
        staticImage.setImageDrawable(new BitmapDrawable(BitmapUtil.getBitmap(imagePaths[currentPoint])));
        final int threadpoint = currentPoint;
        singlepointAppraisal(imagePaths[currentPoint], threadpoint);
    }

    //content类型的uri获取图片路径的方法
    private String getImagePath(Uri uri,String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }



    private void restartPoint() {
        pointsstates[currentPoint] = Constants.WAITING;
        stateChange(currentPoint);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        pointsstates[currentPoint] = Constants.DETECTING;
        stateChange(currentPoint);
        final String filePath = result.getImage().getOriginalPath() ;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String newfilePath=filePath;
                newfilePath = BitmapUtil.myrotate(newfilePath);  //修正个别（小米三星）系统竖排图片旋转
                imagePaths[currentPoint] = newfilePath;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        staticImage.setImageDrawable(new BitmapDrawable(BitmapUtil.getBitmap(imagePaths[currentPoint])));
                    }
                });
                final int threadpoint = currentPoint;
                singlepointAppraisal(imagePaths[currentPoint], threadpoint);
            }
        }).start();
    }

    private void singlepointAppraisal(final String path, final int threadpoint) {
        final long starttime = System.currentTimeMillis();
        String base64 = PicProcessUtils.convertIconToString(PicProcessUtils.getCompressBm(path));
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("point_key", appraisalPointItemList.get(currentPoint).getKey());
        jsonObject.addProperty("img_base64", base64);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        long one = System.currentTimeMillis();
//        ToastUtils.showShort("one" + (one - starttime) + "ms");
        Callback.MyOkhttp(requestBody, NetInterface.TSSingleAppraisalRequestV2, new Callback.OkhttpRun() {
            @Override
            public void run(JSONObject jsonObject) {
                try {
                  //  Log.d("wrong",jsonObject.toString());
                    if (jsonObject.getJSONObject("err").getInt("code") == 0) {
                        pointsstates[threadpoint] = Constants.SUCCESS;
                        uuids[threadpoint] = jsonObject.getString("uuid");
                    } else {
                        pointsstates[threadpoint] = Constants.FAILURE;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showShort(Callback.CONNECT_ERROR);
                    pointsstates[threadpoint] = Constants.FAILURE;
                }
                if (currentPoint == threadpoint) {
//                        ToastUtils.showShort("current" + currentPoint + "threadpoint" + threadpoint);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            stateChange(threadpoint);
//                                long sed = System.currentTimeMillis() - starttime;
//                                ToastUtils.showShort("sed" + sed);
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
                                            staticImage.setImageDrawable(new BitmapDrawable(BitmapUtil.getBitmap(imagePaths[threadpoint])));
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
        appraisalPointDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
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
            Log.d("hererer","cur"+currentPoint+"last"+lastPoint+"position"+position);
            if (lastPoint >= 0) {
                getRecyclerItemView(lastPoint, R.id.selectstate).setVisibility(View.INVISIBLE);
            }
            getRecyclerItemView(currentPoint, R.id.selectstate).setVisibility(View.VISIBLE);
            Glide.with(this).load(appraisalPointItemList.get(position).getBigStickFigureURL()).into(bigStickFigureImage);
            Glide.with(this).load(appraisalPointItemList.get(position).getPointimageUrl()).fitCenter().into(pointImage);
            stateChange(currentPoint);

    }

    private void stateChange(int position) {
        getRecyclerItemView(position, R.id.stickFigure).setVisibility(View.INVISIBLE);
        getRecyclerItemView(position, R.id.stateImage).setVisibility(View.VISIBLE);
        staticImage.setImageDrawable(new BitmapDrawable(BitmapUtil.getBitmap(imagePaths[position])));
        setButtonUnclickable();
        switch (pointsstates[position]) {
            case Constants.WAITING:
                pointResultDialog.setVisibility(View.GONE);
                clicktorestartButton.setVisibility(View.GONE);
                getRecyclerItemView(position, R.id.stickFigure).setVisibility(View.VISIBLE);
                getRecyclerItemView(position, R.id.stateImage).setVisibility(View.INVISIBLE);
                setButtonClickable();
                staticImage.setImageDrawable(null);
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



    private void setButtonUnclickable() {
        usePhotoButton.setTextColor(getColor(R.color.grey));
        usePhotoButton.setClickable(false);
        takePhotoButton.setClickable(false);
    }

    private void setButtonClickable() {
        usePhotoButton.setTextColor(getColor(R.color.white));
        usePhotoButton.setClickable(true);
        takePhotoButton.setClickable(true);
    }

    private View getRecyclerItemView(int position, int id) {
//        Log.d("why",""+position);pointsRecyclerView.
//        return pointsRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(id);
                return stickFigureAdapter.holders.get(position).itemView.findViewById(id);

    }

    @Override
    public void onGlobalLayout() {
        if (currentPoint == -1)
            pointsRecyclerView.getChildAt(0).performClick();
    }


}
