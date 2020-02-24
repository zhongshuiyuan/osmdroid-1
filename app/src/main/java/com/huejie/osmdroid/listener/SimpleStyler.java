package com.huejie.osmdroid.listener;

import com.huejie.osmdroid.util.GISUtils;

import org.bonuspack.kml.KmlFeature;
import org.bonuspack.kml.KmlLineString;
import org.bonuspack.kml.KmlPlacemark;
import org.bonuspack.kml.KmlPoint;
import org.bonuspack.kml.KmlPolygon;
import org.bonuspack.kml.KmlTrack;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class SimpleStyler implements KmlFeature.Styler {
    private ITileSource tileSource;

    private SimpleStyler() {
    }

    public SimpleStyler(ITileSource tileSource) {
        this.tileSource = tileSource;
    }

    @Override
    public void onFeature(Overlay overlay, KmlFeature kmlFeature) {

    }

    @Override
    public void onPoint(Marker marker, KmlPlacemark kmlPlacemark, KmlPoint kmlPoint) {
        if (GISUtils.judgeMapNeedCorrection(tileSource)) {
            ArrayList<GeoPoint> points = kmlPoint.mCoordinates;
            for (int i = 0; i < points.size(); i++) {
                GeoPoint point = points.get(i);
                double[] d = GISUtils.transform(point.getLatitude(), point.getLongitude());
                points.get(i).setLongitude(d[1]);
                points.get(i).setLatitude(d[0]);
                marker.setPosition(new GeoPoint(d[0], d[1]));
                marker.setInfoWindow(null);

            }
        }
    }

    @Override
    public void onLineString(Polyline polyline, KmlPlacemark kmlPlacemark, KmlLineString kmlLineString) {
        if (GISUtils.judgeMapNeedCorrection(tileSource)) {
            ArrayList<GeoPoint> points = kmlLineString.mCoordinates;
            for (int i = 0; i < points.size(); i++) {
                GeoPoint point = points.get(i);
                double[] d = GISUtils.transform(point.getLatitude(), point.getLongitude());
                points.get(i).setLongitude(d[1]);
                points.get(i).setLatitude(d[0]);

            }

        }
    }

    @Override
    public void onPolygon(Polygon polygon, KmlPlacemark kmlPlacemark, KmlPolygon kmlPolygon) {
        if (GISUtils.judgeMapNeedCorrection(tileSource)) {
            ArrayList<GeoPoint> points = kmlPolygon.mCoordinates;
            for (int i = 0; i < points.size(); i++) {
                GeoPoint point = points.get(i);
                double[] d = GISUtils.transform(point.getLatitude(), point.getLongitude());
                points.get(i).setLongitude(d[1]);
                points.get(i).setLatitude(d[0]);

            }

        }
    }

    @Override
    public void onTrack(Polyline polyline, KmlPlacemark kmlPlacemark, KmlTrack kmlTrack) {
        if (GISUtils.judgeMapNeedCorrection(tileSource)) {
            ArrayList<GeoPoint> points = kmlTrack.mCoordinates;
            for (int i = 0; i < points.size(); i++) {
                GeoPoint point = points.get(i);
                double[] d = GISUtils.transform(point.getLatitude(), point.getLongitude());
                points.get(i).setLongitude(d[1]);
                points.get(i).setLatitude(d[0]);

            }

        }
    }
}
