package com.lu.kuaichuan.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.lu.kuaichuan.Activity.FtpActivity;

import java.io.File;

import lu.com.kuaichuan.R;

/**
 * Created by Lu on 2016/5/16.
 */
public class Fragment_connect_mian extends Fragment {

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){

        View mainView = inflater.inflate(R.layout.fragment_connect_main,container,false);

        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);
        Button phone = (Button)getActivity().findViewById(R.id.phone_connect_id);
        Button pc =(Button) getActivity().findViewById(R.id.pc_connect_id);
        Button bluetooth =(Button) getActivity().findViewById(R.id.bluttooth_id);

        phone.setOnClickListener(new WayClickListener());
        pc.setOnClickListener(new WayClickListener());
        bluetooth.setOnClickListener(new WayClickListener());

    }

    class WayClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view){
            switch (view.getId()){

                case R.id.phone_connect_id:
                    break;
                case R.id.pc_connect_id:

                    Intent intent_pc = new Intent(getActivity(), FtpActivity.class);
                    getActivity().startActivity(intent_pc);

                    break;

                case R.id.bluttooth_id:

                    Intent intent_bluetooth = new Intent();
                    intent_bluetooth.setAction(Intent.ACTION_SEND);
                    intent_bluetooth.setType("*/*");
                    intent_bluetooth.setClassName("com.android.bluetooth", "com.android.bluetooth.opp.BluetoothOppLauncherActivity");
                    File f = new File("file:///data/app/com.lu.kuaichuan-2/base.apk");
                    intent_bluetooth.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
                    startActivity(intent_bluetooth);

                    break;

                default:
                    break;
            }
        }
    }
}
