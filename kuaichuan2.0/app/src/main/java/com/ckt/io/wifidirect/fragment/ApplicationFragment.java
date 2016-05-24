package com.ckt.io.wifidirect.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.ckt.io.wifidirect.activity.MainActivity;
import com.ckt.io.wifidirect.R;
import com.ckt.io.wifidirect.adapter.MyGridViewAdapter;
import com.ckt.io.wifidirect.p2p.WifiP2pHelper;
import com.ckt.io.wifidirect.utils.FileResLoaderUtils;
import com.ckt.io.wifidirect.utils.LogUtils;
import com.ckt.io.wifidirect.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ckt on 2/29/16.
 */
public class ApplicationFragment extends MyBaseFragment implements AdapterView.OnItemClickListener,
        MainActivity.OnSendFileListChangeListener,
        FileResLoaderUtils.OnLoadFinishedListener {
    private MyGridViewAdapter adapter;
    private GridView gridView;
    private PackageManager manager;
    private TextView applicationNumber;
    List<PackageInfo> packageInfoList = new ArrayList<>();
    List<PackageInfo> apps = new ArrayList<>();
    ArrayList<String> mNameList = new ArrayList<>();
    ArrayList<String> mPathList = new ArrayList<>();
    ArrayList<String> mPackageList = new ArrayList<>();
    ArrayList<Boolean> mCheckBoxList = new ArrayList<>();

    private FileResLoaderUtils drawableLoaderUtils; //用来异步加载图片

    //用来还原gridview的位置
    private int gridViewState_pos = 0;

    private BroadcastReceiver appReceiver = new AppReceiver();

    public static final int LOAD_DATA_FINISHED = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_DATA_FINISHED:
                    gridView.setAdapter(adapter);
                    gridView.setSelection(gridViewState_pos);
                    applicationNumber.setText(getResources().getString(R.string.installed_apps) + "(" + mNameList.size() + ")");
                    if (drawableLoaderUtils != null) {
                        for (int i = 0; i < mNameList.size(); i++) {
                            //启动异步任务加载图片,加载完成一个图片后会调用onLoadOneFinished
                            drawableLoaderUtils.load(getContext(), mPathList.get(i));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(WifiP2pHelper.TAG, "ApplicationFragment-->onCreateView");
        View view = inflater.inflate(R.layout.application_layout, container, false);
        gridView = (GridView) view.findViewById(R.id.id_grid_view);
        applicationNumber = (TextView) view.findViewById(R.id.id_application_number);
        manager = getActivity().getPackageManager();
        if (adapter == null) {//first loaded data
            LogUtils.i(WifiP2pHelper.TAG, "ApplicationFragment first oncreateView()");
            drawableLoaderUtils = FileResLoaderUtils.getInstance(this);
            loadData();
        } else {
            handler.sendEmptyMessage(LOAD_DATA_FINISHED);
        }
        gridView.setOnItemClickListener(this);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) { //stop
                    gridView.setTag(false);
                    ((BaseAdapter) gridView.getAdapter()).notifyDataSetChanged();
                } else { //scrolling
                    gridView.setTag(true);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        registerForContextMenu(gridView);
        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        getActivity().registerReceiver(appReceiver, filter);


        return view;
    }

    @Override
    protected String getPositonPath(int position) {
        return mPathList.get(position);
    }

    @Override
    protected boolean isApkInstalled(String path) {
        return true;
    }

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo menuInfo1 = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int pos = menuInfo1.position;
        LogUtils.i(WifiP2pHelper.TAG, "onCreateContextMenu"+pos);
        MenuInflater inflater = new MenuInflater(getActivity());
        inflater.inflate(R.menu.menu_context, menu);
    }*/

   /* @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_open:
                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                String packageName = mPackageList.get((int) menuInfo.id);
                Intent it = getActivity().getPackageManager().getLaunchIntentForPackage(packageName);
                startActivity(it);
                Log.i("Activity", mPackageList.get((int) menuInfo.id) + "---->");
                break;
                AdapterView.AdapterContextMenuInfo menuInfo_1 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                String packageName_1 = mPackageList.get((int) menuInfo_1.id);
                Uri packageUri = Uri.parse("package:" + packageName_1);
                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
                startActivity(uninstallIntent);

                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }*/


    public void loadData() {
        new Thread() {
            @Override
            public void run() {
                apps.clear();
                mNameList.clear();
                mPathList.clear();
                mPackageList.clear();
                mCheckBoxList.clear();
                packageInfoList = manager.getInstalledPackages(0);
                for (int i = 0; i < packageInfoList.size(); i++) {
                    PackageInfo packageInfo = packageInfoList.get(i);
                    if ((packageInfo.applicationInfo.flags & packageInfo.applicationInfo.FLAG_SYSTEM) <= 0) {
                        //第三方应用
                        apps.add(packageInfo);
                        mNameList.add(manager.getApplicationLabel(packageInfo.applicationInfo).toString());
                        mPathList.add(packageInfo.applicationInfo.sourceDir);
                        mPackageList.add(packageInfo.packageName);
                        mCheckBoxList.add(false);
                    } else {
                    }
                }
                adapter = new MyGridViewAdapter(getActivity(), mNameList, mPathList, mCheckBoxList, 60);
                handler.sendEmptyMessage(LOAD_DATA_FINISHED);
            }
        }.start();
    }



    @Override
    public void onPause() {
        super.onPause();
        gridViewState_pos = gridView.getFirstVisiblePosition();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unregisterReceiver
        //不要在onPause里面注销
        getActivity().unregisterReceiver(appReceiver);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCheckBoxList = adapter.getmCheckBoxList();
        mCheckBoxList.set(position, !mCheckBoxList.get(position));
        MainActivity activity = (MainActivity) getActivity();
        if (mCheckBoxList.get(position)) {//checked--->add to sendfile-list
            activity.addFileToSendFileList(mPathList.get(position), mNameList.get(position));
        } else {//unchecked--->remove from sendfile-list
            activity.removeFileFromSendFileList(mPathList.get(position));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSendFileListChange(ArrayList<String> sendFiles, int num) {
        if (adapter != null) {
            ArrayList<Boolean> checkList = adapter.getmCheckBoxList();
            String data = sendFiles.toString();
            Log.d(WifiP2pHelper.TAG, data);
            for (int i = 0; i < checkList.size(); i++) {
                String temp = mPathList.get(i);
                if (data.contains(temp)) {
                    checkList.set(i, true);
                } else {
                    checkList.set(i, false);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    //加载完一张图片的回调
    @Override
    public void onLoadOneFinished(String path, Object obj, boolean isAllFinished) {
        int index = mPathList.indexOf(path);
        if (gridView.getTag() == null || !(boolean) gridView.getTag()) { //gridview没有滑动
            if (index % 5 == 0 || isAllFinished) {
                ((BaseAdapter) (gridView.getAdapter())).notifyDataSetChanged();
            }
        }
    }

    public class AppReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();
            String packageName = intent.getData().getSchemeSpecificPart();
            if(Intent.ACTION_PACKAGE_REMOVED.equals(action)) { //卸载
                LogUtils.i(WifiP2pHelper.TAG, "uninstalled a app--->"+packageName);
                int index = mPackageList.indexOf(packageName);
                String appName = "";
                if(index >=0 ) {
                    appName = mNameList.get(index);
                    boolean isChecked = mCheckBoxList.get(index);
                    String path = mPathList.get(index);
                    PackageInfo info = apps.get(index);
                    apps.remove(index);
                    packageInfoList.remove(info);
                    mNameList.remove(index);
                    mPathList.remove(index);
                    mPackageList.remove(index);
                    mCheckBoxList.remove(index);
                    if(isChecked) {
                        MainActivity activity = (MainActivity) getActivity();
                        activity.removeFileFromSendFileList(path);
                    }
                    if(adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
                ToastUtils.toast(getContext(), appName + " " + getResources().getString(R.string.package_uninstalled));
            }else if(Intent.ACTION_PACKAGE_ADDED.equals(action)){//新增
                LogUtils.i(WifiP2pHelper.TAG, "installed a app------->"+packageName);
            }
        }
    }
}
