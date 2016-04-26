package com.example.mechrevo.bluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.btn);
        textView = (TextView)findViewById(R.id.text_view);
        button.setOnClickListener(new ButtonListener());
    }

    private class ButtonListener implements View.OnClickListener{

        public void onClick(View v){
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
        }
    }

}
