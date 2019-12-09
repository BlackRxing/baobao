package com.example.baoxiaojianapp.Utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import static android.os.Environment.MEDIA_MOUNTED;


////使用手机自带文件管理器对应查看目录为：Android - data - 包名- files - Music -
// mFileName
//         getExternalFilesDir(Environment.DIRECTORY_MUSIC) + File.separator + mFileName
public class PathUtils {
    public static String getFilePath(Context context, String dir) {
        String directoryPath="";
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ) {//判断外部存储是否可用
            directoryPath =context.getExternalFilesDir(dir).getAbsolutePath();
        }else{//没外部存储就使用内部存储
            directoryPath=context.getFilesDir()+File.separator+dir;
        }
        File file = new File(directoryPath);
        return directoryPath;
    }
}
