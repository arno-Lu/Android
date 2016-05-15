package com.lu.kuaichuan.Fragment;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.lu.kuaichuan.Adapter.ListViewAdapter;
import com.lu.kuaichuan.File.FEApplication;
import com.lu.kuaichuan.File.FileManager;
import com.lu.kuaichuan.File.TFile;

import java.util.ArrayList;
import java.util.List;

import lu.com.kuaichuan.R;

/**
 * Created by Lu on 2016/5/14.
 */
public class Fragment_apk extends Fragment implements AdapterView.OnItemClickListener{
    private ListView mListView;
    private List<TFile> mAppInfoList;
    // private FrameLayout mRootFrameLayout;
    public final int GET_APK_FINISH = 9527;
    //public ProgressBar mProgressBar;
    private PackageManager mPackageManager;
    private ListViewAdapter mListViewAdapter;
    private FileManager bfm;




    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == GET_APK_FINISH) {
                //dismissProgressBar();
                mListViewAdapter.notifyDataSetChanged();
            }
            super.handleMessage(msg);

        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        mPackageManager = getActivity().getPackageManager();
        View mainView = inflater.inflate(R.layout.fragment_apk_main, container, false);

        mListView = (ListView) mainView.findViewById(R.id.listView);
        //mProgressBar = new ProgressBar(mContext);

        mAppInfoList = new ArrayList<TFile>();
        mListViewAdapter = new ListViewAdapter(getActivity());
        mListViewAdapter.setList(mAppInfoList);
        mListView.setAdapter(mListViewAdapter);
        mListView.setOnItemClickListener(this);
        // showProgressBar();

        FEApplication bxApp = (FEApplication) getActivity().getApplication();
        bxApp.execRunnable(new Runnable() {
            @Override
            public void run() {
                mAppInfoList.clear();
                mAppInfoList.addAll(getAllAppInfo());
                handler.sendEmptyMessage(GET_APK_FINISH);
            }
        });



        return mainView;
    }



    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3){
        TFile apkFile = mAppInfoList.get(position);
        bfm = FileManager.getInstance();
        CheckBox fileCheckBox = (CheckBox) view.findViewById(R.id.fileCheckBox);
        List<TFile> choosedFiles = bfm.getChoosedFiles();

        if (choosedFiles.contains(apkFile)) {
            choosedFiles.remove(apkFile);
            fileCheckBox.setChecked(false);
        } else {

            choosedFiles.add(apkFile);
            fileCheckBox.setChecked(true);
        }
        int cnt = bfm.getFilesCnt();
        sendBroadcast(cnt);
    }




    /**
     * 获取已经安装的应用
     */
    private List<TFile> getAllAppInfo() {
        mAppInfoList = new ArrayList<TFile>();
        mPackageManager = getActivity().getPackageManager();
        //获取已安装所有应用对应的PackageInfo
        List<PackageInfo> packageInfoList = mPackageManager.getInstalledPackages(0);
        for (int i = 0; i < packageInfoList.size(); i++) {
            PackageInfo packageInfo = packageInfoList.get(i);
            if((packageInfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)>0) { //只获取用户应用，排除系统应用
                //获取应用名称
                continue;
                }else {
                    TFile.appBuild builder = new TFile.appBuild(packageInfo, mPackageManager);
                    TFile bxfile = builder.build();
                    if (bxfile != null) {
                        mAppInfoList.add(bxfile);
                }
            }
        }
        return mAppInfoList;
    }


    public void sendBroadcast(int cnt){
        Intent intent = new Intent("send_cnt_change");
        intent.putExtra("cnt",cnt);
        getActivity().sendBroadcast(intent);
    }


}