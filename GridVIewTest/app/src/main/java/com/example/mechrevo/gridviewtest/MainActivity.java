package com.example.mechrevo.gridviewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private ArrayList<Map<String,Object>> date_list;
    private SimpleAdapter simpleAdapter;

    private int[] icon = { R.drawable.address_book, R.drawable.calendar,
            R.drawable.camera, R.drawable.clock, R.drawable.games_control,
            R.drawable.messenger, R.drawable.ringtone, R.drawable.settings,
            R.drawable.speech_balloon, R.drawable.weather, R.drawable.world,
            R.drawable.youtube };
    private String[] iconName = { "通讯录", "日历", "照相机", "时钟", "游戏", "短信", "铃声",
            "设置", "语音", "天气", "浏览器", "视频" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gview);
        date_list = new ArrayList<Map<String,Object>>();
        getDate();
        String[] from = {"image","text"};
        int[] to = {R.id.image,R.id.text};

        simpleAdapter = new SimpleAdapter(this,date_list,R.layout.item_view,from,to);
        gridView.setAdapter(simpleAdapter);
    }

    public List<Map<String,Object>> getDate(){
        for(int i=0;i<icon.length;i++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("image",icon[i]);
            map.put("text",iconName[i]);
            date_list.add(map);
        }
        return date_list;
    }


}

