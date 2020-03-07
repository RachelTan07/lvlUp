package com.example.levelup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Main Screen XP Bar
    private ProgressBar mXPBar;
    private TextView mLevelText;
    private Handler mHandler = new Handler();

    // Main Screen Step Counter
    TextView tv_steps;
    SensorManager sensorManager;
    boolean running = false;
    int currentSteps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Progress Bar Init
        mXPBar = findViewById(R.id.xpbar);
        mLevelText = findViewById(R.id.xpmax);

        // Step Counter Init
        tv_steps = findViewById(R.id.tv_steps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Set xp bar to number of steps
                while(currentSteps < 100){
                    android.os.SystemClock.sleep(50);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mXPBar.setProgress(currentSteps);
                        }
                    });
                }

                // Display level up when xp bar is filled
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (currentSteps == 100) {
                            mLevelText.setVisibility(View.VISIBLE);
                            running = false;
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Sensor not found!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        running = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running) {

            currentSteps = (int) event.values[0];
            if (currentSteps > 100) {
                currentSteps = currentSteps % 100;
            }
            tv_steps.setText(String.valueOf(currentSteps));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
