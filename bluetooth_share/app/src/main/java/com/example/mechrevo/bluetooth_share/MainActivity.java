package com.example.mechrevo.bluetooth_share;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView =null;
    private PackageManager packageManager = null;
    //保存包信息
    private ArrayList<PackageInfo> packageInfos = null;
    //提示对话框
    private AlertDialog.Builder alterBuilder  = null;
    private BluetoothAdapter bluetoothAdapter =null;
    //apk信息，保存包名，路径
    public static HashMap<String,String> apkInfo = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化必要信息
        this.init();
    }

    @Override
    protected void onDestroy(){  //退出回收信息，protect属性

        apkInfo = null;
        if(this.bluetoothAdapter.isEnabled()){
            this.bluetoothAdapter.disable(); //关闭
        }
        super.onDestroy();
    }

    private void init(){

        listView = (ListView)findViewById(R.id.list_app);
        listView.setOnItemClickListener(new ListItemClick());
        packageManager = (PackageManager)getPackageManager();
        packageInfos = new ArrayList<PackageInfo>();
        getAppInfo(packageInfos,packageManager);
        listView.setAdapter(new AppAdapter(this,packageInfos));

        // 获取蓝牙设备
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //本机蓝牙不可用
        if(bluetoothAdapter == null){
            alterBuilder = new AlertDialog.Builder(this);
            alterBuilder.setMessage(R.string.title_unuse)
                    .setNegativeButton(R.string.title_ok,null);  //封装的button
        }
    }

    private class ListItemClick implements AdapterView.OnItemClickListener{

        public void onItemClick(AdapterView<?>parent, View view,int position,long id){

            if(bluetoothAdapter == null){
                alterBuilder.create().show();
            }else {
                Intent intent = new Intent(MainActivity.this,TipDialogActivity.this);
                intent.putExtra("pack_name",packageInfos.get(position).applicationInfo.packageName);
                startActivity(intent);
            }
        }
    }
    @SuppressWarnings("static-access")
    private void getAppInfo(ArrayList<PackageInfo> infos, PackageManager packageManager){

        infos.clear();
        List<PackageInfo> packlist =packageManager.getInstalledPackages(0);
        for(int i=0;i<packlist.size();i++){
            PackageInfo pack = (PackageInfo) packlist.get(i);
            //只保存用户安装软件，非系统应用
            if((pack.applicationInfo.flags & pack.applicationInfo.FLAG_SYSTEM) <= 0){
                infos.add(pack);
            }
        }
    }
}
