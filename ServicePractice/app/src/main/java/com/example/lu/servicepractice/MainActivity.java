package com.example.lu.servicepractice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this,LongRunningService.class);
        startService(intent);
        setContentView(R.layout.activity_main);
    }
}
