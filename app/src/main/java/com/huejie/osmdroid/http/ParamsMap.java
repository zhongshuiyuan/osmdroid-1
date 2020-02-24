package com.huejie.osmdroid.http;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by guchao on 16/10/18.
 */
public class ParamsMap extends HashMap implements Cloneable, Serializable {
    public ParamsMap() {

    }

    public ParamsMap with(String key, Object value) {
        put(key, null == value ? "" : value);
        return this;
    }


    public String toJsonString() {
        System.out.println(JSONObject.toJSONString(this));
        return JSONObject.toJSONString(this);
    }
}
