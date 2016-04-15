package com.example.mechrevo.toolbartest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import drawerLayoutActivity.SimpleDrawerActivity;
import toolbar.ToolBarActivity;
import toolbar.ZhiHuActivity;
import translucentbar.ColorTranslucentBarActivity;
import translucentbar.ImageTranslucentBarActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button_toolbar;
    private Button zhihu_button;
    private Button image_button;
    private Button color_button;
    private Button simple_drawer_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_toolbar = (Button)findViewById(R.id.ToolbarButton);
        zhihu_button = (Button)findViewById(R.id.ZhiHu);
        image_button = (Button)findViewById(R.id.ImageTranslucentBarButton);
        color_button = (Button)findViewById(R.id.ColorTranslucentBarButton);
        simple_drawer_button = (Button)findViewById(R.id.Simple_Drawer_button);

        button_toolbar.setOnClickListener(this);
        zhihu_button.setOnClickListener(this);
        image_button.setOnClickListener(this);
        color_button.setOnClickListener(this);
        simple_drawer_button.setOnClickListener(this);
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
            case R.id.ImageTranslucentBarButton:
                Intent intent3 = new Intent(MainActivity.this, ImageTranslucentBarActivity.class);
                startActivity(intent3);
                break;
            case R.id.ColorTranslucentBarButton:
                Intent intent4 = new Intent(MainActivity.this, ColorTranslucentBarActivity.class);
                startActivity(intent4);
                break;
            case R.id.Simple_Drawer_button:
                Intent intent5 = new Intent(MainActivity.this, SimpleDrawerActivity.class);
                startActivity(intent5);
                break;
            default:
                break;
        }
    }
}
