package com.example.lu.notificationtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button sendNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendNotice = (Button) findViewById(R.id.send_notice);
        sendNotice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.send_notice:
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification notification = new Notification.Builder(this)               //new Notification()这种方式已经过时，无法使用。用builder方法直接通过set进行设置
                        .setContentTitle("This is title")
                        .setContentText("This is ContentText")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setTicker(" Ticker  ")                                          //setTicker无法显示
                        .build();
                manager.notify(1,notification);
                break;
            default:
                break;
        }
    }
}
