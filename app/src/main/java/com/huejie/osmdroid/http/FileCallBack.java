package com.huejie.osmdroid.http;

import android.os.Environment;
import android.text.TextUtils;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;


/**
 * Created by zhy on 15/12/15.
 */
public abstract class FileCallBack extends Callback<File> {
    public static final String DM_TARGET_FOLDER = File.separator + "download" + File.separator; //下载目标文件夹
    /**
     * 目标文件存储的文件夹路径
     */
    private String destFileDir;
    /**
     * 目标文件存储的文件名
     */
    private String destFileName;


    public abstract void downLoadError(Exception e);

    public FileCallBack(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }



    @Override
    public void onResponse(File response, int id) {
        System.out.println("onResponse---》" + response == null);
    }


    @Override
    public abstract void inProgress(float progress, long total, int id);

    public abstract void onSuccess(File file, int id);

    public abstract void onComplete();

    @Override
    public File parseNetworkResponse(Response response, int id) throws Exception {
        return saveFile(response, id);
    }

    public File saveFile(Response response, final int id) throws IOException {
        if (TextUtils.isEmpty(destFileDir)) {
            destFileDir = Environment.getExternalStorageDirectory() + DM_TARGET_FOLDER;
        }
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0;
            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                OkHttpUtils.getInstance().getDelivery().execute(new Runnable() {
                    @Override
                    public void run() {
                        inProgress(finalSum * 1.0f / total, total, id);
                    }
                });
            }
            fos.flush();
            onSuccess(file, id);
            return file;

        } catch (Exception e) {
            downLoadError(e);
            return null;
        } finally {
            onComplete();
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
            }

        }


    }

}
