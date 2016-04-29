package com.example.mechrevo.pic.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.mechrevo.pic.R;
import com.example.mechrevo.pic.adapter.MainGVAdapter;
import com.example.mechrevo.pic.utils.ScreenUtils;
import com.example.mechrevo.pic.utils.Utility;

import java.util.ArrayList;

/**
 * 主页面，可跳转至相册选择照片，并在此页面显示所选择的照片。
 * Created by hanj on 14-10-13.
 */
public class MainActivity extends Activity {
    private MainGVAdapter adapter;
    private ArrayList<String> imagePathList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取屏幕像素
        ScreenUtils.initScreen(this);

        TextView titleTV = (TextView) findViewById(R.id.topbar_title_tv);
        Button selectImgBtn = (Button) findViewById(R.id.main_select_image);
        GridView mainGV = (GridView) findViewById(R.id.main_gridView);

        titleTV.setText(R.string.app_name);
        imagePathList = new ArrayList<String>();
        adapter = new MainGVAdapter(this, imagePathList);
        mainGV.setAdapter(adapter);

        selectImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至最终的选择图片页面
                Intent intent = new Intent(MainActivity.this, PhotoWallActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int code = intent.getIntExtra("code", -1);
        if (code != 100) {
            return;
        }

        ArrayList<String> paths = intent.getStringArrayListExtra("paths");
        //添加，去重
        boolean hasUpdate = false;
        for (String path : paths) {
            if (!imagePathList.contains(path)) {
                //最多9张
                if (imagePathList.size() == 9) {
                    Utility.showToast(this, "最多可添加9张图片。");
                    break;
                }

                imagePathList.add(path);
                hasUpdate = true;
            }
        }

        if (hasUpdate) {
            adapter.notifyDataSetChanged();
        }
    }
}
