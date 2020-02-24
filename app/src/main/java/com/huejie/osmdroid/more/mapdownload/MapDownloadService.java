package com.huejie.osmdroid.more.mapdownload;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.huejie.osmdroid.manager.MapViewManager;
import com.huejie.osmdroid.model.MapDownloadHistory;
import com.huejie.osmdroid.more.mapdownload.event.MapCompleteDownloadEvent;
import com.huejie.osmdroid.more.mapdownload.event.MapDownloadStartEvent;
import com.huejie.osmdroid.more.mapdownload.event.MapDownloadingEvent;
import com.huejie.osmdroid.more.mapdownload.event.MapPauseDownloadEvent;
import com.huejie.osmdroid.more.mapdownload.event.MapStopDownloadEvent;
import com.huejie.osmdroid.more.mapdownload.event.ServiceStartEvent;
import com.huejie.osmdroid.tileprovider.MySqlTileWriter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.osmdroid.util.BoundingBox;

public class MapDownloadService extends Service {
    private MyCacheManager manager;

    public MapDownloadService() {

    }

    private long time;

    @Override
    public void onCreate() {
        super.onCreate();
        time = System.currentTimeMillis();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMapStopEvent(MapStopDownloadEvent event) {
        manager.cancelAllJobs();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMapPauseEvent(MapPauseDownloadEvent event) {
        manager.cancelAllJobs();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMapStartEvent(MapDownloadStartEvent event) {
        manager = new MyCacheManager(MapViewManager.getInstance().mMapView, new MySqlTileWriter(true, time, event.mapType));
        startDownLoad(event.boundingBox, event.zoomMax, 1);
        //保存下载信息
        MapDownloadHistory history = new MapDownloadHistory();
        history.id = time;
        history.mapType = event.mapType;
        history.saveOrUpdate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        EventBus.getDefault().post(new ServiceStartEvent());
        return START_NOT_STICKY;
    }

    private void startDownLoad(BoundingBox boundingBox, int zoomMax, int zoomMin) {
        try {
            //下载区域
            manager.downloadAreaAsyncNoUI(this, boundingBox, zoomMin, zoomMax, new MyCacheManager.CacheManagerCallback() {
                @Override
                public void onTaskComplete() {
                    EventBus.getDefault().post(new MapCompleteDownloadEvent());
                }

                @Override
                public void updateProgress(int progress, int currentZoomLevel, int zoomMin, int zoomMax) {
                    EventBus.getDefault().post(new MapDownloadingEvent(progress));


                }

                @Override
                public void downloadStarted() {

                }

                @Override
                public void setPossibleTilesInArea(int total) {

                }

                @Override
                public void onTaskFailed(int errors) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
