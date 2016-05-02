package com.example.mechrevo.bluetooth_share;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.File;

/**
 * Created by mechrevo on 2016/5/2.
 */
public class FileUtil {

    private static String PATH = "/date/app";
    private static String FILE_TYPE = ".apk";

    /**
     * 通过apk文件获取包名
     * @param context 上下文
     * @param path	      文件路径
     * @return
     */
    public static String getPackageName(Context context,String path){

        PackageManager packageManager = context.getPackageManager();
        PackageInfo info = packageManager.getPackageArchiveInfo(path,PackageManager.GET_ACTIVITIES);
        ApplicationInfo applicationInfo = null;
        if(info!=null){
            applicationInfo = info.applicationInfo;
            return applicationInfo.packageName;
        }
        return null;
    }


    /**
     * 通过包名获取路径/data/app/ **.apk
     * @param packageName
     * @return
     *
     * 与原文件说明的路径发生了变化，apk文件储存在相应文件夹下的base.apk中
     * 不同系统版本储存兼容问题？？？？
     */
    public static String getFilePath(String packageName){ //返回一个文件名，不是路径？
        /* /data/app/{package_name}-[1-9].apk */
        String fileName = PATH +packageName;
        if(new File(fileName + FILE_TYPE).exists()){
            return packageName+FILE_TYPE;
        }
        //apk文件夹命名方式：包名 + "-" + [1-9].apk （1-9可选）
        for(int i=1;i<=9;i++){
            if(new File(fileName + "-"+ i+"base"+FILE_TYPE).exists()){   //改变为相应文件夹下的base.apk文件
                return packageName+ "-"+ i+"base"+FILE_TYPE;
            }
        }
        return null;
    }
}
