package com.example.baoxiaojianapp.baoxiaojianapp.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PicProcessUtils {

    /**
     * 图片bitmap转成base64数据
     *
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        String bitString = Base64.encodeToString(appicon, Base64.DEFAULT);
        return bitString;
    }

    /**
     * 将图片base64数据转化为bitmap
     * @param imgBase64
     */
    public Bitmap base64ToPicture(String imgBase64) {
        byte[] decode = Base64.decode(imgBase64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        return bitmap;
    }


    /**
     *
     * 获取指定路径指定大小的图片（500*500）
     *
     * @param filePath
     * @return Bitmap
     * @since 从类的哪一个版本，此方法被添加进来。（可选）
     * @deprecated该方法从类的那一个版本后，已经被其它方法替换。（可选）
     */
    public static Bitmap getCompressBm(String filePath) {
        Bitmap bm = null;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
//         Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, 500, 500);
//         Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(filePath, options);
        return bm;
    }
    /**
     *
     * 获取照片旋转角度
     *
     * @since 从类的哪一个版本，此方法被添加进来。（可选）
     * @deprecated该方法从类的那一个版本后，已经被其它方法替换。（可选）
     */
    public static int getCameraPhotoOrientation(String imagePath) {
        int rotate = 0;
        try {
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            // Log.v(TAG, "Exif orientation: " + orientation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    /**
     *
     * 旋转图片
     *
     * @param bitmap
     *            rotate
     * @return 返回类型
     * @since 从类的哪一个版本，此方法被添加进来。（可选）
     * @deprecated该方法从类的那一个版本后，已经被其它方法替换。（可选）
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    /**
     * @Description 保存图片到指定路径
     * @param bitmap
     *            要保存的图片
     * @param filePath
     *            目标路径
     * @return 是否成功
     */
    @SuppressWarnings("finally")
    public static boolean saveBmpToPath(final Bitmap bitmap, final String filePath) {
        if (bitmap == null || filePath == null) {
            return false;
        }
        boolean result = false; // 默认结果
        File file = new File(filePath);
        OutputStream outputStream = null; // 文件输出流
        try {
            outputStream = new FileOutputStream(file);
            result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    outputStream); // 将图片压缩为JPEG格式写到文件输出流，100是最大的质量程度
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close(); // 关闭输出流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }
}
