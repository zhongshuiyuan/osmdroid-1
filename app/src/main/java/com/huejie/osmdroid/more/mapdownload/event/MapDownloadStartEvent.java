package com.huejie.osmdroid.more.mapdownload.event;

import org.osmdroid.util.BoundingBox;

public class MapDownloadStartEvent {
    public BoundingBox boundingBox;
    public int zoomMax;
    public String mapType;


    public MapDownloadStartEvent(BoundingBox boundingBox, int zoomMax, String mapType) {
        this.boundingBox = boundingBox;
        this.zoomMax = zoomMax;
        this.mapType = mapType;
    }
}
