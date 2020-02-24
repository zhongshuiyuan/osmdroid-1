package com.huejie.osmdroid.tilesource;

import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.MapTileIndex;

public class BaiduTileSource extends XYTileSource {

    static int[] baiduX = {0, 0, 1, 3, 6, 12, 24, 49, 98, 197, 395, 790, 1581,
            3163, 6327, 12654, 25308, 50617};
    static int[] baiduY = {0, 0, 0, 1, 2, 4, 9, 18, 36, 73, 147, 294, 589,
            1178, 2356, 4712, 9425, 18851};
    static int[] googleX = {0, 1, 3, 7, 13, 26, 52, 106, 212, 425, 851, 1702,
            3405, 6811, 13623, 27246, 54492, 107917};
    static int[] googleY = {0, 0, 1, 2, 5, 12, 23, 47, 95, 190, 380, 761, 1522, 3045, 6091, 12183, 24366, 47261};

    //Google地形图
    public static final String[] baseUrl_baidu = new String[]{
            "http://online2.map.bdimg.com/onlinelabel/?qt=tile&x={x}&y={y}&z={z}&styles=pl&udt=20160321&scaler=2&p=0",

    };

    public BaiduTileSource() {
        super("baidu", 1, 18, 256, "png", baseUrl_baidu);
    }


    private static int googleToBaiduX(int x, int z) {
        int b = baiduX[z - 1];// 395
        int g = googleX[z - 1];// 11:843,12:1685
        // int gx = g + (x-b);// --- 1587+
        int gx = x - g + b;// --- 1587+
        // 谷歌瓦片行编号=[谷歌参照瓦片行编号+(百度行编号 – 百度参照瓦片行编号)]
        return gx;
    }

    private static int googleToBaiduY(int y, int z) {
        int b = baiduY[z - 1];// 147
        int g = googleY[z - 1];// 10:
        // int gy = g - (y-b);//
        int gy = g + b - y;//
        // 谷歌瓦片列编号=[谷歌参照瓦片列编号- (百度列编号 – 百度参照瓦片列编号)] //向上，列为递减
        return gy;
    }

    @Override
    public String getTileURLString(long pMapTileIndex) {
        return getBaseUrl()
                .replace("{x}", googleToBaiduX(MapTileIndex.getX(pMapTileIndex), MapTileIndex.getZoom(pMapTileIndex)) + "")
                .replace("{y}", googleToBaiduY(MapTileIndex.getY(pMapTileIndex), MapTileIndex.getZoom(pMapTileIndex)) + "")
                .replace("{z}", MapTileIndex.getZoom(pMapTileIndex) + "");
    }

}
