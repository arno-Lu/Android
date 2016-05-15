package com.lu.kuaichuan.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lu.kuaichuan.util.FileUtils;

import lu.com.kuaichuan.R;

/**
 * Created by Lu on 2016/5/15.
 */
public class Fragment_other_main extends Fragment{

    private String extSdCardPath;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View mainView = inflater.inflate(R.layout.fragment_other_main,container,false);

        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);

        View ram = getActivity().findViewById(R.id.localefile_ram);
        View sdcard = getActivity().findViewById(R.id.localefile_sdcard);
        View extSdcard = getActivity().findViewById(R.id.localefile_extSdcard);
        View zip = getActivity().findViewById(R.id.localefile_zip);
        View rar = getActivity().findViewById(R.id.localefile_rar);

        extSdCardPath = FileUtils.getExtSdCardPath();
        if(!TextUtils.isEmpty(extSdCardPath)) {
            extSdcard.setVisibility(View.VISIBLE);
        }

        ram.setOnClickListener(new PathClickListener());
        sdcard.setOnClickListener(new PathClickListener());
        extSdcard.setOnClickListener(new PathClickListener());
        zip.setOnClickListener(new PathClickListener());
        rar.setOnClickListener(new PathClickListener());


    }

    class PathClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view){
            switch (view.getId()){

                case R.id.localefile_ram:

                    FragmentManager fragmentManager_ram =getFragmentManager();
                    FragmentTransaction transaction_ram = fragmentManager_ram.beginTransaction();

                    Bundle bundle = new Bundle();
                    bundle.putString("startPath","/");
                    Fragment_browser fragment_browser_ram = new Fragment_browser();
                    fragment_browser_ram.setArguments(bundle);

                    transaction_ram.replace(R.id.main_other_id,fragment_browser_ram);
                    transaction_ram.addToBackStack(null);
                    transaction_ram.commit();
                    break;

                case R.id.localefile_sdcard:
                    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        FragmentManager fragmentManager_sdcard = getFragmentManager();
                        FragmentTransaction transaction_sdcard = fragmentManager_sdcard.beginTransaction();

                        Bundle bundle_sdcard = new Bundle();
                        bundle_sdcard.putString("startPath", Environment.getExternalStorageDirectory().getAbsolutePath());
                        Fragment_browser fragment_browser_sdcard = new Fragment_browser();
                        fragment_browser_sdcard.setArguments(bundle_sdcard);

                        transaction_sdcard.replace(R.id.main_other_id, fragment_browser_sdcard);
                        transaction_sdcard.addToBackStack(null);
                        transaction_sdcard.commit();
                    }else{
                        Toast.makeText(getActivity(), getString(R.string.SDCardNotMounted), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.localefile_extSdcard:
                    FragmentManager fragmentManager_extSdcard =getFragmentManager();
                    FragmentTransaction transaction_extSdcard = fragmentManager_extSdcard.beginTransaction();

                    Bundle bundle_extSdcard = new Bundle();
                    bundle_extSdcard.putString("startPath",extSdCardPath);
                    Fragment_browser fragment_browser_extSdcard = new Fragment_browser();
                    fragment_browser_extSdcard.setArguments(bundle_extSdcard);

                    transaction_extSdcard.replace(R.id.main_other_id,fragment_browser_extSdcard);
                    transaction_extSdcard.addToBackStack(null);
                    transaction_extSdcard.commit();
                    break;

                case R.id.localefile_zip:

                    FragmentManager fragmentManager_zip =getFragmentManager();
                    FragmentTransaction transaction_zip = fragmentManager_zip.beginTransaction();

                    Bundle bundle_zip = new Bundle();
                    bundle_zip.putString("Type","ZIP");
                    Fragment_office_make fragment_zip = new Fragment_office_make();
                    fragment_zip.setArguments(bundle_zip);

                    transaction_zip.replace(R.id.main_other_id,fragment_zip);
                    transaction_zip.addToBackStack(null);
                    transaction_zip.commit();
                    break;

                case R.id.localefile_rar:

                    FragmentManager fragmentManager_rar =getFragmentManager();
                    FragmentTransaction transaction_rar = fragmentManager_rar.beginTransaction();

                    Bundle bundle_rar = new Bundle();
                    bundle_rar.putString("Type","RAR");
                    Fragment_office_make fragment_rar= new Fragment_office_make();
                    fragment_rar.setArguments(bundle_rar);

                    transaction_rar.replace(R.id.main_other_id,fragment_rar);
                    transaction_rar.addToBackStack(null);
                    transaction_rar.commit();

            }
        }
    }

}
