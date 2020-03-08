package com.example.levelup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Level;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Main Screen XP Bar
    private ProgressBar mXPBar;
    private Handler mHandler = new Handler();
    public boolean levelupDisplay = false;

    // Main Screen Step Counter
    TextView tv_steps;
    SensorManager sensorManager;
    boolean running = false;
    int currentSteps = 0;

    // Navigation
    Button mTraining;
    Button mAchievements;
    Button mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets fullscreen and remove title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        // Progress Bar Init
        mXPBar = findViewById(R.id.xpbar);

        // Step Counter Init
        tv_steps = findViewById(R.id.tv_steps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mTraining = findViewById(R.id.trainingbutton);
        mAchievements = findViewById(R.id.achievementsbutton);
        mMap = findViewById(R.id.mapbutton);

        mTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Training.class));
            }
        });

        mAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Achievements.class));
            }
        });

        mMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Map.class));
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Set XP bar to number of steps
                while (currentSteps < 100) {
                    android.os.SystemClock.sleep(2);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // Update XP Bar
                            mXPBar.setProgress(currentSteps);

                            if (currentSteps == 50) {
                                onPause();
                                openDialog();
                            }
                        }
                    });
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

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
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.unregisterListener(this);
        } else {
            Toast.makeText(this, "Sensor not found!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running) {
            currentSteps = ((int) event.values[0]) % 100;
            tv_steps.setText(String.valueOf(currentSteps));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    // Open level up dialog
    public void openDialog() {
        LevelDialog ld = new LevelDialog();

        if (!levelupDisplay) {
            levelupDisplay = true;
            ld.show(getSupportFragmentManager(), "level dialog");
        }

    }

}
