package com.huejie.osmdroid.manager;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.util.DisplayMetrics;

import com.blankj.utilcode.utils.LocationUtils;
import com.blankj.utilcode.utils.SizeUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.app.AppContext;
import com.huejie.osmdroid.listener.SimpleOnLocationChangeListener;
import com.huejie.osmdroid.tileprovider.MySqlTileWriter;
import com.huejie.osmdroid.tileprovider.MyTileProvider;
import com.huejie.osmdroid.tilesource.GoogleTileSource;
import com.huejie.osmdroid.tilesource.TianDiTuTileSource;
import com.huejie.osmdroid.util.Config;
import com.huejie.osmdroid.util.GISUtils;
import com.huejie.osmdroid.util.Util;

import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.CompressDirectedLocationOverlay;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;

import java.util.ArrayList;
import java.util.List;

public class MapViewManager {
    private static MapViewManager mapViewManager;
    private Context mContext;
    public MapView mMapView;
    public ITileSource currentTileSource = new GoogleTileSource(Config.TileSourceName.GOOGLEROAD);
    public TilesOverlay tianCvaTile;
    public static final double MinZoomLevel = 3.0;
    private RotationGestureOverlay mRotationGestureOverlay;
    private ScaleBarOverlay mScaleBarOverlay;
    private CompassOverlay mCompassOverlay;
    private DirectedLocationOverlay mLocationOverlay;
    private List<Overlay> overlayList = new ArrayList<>();


    private MapViewManager() {

    }

    public static MapViewManager getInstance() {
        if (null == mapViewManager) {
            mapViewManager = new MapViewManager();
        }
        return mapViewManager;
    }

    public void init(Context context, MapView mapView) {
        mContext = context;
        mMapView = mapView;
        if (null != mapEventListener) {
            mapEventListener = null;
        }
        initMap();
    }

    private void initMap() {
        mMapView.setDrawingCacheEnabled(true);
        mMapView.setMultiTouchControls(true);// 触控放大缩小
        mMapView.setMaxZoomLevel(Double.valueOf(AppContext.sp.getInt(Config.ZDJB)));
        mMapView.setMinZoomLevel(MinZoomLevel);
        mMapView.getController().setZoom(12.0);
        mMapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        mMapView.setUseDataConnection(true);
        //是否显示地图数据源
        mMapView.getOverlayManager().getTilesOverlay().setEnabled(true);
        //加载谷歌地图，设置地图数据源的形式
        String mapType = AppContext.sp.getString(Config.CURRENT_MAP);
        mMapView.setTileProvider(new MyTileProvider(mContext, Util.getTileSourceByType(mapType), new MySqlTileWriter()));
        if (mapType.equals(Config.TileSourceName.TIANDITUROAD) || mapType.equals(Config.TileSourceName.TIANDITUIMAGE) || mapType.equals(Config.TileSourceName.TIANDITUTERRAIN)) {
            tianCvaTile = new TilesOverlay(new MyTileProvider(mContext, new TianDiTuTileSource(Config.TileSourceName.TIANDITUCVA), new MySqlTileWriter()), mContext);
            mMapView.getOverlays().add(0, tianCvaTile);
        }
        overlayList.clear();
        MapEventsOverlay eventOverlay = new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                if (null != mapEventListener) {
                    return mapEventListener.singleTapConfirmedHelper(p);
                }
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                if (null != mapEventListener) {
                    return mapEventListener.longPressHelper(p);
                }
                return false;
            }
        });
        overlayList.add(eventOverlay);
        mMapView.getOverlays().add(eventOverlay);
    }

    public void setCurrentMap(String currentMap) {
        mMapView.setTileProvider(new MyTileProvider(mContext, Util.getTileSourceByType(currentMap), new MySqlTileWriter()));
        if (currentMap.equals(Config.TileSourceName.TIANDITUROAD) || currentMap.equals(Config.TileSourceName.TIANDITUIMAGE) || currentMap.equals(Config.TileSourceName.TIANDITUTERRAIN)) {
            tianCvaTile = new TilesOverlay(new MyTileProvider(mContext, new TianDiTuTileSource(Config.TileSourceName.TIANDITUCVA), new MySqlTileWriter()), mContext);
            mMapView.getOverlays().add(0, tianCvaTile);
        } else {
            if (null != tianCvaTile) {
                mMapView.getOverlays().remove(tianCvaTile);
                tianCvaTile = null;
            }
        }
    }

    public MapViewManager setRotationGesture() {
        //地图自由旋转
        if (!overlayList.contains(mRotationGestureOverlay)) {
            mRotationGestureOverlay = new RotationGestureOverlay(mMapView);
            mRotationGestureOverlay.setEnabled(true);
            overlayList.add(mRotationGestureOverlay);
            mMapView.getOverlays().add(mRotationGestureOverlay);
        }
        return mapViewManager;
    }


    public void removeRotationGesture() {
        //移除自由旋转
        if (overlayList.contains(mRotationGestureOverlay)) {
            overlayList.remove(mRotationGestureOverlay);
            mMapView.getOverlays().remove(mRotationGestureOverlay);
        }

    }

    private MapEventListener mapEventListener;

    public void setMapEventListener(MapEventListener mapEventListener) {
        this.mapEventListener = mapEventListener;
    }

    public interface MapEventListener {
        boolean singleTapConfirmedHelper(GeoPoint p);

        boolean longPressHelper(GeoPoint p);

    }

    public void setMaxZoomLevel(double maxZoomLevel) {
        mMapView.setMaxZoomLevel(maxZoomLevel);
    }

    public MapViewManager addScaleBar() {
        //添加比例尺
        if (!overlayList.contains(mScaleBarOverlay)) {
            final DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            mScaleBarOverlay = new ScaleBarOverlay(mMapView);
            mScaleBarOverlay.setAlignRight(true);
            mScaleBarOverlay.setAlignBottom(true); //底部显示
            mScaleBarOverlay.setMaxLength(2);
            mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 8, SizeUtils.dp2px(10));
            mMapView.getOverlays().add(mScaleBarOverlay);
            overlayList.add(mScaleBarOverlay);
        }
        return mapViewManager;
    }

    public void removeScaleBar() {
        //添加比例尺
        if (overlayList.contains(mScaleBarOverlay)) {
            mMapView.getOverlays().remove(mScaleBarOverlay);
            overlayList.remove(mScaleBarOverlay);
        }
    }


    public MapViewManager addCompass() {
        //添加比例尺
        if (!overlayList.contains(mCompassOverlay)) {
            mCompassOverlay = new CompassOverlay(mContext, new InternalCompassOrientationProvider(mContext), mMapView);
            mCompassOverlay.enableCompass();
            mCompassOverlay.setCompassCenter(20f, 60f);
            mMapView.getOverlays().add(mCompassOverlay);
            overlayList.add(mCompassOverlay);
        }
        return mapViewManager;
    }

    public void removeCompass() {
        //添加比例尺
        if (overlayList.contains(mCompassOverlay)) {
            mMapView.getOverlays().remove(mCompassOverlay);
            overlayList.remove(mCompassOverlay);
        }
    }


    public void removeLocation() {
        if (overlayList.contains(mLocationOverlay)) {
            mMapView.getOverlays().remove(mLocationOverlay);
            overlayList.remove(mLocationOverlay);

        }
    }

    public MapViewManager location() {
        //定位
        LocationUtils.register(-1, 10, new SimpleOnLocationChangeListener() {
            @Override
            public void getLastKnownLocation(Location location) {
                GeoPoint mLocation;
                if (GISUtils.judgeMapNeedCorrection(currentTileSource)) {
                    double[] res = GISUtils.transform(location.getLatitude(), location.getLongitude());
                    mLocation = new GeoPoint(res[0], res[1]);
                } else {
                    mLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                }
                //设置导航图标
                if (!overlayList.contains(mLocationOverlay)) {
                    mLocationOverlay = new CompressDirectedLocationOverlay(mContext, mMapView);
                    mLocationOverlay.setAccuracy((int) location.getAccuracy());
                    mLocationOverlay.setDirectionArrow(((BitmapDrawable) mContext.getResources().getDrawable(R.mipmap.position)).getBitmap());
                    mMapView.getOverlays().add(mLocationOverlay);
                    overlayList.add(mLocationOverlay);
                }
                mLocationOverlay.setLocation(mLocation);

            }
        });
        return mapViewManager;
    }

    public void toLocationPosition() {
        if (mLocationOverlay != null) {
            mMapView.getController().setCenter(mLocationOverlay.getLocation());
        }

    }

    public void clean() {
        mMapView.getOverlays().clear();
        if (tianCvaTile != null) {
            mMapView.getOverlays().add(0, tianCvaTile);
        }
        mMapView.getOverlays().addAll(overlayList);
        mMapView.invalidate();
    }


}
