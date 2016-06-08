package com.lu.kuaichuan.wifidirect.File;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lu on 2016/5/24.
 */
public class FileUtils {

    static String tag = "FileUtils";

    public static File createFile(String folderPath, String fileName) {
        File destDir = new File(folderPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return new File(folderPath, fileName + fileName);
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName
     * @return
     */
    public static String getFileFormat(String fileName) {
        if (TextUtils.isEmpty(fileName))
            return "";

        int point = fileName.lastIndexOf('.');
        return fileName.substring(point + 1);
    }

    /**
     * 获取文件大小
     *
     * @param size
     *            字节
     * @return
     */
    public static String getFileSizeStr(long size) {
        if (size <= 0)
            return "0.0B";
        java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
        float temp = (float) size / 1024;
        if (temp >= 1024) {
            return df.format(temp / 1024) + "M";
        } else {
            return df.format(temp) + "K";
        }
    }

    /**
     * 列出root目录下所有子目录
     *
     * @param
     * @return 绝对路径
     */
    public static List<String> listPath(String root) {
        List<String> allDir = new ArrayList<String>();
        SecurityManager checker = new SecurityManager();
        File path = new File(root);
        checker.checkRead(root);
        if (path.isDirectory()) {
            for (File f : path.listFiles()) {
                if (f.isDirectory()) {
                    allDir.add(f.getAbsolutePath());
                }
            }
        }
        return allDir;
    }

    public static List<File> getChild(String root) {
        SecurityManager checker = new SecurityManager();
        File path = new File(root);
        checker.checkRead(root);
        if (path.isDirectory())
            return  Arrays.asList(path.listFiles());
        else
            return null;

    }



    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static boolean isDir(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isDirectory();
    }

    //获取后缀
    public static String getExspansion(String fileName){
        if(TextUtils.isEmpty(fileName))
            return null;
        int index = fileName.lastIndexOf(".");
        if(-1==index || index==(fileName.length()-1))
            return null;
        return fileName.substring(index);
    }

    public static void prepareFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void delete(String filePath) {
        if (filePath == null) {
            return;
        }
        try {
            File file = new File(filePath);
            if (file == null || !file.exists()) {
                return;
            }
            if (file.isDirectory()) {
                deleteDirRecursive(file);
            } else {
                file.delete();
            }
        } catch (Exception e) {
            Log.e(tag, e.toString());
        }
    }
    /*
	 * 递归删除目录
	 */
    public static void deleteDirRecursive(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File f : files) {
            if (f.isFile()) {
                f.delete();
            } else {
                deleteDirRecursive(f);
            }
        }
        dir.delete();
    }

    /**
     * 判断SD卡是否已经准备好
     *
     * @return 是否有SDCARD
     */
    public static boolean isSDCardReady() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 可扩展卡路径
     * @return
     */
    public static String getExtSdCardPath(){
        File file = new File("/mnt/external_sd/");
        if(file.exists()){
            return file.getAbsolutePath();
        }else{
            file = new File("/mnt/extSdCard/");
            if(file.exists())
                return file.getAbsolutePath();
        }
        return null;
    }
}
