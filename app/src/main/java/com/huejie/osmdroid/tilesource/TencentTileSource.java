package com.huejie.osmdroid.tilesource;

import com.huejie.osmdroid.util.Config;

import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.MapTileIndex;

/**
 * 腾讯地图
 */
public class TencentTileSource extends XYTileSource {
    //矢量底图 lyrs=m  lyrs=是指瓦片类型 有标注  在国内但有偏移，国外暂无测试
    static final String[] baseUrl_TXRoad_new = new String[]{

            "http://p1.map.gtimg.com/maptilesv2/{Z}/{$X}/{$Y}/{X}_{Y}.png?version=20130701",
            "http://p2.map.gtimg.com/maptilesv2/{Z}/{$X}/{$Y}/{X}_{Y}.png?version=20130701",
            "http://p3.map.gtimg.com/maptilesv2/{Z}/{$X}/{$Y}/{X}_{Y}.png?version=20130701",

    };

    public TencentTileSource() {
        super(Config.TileSourceName.TENCENT, 1, 18, 256, "png", baseUrl_TXRoad_new);
    }

    @Override
    public String getTileURLString(long pMapTileIndex) {
        int Y = ((int) Math.pow(2, MapTileIndex.getZoom(pMapTileIndex)) - 1 - MapTileIndex.getY(pMapTileIndex));
        return getBaseUrl()
                .replace("{Z}", MapTileIndex.getZoom(pMapTileIndex) + "")
                .replace("{$X}", Math.floor(MapTileIndex.getX(pMapTileIndex) / 16.0) + "")
                .replace("{$Y}", Math.floor(Y / 16.0) + "")
                .replace("{X}", MapTileIndex.getX(pMapTileIndex) + "")
                .replace("{Y}", Y + "");
    }
}
