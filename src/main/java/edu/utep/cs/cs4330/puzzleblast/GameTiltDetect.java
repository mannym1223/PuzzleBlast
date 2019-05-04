package edu.utep.cs.cs4330.puzzleblast;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;


public class GameTiltDetect implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private float[] accelerometerValues;
    private float[] magnometerValues;
    private float azimuth;
    private float pitch;
    private float roll;
    private float azimuth_last;
    private float roll_last;
    private float pitch_last;
    private GameGrid grid;

    public GameTiltDetect(Context context){
        grid = GameGrid.getInstance();

        sensorManager = (SensorManager) context.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        initListeners();

    }


    public void initListeners() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        float[] rotationMatrix = new float[9];
        float orientationValues[] = new float[3];
        boolean rotation = false;
        long curTime = System.currentTimeMillis();
        long lastUpdate = 0;

        switch (sensorType) {
            case Sensor.TYPE_GRAVITY:
                accelerometerValues = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magnometerValues = event.values.clone();
                break;
            default:
                return;
        }

        if(accelerometerValues != null && magnometerValues != null){
            rotation = SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerValues, magnometerValues);
        }

        if (rotation) {
            SensorManager.getOrientation(rotationMatrix, orientationValues);
            if ((curTime - lastUpdate) > 100) {
                lastUpdate = curTime;

                azimuth = orientationValues[0];
                pitch = orientationValues[1];
                roll = orientationValues[2];

                determineTilt(roll, roll_last, pitch, pitch_last);

                azimuth_last = azimuth;
                pitch_last = pitch;
                roll_last = roll;

            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void determineTilt(float roll, float roll_last, float pitch, float pitch_last) {
        float degrRoll = (float) (Math.toDegrees(roll));
        float degrPitch = (float) (Math.toDegrees(pitch));
        float degrRoll_last = (float) (Math.toDegrees(roll_last));
        float degrPitch_last = (float) (Math.toDegrees(pitch_last));

        if((degrRoll > 90 || degrRoll < -90)){
            Log.d("Tilt", "Faulty Reading:");
        }
        else if ((degrPitch > 30) && Math.abs(degrPitch - degrPitch_last) > 30){
            Log.d("Tilt", "UpTilt: " + degrPitch);
            onUpTilt();
        }
        else if ((degrPitch < -30) && Math.abs(degrPitch - degrPitch_last) > 30){
            Log.d("Tilt", "DowntTilt: " + degrPitch);
            onDownTilt();
        }

        else if((degrRoll < -30) && Math.abs(degrRoll - degrRoll_last) > 30){
            Log.d("Tilt", "LeftTilt: " + degrRoll);
            onLeftTilt();
        }

        else if((degrRoll > 30) && Math.abs(degrRoll - degrRoll_last) > 30){
            Log.d("Tilt", "RightTilt: " + degrRoll);
            onRightTilt();
        }

        else{
            Log.d("Tilt", "Center");
            onCentered();
        }

    }

    public void onLeftTilt() {

    }

    public void onRightTilt() {

    }
    public void onUpTilt() {

    }

    public void onDownTilt() {

    }
    public void onCentered() {

    }

}