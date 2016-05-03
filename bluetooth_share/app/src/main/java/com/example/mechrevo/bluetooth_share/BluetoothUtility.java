package com.example.mechrevo.bluetooth_share;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by mechrevo on 2016/5/3.
 */
public class BluetoothUtility implements StaticVar {
    private Context context =null;
    private BluetoothAdapter bluetoothAdapter = null;
    private ArrayList<BluetoothDevice> drivers = null;	// 连接设备
    private ProgressDialog progressDialog = null;
    private Handler uiHandler = null;
    private BluetoothSocket btSocket = null;

    public BluetoothUtility(Context context, BluetoothAdapter bluetoothAdapter,
                            ProgressDialog progressDialog, Handler uiHandler) {
        this.context = context;
        this.bluetoothAdapter = bluetoothAdapter;
        this.progressDialog = progressDialog;
        this.drivers = new ArrayList<BluetoothDevice>();
        this.uiHandler = uiHandler;
    }

    /** 打开蓝牙 */
    public void openBluetooth() {
        // 循环直到打开
        while(!bluetoothAdapter.isEnabled())
            bluetoothAdapter.enable();

        if (!bluetoothAdapter.isEnabled()) {
            // 只要权限够，直接打开不需要询问
            bluetoothAdapter.enable();
		    /* 需要询问用户代码 @Deprecated */
//		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//		    ((Activity)context).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//		    // 设置对外可见, 300秒是最大时间
//		    Intent discoverableIntent = new Intent(BluetoothAdapter. ACTION_REQUEST_DISCOVERABLE );
//		    discoverableIntent.putExtra(BluetoothAdapter. EXTRA_DISCOVERABLE_DURATION , 300);
//		    ((Activity)context).startActivity(discoverableIntent);
        }
    }

    /** 关闭蓝牙 */
    public void closeBluetooth() {
        if(this.bluetoothAdapter.isEnabled())
            this.bluetoothAdapter.disable();
    }

    /** 搜索蓝牙 */
    public void searchBluetooth() {
        this.progressDialog.show();
        // 开始找寻设备
        if(!bluetoothAdapter.isEnabled())
            this.openBluetooth();

        // 注册监听器，收取发现设备信息， 记得多注册一个 ACTION_DISCOVERY_FINISHED
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        // test
        filter.addAction("android.bluetooth.device.action.PAIRING_REQUEST");

        this.context.registerReceiver(mReceiver, filter);
        Log.d("Debug", "registerReceiver receiver");
        // bluetoothAdapter打开的准备阶段会无法搜索
        // 这样直到直到打开成功
        while(!this.bluetoothAdapter.startDiscovery()) ;
        // 清理之前搜索过的设备
        this.drivers.clear();
        Log.d("Debug", "startDiscovery");
    }

    /**
     * 发送文件
     * @param macAddress 蓝牙地址
     */
    public void sendFile(String macAddress, String path) {
        BluetoothDevice bluetoothDevice = this.bluetoothAdapter.getRemoteDevice(macAddress);
        try {
            Method method = bluetoothDevice.getClass().getMethod("createRfcommSocket",
                    new Class[] {int.class});
            // this.btSocket = (BluetoothSocket) method.invoke(bluetoothDevice, 1);
            method.invoke(bluetoothDevice, 1);
            ContentValues cv = new ContentValues();
            // 文件名字是 file:// + 文件名，这个地方需要注意 多加 /
            // eg: cv.put("uri", "file:///system/app/Contacts.apk");
            // socket可以不用连接
			/* this.btSocket.connect(); */
            cv.put("uri", PATH + path);
            cv.put("destination", macAddress);
            cv.put("direction", 0);
            cv.put("timestamp", System.currentTimeMillis());
            this.context.getContentResolver().insert(
                    Uri.parse("content://com.android.bluetooth.opp/btopp"), cv);
            // 发送完毕取消连接
            // btSocket.close();
        } catch (Exception e) {
            // 其他错误
            e.printStackTrace();
            Toast.makeText(context, context.getResources().getString(R.string.title_send_fail),
                    Toast.LENGTH_LONG).show();
        } finally {
            Toast.makeText(context, context.getResources().getString(R.string.title_sending),
                    Toast.LENGTH_LONG).show();
            if(btSocket != null) {
                try {
                    btSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /** 搜索到的设备 */
    public ArrayList<BluetoothDevice> getDrivers() {
        return this.drivers;
    }

    /** 完成查找，释放资源 */
    public void finishSearch() {
        // 解除监听器
        this.context.unregisterReceiver(mReceiver);
        // 取消搜索，建立连接提高效率
        this.bluetoothAdapter.cancelDiscovery();
        Log.d("Debug", "unregisterReceiver receiver an cancelDiscovery");
    }

    /** 接受找寻蓝牙设备信息 */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("Debug", action.toString());
            // 发现一个设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // 拿出蓝牙设备信息
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 保存信息，供列表显示
                // 防止重复添加
                if(drivers.contains(device)) return ;
                drivers.add(device);
                Log.d("Debug", "ACTION_FOUND:" + drivers.size());
            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                progressDialog.dismiss();
                // 未找到设备
                uiHandler.sendEmptyMessage(CHANG_LIST);
                Log.d("Debug", "ACTION_DISCOVERY_FINISHED");
            } else if(action.equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
                BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String tmp = "123";
                Method setPsd;
                try {
                    setPsd = btDevice.getClass().getDeclaredMethod(
                            "setPin", new Class[]{byte[].class});
                    setPsd.invoke(btDevice, new Object[] {tmp.getBytes()});
                    Method createBondMethod = btDevice.getClass().getMethod("createBond");
                    createBondMethod.invoke(btDevice);
                    // return returnValue.booleanValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("android.bluetooth.device.action.PAIRING_REQUEST");
            }
        }
    };
}

