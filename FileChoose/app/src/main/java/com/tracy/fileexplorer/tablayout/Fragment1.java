package com.tracy.fileexplorer.tablayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.tracy.fileexplorer.FEApplication;
import com.tracy.fileexplorer.FileManager;
import com.tracy.fileexplorer.LocaleFileAdapter;
import com.tracy.fileexplorer.LocaleMediaFileBrowser;
import com.tracy.fileexplorer.R;
import com.tracy.fileexplorer.TFile;

import java.util.Collections;
import java.util.List;

/**
 * Created by mechrevo on 2016/5/9.
 */
public class Fragment1 extends Fragment implements AdapterView.OnItemClickListener{

    private ListView lv;
    private List<TFile> data;
    private LocaleFileAdapter adapter;
    private TextView emptyView;
    private FileManager bfm;




    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // TODO Auto-generated method stub
            if (1 == msg.what) {
                lv.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                adapter = new LocaleFileAdapter(data, getActivity().getApplicationContext(), null, null);
                lv.setAdapter(adapter);
            } else if (0 == msg.what) {
                lv.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText(getString(R.string.curCatagoryNoFiles));
            }

        }

    };

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        final View mainView = inflater.inflate(R.layout.localefile_browser, container, false);
        bfm = FileManager.getInstance();

        lv = (ListView) mainView.findViewById(R.id.listView);
        lv.setOnItemClickListener(this);
        emptyView = (TextView) mainView.findViewById(R.id.emptyView);

        Bundle bundle = getArguments();
        if (bundle.getInt("music") == 1) {
            setData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        }

        return mainView;
    }



    private void setData(final Uri uri) {
      new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                data = bfm.getMediaFiles(getActivity(), uri);
                if (null != data) {
                    Collections.sort(data);

                    handler.sendEmptyMessage(1);

                } else
                    handler.sendEmptyMessage(0);
            }

        }).start();
    }


    //点击文件进行勾选操作
    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
        CheckBox fileCheckBox = (CheckBox) view.findViewById(R.id.fileCheckBox);
        TFile bxfile = data.get(pos);
        List<TFile> choosedFiles = bfm.getChoosedFiles();

        if (choosedFiles.contains(bxfile)) {
            choosedFiles.remove(bxfile);
            fileCheckBox.setChecked(false);

        } else {

            choosedFiles.add(bxfile);
            fileCheckBox.setChecked(true);

        }
        send_cnt_change();
    }

   private void send_cnt_change(){
       Intent intent_cnt = new Intent("change");
       getActivity().sendBroadcast(intent_cnt);

   }

}
