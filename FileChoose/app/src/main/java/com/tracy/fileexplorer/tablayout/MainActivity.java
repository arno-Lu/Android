package com.tracy.fileexplorer.tablayout;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;


import com.tracy.fileexplorer.FileManager;
import com.tracy.fileexplorer.R;
import com.tracy.fileexplorer.TFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mechrevo on 2016/5/8.
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Button clear_button;
    private Button localefile_bottom_btn;
    private View local_bottom;
    private TextView localefile_bottom_tv;
    private FileManager bfm;
    private IntentFilter filter;
    private CntChangeReceiver cntChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        local_bottom =findViewById(R.id.localefile_bottom) ;
        clear_button =( Button)findViewById(R.id.clear_bottom_btn);
        localefile_bottom_btn = (Button)findViewById(R.id.localefile_bottom_btn);
        localefile_bottom_tv = (TextView) findViewById(R.id.localefile_bottom_tv);

        filter = new IntentFilter();
        filter.addAction("change");
        cntChangeReceiver = new CntChangeReceiver();
        registerReceiver(cntChangeReceiver,filter);


        setSupportActionBar(toolbar);
        mViewPager=(ViewPager)findViewById(R.id.viewpager);

        initViewPager();


    }

    public class CntChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context,Intent intent){
            onFileClick();
        }
    }


    public void onFileClick() {
        bfm = FileManager.getInstance();

        int cnt = bfm.getFilesCnt();
        if(cnt==0){
            local_bottom.setVisibility(View.GONE);
        }else {
            local_bottom.setVisibility(View.VISIBLE);

            localefile_bottom_tv.setText(bfm.getFilesSizes());
            localefile_bottom_btn.setText(String.format(getString(R.string.bxfile_choosedCnt), cnt));
            localefile_bottom_btn.setEnabled(cnt>0);
        }

    }






    private void initViewPager() {
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        List<String> titles = new ArrayList<>();
        titles.add("应用");
        titles.add("图库");
        titles.add("音乐");
        titles.add("视频");
        titles.add("文件");


        for (int i = 0; i < titles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }
        List<Fragment> fragments = new ArrayList<>();

        Bundle bundle = new Bundle();
        bundle.putInt("music",1);
        Fragment1 fragment1 = new Fragment1();
        fragment1.setArguments(bundle);
        fragments.add(fragment1);
        Fragment_apk fragment_apk = new Fragment_apk();
        fragments.add(fragment_apk);
        Fragment_pic fragment_pic =new Fragment_pic();
        fragments.add(fragment_pic);



        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),fragments,titles);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(mFragmentAdapter);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(cntChangeReceiver);
    }
}