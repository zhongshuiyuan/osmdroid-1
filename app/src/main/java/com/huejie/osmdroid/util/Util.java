package com.huejie.osmdroid.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.blankj.utilcode.utils.ConvertUtils;
import com.blankj.utilcode.utils.FileUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.adapter.PopLvAdapter;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.http.JsonHelper;
import com.huejie.osmdroid.tilesource.GoogleTileSource;
import com.huejie.osmdroid.tilesource.TencentTileSource;
import com.huejie.osmdroid.tilesource.TianDiTuTileSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.bing.BingMapTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.TileSystemWebMercator;
import org.osmdroid.views.MapView;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class Util {

    public static final String Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";
    public static final String H_M_S = "HH:mm:ss";
    public static final String Y_M_D = "yyyy-MM-dd";
    public static final String M_D = "MM-dd";
    public static final String H_M = "HH:mm";


    public static final String PATH_BACKUP = "backup";
    public static final String PATH_PROJECTS = "projects";
    public static final String PATH_TILES = "tiles";
    public static final String PATH_FAVORITES = "favorites";
    public static final String PATH_BASE = "base";
    public static final String PATH_PROJECT = "project";

    /**
     * 将时间戳转换成年月日格式 formate
     *
     * @param date 时间戳
     */
    public static String getFromatDate(long date, String formate) {
        SimpleDateFormat format = new SimpleDateFormat(formate);
        return format.format(new Date(date));

    }

    public static String m1(double d) {
        DecimalFormat df = new DecimalFormat("#.0");
        return df.format(d);
    }


    public static String getFourString(int count) {
        if (count < 10) {
            return "000" + count;

        } else if (count < 100) {
            return "00" + count;
        } else if (count < 1000) {
            return "0" + count;
        } else {
            return count + "";
        }
    }

    public static String getTowString(int count) {
        if (count < 10) {
            return "0" + count;

        } else {
            return count + "";
        }
    }

    /**
     * 获取文件目录
     */
    public static String getMyRootPath(Context context) {
        return context.getExternalFilesDir("") + File.separator;
    }

    public static String getOfflineMapPathByName(String name, long pMapTileIndex) {
        String parent = Configuration.getInstance().getOsmdroidTileCache().getAbsolutePath() + File.separator;
        int zoom = MapTileIndex.getZoom(pMapTileIndex);
        int px = MapTileIndex.getX(pMapTileIndex);
        int py = MapTileIndex.getY(pMapTileIndex);
        switch (name) {
            case Config.TileSourceName.GOOGLEROAD:
                if (zoom <= 8) {
                    return parent + ("100/100_01_8.db");
                } else {
                    TileSystemWebMercator tileSystemWebMercator = new TileSystemWebMercator();
                    final int totalX = MapView.getTileSystem().getTileXFromLongitude(tileSystemWebMercator.getMaxLongitude(), zoom) + 1;
                    final int totalY = MapView.getTileSystem().getTileYFromLatitude(tileSystemWebMercator.getMinLatitude(), zoom) + 1;
                    int count = (int) Math.pow(2, zoom - 8);
                    for (int i = 0; i < count; i++) {
                        int pageSizeX = totalX / count;
                        int startX = i * pageSizeX;
                        int endX = (i + 1) * pageSizeX - 1;
                        for (int j = 0; j < count; j++) {
                            int pageSizeY = totalY / count;
                            int startY = j * pageSizeY;
                            int endY = (j + 1) * pageSizeY - 1;
                            if (px >= startX && px <= endX && py >= startY && py <= endY) {
                                return parent + ("100/100_" + getTowString(zoom) + "_" + getFourString(i) + "_" + getFourString(j) + ".db");
                            }
                        }

                    }
                }
                break;
            case Config.TileSourceName.GOOGLETERRAIN:
                if (zoom <= 8) {
                    return parent + ("101/101_01_8.db");
                } else {
                    TileSystemWebMercator tileSystemWebMercator = new TileSystemWebMercator();
                    final int totalX = MapView.getTileSystem().getTileXFromLongitude(tileSystemWebMercator.getMaxLongitude(), zoom) + 1;
                    final int totalY = MapView.getTileSystem().getTileYFromLatitude(tileSystemWebMercator.getMinLatitude(), zoom) + 1;
                    int count = (int) Math.pow(2, zoom - 8);
                    for (int i = 0; i < count; i++) {
                        int pageSizeX = totalX / count;
                        int startX = i * pageSizeX;
                        int endX = (i + 1) * pageSizeX - 1;
                        for (int j = 0; j < count; j++) {
                            int pageSizeY = totalY / count;
                            int startY = j * pageSizeY;
                            int endY = (j + 1) * pageSizeY - 1;
                            if (px >= startX && px <= endX && py >= startY && py <= endY) {
                                return parent + ("101/101_" + getTowString(zoom) + "_" + getFourString(i) + "_" + getFourString(j) + ".db");
                            }
                        }

                    }
                }
                break;
            case Config.TileSourceName.GOOGLEIMAGE:
                if (zoom <= 8) {
                    return parent + ("102/102_01_8.db");
                } else {
                    TileSystemWebMercator tileSystemWebMercator = new TileSystemWebMercator();
                    final int totalX = MapView.getTileSystem().getTileXFromLongitude(tileSystemWebMercator.getMaxLongitude(), zoom) + 1;
                    final int totalY = MapView.getTileSystem().getTileYFromLatitude(tileSystemWebMercator.getMinLatitude(), zoom) + 1;
                    int count = (int) Math.pow(2, zoom - 8);
                    for (int i = 0; i < count; i++) {
                        int pageSizeX = totalX / count;
                        int startX = i * pageSizeX;
                        int endX = (i + 1) * pageSizeX - 1;
                        for (int j = 0; j < count; j++) {
                            int pageSizeY = totalY / count;
                            int startY = j * pageSizeY;
                            int endY = (j + 1) * pageSizeY - 1;
                            if (px >= startX && px <= endX && py >= startY && py <= endY) {
                                return parent + ("102/102_" + getTowString(zoom) + "_" + getFourString(i) + "_" + getFourString(j) + ".db");
                            }
                        }

                    }
                }
                break;
            case Config.TileSourceName.TIANDITUROAD:
                if (zoom <= 8) {
                    return parent + ("200/200_01_8.db");
                } else {
                    TileSystemWebMercator tileSystemWebMercator = new TileSystemWebMercator();
                    final int totalX = MapView.getTileSystem().getTileXFromLongitude(tileSystemWebMercator.getMaxLongitude(), zoom) + 1;
                    final int totalY = MapView.getTileSystem().getTileYFromLatitude(tileSystemWebMercator.getMinLatitude(), zoom) + 1;
                    int count = (int) Math.pow(2, zoom - 8);
                    for (int i = 0; i < count; i++) {
                        int pageSizeX = totalX / count;
                        int startX = i * pageSizeX;
                        int endX = (i + 1) * pageSizeX - 1;
                        for (int j = 0; j < count; j++) {
                            int pageSizeY = totalY / count;
                            int startY = j * pageSizeY;
                            int endY = (j + 1) * pageSizeY - 1;
                            if (px >= startX && px <= endX && py >= startY && py <= endY) {
                                return parent + ("200/200_" + getTowString(zoom) + "_" + getFourString(i) + "_" + getFourString(j) + ".db");
                            }
                        }

                    }
                }
                break;
            case Config.TileSourceName.TIANDITUTERRAIN:
                if (zoom <= 8) {
                    return parent + ("201/201_01_8.db");
                } else {
                    TileSystemWebMercator tileSystemWebMercator = new TileSystemWebMercator();
                    final int totalX = MapView.getTileSystem().getTileXFromLongitude(tileSystemWebMercator.getMaxLongitude(), zoom) + 1;
                    final int totalY = MapView.getTileSystem().getTileYFromLatitude(tileSystemWebMercator.getMinLatitude(), zoom) + 1;
                    int count = (int) Math.pow(2, zoom - 8);
                    for (int i = 0; i < count; i++) {
                        int pageSizeX = totalX / count;
                        int startX = i * pageSizeX;
                        int endX = (i + 1) * pageSizeX - 1;
                        for (int j = 0; j < count; j++) {
                            int pageSizeY = totalY / count;
                            int startY = j * pageSizeY;
                            int endY = (j + 1) * pageSizeY - 1;
                            if (px >= startX && px <= endX && py >= startY && py <= endY) {
                                return parent + ("201/201_" + getTowString(zoom) + "_" + getFourString(i) + "_" + getFourString(j) + ".db");
                            }
                        }

                    }
                }
                break;
            case Config.TileSourceName.TIANDITUIMAGE:
                if (zoom <= 8) {
                    return parent + ("202/202_01_8.db");
                } else {
                    TileSystemWebMercator tileSystemWebMercator = new TileSystemWebMercator();
                    final int totalX = MapView.getTileSystem().getTileXFromLongitude(tileSystemWebMercator.getMaxLongitude(), zoom) + 1;
                    final int totalY = MapView.getTileSystem().getTileYFromLatitude(tileSystemWebMercator.getMinLatitude(), zoom) + 1;
                    int count = (int) Math.pow(2, zoom - 8);
                    for (int i = 0; i < count; i++) {
                        int pageSizeX = totalX / count;
                        int startX = i * pageSizeX;
                        int endX = (i + 1) * pageSizeX - 1;
                        for (int j = 0; j < count; j++) {
                            int pageSizeY = totalY / count;
                            int startY = j * pageSizeY;
                            int endY = (j + 1) * pageSizeY - 1;
                            if (px >= startX && px <= endX && py >= startY && py <= endY) {
                                return parent + ("202/202_" + getTowString(zoom) + "_" + getFourString(i) + "_" + getFourString(j) + ".db");
                            }
                        }

                    }
                }
                break;
            case Config.TileSourceName.TIANDITUCVA:
                if (zoom <= 8) {
                    return parent + ("203/203_01_8.db");
                } else {
                    TileSystemWebMercator tileSystemWebMercator = new TileSystemWebMercator();
                    final int totalX = MapView.getTileSystem().getTileXFromLongitude(tileSystemWebMercator.getMaxLongitude(), zoom) + 1;
                    final int totalY = MapView.getTileSystem().getTileYFromLatitude(tileSystemWebMercator.getMinLatitude(), zoom) + 1;
                    int count = (int) Math.pow(2, zoom - 8);
                    for (int i = 0; i < count; i++) {
                        int pageSizeX = totalX / count;
                        int startX = i * pageSizeX;
                        int endX = (i + 1) * pageSizeX - 1;
                        for (int j = 0; j < count; j++) {
                            int pageSizeY = totalY / count;
                            int startY = j * pageSizeY;
                            int endY = (j + 1) * pageSizeY - 1;
                            if (px >= startX && px <= endX && py >= startY && py <= endY) {
                                return parent + ("203/203_" + getTowString(zoom) + "_" + getFourString(i) + "_" + getFourString(j) + ".db");
                            }
                        }

                    }
                }
                break;
            case Config.TileSourceName.TENCENT:
                if (zoom <= 8) {
                    return parent + ("300/300_01_8.db");
                } else {
                    TileSystemWebMercator tileSystemWebMercator = new TileSystemWebMercator();
                    final int totalX = MapView.getTileSystem().getTileXFromLongitude(tileSystemWebMercator.getMaxLongitude(), zoom) + 1;
                    final int totalY = MapView.getTileSystem().getTileYFromLatitude(tileSystemWebMercator.getMinLatitude(), zoom) + 1;
                    int count = (int) Math.pow(2, zoom - 8);
                    for (int i = 0; i < count; i++) {
                        int pageSizeX = totalX / count;
                        int startX = i * pageSizeX;
                        int endX = (i + 1) * pageSizeX - 1;
                        for (int j = 0; j < count; j++) {
                            int pageSizeY = totalY / count;
                            int startY = j * pageSizeY;
                            int endY = (j + 1) * pageSizeY - 1;
                            if (px >= startX && px <= endX && py >= startY && py <= endY) {
                                return parent + ("300/300_" + getTowString(zoom) + "_" + getFourString(i) + "_" + getFourString(j) + ".db");
                            }
                        }

                    }
                }
                break;
            case Config.TileSourceName.BING:
                if (zoom <= 8) {
                    return parent + ("400/400_01_8.db");
                } else {
                    TileSystemWebMercator tileSystemWebMercator = new TileSystemWebMercator();
                    final int totalX = MapView.getTileSystem().getTileXFromLongitude(tileSystemWebMercator.getMaxLongitude(), zoom) + 1;
                    final int totalY = MapView.getTileSystem().getTileYFromLatitude(tileSystemWebMercator.getMinLatitude(), zoom) + 1;
                    int count = (int) Math.pow(2, zoom - 8);
                    for (int i = 0; i < count; i++) {
                        int pageSizeX = totalX / count;
                        int startX = i * pageSizeX;
                        int endX = (i + 1) * pageSizeX - 1;
                        for (int j = 0; j < count; j++) {
                            int pageSizeY = totalY / count;
                            int startY = j * pageSizeY;
                            int endY = (j + 1) * pageSizeY - 1;
                            if (px >= startX && px <= endX && py >= startY && py <= endY) {
                                return parent + ("400/400_" + getTowString(zoom) + "_" + getFourString(i) + "_" + getFourString(j) + ".db");
                            }
                        }

                    }
                }
                break;
        }
        return parent;

    }

    public static String getTilesPath(Context context) {
        String path = Util.getMyRootPath(context) + PATH_TILES;
        FileUtils.createOrExistsDir(path);
        return path;
    }


    public static String getBackUpPath(Context context, String workDir) {
        return Util.getMyRootPath(context) + workDir + File.separator + PATH_BACKUP;
    }

    public static String getProjectsPath(Context context, String workDir) {
        return Util.getMyRootPath(context) + workDir + File.separator + PATH_PROJECTS;
    }

    public static String getBaseDbPath(Context context, String workDir) {
        return Util.getMyRootPath(context) + workDir + File.separator + PATH_BASE;
    }

    public static String getProjectPath(Context context, String workDir) {
        return Util.getMyRootPath(context) + workDir + File.separator + PATH_PROJECT;
    }

    public static String getProjectsPathByName(Context context, String workDir, String projectName) {
        return Util.getMyRootPath(context) + workDir + File.separator + PATH_PROJECTS + File.separator + projectName;
    }


    public static String getMediaPathByProjectName(Context context, String workDir, String projectName) {
        String path = Util.getMyRootPath(context) + workDir + File.separator + PATH_PROJECTS + File.separator + projectName + File.separator + "Medias";
        FileUtils.createOrExistsDir(path);
        return path;
    }

    public static String getLinePathByProjectName(Context context, String workDir, String projectName) {
        String path = Util.getMyRootPath(context) + workDir + File.separator + PATH_PROJECTS + File.separator + projectName + File.separator + "Lines";
        FileUtils.createOrExistsDir(path);
        return path;
    }

    public static String getMapsPathByProjectName(Context context, String workDir, String projectName) {
        String path = Util.getMyRootPath(context) + workDir + File.separator + PATH_PROJECTS + File.separator + projectName + File.separator + "Maps";
        FileUtils.createOrExistsDir(path);
        return path;
    }

    /**
     * @param pref 前缀
     */
    public static void showPopwindow(final Context context, final CheckedTextView cView, final String[] datas, final String pref) {
        cView.post(new Runnable() {
            @Override
            public void run() {
                int mWidth = cView.getWidth();
                cView.setChecked(true);
                View contentView = LayoutInflater.from(context).inflate(R.layout.pop_lv, null);
                final PopupWindow pop_grade = new PopupWindow(contentView, mWidth, LinearLayout.LayoutParams.WRAP_CONTENT);  //设置popwindow为布局大小
                // 点击PopupWindow以外的区域，PopupWindow是否会消失。
                pop_grade.setBackgroundDrawable(new BitmapDrawable());
                pop_grade.setOutsideTouchable(true);
                pop_grade.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        cView.setChecked(false);
                    }
                });
                ListView listView = (ListView) contentView.findViewById(R.id.listView);
                PopLvAdapter adapter = new PopLvAdapter(context, datas);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String choose = datas[position];
                        cView.setText(pref + choose);
                        cView.setTag(choose);

                        pop_grade.dismiss();
                    }
                });
                pop_grade.showAsDropDown(cView);    //popwindow显示在控件下方
            }
        });

    }


    /**
     * 将时间戳转换成年月日格式 yyyy-MM-dd
     *
     * @param date 时间戳
     */
    public static String getDateString(long date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(date));
    }

    /**
     * 将时间戳转换成时分秒格式
     *
     * @param timestamp 时间戳
     */
    public static String getDateStringTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Date date = new Date(timestamp);
        return sdf.format(date);
    }


    /**
     * 将时间戳转换成年月日格式
     *
     * @param date 时间戳
     */
    public static String getDateTimeString(long date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(new Date(date));
    }

    /**
     * 获取视频文件截图
     *
     * @param path 视频文件的路径
     * @return Bitmap 返回获取的Bitmap
     */

    public static Bitmap getVideoThumb(String path) {

        MediaMetadataRetriever media = new MediaMetadataRetriever();

        media.setDataSource(path);

        return media.getFrameAtTime();

    }

    public static String mapTranslate(String map) {
        if (TextUtils.isEmpty(map)) {
            return null;
        }
        switch (map) {
            case "Google街道图":
                return Config.TileSourceName.GOOGLEROAD;
            case "Google卫星图":
                return Config.TileSourceName.GOOGLEIMAGE;
            case "Google地形图":
                return Config.TileSourceName.GOOGLETERRAIN;
            case "天地图街道图":
                return Config.TileSourceName.TIANDITUROAD;
            case "天地图卫星图":
                return Config.TileSourceName.TIANDITUIMAGE;
            case "天地图地形图":
                return Config.TileSourceName.TIANDITUTERRAIN;
            case "腾讯地图":
                return Config.TileSourceName.TENCENT;
            case "Bing地图":
                return Config.TileSourceName.BING;
            default:
                return Config.TileSourceName.GOOGLEROAD;

        }
    }

    public static String mapTranslateOver(String map) {
        if (TextUtils.isEmpty(map)) {
            return null;
        }
        switch (map) {
            case Config.TileSourceName.GOOGLEROAD:
                return "Google街道图";
            case Config.TileSourceName.GOOGLEIMAGE:
                return "Google卫星图";
            case Config.TileSourceName.GOOGLETERRAIN:
                return "Google地形图";
            case Config.TileSourceName.TIANDITUROAD:
                return "天地图街道图";
            case Config.TileSourceName.TIANDITUIMAGE:
                return "天地图卫星图";
            case Config.TileSourceName.TIANDITUTERRAIN:
                return "天地图地形图";
            case Config.TileSourceName.TENCENT:
                return "腾讯地图";
            case Config.TileSourceName.BING:
                return "Bing地图";
            default:
                return "Google街道图";
        }
    }

    public static int getLogoByMapType(String map) {

        switch (map) {
            case Config.TileSourceName.GOOGLEROAD:
            case Config.TileSourceName.GOOGLEIMAGE:
            case Config.TileSourceName.GOOGLETERRAIN:
                return R.mipmap.google;
            case Config.TileSourceName.TIANDITUROAD:
            case Config.TileSourceName.TIANDITUIMAGE:
            case Config.TileSourceName.TIANDITUTERRAIN:
                return R.mipmap.tianmap;
            case Config.TileSourceName.TENCENT:
                return R.mipmap.tencent;
            case Config.TileSourceName.BING:
                return 0;
            default:
                return 0;

        }
    }

    public static ITileSource getTileSourceByType(@NonNull String map) {
        switch (map) {
            case Config.TileSourceName.GOOGLEROAD:
                return new GoogleTileSource(Config.TileSourceName.GOOGLEROAD);
            case Config.TileSourceName.GOOGLEIMAGE:
                return new GoogleTileSource(Config.TileSourceName.GOOGLEIMAGE);
            case Config.TileSourceName.GOOGLETERRAIN:
                return new GoogleTileSource(Config.TileSourceName.GOOGLETERRAIN);
            case Config.TileSourceName.TIANDITUROAD:
                return new TianDiTuTileSource(Config.TileSourceName.TIANDITUROAD);
            case Config.TileSourceName.TIANDITUIMAGE:
                return new TianDiTuTileSource(Config.TileSourceName.TIANDITUIMAGE);
            case Config.TileSourceName.TIANDITUTERRAIN:
                return new TianDiTuTileSource(Config.TileSourceName.TIANDITUTERRAIN);
            case Config.TileSourceName.TENCENT:
                return new TencentTileSource();
            case Config.TileSourceName.BING:
                return new BingMapTileSource(null);
            default:
                return new GoogleTileSource(Config.TileSourceName.GOOGLEROAD);
        }
    }

    public static String getMIMEType(Context context, String ext) {
        try {
            JSONObject object = new JSONObject(ConvertUtils.inputStream2String(context.getAssets().open("mime"), "UTF8"));
            return object.getString(ext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "*/*";
    }

    public static String ipToName(String ip) {
        if (TextUtils.isEmpty(ip) || !ip.contains(".") || !ip.contains(":")) {
            return null;
        }
        return ip.replace("http://", "").replace(".", "_").replace(":", "-");
    }

    /**
     * 获取存储的url格式   /程吉祥测试2/Lines/谷城至丹江口.kml
     */
    public static String getStoreUrl(Context context, String path) {
        String proPath = getProjectsPath(context, AppContext.sp.getString(Config.SP.CURRENT_WORK_DIR));
        if (TextUtils.isEmpty(path) || !path.contains(proPath)) {
            return path;
        }
        return path.replace(proPath, "");

    }

    public static double valueDouble(String value) {
        if (TextUtils.isEmpty(value)) {
            return -1;
        }
        return Double.valueOf(value);

    }

    public static long valueLong(String value) {
        if (TextUtils.isEmpty(value)) {
            return -1;
        }
        return Long.valueOf(value);

    }


    public static String valueString(double value) {
        if (value <= 0) {
            return "";
        } else {
            return new StringBuffer().append(value).toString();
        }

    }

    public static String valueString(long value) {
        if (value <= 0) {
            return "";
        } else {
            return new StringBuffer().append(value).toString();
        }

    }
    public static String valueString(String value) {
        if (TextUtils.isEmpty(value)) {
            return "";
        } else {
            return new StringBuffer().append(value).toString();
        }

    }

    public static int valueInteger(String value) {
        if (TextUtils.isEmpty(value)) {
            return -1;
        }
        return Integer.valueOf(value);

    }

    public static String valueString(int value) {
        if (value <= 0) {
            return "";
        } else {
            return new StringBuffer().append(value).toString();
        }

    }

    /**
     * 将String类型的字符串转换成list
     */
    public static List<GeoPoint> string2geoPoints(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            JSONArray array = new JSONArray(str);
            List<GeoPoint> list = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONArray arr = JsonHelper.getJsonArray(array, i);
                double lng = arr.getDouble(0);
                double lat = arr.getDouble(1);
                list.add(new GeoPoint(lat, lng));
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 将String类型的字符串转换成list
     */
    public static GeoPoint string2geoPoint(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            JSONArray array = new JSONArray(str);
            return new GeoPoint(array.getDouble(1), array.getDouble(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

}
