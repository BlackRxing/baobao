package com.example.baoxiaojianapp.Utils;

public interface Constants {

    public static final String  shareLink="https://www.jianshu.com/p/eb570935d586";


    //weibo
    /** 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY */
    public static final String APP_KEY = "1373673421";

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     */
    public static final String REDIRECT_URL = "http://www.sina.com";

    /**
     * WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。
     * 详情请查看 Demo 中对应的注释。
     */
    public static final String SCOPE = "";



    //weixin
    public static final String APP_ID="wx1470dc6b3fb406b6";
    public static final String APP_SECRET="c025b56f9ec1f88975ee7b0b31cbf292";

    public static final int WEIBO_ID=1;
    public static final int WEIXIN_ID=2;

    public static final int WAITING=0;
    public static final int DETECTING=1;
    public static final int FAILURE=2;
    public static final int SUCCESS=3;

    public static final int ENTERPRISE=1;
    public static final int PERSON=0;
    public static final int hasPunch=1;
    public static final int noPunch=0;
    public static final int CODE_SUCCESS=0;

}
