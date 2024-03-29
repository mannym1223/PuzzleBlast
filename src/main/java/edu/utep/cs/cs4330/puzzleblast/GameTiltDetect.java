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
    private float roll_last;
    private float pitch_last;

    public GameTiltDetect(Context context){
        sensorManager = (SensorManager) context.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        initListeners();

    }

    /**
     * Initialize sensor listeners, using SensorManager to register device sensors.
     *
     */
    public void initListeners() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     *
     * Gets the sensor type provided by the sensor event.
     * Creates a rotation matrix to map device coordinates.
     * Creates a orientation matrix to map device's relative position.
     * Determines the sensor type and stores readings in their respective arrays
     * Provides an interval of 100 milliseconds and compares the current sensor readings to previous
     * Obtains the pitch and roll values from orientation matrix.
     *
     * @param event the sensor event from the device
     */
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

                float azimuth = orientationValues[0];
                float pitch = orientationValues[1];
                float roll = orientationValues[2];

                determineTilt(roll, roll_last, pitch, pitch_last);

                float azimuth_last = azimuth;
                pitch_last = pitch;
                roll_last = roll;

            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     *
     * Converts roll and pitch readings into their respective angles
     * Checks to see which direction the device is tilted based on the roll and pitch angles
     *
     * @param roll the current roll value of sensor reading
     * @param roll_last the previous roll value of sensor reading
     * @param pitch  the current pitch value of sensor reading
     * @param pitch_last the previous pitch value of sensor reading
     */
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

    /**
     * To be overriden called to update grid depending on direction of the tilt
     *
     */
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