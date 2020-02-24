package com.huajie.fileexport.utils;

import android.text.TextUtils;

public class Util {
    public static boolean endsWith(String str, String params) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(params)) {
            return false;
        }
        return str.endsWith(params) || str.endsWith(params.toUpperCase()) || str.endsWith(params.toLowerCase());
    }
}
