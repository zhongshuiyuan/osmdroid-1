package com.huejie.osmdroid.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.ImageView;

import com.huejie.osmdroid.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by renlei
 * DATE: 15-11-5
 * Time: 下午4:57
 * Email: lei.ren@renren-inc.com
 */
public class CameraUtil {
    private Camera mCamera;
    private static CameraUtil mCameraUtil;
    private boolean isPreview;
    private int cameraId = -1; //0表示后置，1表示前置
    private Camera.CameraInfo mCameraInfo = new Camera.CameraInfo();
    public static final int PREVIEW_HAS_STARTED = 110;
    public static final int RECEIVE_FACE_MSG = 111;
    private boolean isBackCamera;
    public static synchronized CameraUtil getInstance() {
        if (mCameraUtil == null) {
            mCameraUtil = new CameraUtil();

        }
        return mCameraUtil;
    }

    private OnSavePhotoeListener mOnSavePhotoeListener;

    public interface OnSavePhotoeListener {
        void onSavePhoto();
    }

    public void setOnSavePhotoeListener(OnSavePhotoeListener onSavePhotoeListener){
        mOnSavePhotoeListener=onSavePhotoeListener;
    }

    /**
     * 打开相机
     * @param cameraId
     */
    public void doOpenCamera(int cameraId) {
        Log.d("renlei", "open camera"+cameraId);
        try {
            this.cameraId = cameraId;
            mCamera = Camera.open(cameraId);
            Camera.getCameraInfo(cameraId, mCameraInfo);///这里的mCamerainfo必须是new出来的，不能是个null
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启预览
     * @param holder
     */
    public void doStartPreview(SurfaceHolder holder) {
//        Log.d("CAmerautil","doStartPreview");
        if (isPreview) {
            mCamera.stopPreview();
        }
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPictureFormat(PixelFormat.JPEG);//设置照片拍摄后的保存格式
            mCamera.setDisplayOrientation(90);//否则方向会有问题
            if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//前置与后置的不一样，这里暂时只设置前置的，后置的可以相应的去设置
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            }
            printSupportPreviewSize(parameters);
            printSupportPictureSize(parameters);
            printSupportFocusMode(parameters);
//            parameters.setPictureSize(parameters.getPreviewSize().width,parameters.getPictureSize().height);
            //设置的这两个size必须时支持的size大小，否则时不可以的，会出现setparameters错误
            parameters.setPreviewSize(1920, 1080);
            parameters.setPictureSize(1600, 1200);    //之前照片分辨率是640  480
            mCamera.setParameters(parameters);
            Camera.Parameters mParams = mCamera.getParameters();
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
            isPreview = true;
        }
    }

    /**
     * 结束预览
     */
    public void doStopPreview() {
        if (isPreview) {
            isPreview = false;
            mCamera.stopPreview();

            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 拍照
     */
    public void doTakePic(Context context, boolean isBackCamera) {
        this.isBackCamera=isBackCamera;
        if (isPreview && mCamera != null) {
            mCamera.takePicture(new ShutCallBackImpl(), null, new PicCallBacKImpl(context));
        }
    }

    /**
     * 拍照时的动作
     * 默认会有咔嚓一声
     */
    private class ShutCallBackImpl implements Camera.ShutterCallback {
        @Override
        public void onShutter() {

        }
    }

    /**
     * 拍照后的最主要的返回
     */
    private class PicCallBacKImpl implements Camera.PictureCallback {

        private Context mContext;

        public PicCallBacKImpl(Context context){
            mContext=context;
        }

        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            isPreview = false;
            new Thread(new Runnable() {
                @Override
                public void run() {

                    Bitmap b = null;
                    if(null != data){
                        b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
                    }
                    //保存图片到sdcard
                    if(null != b)
                    {
                        //设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。
                        //图片竟然不能旋转了，故这里要旋转下   //前置和后置摄像头有区别
                        Bitmap rotaBitmap;
                        if (isBackCamera){
                            rotaBitmap = ImageUtil.getRotateBitmap(b, 90.0f);
                        }else {
                            rotaBitmap = ImageUtil.getRotateBitmap(b, -90.0f);
                        }
                        saveBitmap(mContext,rotaBitmap);
                    }
                }
            }).start();

            mCamera.startPreview();//重新开启预览 ，不然不能继续拍照
            isPreview = true;
        }
    }

    /**
     * 保存Bitmap到sdcard
     *
     * @param context,b
     */
    public void saveBitmap(Context context, Bitmap b) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path=getSDPath(context);
            String jpegName = path + "/" + getTime() + ".jpg";
            System.out.println("saveBitmap:jpegName = " + jpegName);
            try {
                FileOutputStream fout = new FileOutputStream(jpegName);
                BufferedOutputStream bos = new BufferedOutputStream(fout);
                b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
                System.out.println("saveBitmap成功");
                Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                scanIntent.setData(Uri.fromFile(new File(jpegName)));
                context.sendBroadcast(scanIntent);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("saveBitmap:失败");
                e.printStackTrace();
            }
            mOnSavePhotoeListener.onSavePhoto();
        }
    }

    private String getTime() {
        SimpleDateFormat formatter=new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate=new Date(System.currentTimeMillis());
        String str=formatter.format(curDate);
        return str;
    }

    public String getSDPath(Context context) {

        File sdDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Sdfams/Temp/");
        try {
            if (!sdDir.exists()) {
                sdDir.mkdirs();
            }
        } catch (Exception e) {

        }
        return sdDir.getPath();

    }


    /**
     * 打印支持的previewSizes
     *
     * @param params
     */
    public void printSupportPreviewSize(Camera.Parameters params) {
        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        for (int i = 0; i < previewSizes.size(); i++) {
            Camera.Size size = previewSizes.get(i);
            Log.i("camerautil", "previewSizes:width = " + size.width + " height = " + size.height);
        }

    }

    /**
     * 打印支持的pictureSizes
     *
     * @param params
     */
    public void printSupportPictureSize(Camera.Parameters params) {
        List<Camera.Size> pictureSizes = params.getSupportedPictureSizes();
        for (int i = 0; i < pictureSizes.size(); i++) {
            Camera.Size size = pictureSizes.get(i);
            System.out.println("pictureSize:width = " + size.width+  " height = " + size.height);
        }
    }

    /**
     * 设置闪光灯的模式
     * @param imageView
     */
    public void setFlashMode(ImageView imageView) {
        Camera.Parameters parameters = mCamera.getParameters();
        String flashMode = parameters.getFlashMode();
        Log.d("setFlashMode  ", flashMode);
        if (flashMode != null) {
            if (flashMode.equals(Camera.Parameters.FLASH_MODE_OFF)) {
                imageView.setImageResource(R.mipmap.light_on);
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            } else if (flashMode.equals(Camera.Parameters.FLASH_MODE_ON)) {
                imageView.setImageResource(R.mipmap.light_auto);
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            } else if (flashMode.equals(Camera.Parameters.FLASH_MODE_AUTO)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                imageView.setImageResource(R.mipmap.light_off);
            } else {
                imageView.setImageResource(R.mipmap.light_off);
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            }
            mCamera.setParameters(parameters);
        }
    }

    public int getCameraId() {
        return cameraId;
    }


    /**
     * 打印支持的聚焦模式
     *
     * @param params
     */
    public void printSupportFocusMode(Camera.Parameters params) {
        List<String> focusModes = params.getSupportedFocusModes();
        for (String mode : focusModes) {
            Log.i("CameraUtil", "focusModes--" + mode);
        }
    }

    public Camera.CameraInfo getmCameraInfo(){
        return mCameraInfo;
    }

    public Camera getCamera(){
        return mCamera;
    }
    public Camera.Parameters getCameraParaters(){
        if (mCamera!=null){
            return mCamera.getParameters();
        }
        return null;
    }
}
