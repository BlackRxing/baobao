package com.example.baoxiaojianapp.baoxiaojianapp.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.baoxiaojianapp.R;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.utils.Utility;

public class ShareUtils {
    //weixin
    /**
     * 创建文本消息对象。
     * @return 文本消息对象。
     */
    public static TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = "AI鉴定，秒知真假";
        //textObject.title = "xxxx";
        //textObject.actionUrl = "http://www.baidu.com";
        return textObject;
    }

    /**
     * 创建图片消息对象。
     * @return 图片消息对象。
     */
    public static ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        Bitmap bitmap = BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.drawable.baoxiaojianfont);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    public static WebpageObject getWebpageObj() {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title ="包小鉴Android";
        mediaObject.description = "下载链接";
        Bitmap  bitmap = BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.drawable.icon);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        //必须是安全连接才显示https开头
        mediaObject.actionUrl = Constants.shareLink;
        mediaObject.defaultText = "Webpage 默认文案";
        return mediaObject;
    }
}
