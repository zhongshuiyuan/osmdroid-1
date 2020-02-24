package com.huejie.osmdroid.http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by weihua on 16/3/28.
 */
public class JsonHelper {

    public static JSONObject parseJSONObject(String str) {
        try {
            return new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static JSONArray parseJSONArray(String str) {
        try {
            return new JSONArray(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getJsonObject(JSONObject jsonObject, String name) {
        try {
            if (jsonObject.has(name)) {
                return jsonObject.getJSONObject(name);
            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray getJsonArray(JSONObject jsonObject, String name) {
        try {
            if (jsonObject.has(name) && !jsonObject.isNull(name)) {
                return jsonObject.getJSONArray(name);
            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getJsonObject(JSONArray jsonArray, int i) {
        try {
            return jsonArray.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String getJsonString(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key)) {
                return jsonObject.getString(key);
            } else {
                return "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String getJsonString(JSONArray jsonArray, int i) {
        try {
            return jsonArray.getString(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static int getJsonInt(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key) && null != jsonObject.get(key) && !jsonObject.get(key).toString().equals("null")) {
                return jsonObject.getInt(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;

    }

    public static int getJsonInt(JSONArray jsonArray, int i) {
        try {
            return jsonArray.getInt(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;

    }

    public static boolean getJsonBoolean(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key)) {
                return jsonObject.getBoolean(key);
            } else {
                return false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;

    }

    public static double getJsonDouble(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key) && null != jsonObject.get(key)) {
                return jsonObject.getDouble(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;

    }

    public static long getJsonLong(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.has(key) && null != jsonObject.get(key) && !jsonObject.get(key).toString().equals("null")) {
                return jsonObject.getLong(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;

    }

    public static JSONArray getJsonArray(JSONArray jsonArray, int index) {
        try {
            return jsonArray.getJSONArray(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

}
