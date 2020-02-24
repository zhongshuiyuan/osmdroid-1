package com.huejie.osmdroid.overlay;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ClickableIconOverlay;

public class CustomIconOverlay extends ClickableIconOverlay {


    /**
     * save to be called in non-gui-thread
     *
     * @param data
     */
    public CustomIconOverlay(Object data) {
        super(data);
    }

    @Override
    public boolean onMarkerClicked(MapView mapView, int markerId, IGeoPoint makerPosition, Object markerData) {
        if (null != markClickListener) {
            markClickListener.onMarkerClicked(mapView, markerId, makerPosition, markerData);
        }
        return false;
    }

    @Override
    public boolean onMarkerLongPress(MapView mapView, int markerId, IGeoPoint geoPosition, Object data) {
        if (null != markLongClickListener) {
            markLongClickListener.onMarkerLongClicked(mapView, markerId, geoPosition, data);
        }
        return true;
    }


    private MarkClickListener markClickListener;

    public void setOnMarkClickListener(MarkClickListener markClickListener) {
        this.markClickListener = markClickListener;
    }

    public static interface MarkClickListener {
        boolean onMarkerClicked(MapView mapView, int markerId, IGeoPoint makerPosition, Object markerData);
    }

    private MarkLongClickListener markLongClickListener;

    public void setMarkLongClickListener(MarkLongClickListener markLongClickListener) {
        this.markLongClickListener = markLongClickListener;
    }

    public static interface MarkLongClickListener {
        boolean onMarkerLongClicked(MapView mapView, int markerId, IGeoPoint makerPosition, Object markerData);
    }
}
