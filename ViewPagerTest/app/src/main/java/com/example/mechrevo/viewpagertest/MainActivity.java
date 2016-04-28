package com.example.mechrevo.viewpagertest;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Objects;
//参考代码：http://my.oschina.net/summerpxy/blog/210026
public class MainActivity extends AppCompatActivity {

    ViewPager pager = null;
    PagerTabStrip tabStrip = null;
    ArrayList<View> viewContainer = new ArrayList<View>();
    ArrayList<String> titleContainer = new ArrayList<String>();
    public  String TAG = "tag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (ViewPager)findViewById(R.id.viewpager);
        tabStrip = (PagerTabStrip)findViewById(R.id.tabstrip);

        tabStrip.setDrawFullUnderline(false);

        tabStrip.setBackgroundColor(this.getResources().getColor(R.color.colorbackground));
        tabStrip.setTabIndicatorColor(this.getResources().getColor(R.color.colorPrimary));
        tabStrip.setTextSpacing(200);

        View view1 = LayoutInflater.from(this).inflate(R.layout.table1,null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.table2,null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.table3,null);
        View view4 = LayoutInflater.from(this).inflate(R.layout.table4,null);

        viewContainer.add(view1);
        viewContainer.add(view2);
        viewContainer.add(view3);
        viewContainer.add(view4);

        titleContainer.add("分类1");
        titleContainer.add("分类2");
        titleContainer.add("分类3");
        titleContainer.add("分类4");

        pager.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return viewContainer.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object){
                ( (ViewPager) container).removeView(viewContainer.get(position));
            }

            @Override
            public Object instantiateItem (ViewGroup container,int position){
                ((ViewPager)container).addView(viewContainer.get(position));
                return viewContainer.get(position);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public int getItemPosition(Object object){
                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position){
                return titleContainer.get(position);
            }

        });
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0){
                Log.d(TAG, "--------changed:" + arg0);
            }
            @Override
            public void onPageScrolled(int arg0,float arg1,int arg2){
                Log.d(TAG,"------------changed:"+arg0);
                Log.d(TAG,"------------changed:"+arg1);
                Log.d(TAG,"------------changed:"+arg2);
            }
            @Override
            public void onPageSelected(int arg0){
                Log.d(TAG,"------------changed:"+arg0);
            }
        });
    }
}
