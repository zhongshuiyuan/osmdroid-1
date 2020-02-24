package org.bonuspack.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;

import java.io.File;
import java.util.List;

public class GISUtils {
    public static double calcArea2(List<GeoPoint> Point, String MapUnits) {

        int Count = Point.size();
        if (Count > 2) {
            double mtotalArea = 0;
            if (MapUnits == "DEGREES")//经纬度坐标下的球面多边形
            {
                double LowX = 0.0;
                double LowY = 0.0;
                double MiddleX = 0.0;
                double MiddleY = 0.0;
                double HighX = 0.0;
                double HighY = 0.0;

                double AM = 0.0;
                double BM = 0.0;
                double CM = 0.0;

                double AL = 0.0;
                double BL = 0.0;
                double CL = 0.0;

                double AH = 0.0;
                double BH = 0.0;
                double CH = 0.0;

                double CoefficientL = 0.0;
                double CoefficientH = 0.0;

                double ALtangent = 0.0;
                double BLtangent = 0.0;
                double CLtangent = 0.0;

                double AHtangent = 0.0;
                double BHtangent = 0.0;
                double CHtangent = 0.0;

                double ANormalLine = 0.0;
                double BNormalLine = 0.0;
                double CNormalLine = 0.0;

                double OrientationValue = 0.0;

                double AngleCos = 0.0;

                double Sum1 = 0.0;
                double Sum2 = 0.0;
                double Count2 = 0;
                double Count1 = 0;


                double Sum = 0.0;
                double Radius = 6378000;

                for (int i = 0; i < Count; i++) {
                    if (i == 0) {
                        LowX = Point.get(Count - 1).getLatitude() * Math.PI / 180;
                        LowY = Point.get(Count - 1).getLongitude() * Math.PI / 180;
                        MiddleX = Point.get(0).getLatitude() * Math.PI / 180;
                        MiddleY = Point.get(0).getLongitude() * Math.PI / 180;
                        HighX = Point.get(1).getLatitude() * Math.PI / 180;
                        HighY = Point.get(1).getLongitude() * Math.PI / 180;
                    } else if (i == Count - 1) {
                        LowX = Point.get(Count - 2).getLatitude() * Math.PI / 180;
                        LowY = Point.get(Count - 2).getLongitude() * Math.PI / 180;
                        MiddleX = Point.get(Count - 1).getLatitude() * Math.PI / 180;
                        MiddleY = Point.get(Count - 1).getLongitude() * Math.PI / 180;
                        HighX = Point.get(0).getLatitude() * Math.PI / 180;
                        HighY = Point.get(0).getLongitude() * Math.PI / 180;
                    } else {
                        LowX = Point.get(i - 1).getLatitude() * Math.PI / 180;
                        LowY = Point.get(i - 1).getLongitude() * Math.PI / 180;
                        MiddleX = Point.get(i).getLatitude() * Math.PI / 180;
                        MiddleY = Point.get(i).getLongitude() * Math.PI / 180;
                        HighX = Point.get(i + 1).getLatitude() * Math.PI / 180;
                        HighY = Point.get(i + 1).getLongitude() * Math.PI / 180;
                    }

                    AM = Math.cos(MiddleY) * Math.cos(MiddleX);
                    BM = Math.cos(MiddleY) * Math.sin(MiddleX);
                    CM = Math.sin(MiddleY);
                    AL = Math.cos(LowY) * Math.cos(LowX);
                    BL = Math.cos(LowY) * Math.sin(LowX);
                    CL = Math.sin(LowY);
                    AH = Math.cos(HighY) * Math.cos(HighX);
                    BH = Math.cos(HighY) * Math.sin(HighX);
                    CH = Math.sin(HighY);


                    CoefficientL = (AM * AM + BM * BM + CM * CM) / (AM * AL + BM * BL + CM * CL);
                    CoefficientH = (AM * AM + BM * BM + CM * CM) / (AM * AH + BM * BH + CM * CH);

                    ALtangent = CoefficientL * AL - AM;
                    BLtangent = CoefficientL * BL - BM;
                    CLtangent = CoefficientL * CL - CM;
                    AHtangent = CoefficientH * AH - AM;
                    BHtangent = CoefficientH * BH - BM;
                    CHtangent = CoefficientH * CH - CM;


                    AngleCos = (AHtangent * ALtangent + BHtangent * BLtangent + CHtangent * CLtangent) / (Math.sqrt(AHtangent * AHtangent + BHtangent * BHtangent + CHtangent * CHtangent) * Math.sqrt(ALtangent * ALtangent + BLtangent * BLtangent + CLtangent * CLtangent));

                    AngleCos = Math.acos(AngleCos);

                    ANormalLine = BHtangent * CLtangent - CHtangent * BLtangent;
                    BNormalLine = 0 - (AHtangent * CLtangent - CHtangent * ALtangent);
                    CNormalLine = AHtangent * BLtangent - BHtangent * ALtangent;

                    if (AM != 0) {
                        OrientationValue = ANormalLine / AM;
                    } else if (BM != 0) {
                        OrientationValue = BNormalLine / BM;
                    } else {
                        OrientationValue = CNormalLine / CM;
                    }

                    if (OrientationValue > 0) {
                        Sum1 += AngleCos;
                        Count1++;

                    } else {
                        Sum2 += AngleCos;
                        Count2++;
                        //Sum +=2*Math.PI-AngleCos;
                    }

                }

                if (Sum1 > Sum2) {
                    Sum = Sum1 + (2 * Math.PI * Count2 - Sum2);
                } else {
                    Sum = (2 * Math.PI * Count1 - Sum1) + Sum2;
                }

                //平方米
                mtotalArea = (Sum - (Count - 2) * Math.PI) * Radius * Radius;
            } else { //非经纬度坐标下的平面多边形

                int i, j;
                //double j;
                double p1x, p1y;
                double p2x, p2y;
                for (i = Count - 1, j = 0; j < Count; i = j, j++) {

                    p1x = Point.get(i).getLatitude();
                    p1y = Point.get(i).getLongitude();

                    p2x = Point.get(i).getLatitude();
                    p2y = Point.get(i).getLongitude();

                    mtotalArea += p1x * p2y - p2x * p1y;
                }
                mtotalArea /= 2.0;
            }
            return mtotalArea;
        }
        return 0;
    }

    public static String calcArea(List<GeoPoint> Point, String MapUnits) {
        int Count = Point.size();
        if (Count > 2) {
            double mtotalArea = 0;
            if (MapUnits == "DEGREES")//经纬度坐标下的球面多边形
            {
                double LowX = 0.0;
                double LowY = 0.0;
                double MiddleX = 0.0;
                double MiddleY = 0.0;
                double HighX = 0.0;
                double HighY = 0.0;

                double AM = 0.0;
                double BM = 0.0;
                double CM = 0.0;

                double AL = 0.0;
                double BL = 0.0;
                double CL = 0.0;

                double AH = 0.0;
                double BH = 0.0;
                double CH = 0.0;

                double CoefficientL = 0.0;
                double CoefficientH = 0.0;

                double ALtangent = 0.0;
                double BLtangent = 0.0;
                double CLtangent = 0.0;

                double AHtangent = 0.0;
                double BHtangent = 0.0;
                double CHtangent = 0.0;

                double ANormalLine = 0.0;
                double BNormalLine = 0.0;
                double CNormalLine = 0.0;

                double OrientationValue = 0.0;

                double AngleCos = 0.0;

                double Sum1 = 0.0;
                double Sum2 = 0.0;
                double Count2 = 0;
                double Count1 = 0;


                double Sum = 0.0;
                double Radius = 6378000;

                for (int i = 0; i < Count; i++) {
                    if (i == 0) {
                        LowX = Point.get(Count - 1).getLongitude() * Math.PI / 180;
                        LowY = Point.get(Count - 1).getLatitude() * Math.PI / 180;
                        MiddleX = Point.get(0).getLongitude() * Math.PI / 180;
                        MiddleY = Point.get(0).getLatitude() * Math.PI / 180;
                        HighX = Point.get(1).getLongitude() * Math.PI / 180;
                        HighY = Point.get(1).getLatitude() * Math.PI / 180;
                    } else if (i == Count - 1) {
                        LowX = Point.get(Count - 2).getLongitude() * Math.PI / 180;
                        LowY = Point.get(Count - 2).getLatitude() * Math.PI / 180;
                        MiddleX = Point.get(Count - 1).getLongitude() * Math.PI / 180;
                        MiddleY = Point.get(Count - 1).getLatitude() * Math.PI / 180;
                        HighX = Point.get(0).getLongitude() * Math.PI / 180;
                        HighY = Point.get(0).getLatitude() * Math.PI / 180;
                    } else {
                        LowX = Point.get(i - 1).getLongitude() * Math.PI / 180;
                        LowY = Point.get(i - 1).getLatitude() * Math.PI / 180;
                        MiddleX = Point.get(i).getLongitude() * Math.PI / 180;
                        MiddleY = Point.get(i).getLatitude() * Math.PI / 180;
                        HighX = Point.get(i + 1).getLongitude() * Math.PI / 180;
                        HighY = Point.get(i + 1).getLatitude() * Math.PI / 180;
                    }

                    AM = Math.cos(MiddleY) * Math.cos(MiddleX);
                    BM = Math.cos(MiddleY) * Math.sin(MiddleX);
                    CM = Math.sin(MiddleY);
                    AL = Math.cos(LowY) * Math.cos(LowX);
                    BL = Math.cos(LowY) * Math.sin(LowX);
                    CL = Math.sin(LowY);
                    AH = Math.cos(HighY) * Math.cos(HighX);
                    BH = Math.cos(HighY) * Math.sin(HighX);
                    CH = Math.sin(HighY);


                    CoefficientL = (AM * AM + BM * BM + CM * CM) / (AM * AL + BM * BL + CM * CL);
                    CoefficientH = (AM * AM + BM * BM + CM * CM) / (AM * AH + BM * BH + CM * CH);

                    ALtangent = CoefficientL * AL - AM;
                    BLtangent = CoefficientL * BL - BM;
                    CLtangent = CoefficientL * CL - CM;
                    AHtangent = CoefficientH * AH - AM;
                    BHtangent = CoefficientH * BH - BM;
                    CHtangent = CoefficientH * CH - CM;


                    AngleCos = (AHtangent * ALtangent + BHtangent * BLtangent + CHtangent * CLtangent) / (Math.sqrt(AHtangent * AHtangent + BHtangent * BHtangent + CHtangent * CHtangent) * Math.sqrt(ALtangent * ALtangent + BLtangent * BLtangent + CLtangent * CLtangent));

                    AngleCos = Math.acos(AngleCos);

                    ANormalLine = BHtangent * CLtangent - CHtangent * BLtangent;
                    BNormalLine = 0 - (AHtangent * CLtangent - CHtangent * ALtangent);
                    CNormalLine = AHtangent * BLtangent - BHtangent * ALtangent;

                    if (AM != 0) {
                        OrientationValue = ANormalLine / AM;
                    } else if (BM != 0) {
                        OrientationValue = BNormalLine / BM;
                    } else {
                        OrientationValue = CNormalLine / CM;
                    }

                    if (OrientationValue > 0) {
                        Sum1 += AngleCos;
                        Count1++;

                    } else {
                        Sum2 += AngleCos;
                        Count2++;
                        //Sum +=2*Math.PI-AngleCos;
                    }

                }

                if (Sum1 > Sum2) {
                    Sum = Sum1 + (2 * Math.PI * Count2 - Sum2);
                } else {
                    Sum = (2 * Math.PI * Count1 - Sum1) + Sum2;
                }

                //平方米
                mtotalArea = (Sum - (Count - 2) * Math.PI) * Radius * Radius;
            } else { //非经纬度坐标下的平面多边形

                int i, j;
                //double j;
                double p1x, p1y;
                double p2x, p2y;
                for (i = Count - 1, j = 0; j < Count; i = j, j++) {

                    p1x = Point.get(i).getLongitude();
                    p1y = Point.get(i).getLatitude();

                    p2x = Point.get(i).getLongitude();
                    p2y = Point.get(i).getLatitude();

                    mtotalArea += p1x * p2y - p2x * p1y;
                }
                mtotalArea /= 2.0;
            }

            String string = null;
            if (mtotalArea < 100000) {
                string = String.format("%.5f平方米", mtotalArea);
            } else {
                string = String.format("%.5f平方公里", mtotalArea / 1000 / 1000);
            }
            return string;
        }
        return "";
    }

    final static double a = 6378245.0;
    final static double ee = 0.00669342162296594323;

    public static double[] transform(double wgLat, double wgLon) {
        double[] latlng = new double[2];
        if (outOfChina(wgLat, wgLon)) {
            latlng[0] = wgLat;
            latlng[1] = wgLon;
            return latlng;
        }
        double dLat = transformlat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformlng(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * Math.PI);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * Math.PI);
        latlng[0] = wgLat + dLat;
        latlng[1] = wgLon + dLon;
        return latlng;
    }


    public static double transformlat(double lng, double lat) {
        double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * Math.PI) + 20.0 * Math.sin(2.0 * lng * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * Math.PI) + 40.0 * Math.sin(lat / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * Math.PI) + 320 * Math.sin(lat * Math.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformlng(double lng, double lat) {
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * Math.PI) + 20.0 * Math.sin(2.0 * lng * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * Math.PI) + 40.0 * Math.sin(lng / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * Math.PI) + 300.0 * Math.sin(lng / 30.0 * Math.PI)) * 2.0 / 3.0;
        return ret;
    }

    private static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347) {
            return true;
        }
        if (lat < 0.8293 || lat > 55.8271) {
            return true;
        }
        return false;
    }

    /**
     * 获取两个坐标点之间的距离
     */
    public static double getDistance(GeoPoint sLatLng, GeoPoint eLatLng) {
        float[] results = new float[1];
        Location.distanceBetween(sLatLng.getLatitude(), sLatLng.getLongitude(), eLatLng.getLatitude(), eLatLng.getLongitude(), results);
        return results[0];
    }


    /**
     * 获取两条线的夹角
     *
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @return
     */
    public static int getRotationBetweenLines(float startX, float startY, float endX, float endY) {
        double rotation = 0;

        double k1 = (double) (startY - startY) / (startX * 2 - startX);
        double k2 = (double) (endY - startY) / (endX - startX);
        double tmpDegree = Math.atan((Math.abs(k1 - k2)) / (1 + k1 * k2)) / Math.PI * 180;

        if (endX > startX && endY < startY) {  //第一象限
            rotation = 90 - tmpDegree;
        } else if (endX > startX && endY > startY) //第二象限
        {
            rotation = 90 + tmpDegree;
        } else if (endX < startX && endY > startY) { //第三象限
            rotation = 270 - tmpDegree;
        } else if (endX < startX && endY < startY) { //第四象限
            rotation = 270 + tmpDegree;
        } else if (endX == startX && endY < startY) {
            rotation = 0;
        } else if (endX == startX && endY > startY) {
            rotation = 180;
        }

        return (int) rotation;
    }

    public static BoundingBox getMbtilesBoundingBox(File file) {
        SQLiteDatabase mDatabase = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READONLY);
        final Cursor cur = mDatabase.query("metadata", new String[]{"value"}, "name=?", new String[]{"bounds"}, null, null, null);
        BoundingBox boundingBox = new BoundingBox();
        if (cur.getCount() != 0) {
            cur.moveToFirst();
            String bounding = cur.getString(0);
            String[] s = bounding.split(",");
            boundingBox.set(Double.valueOf(s[3]), Double.valueOf(s[2]), Double.valueOf(s[1]), Double.valueOf(s[0]));
        }
        cur.close();
        mDatabase.close();
        return boundingBox;

    }

}
