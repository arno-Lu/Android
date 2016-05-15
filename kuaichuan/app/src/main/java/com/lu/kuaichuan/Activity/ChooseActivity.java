package com.lu.kuaichuan.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.BoolRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lu.kuaichuan.File.FileManager;
import com.lu.kuaichuan.File.TFile;
import com.lu.kuaichuan.Adapter.FragmentAdapter;
import com.lu.kuaichuan.Fragment.Fragment_browser;
import com.lu.kuaichuan.Fragment.Fragment_media_make;
import com.lu.kuaichuan.Fragment.Fragment_apk;
import com.lu.kuaichuan.Fragment.Fragment_office_main;
import com.lu.kuaichuan.Fragment.Fragment_other_main;
import com.lu.kuaichuan.Fragment.Fragment_pic;

import java.util.ArrayList;
import java.util.List;

import lu.com.kuaichuan.R;

public class ChooseActivity extends AppCompatActivity implements View.OnClickListener{

    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private View local_file_bottom;
    private Button send_button;
    private Button clear_button;
    private TextView localfile_bottom_tv;

    boolean State=false;
    FileManager bfm = FileManager.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose);

        local_file_bottom = findViewById(R.id.localefile_bottom);
        send_button = (Button)findViewById(R.id.localefile_bottom_btn);
        clear_button = (Button)findViewById(R.id.clear_bottom_btn);
        localfile_bottom_tv = (TextView)findViewById(R.id.localefile_bottom_tv);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("send_cnt_change");
        registerReceiver(cntChangeBroadcastReceiver,intentFilter);

        initToolBar();
        initNavigationDrawer();
        initViewPager();


    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(cntChangeBroadcastReceiver);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){

            case R.id.clear_bottom_btn:
                List<TFile> choosedFiles = bfm.getChoosedFiles();
                choosedFiles.clear();
                local_file_bottom.setVisibility(View.GONE);
                break;
            case R.id.localefile_bottom_btn:
                break;
            default:
                break;
        }
    }



    private BroadcastReceiver cntChangeBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int cnt = intent.getIntExtra("cnt", 0);
            if (cnt == 0) {
                local_file_bottom.setVisibility(View.GONE);
            } else {
                local_file_bottom.setVisibility(View.VISIBLE);

                localfile_bottom_tv.setText(bfm.getFilesSizes());
                send_button.setText(String.format(getString(R.string.bxfile_choosedCnt), cnt));
                send_button.setEnabled(cnt > 0);
            }
        }
    };


    private void initToolBar(){

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);

        toolbar.setNavigationIcon(R.mipmap.ic_drawer_home);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mDrawer.openDrawer(Gravity.LEFT);
            }
        });
        toolbar.setTitle(R.string.disconnect);
        toolbar.setTitleTextColor(getResources().getColor(R.color.titleColor));
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item){
                int menuId = item.getItemId();
                switch (menuId){
                    case R.id.download:
                         Toast.makeText(ChooseActivity.this,"下载",Toast.LENGTH_SHORT).show();
                         break;
                    default:
                        break;

                }
                return true;
            }
        });
    }

    private void initNavigationDrawer(){

        mDrawer = (DrawerLayout)findViewById(R.id.drawer);
        mNavigationView = (NavigationView)findViewById(R.id.design_navigation_view);

        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                int menuItemId = item.getItemId();
                switch (menuItemId){
                    case R.id.user_name:
                        break;
                    case R.id.filter_size:
                        break;
                    default:
                        break;
                }
                mDrawer.closeDrawers();
                return true;
            }

        });
    }

    private void initViewPager(){

        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        List<String> titles = new ArrayList<>();
        titles.add("图库");
        titles.add("音乐");
        titles.add("视频");
        titles.add("应用");
        titles.add("文档");
        titles.add("其他");


        for (int i = 0; i < titles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }
        List<Fragment> fragments = new ArrayList<>();

        Fragment_pic fragment_pic = new Fragment_pic();

        Bundle bundle_music = new Bundle();
        bundle_music.putString("Type","Music");
        Fragment_media_make fragment_music = new Fragment_media_make();
        fragment_music.setArguments(bundle_music);

        Bundle bundle_video =new Bundle();
        bundle_video.putString("Type","Video");
        Fragment_media_make fragment_video = new Fragment_media_make();
        fragment_video.setArguments(bundle_video);

        Fragment_apk fragment_apk = new Fragment_apk();

        Fragment_office_main fragment_office_decide = new Fragment_office_main();

        Fragment_other_main fragment_other_main = new Fragment_other_main();


        fragments.add(fragment_pic);
        fragments.add(fragment_music);
        fragments.add(fragment_video);
        fragments.add(fragment_apk);
        fragments.add(fragment_office_decide);
        fragments.add(fragment_other_main);


        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),fragments,titles);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(mFragmentAdapter);
    }

}
