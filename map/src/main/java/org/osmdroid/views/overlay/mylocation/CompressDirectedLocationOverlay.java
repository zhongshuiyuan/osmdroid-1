package org.osmdroid.views.overlay.mylocation;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.osmdroid.views.MapView;

public class CompressDirectedLocationOverlay extends DirectedLocationOverlay implements SensorEventListener {
    private final SensorManager mSensorManager;
    private MapView mapView;

    public CompressDirectedLocationOverlay(Context ctx, MapView mapView) {
        super(ctx);
        mSensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        this.mapView = mapView;
        final Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        if (sensor != null) {
            mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            if (event.values != null) {
                setBearing(event.values[0]);
                mapView.invalidate();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        sensor.getResolution();
    }

    @Override
    public void onDetach(MapView view) {
        super.onDetach(view);
        mSensorManager.unregisterListener(this);
    }
}
