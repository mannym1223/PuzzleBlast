package cs4330.cs.utep.testaccelerometer;

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
    private GameGrid grid;

    public GameTiltDetect(Context context){
        grid = GameGrid.getInstance();
        
        sensorManager = (SensorManager) context.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        initListeners();

    }


    public void initListeners() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        float[] rotationMatrix = new float[9];
        float orientationValues[] = new float[3];
        boolean rotation = false;

        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
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
            azimuth = orientationValues[0];
            pitch = orientationValues[1];
            roll = orientationValues[2];

            determineTilt();
        }

    }

    public void pause(){
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void determineTilt() {
        float degrRoll = (float) (Math.toDegrees(roll));
        float degrPitch = (float) (Math.toDegrees(pitch));

        if (degrRoll < -45) {
            Log.d("Tilt", "LeftTilt");
            grid.shiftLeft();
        }
        else if (degrRoll > 45) {
            Log.d("Tilt", "RightTilt");
            grid.shiftRight();
        }

        else if(degrPitch > 45){
            Log.d("Tilt", "UpTilt");
            grid.shiftUp();
        }

        else if(degrPitch < -45){
            Log.d("Tilt", "DownTilt");
            grid.shiftDown();
        }

        else{
            Log.d("Tilt", "Center");
        }
    }



}
