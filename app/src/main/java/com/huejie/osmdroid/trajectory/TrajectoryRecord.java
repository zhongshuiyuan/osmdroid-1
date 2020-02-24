package com.huejie.osmdroid.trajectory;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.widget.RemoteViews;

import com.blankj.utilcode.utils.ServiceUtils;
import com.huejie.osmdroid.R;
import com.huejie.osmdroid.trajectory.event.TrajectoryCancelCompleteEvent;
import com.huejie.osmdroid.trajectory.event.TrajectoryCancelEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 记录轨迹
 */
public class TrajectoryRecord {
    private Context context;
    private static final int PUSH_NOTIFICATION_ID = 0x001;
    private static final String PUSH_CHANNEL_ID = "TRAJECTORY_PUSH_NOTIFY_ID";
    private static final String PUSH_CHANNEL_NAME = "TRAJECTORY_PUSH_NOTIFY_NAME";
    private static final String TRAJECTORY_NOTIFY = "trajectory_notify";
    private static final String TRAJECTORY_CODE = "trajectory_code";
    private static final int STOP_STATUS_SWITCH = 0;
    private static final int CANCEL_STATUS_SWITCH = 1;
    private NotificationManager mNotificationManager;
    private Notification notification;
    private TrajectoryReceiver trajectoryReceiver;


    public TrajectoryRecord(Context context) {
        this.context = context;

    }

    public void startTrajectoryRecord() {

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("记录当前位置信息")
                .setContentTitle("记录当前位置信息")
                .setContentText("正在记录当前位置信息...")
                .setWhen(System.currentTimeMillis())
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder.setCustomBigContentView(createContentView()).setCustomContentView(createContentViewSmall());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
            builder.setChannelId(PUSH_CHANNEL_ID);
        }
        notification = builder.build();
        mNotificationManager.notify(PUSH_NOTIFICATION_ID, notification);

        if (!ServiceUtils.isServiceRunning(context, "TrajectoryService")) {
            context.startService(new Intent(context, TrajectoryService.class));
        }
        registBroadcastReceiver();

    }

    public RemoteViews createContentView() {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.layout_record_gj);
        setCommonClickPending(remoteView);
        return remoteView;
    }

    public RemoteViews createContentViewSmall() {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.layout_record_gj_small);
        setCommonClickPending(remoteView);
        return remoteView;
    }

    // 停止或暂停
    private void setCommonClickPending(RemoteViews view) {
        Intent stop = new Intent(TRAJECTORY_NOTIFY);
        stop.putExtra(TRAJECTORY_CODE, STOP_STATUS_SWITCH);
        PendingIntent p1 = PendingIntent.getBroadcast(context, STOP_STATUS_SWITCH, stop, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.tv_confirm, p1);

        Intent cancel = new Intent(TRAJECTORY_NOTIFY);
        cancel.putExtra(TRAJECTORY_CODE, CANCEL_STATUS_SWITCH);
        PendingIntent p2 = PendingIntent.getBroadcast(context, CANCEL_STATUS_SWITCH, cancel, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.tv_cancel, p2);
    }

    public void stopTrajectoryRecord() {
        if (ServiceUtils.isServiceRunning(context, "TrajectoryService")) {
            context.stopService(new Intent(context, TrajectoryService.class));
        }
        unregistBroadcastReceiver();
    }

    private void registBroadcastReceiver() {
        trajectoryReceiver = new TrajectoryReceiver();
        IntentFilter filter = new IntentFilter(TRAJECTORY_NOTIFY);
        context.registerReceiver(trajectoryReceiver, filter);
        EventBus.getDefault().register(this);
    }

    private void unregistBroadcastReceiver() {
        context.unregisterReceiver(trajectoryReceiver);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTrajectoryCancelCompleteEvent(TrajectoryCancelCompleteEvent event) {
        //取消操作完成
        mNotificationManager.cancel(PUSH_NOTIFICATION_ID);
        stopTrajectoryRecord();

    }

    private class TrajectoryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context activity, Intent intent) {
            int code = intent.getIntExtra(TRAJECTORY_CODE, -1);
            if (code == STOP_STATUS_SWITCH) {
                mNotificationManager.cancel(PUSH_NOTIFICATION_ID);
                stopTrajectoryRecord();
            }
            if (code == CANCEL_STATUS_SWITCH) {
                //删除掉记录的轨迹信息
                EventBus.getDefault().post(new TrajectoryCancelEvent());
            }


        }
    }
}
