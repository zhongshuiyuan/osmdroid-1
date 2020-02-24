package com.huejie.osmdroid.model.favorites;


import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class FavoritesLatLng extends LitePalSupport implements Serializable {
    public long id;
    @Column(nullable = false)
    public String favoritesName;
    @Column(nullable = false)
    public String parentName;
    public double lat;
    public double lng;
    //经纬度  [114.306429682802,30.5906774460664],[114.328851388331,30.587755172198],[114.319365282146,30.5743240034357],[114.319365282146,30.5743240034357]
    public String coordinates;

}
