package com.androidlearning.albert.androidstepcounter;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager sensorManager;
    private TextView count;
    boolean activityRunning;
    private static final float SHAKE_THRESHOLD_GRAVITY = 15.0f;
    private static final int SHAKE_TIME_LAPSE = 500;
    private long mTimeOfLastShake;

    private int testInt = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        count = (TextView) findViewById(R.id.textCount);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


    }

    @Override
    protected void onResume() {
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        if(countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Not available!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityRunning = false;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(activityRunning) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//TASK l: COLLECT SENSOR VALUES ON ALL THREE AXIS
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
//TASK 2: CONVERT EACH ACCELEROMETER MEASUREMENT INTO
// A G-FORCE MEASUREMENT BY SUBTRACTING GRAVITY_EARTH.
                float gForceX = x - SensorManager.GRAVITY_EARTH;
                float gForceY = y - SensorManager.GRAVITY_EARTH;
                float gForceZ = z - SensorManager.GRAVITY_EARTH;

                //TASK 3: COMPUTE G-FORCE AS A DIRECTIONLESS MEASUREMENT
                // NOTE: G-FORCE WILL BE APPROXIMATELY l WHEN
                // THERE IS NO SHAKING MOVEMENT.
                double value = Math.pow(gForceX, 2.0) +
                        Math.pow(gForceY, 2.0) +
                        Math.pow(gForceZ, 2.0);
                float gForce = (float) Math.sqrt(value);
                if (gForce > SHAKE_THRESHOLD_GRAVITY){
                    final long now = System.currentTimeMillis();
                    if (mTimeOfLastShake + SHAKE_TIME_LAPSE > now)
                        return;

                    mTimeOfLastShake = now;
                    testInt++;
                    count.setText(String.valueOf(testInt));
                }




            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
