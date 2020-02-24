package com.huejie.osmdroid.model.favorites;

import com.huejie.osmdroid.R;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 收藏夹
 */

public class Favorites extends LitePalSupport implements Serializable {
    public long id;
    public int type;//0:点 1:线 2:多边形 3:圆
    public int fileType;//0:文件 1：目录
    public int fileStatus;//0:显示 1：隐藏

    @Column(nullable = false)
    public String name;
    @Column(defaultValue = "本地收藏夹", nullable = false)
    public String parentName;//以"/"分割，默认都在favorites下
    public String remark;

    //点的属性
    public int icon = R.mipmap.marker_icon_1;
    //线的属性
    public int color;
    public int width;
    //透明度 0-255之间
    public int alpha = 255;

    //多边形的属性，包含线的属性
    public int fillColor;

    @Column(ignore = true)
    public boolean isChoose;
    @Column(ignore = true)
    public List<FavoritesLatLng> latLngList = new ArrayList<>();
    @Column(ignore = true)
    public List<FavoritesAccessories> accessoriesList = new ArrayList<>();
    @Column(ignore = true)
    public FavoritesLatLng points;

}
