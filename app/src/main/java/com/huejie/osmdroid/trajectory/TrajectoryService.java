package com.huejie.osmdroid.trajectory;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;

import com.blankj.utilcode.utils.LocationUtils;
import com.huejie.osmdroid.trajectory.event.TrajectoryCancelCompleteEvent;
import com.huejie.osmdroid.trajectory.event.TrajectoryCancelEvent;
import com.huejie.osmdroid.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.concurrent.ScheduledExecutorService;

public class TrajectoryService extends Service implements LocationUtils.OnLocationChangeListener {
    private long createTime;
    private ScheduledExecutorService timer;

    public TrajectoryService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createTime = System.currentTimeMillis();
        TrajectoryList trajectoryList = new TrajectoryList();
        trajectoryList.createTime = createTime;
        trajectoryList.name = Util.getFromatDate(createTime, "yyyy-MM-dd HH:mm:ss");
        trajectoryList.save();
        LocationUtils.register(5000, 5, this);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTrajectoryCancelEvent(TrajectoryCancelEvent event) {
        //取消操作
        LitePal.deleteAll(TrajectoryList.class, "createTime=?", createTime + "");
        LitePal.deleteAll(Trajectory.class, "tag=?", createTime + "");
        EventBus.getDefault().post(new TrajectoryCancelCompleteEvent());

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationUtils.unregister();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void getLastKnownLocation(Location location) {
        //精度小于5米才收集
        if (null != location ) {
//            && location.getAccuracy() <= 5
            Trajectory trajectory = new Trajectory();
            trajectory.lat = location.getLatitude();
            trajectory.lng = location.getLatitude();
            trajectory.createTime = System.currentTimeMillis();
            trajectory.tag = createTime;
            trajectory.save();
        }

        System.out.println(LitePal.findAll(TrajectoryList.class).size());
        System.out.println(LitePal.findAll(Trajectory.class).size());


    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

}
