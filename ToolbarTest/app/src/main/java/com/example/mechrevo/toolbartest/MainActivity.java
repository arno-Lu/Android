package com.example.mechrevo.toolbartest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import toolbar.ToolBarActivity;
import toolbar.ZhiHuActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button_toolbar;
    private Button zhihu_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_toolbar = (Button)findViewById(R.id.ToolbarButton);
        zhihu_button = (Button)findViewById(R.id.ZhiHu);
        button_toolbar.setOnClickListener(this);
        zhihu_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){
        int viewId =view.getId();
        switch (viewId){
            case R.id.ToolbarButton:
                Intent intent =new Intent(MainActivity.this, ToolBarActivity.class);
                startActivity(intent);
                break;
            case R.id.ZhiHu:
                Intent intent2 =new Intent(MainActivity.this, ZhiHuActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }
}
