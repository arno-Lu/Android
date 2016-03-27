package com.example.lu.servicepractice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;


import java.sql.Date;

public class LongRunningService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags,int starId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("LongRunningService", "run : executed at " + new Date(System.currentTimeMillis()).toString());
            }
        }).start();
        AlarmManager mannager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 1000;             //当把时间间隔设置为1s，每过1s，log显示一条消息
        long triggerTime = SystemClock.elapsedRealtime()+anHour;
        Intent i = new Intent(this,AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
        mannager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerTime,pi);
        return  super.onStartCommand(intent,flags,starId);
    }
}
