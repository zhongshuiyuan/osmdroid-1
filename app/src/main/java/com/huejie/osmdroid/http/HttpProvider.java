package com.huejie.osmdroid.http;

import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.util.Config;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;

/**
 * Created by guchao on 16/10/18.
 */
public final class HttpProvider {
    private static final String FILE = "file";
    private static final String FILENAME = "filename";

    public static OkHttpUtils getInstance() {
        return OkHttpUtils.getInstance();
    }

    public static void cancelTag(Object tag) {
        getInstance().cancelTag(tag);
    }

    public static void get(Object object, String url, Map<String, String> params, Callback callback) {
        OkHttpUtils.get()
                .tag(object)
                .url(url)
                .params(params)
                .addHeader(Config.AUTHORIZATION, AppContext.sp.getBoolean(Config.SP.IS_LOGIN) ? AppContext.sp.getString(Config.SP.TOKEN) : Config.DEFAULT_TOKEN)
                .build()
                .execute(callback);
    }

    public static void post(Object object, String url, Map<String, String> params, Callback callback) {
        OkHttpUtils.post()
                .tag(object)
                .url(url)
                .params(params)
                .addHeader(Config.AUTHORIZATION, AppContext.sp.getBoolean(Config.SP.IS_LOGIN) ? AppContext.sp.getString(Config.SP.TOKEN) : Config.DEFAULT_TOKEN)
                .build()
                .execute(callback);
    }

    public static void postString(Object object, String url, ParamsMap params, Callback callback) {
        OkHttpUtils.postString()
                .tag(object)
                .url(url)
                .content(params.toJsonString())
                .addHeader(Config.AUTHORIZATION, AppContext.sp.getBoolean(Config.SP.IS_LOGIN) ? AppContext.sp.getString(Config.SP.TOKEN) : Config.DEFAULT_TOKEN)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(callback);
    }

    public static void postString(Object object, String url, String params, Callback callback) {
        OkHttpUtils.postString()
                .tag(object)
                .url(url)
                .content(params)
                .addHeader(Config.AUTHORIZATION, AppContext.sp.getBoolean(Config.SP.IS_LOGIN) ? AppContext.sp.getString(Config.SP.TOKEN) : Config.DEFAULT_TOKEN)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(callback);
    }

    public static void upload(Object object, String url, File file, Callback callback) {
        OkHttpUtils.post()
                .tag(object)
                .url(url)
                .addHeader(Config.AUTHORIZATION, AppContext.sp.getBoolean(Config.SP.IS_LOGIN) ? AppContext.sp.getString(Config.SP.TOKEN) : Config.DEFAULT_TOKEN)
                .addFile(FILE, FILENAME, file)
                .build()
                .execute(callback);
    }

    public static void download(Object object, String url, ParamsMap params, Callback callback) {
        OkHttpUtils.postString()
                .tag(object)
                .url(url)
                .content(params.toJsonString())
                .addHeader(Config.AUTHORIZATION, AppContext.sp.getBoolean(Config.SP.IS_LOGIN) ? AppContext.sp.getString(Config.SP.TOKEN) : Config.DEFAULT_TOKEN)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(callback);
    }

}
