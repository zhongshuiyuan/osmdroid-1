package org.osmdroid.views.overlay;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

import java.util.ArrayList;
import java.util.List;

public class RectangleOverlay extends OverlayWithIW {
    private List<GeoPoint> points = new ArrayList<>();

    public void setPoints(final List<GeoPoint> points) {
        this.points = points;
    }

    private Paint outlinePaint;
    private Paint inlinePaint;

    public RectangleOverlay() {
        outlinePaint = new Paint();
        outlinePaint.setStrokeWidth(outlineWidth);
        outlinePaint.setColor(outlineColor);
        inlinePaint = new Paint();
        inlinePaint.setColor(inlineColor);
    }


    private int outlineWidth = 1;
    private int outlineColor = Color.BLACK;
    private int inlineColor = Color.TRANSPARENT;

    public void setOutLineWidth(int outlineWidth) {
        this.outlineWidth = outlineWidth;

    }

    public void setOutLineColor(int outlineColor) {
        this.outlineColor = outlineColor;

    }

    public void setInLineColor(int inlineColor) {
        this.inlineColor = inlineColor;

    }

    /**
     * Add the point at the end of the polygon outline.
     * If geodesic mode has been set, the long segments will follow the earth "great circle".
     */
    public void addPoint(GeoPoint p) {
        points.add(p);
    }

    @Override
    public void draw(Canvas pCanvas, Projection pProjection) {
        super.draw(pCanvas, pProjection);
        if (points.size() < 2) {
            return;
        }
        GeoPoint p0 = points.get(0);
        GeoPoint p1 = points.get(1);
        float left = p0.getLongitude() > p1.getLongitude() ? pProjection.getLongPixelXFromLongitude(p1.getLongitude()) : pProjection.getLongPixelXFromLongitude(p0.getLongitude());
        float right = p0.getLongitude() > p1.getLongitude() ? pProjection.getLongPixelXFromLongitude(p0.getLongitude()) : pProjection.getLongPixelXFromLongitude(p1.getLongitude());
        float top = p0.getLatitude() > p1.getLatitude() ? pProjection.getLongPixelYFromLatitude(p0.getLatitude()) : pProjection.getLongPixelYFromLatitude(p1.getLatitude());
        float bottom = p0.getLatitude() > p1.getLatitude() ? pProjection.getLongPixelYFromLatitude(p1.getLatitude()) : pProjection.getLongPixelYFromLatitude(p0.getLatitude());

        //画外部矩形
        //上
        pCanvas.drawLine(left, top + outlineWidth / 2, right, top + outlineWidth / 2, outlinePaint);
        //右
        pCanvas.drawLine(right, top, right, bottom, outlinePaint);
        //下
        pCanvas.drawLine(left, bottom - outlineWidth / 2, right, bottom - outlineWidth / 2, outlinePaint);
        //左
        pCanvas.drawLine(left, top, left, bottom, outlinePaint);

        //画内部矩形

        pCanvas.drawRect(left + outlineWidth, top + outlineWidth, right - outlineWidth, bottom - outlineWidth, inlinePaint);
    }


    @Override
    public boolean onLongPress(MotionEvent event, MapView mapView) {
        Projection pj = mapView.getProjection();
        GeoPoint eventPos = (GeoPoint) pj.fromPixels((int) event.getX(), (int) event.getY());
        boolean tapped = false;
        if (tapped && onlongClickListener != null) {
            return onlongClickListener.onLongClick(this, mapView, eventPos);
        }
        return tapped;
    }

    private OnLongClickListener onlongClickListener;

    public void setOnlongClickListener(OnLongClickListener onlongClickListener) {
        this.onlongClickListener = onlongClickListener;
    }

    public interface OnLongClickListener {
        abstract boolean onLongClick(RectangleOverlay rectangleOverlay, MapView mapView, GeoPoint eventPos);
    }
}
