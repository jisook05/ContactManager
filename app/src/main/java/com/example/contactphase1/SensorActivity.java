package com.example.contactphase1;

/*
 *
 * jxk161230 cs4301.002 ContactPhase2; April 7, 2020
 * Shake Device to reverse contact list order alphabetically from Z-A
 *
 * */

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;


public class SensorActivity implements SensorEventListener {
    private SensorManager mSensorManager;

    // If gravity accel is over 2.7f, register as a shake
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;

    //Ignore after 0.5sec
    private static final float SHAKE_SKIP_TIME_MS = 500;
    private long mShakeTime;
    int mShakeCount = 0;


    private OnShakeListener mListener;

    public void SetOnShakeListener(OnShakeListener listener){
        this.mListener=listener;
    }

    public interface OnShakeListener{
        void onShake(int count);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            // get sensor values
            float x = (event.values[0]);
            float y = (event.values[1]);
            float z = (event.values[2]);

//            Log.d("Coordinate X", String.valueOf(x));
//            Log.d("Coordinate Y", String.valueOf(y));
//            Log.d("Coordinate Z", String.valueOf(z));


            float gX= x/ SensorManager.GRAVITY_EARTH;
            float gY= y/ SensorManager.GRAVITY_EARTH;
            float gZ= z/ SensorManager.GRAVITY_EARTH;

            // force close to 1 when there is no movement
            float gravityForce= (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ );

            //If shake force is > 2.7f
            if(gravityForce> SHAKE_THRESHOLD_GRAVITY){

                //get current time
                final long currentTime = System.currentTimeMillis();

                if(mShakeTime + SHAKE_SKIP_TIME_MS > currentTime){
                    return;
                }

                mShakeTime= currentTime;

                //Increase shake count
                mShakeCount++;
                Log.d("Number of Shakes_____:", String.valueOf(mShakeCount));


                mListener.onShake(mShakeCount);


            }


        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
