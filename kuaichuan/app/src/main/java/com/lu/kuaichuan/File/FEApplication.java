package com.lu.kuaichuan.File;

import android.app.Application;
import android.net.wifi.p2p.WifiP2pInfo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mechrevo on 2016/5/14.
 */
public class FEApplication extends Application{

    private ExecutorService es = Executors.newFixedThreadPool(3); //开启线程池
/*不明白对外任务入口什么意思，继承与Application，可用于全局变量*/
    // app对外 执行任务入口
    public void execRunnable(Runnable r) {
        if (!es.isShutdown()) {
            es.execute(r);
        }
    }

    private String user_name;
    private WifiP2pInfo Info;

    public WifiP2pInfo getInfo(){
        return this.Info;
    }
    public void setInfo(WifiP2pInfo info){
        this.Info = info;
    }
}
