package com.example.baoxiaojianapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.baoxiaojianapp.R;
import com.example.baoxiaojianapp.Utils.Constants;
import com.example.baoxiaojianapp.Utils.PicProcessUtils;
import com.example.baoxiaojianapp.Utils.ShareUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener, WbShareCallback {

    private RelativeLayout rateLayout;
    private RelativeLayout corporateInfoLayout;
    private Button shareButton;
    private RelativeLayout backLayout;
    private ImageView iconImageview;
    private TextView userProtocalText;
    private TextView privacyPolicyText;
    private BottomSheetDialog bottomSheetDialog;
    private LinearLayout weiboLayout;
    private LinearLayout weixinLayout;
    private LinearLayout friendCircleLayout;

    //weixin
    private IWXAPI iwxapi;
    private static final int WEIXIN=0;
    private static final int TIMELINE=1;

    //微博
    private WbShareHandler wbShareHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        bindView();
        initBottomView();
        regToWx();
    }

    private void regToWx(){
        iwxapi= WXAPIFactory.createWXAPI(this,Constants.APP_ID,true);
        iwxapi.registerApp(Constants.APP_ID);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                iwxapi.registerApp(Constants.APP_ID);
            }
        },new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));
    }
    private void bindView(){
        rateLayout=findViewById(R.id.RateLayout);
        shareButton=findViewById(R.id.share_button);
        backLayout=findViewById(R.id.back_layout);
        corporateInfoLayout=findViewById(R.id.corporateInfoLayout);
        iconImageview=findViewById(R.id.icon);
        userProtocalText=findViewById(R.id.userprotocal_textview);
        privacyPolicyText=findViewById(R.id.privacypolicy_textview);
        rateLayout.setOnClickListener(this);
        shareButton.setOnClickListener(this);
        corporateInfoLayout.setOnClickListener(this);
        backLayout.setOnClickListener(this);
        userProtocalText.setOnClickListener(this);
        privacyPolicyText.setOnClickListener(this);
        userProtocalText.setOnClickListener(this);

    }

    private void initBottomView(){

        View bottomview=View.inflate(this,R.layout.share_bottomsheet_layout,null);
        bottomSheetDialog=new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomview);
        weiboLayout=bottomview.findViewById(R.id.weibo_button);
        weixinLayout=bottomview.findViewById(R.id.weixin_button);
        friendCircleLayout=bottomview.findViewById(R.id.friendcircle_button);
        weiboLayout.setOnClickListener(this);
        weixinLayout.setOnClickListener(this);
        friendCircleLayout.setOnClickListener(this);

        RequestOptions options = new RequestOptions().bitmapTransform(new RoundedCorners(30));//图片圆角为30
        Glide.with(this).load(R.drawable.icon).apply(options).into(iconImageview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                finish();
                break;
            case R.id.share_button:
                showShareBottom();
                break;
            case R.id.RateLayout:
                rateus();
                break;
            case R.id.corporateInfoLayout:
                startActivity(new Intent(this,CorporationInfoActivity.class));
                break;
            case R.id.userprotocal_textview:
                startActivity(new Intent(this,UserProtacalActivity.class));
                break;
            case R.id.privacypolicy_textview:
                startActivity(new Intent(this,PrivatePolicyActivity.class));
                break;
            case R.id.weixin_button:
                shareToWX(WEIXIN);
                bottomSheetDialog.dismiss();
                break;
            case R.id.weibo_button:
                shareToWB();
                bottomSheetDialog.dismiss();
                break;
            case R.id.friendcircle_button:
                shareToWX(TIMELINE);
                bottomSheetDialog.dismiss();
                break;
        }
    }
    private void shareToWX(int type){
        WXWebpageObject webpageObject=new WXWebpageObject();
        webpageObject.webpageUrl=Constants.shareLink;

        WXMediaMessage wxMediaMessage=new WXMediaMessage(webpageObject);
        wxMediaMessage.title="AI鉴定，秒知真假";
        //wxMediaMessage.description=null;
        Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.icon40);
        wxMediaMessage.thumbData= PicProcessUtils.bmpToByteArray(bitmap,true);

        SendMessageToWX.Req req=new SendMessageToWX.Req();
        req.transaction=PicProcessUtils.buildTransaction("webpage");
        req.message=wxMediaMessage;
        if (type==WEIXIN){
            req.scene=SendMessageToWX.Req.WXSceneSession;
        }else {
            req.scene=SendMessageToWX.Req.WXSceneTimeline;
        }
        iwxapi.sendReq(req);
    }
    private void shareToWB(){
        try {
            WbSdk.install(this,new AuthInfo(this, Constants.APP_KEY,Constants.REDIRECT_URL,Constants.SCOPE));
            wbShareHandler=new WbShareHandler(this);
            wbShareHandler.registerApp();
            WeiboMultiMessage weiboMultiMessage=new WeiboMultiMessage();
            weiboMultiMessage.textObject= ShareUtils.getTextObj();
//            weiboMultiMessage.imageObject=getImageObj();
            weiboMultiMessage.mediaObject=ShareUtils.getWebpageObj();
            wbShareHandler.shareMessage(weiboMultiMessage,true);
        }catch (UnsatisfiedLinkError e){
            e.printStackTrace();
            ToastUtils.showShort("请安装微信客户端");
        }catch(NoClassDefFoundError e){
            e.printStackTrace();
            ToastUtils.showShort("请安装微信客户端");
        }
    }

    private void showShareBottom(){
        bottomSheetDialog.show();
    }

    private void rateus(){
        try {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.tencent.android.qqdownloader");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShort("请安装应用宝最新版本");
        }

    }





    @Override
    public void onWbShareSuccess() {
        ToastUtils.showShort("分享成功");
        bottomSheetDialog.dismiss();
    }

    @Override
    public void onWbShareCancel() {
        ToastUtils.showShort("取消分享");
    }

    @Override
    public void onWbShareFail() {
        ToastUtils.showShort("分享失败");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        wbShareHandler.doResultIntent(intent,this);
    }
}
