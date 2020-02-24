package com.huejie.osmdroid.more.mapdownload;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.huejie.osmdroid.R;
import com.huejie.osmdroid.more.mapdownload.event.MapCompleteDownloadEvent;
import com.huejie.osmdroid.more.mapdownload.event.MapDownloadStartEvent;
import com.huejie.osmdroid.more.mapdownload.event.MapDownloadingEvent;
import com.huejie.osmdroid.more.mapdownload.event.MapPauseDownloadEvent;
import com.huejie.osmdroid.more.mapdownload.event.MapStopDownloadEvent;
import com.huejie.osmdroid.more.mapdownload.event.ServiceStartEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.osmdroid.util.BoundingBox;


public class MapDownloader {
    private static final int PUSH_NOTIFICATION_ID = 0x001;
    private static final String PUSH_CHANNEL_ID = "push_channel_id";
    private static final String PUSH_CHANNEL_NAME = "离线地图下载";
    private static final String MAP_DOWNLOAD_NOTIFY = "map_download_notify";
    private static final String MAP_DOWNLOAD_CODE = "map_download_code";
    private static final int STOP_STATUS_SWITCH = 0;
    private static final int PAUSE_STATUS_SWITCH = 1;
    private Context context;
    private BoundingBox boundingBox;
    private int zoomMax;
    private String mapType;
    private MapDownloadReceiver mapDownloadReceiver;
    private Notification notification;

    /**
     * @param context     上下文对象
     * @param boundingBox 边界参数
     */

    public MapDownloader(Context context, BoundingBox boundingBox, int zoomMax, String mapType) {
        this.context = context;

        this.boundingBox = boundingBox;
        this.zoomMax = zoomMax;
        this.mapType = mapType;
        registBroadcastReceiver();

    }

    public void showNotificationAndDownload() {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("离线地图下载")
                .setContentTitle("离线地图下载")
                .setContentText("正在下载中...")
                .setWhen(System.currentTimeMillis())
                .setVibrate(new long[]{0})
//                .setOngoing(true)
                .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)
                .setSound(null);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder.setCustomBigContentView(createContentView()).setCustomContentView(createContentViewSmall());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            // 设置通知出现时不震动
            channel.enableVibration(false);
            channel.setVibrationPattern(new long[]{0});
            mNotificationManager.createNotificationChannel(channel);
            builder.setChannelId(PUSH_CHANNEL_ID);
        }
        notification = builder.build();
        mNotificationManager.notify(PUSH_NOTIFICATION_ID, notification);

        Intent intent = new Intent(context, MapDownloadService.class);
        context.startService(intent);


    }

    private int progress;

    /**
     * 正在下载
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMapDownloadEvent(MapDownloadingEvent event) {
        if (event.progress > progress) {
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notification.bigContentView.setProgressBar(R.id.progressBar, 100, event.progress, false);
            notification.bigContentView.setTextViewText(R.id.tv_progress, event.progress + "%");
            notification.contentView.setProgressBar(R.id.progressBar, 100, event.progress, false);
            notification.contentView.setTextViewText(R.id.tv_progress, event.progress + "%");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                // 设置通知出现时不震动
                channel.enableVibration(false);
                channel.setVibrationPattern(new long[]{0});
                channel.setSound(null, null);
                if (mNotificationManager != null) {
                    mNotificationManager.createNotificationChannel(channel);
                }

            }
            mNotificationManager.notify(PUSH_NOTIFICATION_ID, notification);
            progress = event.progress;
        }
    }

    /**
     * 下载完成
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMapCompleteEvent(MapCompleteDownloadEvent event) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(PUSH_NOTIFICATION_ID);
        unregistBroadcastReceiver();
    }

    /**
     * 服务启动了
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartEvent(ServiceStartEvent event) {
        EventBus.getDefault().post(new MapDownloadStartEvent(boundingBox, zoomMax, mapType));
    }

    public RemoteViews createContentView() {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.layout_download);
        setCommonClickPending(remoteView);
        return remoteView;
    }

    public RemoteViews createContentViewSmall() {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.layout_download_small);
        setCommonClickPending(remoteView);
        return remoteView;
    }

    // 停止或暂停
    private void setCommonClickPending(RemoteViews view) {
        Intent stop = new Intent(MAP_DOWNLOAD_NOTIFY);
        stop.putExtra(MAP_DOWNLOAD_CODE, STOP_STATUS_SWITCH);
        PendingIntent p1 = PendingIntent.getBroadcast(context, STOP_STATUS_SWITCH, stop, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.tv_stop, p1);

        Intent pause = new Intent(MAP_DOWNLOAD_NOTIFY);
        pause.putExtra(MAP_DOWNLOAD_CODE, PAUSE_STATUS_SWITCH);
        PendingIntent p2 = PendingIntent.getBroadcast(context, PAUSE_STATUS_SWITCH, pause, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.tv_pause, p2);
    }


    private void registBroadcastReceiver() {
        mapDownloadReceiver = new MapDownloadReceiver();
        IntentFilter filter = new IntentFilter(MAP_DOWNLOAD_NOTIFY);
        context.registerReceiver(mapDownloadReceiver, filter);
        EventBus.getDefault().register(this);

    }

    private void unregistBroadcastReceiver() {
        context.unregisterReceiver(mapDownloadReceiver);
        EventBus.getDefault().unregister(this);
    }

    private boolean pause;

    private class MapDownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context activity, Intent intent) {
            int code = intent.getIntExtra(MAP_DOWNLOAD_CODE, -1);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (code == STOP_STATUS_SWITCH) {
                //停止下载
                EventBus.getDefault().post(new MapStopDownloadEvent());
                mNotificationManager.cancel(PUSH_NOTIFICATION_ID);
                unregistBroadcastReceiver();
                context.stopService(new Intent(context, MapDownloadService.class));
            } else if (code == PAUSE_STATUS_SWITCH) {
                //暂停下载
                pause = !pause;
                notification.bigContentView.setTextViewText(R.id.tv_pause, pause ? "继续" : "暂停");
                notification.contentView.setTextViewText(R.id.tv_pause, pause ? "继续" : "暂停");
                mNotificationManager.notify(PUSH_NOTIFICATION_ID, notification);
                if (pause) {
                    EventBus.getDefault().post(new MapPauseDownloadEvent());
                } else {
                    EventBus.getDefault().post(new MapDownloadStartEvent(boundingBox, zoomMax, mapType));
                }
            }
        }
    }

}
