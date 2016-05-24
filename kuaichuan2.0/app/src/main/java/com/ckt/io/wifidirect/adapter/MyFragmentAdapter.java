package com.ckt.io.wifidirect.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by ckt on 2/29/16.
 */
public class MyFragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> list;

    public MyFragmentAdapter(FragmentManager supportFragmentManager, ArrayList<Fragment> fragmentArrayList) {
        super(supportFragmentManager);
        this.list = fragmentArrayList;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
