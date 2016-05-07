package com.apklist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {
    private Context mContext;
    private Handler mHandler;
    private ListView mListView;
    private List<AppInfo> mAppInfoList;
    private FrameLayout mRootFrameLayout;
    public final int GET_APK_FINISH=9527;
    public ProgressBar mProgressBar;
    private PackageManager mPackageManager ;
    private ListViewAdapter mListViewAdapter;
    private ItemClickListenerImpl mItemClickListenerImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        mContext=this;
        mPackageManager=getApplicationContext().getPackageManager();
        mListView= (ListView) findViewById(R.id.listView);
        mProgressBar=new ProgressBar(mContext);
        mItemClickListenerImpl=new ItemClickListenerImpl();

        mAppInfoList=new ArrayList<AppInfo>();
        mListViewAdapter=new ListViewAdapter(mContext);
        mListViewAdapter.setList(mAppInfoList);
        mListView.setAdapter(mListViewAdapter);
        mListView.setOnItemClickListener(mItemClickListenerImpl);
        showProgressBar();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mAppInfoList.clear();
                mAppInfoList.addAll(getAllAppInfo());
                mHandler.sendEmptyMessage(GET_APK_FINISH);
            }
        }).start();



        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == GET_APK_FINISH) {
                    dismissProgressBar();
                    mListViewAdapter.notifyDataSetChanged();
                }
            }
        };
    }


    private class ItemClickListenerImpl implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            File apkFile = mAppInfoList.get(position).getApkFile();
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(apkFile));
            startActivity(intent);
        }
    }


    /**
     * 获取已经安装的应用
     */
    private List<AppInfo> getAllAppInfo() {
        File apkFile;
        AppInfo appInfo;
        mAppInfoList = new ArrayList<AppInfo>();
        //获取已安装所有应用对应的PackageInfo
        List<PackageInfo> packageInfoList = mPackageManager.getInstalledPackages(0);
        for (int i = 0; i < packageInfoList.size(); i++) {
            appInfo = new AppInfo();
            PackageInfo packageInfo = packageInfoList.get(i);
            //获取应用名称
            appInfo.setAppName(getApplicationName(packageInfo.packageName,mPackageManager));
            //获取应用ICON
            appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(mPackageManager));
            //获取应用的apk文件
            apkFile = new File(packageInfo.applicationInfo.sourceDir);
            appInfo.setApkFile(apkFile);
            //获取到应用大小
            appInfo.setAppSize(((float) apkFile.length() / 1024 / 1024) + "MB");
            //获取应用的更新时间
            appInfo.setAppTime(getDate(packageInfo.lastUpdateTime));
            mAppInfoList.add(appInfo);
        }
        return mAppInfoList;
    }


    /**
     * 获取应用的Icon
     */
    public Drawable getAppliactionIcon(PackageInfo packageInfo, PackageManager packageManager){
        Drawable appliactionIcon=packageInfo.applicationInfo.loadIcon(packageManager);
        return appliactionIcon;
    }


    /**
     * 获取应用的名称
     */
    public String getApplicationName(String packageName,PackageManager packageManager) {
        String applicationName=null;
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {

        }
        return applicationName;
    }

    /**
     * 生成时间
     */
    public static String getDate(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        String formatedDate = simpleDateFormat.format(date);
        return formatedDate;
    }

    /**
     * 在屏幕中间显示风火轮
     */
    private void showProgressBar(){
        mRootFrameLayout=(FrameLayout) findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams= new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity= Gravity.CENTER;
        mProgressBar=new ProgressBar(mContext);
        mProgressBar.setLayoutParams(layoutParams);
        mProgressBar.setVisibility(View.VISIBLE);
        mRootFrameLayout.addView(mProgressBar);
    }

    /**
     * 隐藏风火轮
     */
    private void dismissProgressBar(){
        if(null!=mProgressBar&&null!=mRootFrameLayout){
            mRootFrameLayout.removeView(mProgressBar);
        }
    }


}
