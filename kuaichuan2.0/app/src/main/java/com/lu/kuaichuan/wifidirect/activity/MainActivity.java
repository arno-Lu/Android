package com.lu.kuaichuan.wifidirect.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.lu.kuaichuan.wifidirect.R;
import com.lu.kuaichuan.wifidirect.Views.DeviceConnectDialog;
import com.lu.kuaichuan.wifidirect.fragment.ContentFragment;
import com.lu.kuaichuan.wifidirect.fragment.FileExplorerFragment;
import com.lu.kuaichuan.wifidirect.Views.SpeedFloatWin;
import com.lu.kuaichuan.wifidirect.p2p.WifiP2pHelper;
import com.lu.kuaichuan.wifidirect.provider.Record;
import com.lu.kuaichuan.wifidirect.provider.RecordManager;
import com.lu.kuaichuan.wifidirect.utils.FileResLoaderUtils;
import com.lu.kuaichuan.wifidirect.utils.LogUtils;
import com.lu.kuaichuan.wifidirect.utils.SdcardUtils;
import com.lu.kuaichuan.wifidirect.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends BaseActivity {
    public static final byte REQUEST_CODE_SYSTEM_ALERT_PERMISSION = 5;
    public static final byte REQUEST_CODE_READ_EXTERNAL = 6;
    public static final byte REQUEST_CODE_WRITE_EXTERNAL = 7;

    public static final int UPDATE_TRANSPORT_SPEED_INTERVAL = 500; //更新速度的时间间隔
    public static final int UPDATE_TRANSPORT_PROGRESS_INTERVAL = 50; //更新速度的时间间隔

    private static final String TAG = "MainActivity";
    private DeviceConnectDialog deviceConnectDialog;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    protected Toolbar toolbar;
    private final IntentFilter intentFilter = new IntentFilter();

    private File received_file_path; //收到的文件的保存路径

    private ArrayList<String> selectFiles;  //the selected files to send

    private ContentFragment contentfragment;
    private OnSendFileListChangeListener onSendFileListChangeListener;


    private WifiP2pHelper wifiP2pHelper;
    private boolean isTranfering;
    private int mSendCount;
    private int mReceviceCount;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            RecordManager recordManager = RecordManager.getInstance(getApplicationContext());
            Record record = null;
            File f = null;
            switch (msg.what) {
                case WifiP2pHelper.WIFIP2P_DEVICE_LIST_CHANGED://可用的设备列表更新
                    deviceConnectDialog.updateDeviceList(wifiP2pHelper.getDeviceList());
                    break;
                case WifiP2pHelper.WIFIP2P_DEVICE_CONNECTED_SUCCESS://设备连接成功
                    deviceConnectDialog.updateConnectedInfo(wifiP2pHelper.isServer());
                    //连接成功后关联 dialog
                    if(deviceConnectDialog.isShowing()) {
                        deviceConnectDialog.dismiss();
                    }
                    //隐藏 "传" 按钮
                    contentfragment.toggleConnectImgBut(false);
                    ToastUtils.toast(MainActivity.this, R.string.connect_successed);
                    break;
                case WifiP2pHelper.WIFIP2P_DEVICE_DISCONNECTED: //连接已断开
                    deviceConnectDialog.onDisconnectedInfo();
                    //显示 "传" 按钮
                    contentfragment.toggleConnectImgBut(true);
                    break;

                /////////////////////////////////////////////////////////////////////////
                case WifiP2pHelper.WIFIP2P_SENDFILELIST_ADDED: //正在发送列表新增文件
                    ArrayList<File> addedList = (ArrayList<File>) msg.obj;
                    recordManager.addNewSendingRecord(addedList, wifiP2pHelper.getCurrentConnectMAC());
                    break;
                case WifiP2pHelper.WIFIP2P_BEGIN_SEND_FILE: //开始发送一个文件
                    LogUtils.i(WifiP2pHelper.TAG, "handle--->WIFIP2P_BEGIN_SEND_FILE");
                    f = (File) msg.obj;
                    record = recordManager.findRecord(f.getPath(), Record.STATE_WAIT_FOR_TRANSPORT, true);
                    if(record == null) break;
                    record.setState(Record.STATE_TRANSPORTING);
                    //开始更新传输速度和传输进度
                    handler.obtainMessage(WifiP2pHelper.WIFIP2P_UPDATE_SPEED, f).sendToTarget();
                    handler.obtainMessage(WifiP2pHelper.WIFIP2P_UPDATE_PROGRESS, f).sendToTarget();
                    /////////////////////////////////////////////////////////////
                    break;
                case WifiP2pHelper.WIFIP2P_SEND_ONE_FILE_SUCCESSFULLY:  //发送完一个文件----成功
                case WifiP2pHelper.WIFIP2P_SEND_ONE_FILE_FAILURE:    // 发送完一个文件----失败
                    f = (File) msg.obj;
                    record = recordManager.findRecord(f.getPath(), Record.STATE_TRANSPORTING, true);
                    if(record == null) break;
                    if(msg.what == WifiP2pHelper.WIFIP2P_SEND_ONE_FILE_SUCCESSFULLY) {//发送成功
                        record.setState(Record.STATE_FINISHED);
                    }else { //发送失败
                        record.setState(Record.STATE_FAILED);
                    }
                    break;

                case WifiP2pHelper.WIFIP2P_BEGIN_RECEIVE_FILE:  //开始接收文件
                    LogUtils.i(WifiP2pHelper.TAG, "handle--->WIFIP2P_BEGIN_SEND_FILE");
                    HashMap<String, Object> map = (HashMap<String, Object>) msg.obj;
                    f = (File) map.get("path");
                    String sName = (String) map.get("name");
                    long size = (long) map.get("size");
                    recordManager.addNewRecevingRecord(f, sName, size, wifiP2pHelper.getCurrentConnectMAC());
                    //开始更新传输速度和传输进度
                    handler.obtainMessage(WifiP2pHelper.WIFIP2P_UPDATE_SPEED, f).sendToTarget();
                    handler.obtainMessage(WifiP2pHelper.WIFIP2P_UPDATE_PROGRESS, f).sendToTarget();
                    break;
                case WifiP2pHelper.WIFIP2P_RECEIVE_ONE_FILE_SUCCESSFULLY: //接收完一个文件----成功
                case WifiP2pHelper.WIFIP2P_RECEIVE_ONE_FILE_FAILURE:  //接收完一个文件---失败
                    f = (File) msg.obj;
                    if(f == null) break;
                    record = recordManager.findRecord(f.getPath(), Record.STATE_TRANSPORTING, false);
                    if(record == null) {
                        LogUtils.i(WifiP2pHelper.TAG, "WIFIP2P_RECEIVE_ONE_FILE---->record == null 更新记录状态失败");
                        break;
                    }
                    if(msg.what == WifiP2pHelper.WIFIP2P_RECEIVE_ONE_FILE_SUCCESSFULLY) {//接收成功
                        record.setState(Record.STATE_FINISHED);
                    }else { //接受失败
                        record.setState(Record.STATE_FAILED);
                    }
                    break;

                case WifiP2pHelper.WIFIP2P_UPDATE_SPEED:
                    f = (File) msg.obj;
                    double sendSpeed = 0, receiveSpeed = 0;
                    Record tempRecord;
                    //计算发送速度
                    tempRecord = recordManager.findRecord(f.getPath(), Record.STATE_TRANSPORTING, true);//查找正在发送的记录
                    if(tempRecord != null) {
                        tempRecord.setSpeedAndTranportLen(wifiP2pHelper.getSendSpeed(UPDATE_TRANSPORT_SPEED_INTERVAL), wifiP2pHelper.getSendCount());
                        sendSpeed = tempRecord.getSpeed();
                    }else {
                        LogUtils.i(WifiP2pHelper.TAG, "handler update speed ---> do not find the sending record");
                    }
                    //计算接收速度
                    tempRecord = recordManager.findRecord(f.getPath(), Record.STATE_TRANSPORTING, false);//查找正在接收的记录
                    if(tempRecord != null) {
                        tempRecord.setSpeedAndTranportLen(wifiP2pHelper.getReceiveSpeed(UPDATE_TRANSPORT_SPEED_INTERVAL), wifiP2pHelper.getReceviedCount());
                        receiveSpeed = tempRecord.getSpeed();
                    }else {
                        LogUtils.i(WifiP2pHelper.TAG, "handler update speed ---> do not find the receving record");
                    }
                    //更新悬浮窗显示的速度
                    SpeedFloatWin.updateSpeed(sendSpeed+"M/S",
                            receiveSpeed+"M/S");
                    handler.removeMessages(WifiP2pHelper.WIFIP2P_UPDATE_SPEED);

                    if(wifiP2pHelper.isTranfering()) {
                        Message msg2 = new Message();
                        msg2.what = WifiP2pHelper.WIFIP2P_UPDATE_SPEED;
                        msg2.obj = f;
                        handler.sendMessageDelayed(msg2, UPDATE_TRANSPORT_SPEED_INTERVAL);
                    }
                    break;
                case WifiP2pHelper.WIFIP2P_UPDATE_PROGRESS:
                    f = (File) msg.obj;
                    //更新已接受的长度
                    record = recordManager.findRecord(f.getPath(), Record.STATE_TRANSPORTING, true);//查找正在发送的记录
                    if(record != null) {
                        record.setTransported_len(wifiP2pHelper.getSendCount());
                    }
                    //更新已发送的长度
                    record = recordManager.findRecord(f.getPath(), Record.STATE_TRANSPORTING, false);//查找正在接收的记录
                    if(record != null) {
                        record.setTransported_len(wifiP2pHelper.getReceviedCount());
                    }
                    handler.removeMessages(WifiP2pHelper.WIFIP2P_UPDATE_PROGRESS);
                    if(wifiP2pHelper.isTranfering()) {
                        Message msg2 = new Message();
                        msg2.what = WifiP2pHelper.WIFIP2P_UPDATE_PROGRESS;
                        msg2.obj = f;
                        handler.sendMessageDelayed(msg2, UPDATE_TRANSPORT_PROGRESS_INTERVAL);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        selectFiles = new ArrayList<>();
        wifiP2pHelper = new WifiP2pHelper(this, this.handler);

        contentfragment = (ContentFragment) getSupportFragmentManager().findFragmentById(R.id.id_content);
        setOnSendFileListChangeListener(contentfragment);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawer_layout);
        // 實作 drawer toggle 並放入 toolbar
        toolbar = (Toolbar) findViewById(R.id.id_toolbar_layout);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        deviceConnectDialog = new DeviceConnectDialog(this, R.style.FullHeightDialog);

        clearSendFileList();

        //注册监听

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

        registerReceiver(wifiP2pHelper, intentFilter);

        RecordManager.getInstance(this).clearAllRecord();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.download) {
            Intent intent = new Intent(MainActivity.this,DownloadActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //显示传输速度窗口
        /*requestPermission(this.hashCode()%200 + REQUEST_CODE_SYSTEM_ALERT_PERMISSION,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                new Runnable() {
                    @Override
                    public void run() {
                        SpeedFloatWin.show(MainActivity.this);
                    }
                },null);*/
        RecordManager manager = RecordManager.getInstance(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        SpeedFloatWin.hide(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiP2pHelper);
        LogUtils.i(WifiP2pHelper.TAG, "MainActivity onDestroy");
        wifiP2pHelper.release();
        FileResLoaderUtils.release();
    }



    @Override
    public void onBackPressed() {
        FileExplorerFragment fileExplorerFragment = contentfragment.getFileExplorerFragment();
        if (contentfragment.isNowFileExplorerFragment() && fileExplorerFragment != null && fileExplorerFragment.back()) {
            Log.d(WifiP2pHelper.TAG, "文件浏览器返回");
            return;
        }
        super.onBackPressed();
    }

    public File getReceivedFileDirPath() {
        if(received_file_path == null) {
            received_file_path = new File(SdcardUtils.getUseableSdcardFile(getApplicationContext(), false),
                    getResources().getString(R.string.received_file_dir));
        }
        return received_file_path;
    }

    //添加文件到选择列表中
    public boolean addFileToSendFileList(String path) {
        return addFileToSendFileList(path, null);
    }
    public boolean addFileToSendFileList(String path, String name) {
        if(path==null || "".equals(path)) {
            return false;
        }
        boolean isExist = false;
        for(int i=0; i<selectFiles.size(); i++) {
            String temp = selectFiles.get(i);
            if(path.equals(temp)) {
                isExist = true;
                break;
            }
        }
        if(!isExist) {
            selectFiles.add(path);
            if(this.onSendFileListChangeListener!=null) {
                this.onSendFileListChangeListener.onSendFileListChange(this.selectFiles, selectFiles.size());
            }
        }
        return !isExist;
    }
    public void removeFileFromSendFileList(ArrayList<String> list) {
        if(list == null) return;
        for(int i=0; i<list.size(); i++) {
            removeFileFromSendFileList(list.get(i));
        }
    }
    //remove a file in the sendFile-list
    public boolean removeFileFromSendFileList(String path) {
        if(path==null || "".equals(path)) {
            return false;
        }
        boolean isExist = false;
        for(int i=0; i<selectFiles.size(); i++) {
            String temp = selectFiles.get(i);
            if(path.equals(temp)) {
                selectFiles.remove(i);
                isExist = true;
                if(this.onSendFileListChangeListener!=null) {
                    this.onSendFileListChangeListener.onSendFileListChange(this.selectFiles, selectFiles.size());
                }
                break;
            }
        }
        return isExist;
    }

    //其他地方主动请求更新已选择的发送列表
    public void askUpdatSendFileList(OnSendFileListChangeListener listener) {
        if(listener != null) {
            this.onSendFileListChangeListener.onSendFileListChange(this.selectFiles, selectFiles.size());
        }
    }

    //clear the sendFile-list
    public void clearSendFileList() {
        selectFiles.clear();
        if(this.onSendFileListChangeListener!=null) {
            this.onSendFileListChangeListener.onSendFileListChange(this.selectFiles, selectFiles.size());
        }
    }

    public ArrayList<String> getSendFiles() {
        return this.selectFiles;
    }
    public WifiP2pHelper getWifiP2pHelper() {
        return wifiP2pHelper;
    }

    public Handler getHandler() {
        return this.handler;
    }

    public  DeviceConnectDialog getDeviceConnectDialog() {
        return this.deviceConnectDialog;
    }

    public void setOnSendFileListChangeListener(OnSendFileListChangeListener onSendFileListChangeListener) {
        this.onSendFileListChangeListener = onSendFileListChangeListener;
    }

    //选择的发送列表改变
    public interface OnSendFileListChangeListener {
        public abstract void onSendFileListChange(ArrayList<String> selectFiles, int num);
    }
}
