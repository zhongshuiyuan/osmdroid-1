package com.huejie.osmdroid.util;

import com.huejie.osmdroid.tilesource.GoogleTileSource;
import com.huejie.osmdroid.tilesource.TianDiTuTileSource;

public class TilesNameUtils {

    public static String[] getTileUrlByName(String name) {
        switch (name) {
            case Config.TileSourceName.TIANDITUTERRAIN:
                return TianDiTuTileSource.baseUrl_Ter;
            case Config.TileSourceName.TIANDITUROAD:
                return TianDiTuTileSource.baseUrl_Vec;
            case Config.TileSourceName.TIANDITUIMAGE:
                return TianDiTuTileSource.baseUrl_Img;
            case Config.TileSourceName.TIANDITUCVA:
                return TianDiTuTileSource.baseUrl_Cva;
            case Config.TileSourceName.GOOGLEROAD:
                return GoogleTileSource.baseUrl_GoogleRoad;
            case Config.TileSourceName.GOOGLEIMAGE:
                return GoogleTileSource.baseUrl_GoogleImage;
            case Config.TileSourceName.GOOGLETERRAIN:
                return GoogleTileSource.baseUrl_GoogleTerrain;
            default:
                return null;

        }

    }


}
