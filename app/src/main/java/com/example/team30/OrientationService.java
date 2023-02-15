package com.example.team30;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class OrientationService implements SensorEventListener {

    private static final int SENSOR_DELAY_MICROS = 16 * 1000; // 16ms

    private static OrientationService instance;
    private final Handler handler;
    private final SensorManager sensorManager;
    private final Sensor rotationSensor;

    private MutableLiveData<float[]> orientationValue;

    public static OrientationService singleton(SensorManager sensorManager){
        if(instance == null){
            instance = new OrientationService(sensorManager);
        }
        return instance;
    }

    protected OrientationService(SensorManager sensorManager){
        this.sensorManager = sensorManager;
        this.orientationValue = new MutableLiveData<>();

        // Get the default rotation vector sensor
        this.rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (rotationSensor == null) {
            throw new UnsupportedOperationException("Device does not have a rotation sensor");
        }

        // Use the Looper of the main thread to handle orientation updates
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void registerOrientationListener(){
        sensorManager.registerListener(this, rotationSensor, SENSOR_DELAY_MICROS, handler);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // Calculate the orientation from the rotation vector sensor
            float[] rotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

            float[] orientation = new float[3];
            SensorManager.getOrientation(rotationMatrix, orientation);

            // Convert the orientation angles to degrees and post the updated value
            float[] orientationDegrees = new float[] {
                    (float) Math.toDegrees(orientation[0]),
                    (float) Math.toDegrees(orientation[1]),
                    (float) Math.toDegrees(orientation[2])
            };
            this.orientationValue.postValue(orientationDegrees);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No implementation needed
    }

    private void unregisterOrientationListener() {
        sensorManager.unregisterListener(this);
    }

    public LiveData<float[]> getOrientation() {
        return this.orientationValue;
    }

    public void setMockOrientationSource(MutableLiveData<float[]> mockDataSource){
        unregisterOrientationListener();
        this.orientationValue = mockDataSource;

    }

}
