package com.example.mechrevo.asynctasktest;

/**
 * Created by mechrevo on 2016/4/18.
 */
public class NetOperator {
    public void operator(){
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
