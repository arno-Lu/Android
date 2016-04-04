package com.example.mechrevo.lightsenortest;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
    private SensorManager sensorManager;
    private TextView lightLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lightLevel = (TextView) findViewById(R.id.light_level);

        //获取系统传感器的管理器
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //得到 光照传感器
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        // 光照传感器注册(register)监听,参数为(SensorEventListener实例,传感器对象,输出信息速率)
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float value = event.values[0];
            lightLevel.setText("Current light level is " + value + " lx");

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (sensorManager != null) {
            //注销监听
            sensorManager.unregisterListener(listener);
        }
    }

}