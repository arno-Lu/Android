package com.ckt.io.wifidirect.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 2016/3/15.
 */
public class FileResLoaderUtils {
    private OnLoadFinishedListener listener;
    private static HashMap<String, Object> picMap = new HashMap<>();
    private static HashMap<String, String> fileNameMap = new HashMap<>();
    private LoadTask loadTask;//�����ں�̨����ͼƬ

    private FileResLoaderUtils(){}

    public static FileResLoaderUtils getInstance(OnLoadFinishedListener listener) {
        FileResLoaderUtils drawableLoader = new FileResLoaderUtils();
        drawableLoader.listener = listener;
        return drawableLoader;
    }

    public static Object getPic(String key) {
        if(picMap != null) {
            return picMap.get(key);
        }
        return null;
    }

    public static String getFileName(String key) {
        return fileNameMap.get(key);
    }

    public void load(Context context, String path) {
        if(picMap == null) {
            picMap = new HashMap<>();
        }
        if(picMap.keySet().contains(path)) {

//            LogUtils.i(WifiP2pHelper.TAG, "DrawableLoaderUtils-->load() warning:"+path+" has been loaded before");
            return;
        }
        if(loadTask == null || !loadTask.isLoadiing) {//��̨����û������,����loadlist��������̨����
            loadTask = new LoadTask(context);
            loadTask.loadList.add(path);
            loadTask.execute();
        }else {//��̨��������,����loadlist����
            loadTask.loadList.add(path);
        }
        loadTask.context = context;
    }

    private class LoadTask extends AsyncTask<String, Object, Boolean> {
        private Context context;
        private ArrayList<String> loadList = new ArrayList<>();
        private boolean isLoadiing = false;
        public LoadTask(Context context) {
            this.context = context;
        }
        @Override
        protected Boolean doInBackground(String... params) {
            isLoadiing = true;
            while (loadList.size() != 0) {
                String path = loadList.get(0);
                loadList.remove(0);
//                LogUtils.i(WifiP2pHelper.TAG, "DrawableLoaderUtils-->loading pic of "+path);
                String s = path.toLowerCase();
                Object obj = null;
                if(s.endsWith(".apk")) { //apk�ļ�
                    obj = ApkUtils.getApkIcon(context, path);
                    //˳���apk�ļ���lableҲһ�������
                    fileNameMap.put(path, ApkUtils.getApkLable(context, path));
                }else if(s.endsWith(".mp3")) { //�����ļ�
                    obj = AudioUtils.getMusicBitpMap(path);
                    //˳��������ļ���lableҲһ�������
                    fileNameMap.put(path, AudioUtils.getMusicName(path));
                }else if(s.endsWith(".jpg") || s.endsWith(".jpeg") || s.endsWith(".bmp") || s.endsWith(".gif") || s.endsWith(".png")) {//ͼƬ�ļ�
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 10;
                    Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                    obj = ThumbnailUtils.extractThumbnail(bitmap, 80,
                            80);
                    bitmap.recycle();
                }else if(s.endsWith(".3gp") || s.endsWith(".mp4") || s.endsWith(".rmvb")) { //��Ƶ�ļ�
                    obj = new BitmapDrawable(GetVideoThumbnail.getVideoThumbnailTool(path));
                }

                try {
                    picMap.put(path, obj);
                }catch (Exception e) {
                    return null;
                }
                publishProgress(path, obj);
            }
            isLoadiing = false;
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            if(listener!=null) {
                listener.onLoadOneFinished((String) values[0], values[1], loadList.size() == 0);
            }
        }
    }

    public static void release() {
        /*picMap = null;
        fileNameMap = null;*/
    }

    public interface OnLoadFinishedListener {
        public abstract void onLoadOneFinished(String path, Object obj, boolean isAllFinished);
    }
}
