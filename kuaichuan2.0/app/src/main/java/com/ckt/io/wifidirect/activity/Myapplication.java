package com.ckt.io.wifidirect.activity;

import android.app.Application;

/**
 * Created by Lu on 2016/5/24.
 */
public class Myapplication extends Application{

    private String name;

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }
}
