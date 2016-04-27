package com.example.mechrevo.bluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonStart;
    private Button buttonFind;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonStart = (Button)findViewById(R.id.btn);
        buttonFind = (Button)findViewById(R.id.find);
        textView = (TextView)findViewById(R.id.text_view);
        buttonStart.setOnClickListener(this);
        buttonFind.setOnClickListener(this);
    }

        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.btn:
                    BluetoothAdapter  adapter = BluetoothAdapter.getDefaultAdapter();
                    if(adapter!=null){
                        Toast.makeText(MainActivity.this,"本机拥有蓝牙设备",Toast.LENGTH_SHORT).show();
                        if(!adapter.isEnabled()){
                            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            Toast.makeText(MainActivity.this,"开始启动蓝牙",Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                        }
                        Set<BluetoothDevice> devices = adapter.getBondedDevices();
                        if(devices.size()>0){
                            for(Iterator iterator = devices.iterator();iterator.hasNext();){
                                BluetoothDevice device = (BluetoothDevice) iterator.next();
                                textView.setText("123");
                                textView.setText(device.getAddress());
                            };
                        }
                    }else
                    {
                        Toast.makeText(MainActivity.this,"本机没有蓝牙设备",Toast.LENGTH_SHORT).show();
                    }
                case R.id.find:
                    Intent findIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    findIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,500);
                    startActivity(findIntent);

                    BluetoothAdapter bluetoothAdapter = null;
                    BluetoothReceiver bluetoothReceiver = null;

                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    bluetoothReceiver = new BluetoothReceiver();

                    registerReceiver(bluetoothReceiver,intentFilter);

                    bluetoothAdapter.startDiscovery();
                    break;
                default:
                    break;
            }

        }

    private class BluetoothReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                textView.setText(bluetoothDevice.getAddress());
            }
        }
    }

}
