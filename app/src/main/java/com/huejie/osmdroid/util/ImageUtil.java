package com.huejie.osmdroid.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * 图片处理工具类
 * Created by 王浩 on 2017/5/2.
 *
 */
public class ImageUtil {
    public static void saveImage(File file, byte []data, String filePath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap tempBitmap = BitmapFactory.decodeFile(filePath,options);
        int degrees = getExifRotateDegree(filePath);
    }

    public static int getExifRotateDegree(String path){
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degrees = getExifRotateDegrees(orientation);
            Log.d("imageutil degrees",degrees+"");
            return degrees;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getExifRotateDegrees(int exifOrientation) {
        int degrees = 0;
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                degrees = 0;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                degrees = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degrees = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degrees = 270;
                break;
        }
        return degrees;
    }

    /**
     *Bitmap旋转
     */
    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree){
        Matrix matrix = new Matrix();
        matrix.postRotate((float)rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
        return rotaBitmap;
    }
}
