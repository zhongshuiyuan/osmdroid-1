package com.huejie.osmdroid.tilesource;

import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.TilesNameUtils;

import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.MapTileIndex;

public class GoogleTileSource extends XYTileSource {

    //Google地形图
    public static final String[] baseUrl_GoogleTerrain = new String[]{
            "http://mt0.google.cn/vt/lyrs=p&hl=zh-CN&gl=cn&scale=1",
            "http://mt1.google.cn/vt/lyrs=p&hl=zh-CN&gl=cn&scale=1",
            "http://mt2.google.cn/vt/lyrs=p&hl=zh-CN&gl=cn&scale=1",
    };
    //Google道路
    public static final String[] baseUrl_GoogleRoad = new String[]{
            "http://mt0.google.cn/vt/lyrs=m&hl=zh-CN&gl=cn&scale=1",
            "http://mt1.google.cn/vt/lyrs=m&hl=zh-CN&gl=cn&scale=1",
            "http://mt2.google.cn/vt/lyrs=m&hl=zh-CN&gl=cn&scale=1",
    };
    //Google影像
    public static final String[] baseUrl_GoogleImage = new String[]{
            "http://mt0.google.cn/vt/lyrs=y&hl=zh-CN&gl=cn&scale=1",
            "http://mt1.google.cn/vt/lyrs=y&hl=zh-CN&gl=cn&scale=1",
            "http://mt2.google.cn/vt/lyrs=y&hl=zh-CN&gl=cn&scale=1",
    };

    String urlXYZ = "&x={$x}&y={$y}&z={$z}";

    public GoogleTileSource() {
        super(Config.TileSourceName.GOOGLEROAD, 1, 18, 256, "png", baseUrl_GoogleRoad);
    }

    public GoogleTileSource(String name) {
        super(name, 2, 22, 256, "png", TilesNameUtils.getTileUrlByName(name));
    }

    @Override
    public String getTileURLString(long pMapTileIndex) {
        return getBaseUrl() + urlXYZ
                .replace("{$x}", MapTileIndex.getX(pMapTileIndex) + "")
                .replace("{$y}", MapTileIndex.getY(pMapTileIndex) + "")
                .replace("{$z}", MapTileIndex.getZoom(pMapTileIndex) + "");
    }

}
