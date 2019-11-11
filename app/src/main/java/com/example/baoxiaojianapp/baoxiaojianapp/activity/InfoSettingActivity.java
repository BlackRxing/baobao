package com.example.baoxiaojianapp.baoxiaojianapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.actionsheetdialog.ActionSheetDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.baoxiaojianapp.Callback.NetResquest;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.MyApplication;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.PathUtils;

import com.example.baoxiaojianapp.baoxiaojianapp.Utils.PicProcessUtils;
import com.example.baoxiaojianapp.baoxiaojianapp.Utils.UserInfoCashUtils;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;

import com.jph.takephoto.model.TException;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.smarttop.library.bean.City;
import com.smarttop.library.bean.County;
import com.smarttop.library.bean.Province;
import com.smarttop.library.bean.Street;
import com.smarttop.library.widget.AddressSelector;
import com.smarttop.library.widget.BottomDialog;
import com.smarttop.library.widget.OnAddressSelectedListener;

import java.io.File;


public class InfoSettingActivity extends TakePhotoActivity implements View.OnClickListener,OnAddressSelectedListener,AddressSelector.OnDialogCloseListener {

    private RelativeLayout backLayout;
    private RelativeLayout profileImageLayout;
    private RelativeLayout nickNameLayout;
    private RelativeLayout regionLayout;
    private RelativeLayout sexLayout;
    private static CircleImageView profileImage;
    private static TextView nicknameText;
    private static TextView regionText;
    private static TextView sexText;
    private BottomDialog bottomDialog;
    private LinearLayout mContent;
    private ActionSheetDialog sexActionSheetDialog;
    private ActionSheetDialog profileImageActionSheetDialog;
    private Uri photoOutputUri;
    private File file;
    private Uri corpedimage;
    private Boolean takePhotostate=false;

    public static final int TAKE_PHOTO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_setting);
        mContent=findViewById(R.id.container_layout);
        bindView();
        initData();
    }



    private void initData(){
        String nickname=UserInfoCashUtils.getUserInfo("nick_name");
        String location=UserInfoCashUtils.getUserInfo("location");
        String sex=UserInfoCashUtils.getUserInfo("sex");
        nicknameText.setText(nickname);
        Log.i("locaiton",location);
        if(!location.equals("null"))
            regionText.setText(location);
        if ((sex.equals("m"))) {
            sexText.setText("男");
        } else {
            sexText.setText("女");
        }
        Glide.with(this).load(UserInfoCashUtils.getUserInfo("avatar_url")).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                profileImage.setImageDrawable(resource);
            }
        });
    }

    private void bindView(){
        backLayout=findViewById(R.id.back_layout);
        profileImageLayout=findViewById(R.id.profile_image_layout);
        nickNameLayout=findViewById(R.id.nickname_layout);
        regionLayout=findViewById(R.id.region_layout);
        sexLayout=findViewById(R.id.sex_layout);
        profileImage=findViewById(R.id.profile_image);
        nicknameText=findViewById(R.id.nickname_textview);
        regionText=findViewById(R.id.region_textview);
        sexText=findViewById(R.id.sex_textview);
        nickNameLayout.setOnClickListener(this);
        profileImageLayout.setOnClickListener(this);
        regionLayout.setOnClickListener(this);
        sexLayout.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                break;
//            case R.id.profile_image_layout:
//                chooseProfileImage();
//                break;
            case R.id.nickname_layout:
                startActivity(new Intent(this,EditNickNameActivity.class));
                break;
            case R.id.region_layout:
                selectRegion();
                break;
            case R.id.sex_layout:
                sexChoose();
                break;
            case R.id.profile_image_layout:
                chooseProfileImage();
                break;
        }
    }







    private void selectRegion(){
        bottomDialog= new BottomDialog(this);
        bottomDialog.setOnAddressSelectedListener(this);
        bottomDialog.setDialogDismisListener(this);
        bottomDialog.setTextSelectedColor(R.color.black);
        bottomDialog.setTextUnSelectedColor(R.color.black);
        bottomDialog.show();
    }

    private void chooseProfileImage(){
        profileImageActionSheetDialog = new ActionSheetDialog.ActionSheetBuilder(this,R.style.ActionSheetDialogBase_SampleStyle)
                .setItems(new CharSequence[]{"从相册选取","拍照"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                FromGallery(getTakePhoto());
                                profileImageActionSheetDialog.dismiss();
                                break;
                            case 1:
                                FromCamera(getTakePhoto());
                                profileImageActionSheetDialog.dismiss();
                                break;
                        }
                    }
                })
                .setCancelable(true)
                .create();
        profileImageActionSheetDialog.show();
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        if (takePhotostate){
            Glide.with(MyApplication.getContext()).load(corpedimage).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    profileImage.setImageDrawable(resource);
                }
            });
            upLoadAvatar(corpedimage.getPath());
            takePhotostate=false;
        }else{
            Glide.with(MyApplication.getContext()).load(result.getImage().getCompressPath()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    profileImage.setImageDrawable(resource);
                }
            });
            upLoadAvatar(result.getImage().getCompressPath());
        }
    }

    private void upLoadAvatar(String avatar_path){
        String avtar_base64=PicProcessUtils.convertIconToString(PicProcessUtils.getCompressBm(avatar_path));
        NetResquest.RevisePersonInfo("avatar",avtar_base64);
    }

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        String region = (province == null ? "" : province.name) + (city == null ? "" : city.name) + (county == null ? "" : county.name) +
                (street == null ? "" : street.name);
        if (bottomDialog!=null){
            bottomDialog.dismiss();
        }
        NetResquest.RevisePersonInfo("location",region);
    }

    @Override
    public void dialogclose() {
        if(bottomDialog!=null){
            bottomDialog.dismiss();
        }
    }

    private void sexChoose(){
        sexActionSheetDialog=new ActionSheetDialog.ActionSheetBuilder(this,R.style.ActionSheetDialogBase_SampleStyle)
                .setItems(new CharSequence[]{"男","女"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sexvalue=(which==0?"m":"f");
                        NetResquest.RevisePersonInfo(NetResquest.SEX,sexvalue);
                        sexActionSheetDialog.dismiss();
                    }
                })
                .setCancelable(true)
                .create();
        sexActionSheetDialog.show();

    }

    public void FromCamera(TakePhoto takePhoto){
        file = new File(PathUtils.getFilePath(this,"temp"), System.currentTimeMillis() + ".jpg");
        if (Build.VERSION.SDK_INT >= 24) {
            Log.i("path",file.toString());
            photoOutputUri = FileProvider.getUriForFile(this,"com.example.baoxiaojianapp.filesProvider",file);
        } else {
            //将File对象转换成URI对象，这个URI对象标识着output_image.jpg这张图片的本地真是路径
            photoOutputUri = Uri.fromFile(file);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoOutputUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    public void FromGallery(TakePhoto takePhoto) {
        configCompress(takePhoto);
        File file = new File(PathUtils.getFilePath(this,"temp"), System.currentTimeMillis() + ".jpg");
        takePhoto.onPickFromGalleryWithCrop(Uri.fromFile(file),new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create());
    }

    private void configCompress(TakePhoto takePhoto) {//压缩配置
        int maxSize = Integer.parseInt("11409600");//最大 压缩B
        int width = Integer.parseInt("500");//宽
        int height = Integer.parseInt("500");//高
        CompressConfig config;
        config = new CompressConfig.Builder().setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .create();
        takePhoto.onEnableCompress(config, false);//是否显示进度
    }

    private void cropAndCompressCameraPhoto(){
        TakePhoto takePhoto=getTakePhoto();
        corpedimage=Uri.fromFile(new File(PathUtils.getFilePath(this,"temp"),System.currentTimeMillis() + ".jpg"));
        try{
            takePhoto.onCrop(Uri.fromFile(file),corpedimage,new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create());
            takePhotostate=true;
        }catch (TException t){
            t.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                cropAndCompressCameraPhoto();
                break;
            default:
                break;
        }
    }

    public static void changeUI(String key){
        switch (key){
            case NetResquest.NICK_NAME:
                nicknameText.setText(UserInfoCashUtils.getUserInfo("nick_name"));
                break;
            case NetResquest.SEX:
                sexText.setText(UserInfoCashUtils.getUserInfo("sex")=="m"?"男":"女");
                break;
            case NetResquest.LOCATION:
                regionText.setText(UserInfoCashUtils.getUserInfo("location"));
                break;
            case NetResquest.AVATAR:
                break;

        }
    }
}
