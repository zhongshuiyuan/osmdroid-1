package com.huejie.osmdroid.http;

import com.blankj.utilcode.utils.ToastUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/18.
 */

public abstract class ResponseCallBack extends StringCallback {

    public ResponseCallBack() {
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        ToastUtils.showShortToast("网络异常");
        onComplete();
    }

    @Override
    public void onResponse(String response, int id) {
        try {
            System.out.println(response);
            JSONObject object = new JSONObject(response);
            if (object.has("code") && JsonHelper.getJsonString(object, "code").equals("200")) {
                onSuccess(response);
            } else {
                ToastUtils.showShortToast(JsonHelper.getJsonString(object, "message"));
                onFail(JsonHelper.getJsonString(object, "message"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            onComplete();
        }
    }

    public abstract void onSuccess(String json);

    public abstract void onFail(String message);

    public abstract void onComplete();
}
