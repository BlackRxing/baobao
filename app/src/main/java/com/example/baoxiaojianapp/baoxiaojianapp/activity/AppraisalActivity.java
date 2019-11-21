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
import com.example.baoxiaojianapp.baoxiaojianapp.view.CameraSurfaceView;
import com.smarttop.library.utils.LogUtil;

import java.io.IOException;
import java.util.List;

import static java.util.Collections.sort;
@SuppressWarnings("deprecation")
public class AppraisalActivity extends AppCompatActivity {

    private CameraSurfaceView cameraSurfaceView;
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
    }
    private void initView(){

    }



//    private void setCameraParams(int width, int height) {
//        LogUtil.i(TAG, "setCameraParams  width=" + width + "  height=" + height);
//        Camera.Parameters parameters = camera.getParameters();
//        /*************************** 获取摄像头支持的PictureSize列表********************/
//        List<Camera.Size> pictureSizeList = parameters.getSupportedPictureSizes();
////        sort(pictureSizeList);//排序
//        for (Camera.Size size : pictureSizeList) {
//            LogUtil.i(TAG, "摄像头支持的分辨率：" + " size.width=" + size.width + "  size.height=" + size.height);
//        }
////        Camera.Size picSize = getBestSupportedSize(pictureSizeList, ((float) height / width));//从列表中选取合适的分辨率
////        if (null == picSize) {
////            picSize = parameters.getPictureSize();
////        }
////        LogUtil.e(TAG, "我们选择的摄像头分辨率：" + "picSize.width=" + picSize.width + "  picSize.height=" + picSize.height);
////        // 根据选出的PictureSize重新设置SurfaceView大小
////        parameters.setPictureSize(picSize.width, picSize.height);
//        /*************************** 获取摄像头支持的PreviewSize列表********************/
//        List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();
////        sort(previewSizeList);
//        for (Camera.Size size : previewSizeList) {
//            LogUtil.i(TAG, "摄像支持可预览的分辨率：" + " size.width=" + size.width + "  size.height=" + size.height);
//        }
//        Camera.Size preSize = getBestSupportedSize(previewSizeList, ((float) height) / width);
//        if (null != preSize) {
//            LogUtil.e(TAG, "我们选择的预览分辨率：" + "preSize.width=" + preSize.width + "  preSize.height=" + preSize.height);
//            parameters.setPreviewSize(preSize.width, preSize.height);
//        }
//        /*************************** 对焦模式的选择 ********************/
//        if(cameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
//            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);//手动区域自动对焦
//        }
//        //图片质量
//        parameters.setJpegQuality(100); // 设置照片质量
//        parameters.setPreviewFormat(PixelFormat.YCbCr_420_SP); // 预览格式
//        parameters.setPictureFormat(PixelFormat.JPEG); // 相片格式为JPEG，默认为NV21
//        // 关闪光灯
//        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//        // 横竖屏镜头自动调整
//        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
//            camera.setDisplayOrientation(90);
//        } else {
//            camera.setDisplayOrientation(0);
//        }
//        //相机异常监听
//        camera.setErrorCallback(new Camera.ErrorCallback() {
//            @Override
//            public void onError(int error, Camera camera) {
//                String error_str;
//                switch (error) {
//                    case Camera.CAMERA_ERROR_SERVER_DIED: // 摄像头已损坏
//                        error_str = "摄像头已损坏";
//                        break;
//                    case Camera.CAMERA_ERROR_UNKNOWN:
//                        error_str = "摄像头异常，请检查摄像头权限是否应许";
//                        break;
//                    default:
//                        error_str = "摄像头异常，请检查摄像头权限是否应许";
//                        break;
//                }
//                Log.i(TAG, error_str);
//            }
//        });
//        camera.cancelAutoFocus();
//        camera.setParameters(parameters);
//    }

}
