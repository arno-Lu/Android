package com.lu.kuaichuan.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.lu.kuaichuan.Adapter.LocalFileAdapter;
import com.lu.kuaichuan.File.FEApplication;
import com.lu.kuaichuan.File.FileManager;
import com.lu.kuaichuan.File.TFile;

import java.util.Collections;
import java.util.List;

import lu.com.kuaichuan.R;

/**
 * Created by Lu on 2016/5/15.
 */
public class Fragment_office_make extends Fragment implements AdapterView.OnItemClickListener {

    private ListView lv;
    private List<TFile> data;
    private LocalFileAdapter adapter;
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
                adapter = new LocalFileAdapter(data, getActivity().getApplicationContext(), null, null);
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
        final View mainView = inflater.inflate(R.layout.localfile_browser_fragmentmake, container, false);
        bfm = FileManager.getInstance();

        lv = (ListView) mainView.findViewById(R.id.listView);
        lv.setOnItemClickListener(this);
        emptyView = (TextView) mainView.findViewById(R.id.emptyView);


        Bundle bundle = getArguments();
        if (bundle.getString("Type")=="DOC")
            setData("(" + MediaStore.Files.FileColumns.MIME_TYPE + "=='application/msword')");
        if (bundle.getString("Type")=="PPT")
            setData("(" + MediaStore.Files.FileColumns.MIME_TYPE + "=='application/mspowerpoint')");
        if (bundle.getString("Type")=="XLS")
            setData("(" + MediaStore.Files.FileColumns.MIME_TYPE + "=='application/msexcel')");
        if (bundle.getString("Type")=="PDF")
            setData("(" + MediaStore.Files.FileColumns.MIME_TYPE + "=='application/pdf')");
        if (bundle.getString("Type")=="TXT")
            setData("(" + MediaStore.Files.FileColumns.MIME_TYPE + "=='text/plain')");
        if (bundle.getString("Type")=="ZIP")
            setData("(" + MediaStore.Files.FileColumns.MIME_TYPE + "=='application/zip')");
        if (bundle.getString("Type")=="RAR")
            setData("(" + MediaStore.Files.FileColumns.MIME_TYPE + "=='application/x-rar-compressed')");

        return mainView;
    }


    private void setData(final String type) {
        FEApplication bxApp = (FEApplication) getActivity().getApplication();
        bxApp.execRunnable(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                data = bfm.getMediaFiles(getActivity(),MediaStore.Files.getContentUri("external"),type);
                if (null != data) {
                    Collections.sort(data);

                    handler.sendEmptyMessage(1);

                } else
                    handler.sendEmptyMessage(0);
            }

        });
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
        int cnt = bfm.getFilesCnt();
        sendBroadcast(cnt);
    }

    public void sendBroadcast(int cnt) {
        Intent intent = new Intent("send_cnt_change");
        intent.putExtra("cnt", cnt);
        getActivity().sendBroadcast(intent);

    }
}