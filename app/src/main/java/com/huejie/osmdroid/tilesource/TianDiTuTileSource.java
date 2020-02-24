package com.huejie.osmdroid.tilesource;

import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.TilesNameUtils;

import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.MapTileIndex;

/**
 * 天地图
 */
public class TianDiTuTileSource extends XYTileSource {

    //天地图道路图
    public static final String[] baseUrl_Vec = new String[]{
            "http://t0.tianditu.gov.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk=560c46eb9553400f708416a53337fd72",
            "http://t1.tianditu.gov.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk=560c46eb9553400f708416a53337fd72",
            "http://t2.tianditu.gov.cn/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk=560c46eb9553400f708416a53337fd72",
    };
    public static final String[] baseUrl_Cva = new String[]{
            "http://t0.tianditu.gov.cn/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk=560c46eb9553400f708416a53337fd72",
            "http://t1.tianditu.gov.cn/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk=560c46eb9553400f708416a53337fd72",
            "http://t2.tianditu.gov.cn/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk=560c46eb9553400f708416a53337fd72",
    };

    //天地图影像图
    public static final String[] baseUrl_Img = new String[]{
            "http://t0.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk=818d19356285a8d591bf787825288bcf",
            "http://t1.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk=818d19356285a8d591bf787825288bcf",
            "http://t2.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk=818d19356285a8d591bf787825288bcf",
    };
    //天地图地形图
    public static final String[] baseUrl_Ter = new String[]{
            "http://t0.tianditu.gov.cn/ter_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=ter&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk=818d19356285a8d591bf787825288bcf",
            "http://t1.tianditu.gov.cn/ter_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=ter&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk=818d19356285a8d591bf787825288bcf",
            "http://t2.tianditu.gov.cn/ter_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=ter&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&tk=818d19356285a8d591bf787825288bcf",
    };


    private TianDiTuTileSource() {
//        super("TianDiTuVec", 2, 18, 256, ".png", baseUrl_Vec);
        super(Config.TileSourceName.TIANDITUROAD, 2, 18, 256, "png", baseUrl_Vec);

    }

    public TianDiTuTileSource(String name) {
        super(name, 2, 18, 256, "png", TilesNameUtils.getTileUrlByName(name));
    }

    @Override
    public String getTileURLString(final long pMapTileIndex) {
        return getBaseUrl()
                .replace("{x}", MapTileIndex.getX(pMapTileIndex) + "")
                .replace("{y}", MapTileIndex.getY(pMapTileIndex) + "")
                .replace("{z}", MapTileIndex.getZoom(pMapTileIndex) + "")
                ;
    }
}
