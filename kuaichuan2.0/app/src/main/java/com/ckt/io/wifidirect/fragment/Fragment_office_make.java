package com.ckt.io.wifidirect.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ckt.io.wifidirect.activity.MainActivity;
import com.ckt.io.wifidirect.R;
import com.ckt.io.wifidirect.adapter.LocalFileAdapter;
import com.ckt.io.wifidirect.File.FileManager;
import com.ckt.io.wifidirect.File.TFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Lu on 2016/5/24.
 */
public class Fragment_office_make extends Fragment implements AdapterView.OnItemClickListener {

    private ListView lv;
    private List<TFile> data;
    private LocalFileAdapter adapter;
    private TextView emptyView;
    private FileManager bfm;

    ArrayList<Boolean> checkBoxList = new ArrayList<>();
    ArrayList<String> mPathList = new ArrayList<>();


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
        new Thread(){
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

        }.start();
    }


    //点击文件进行勾选操作
    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {

        TFile bxfile = data.get(pos);
        Log.d("PIC", "onItemClick: "+bxfile.getFilePath()+bxfile.getFileName());
        checkBoxList = adapter.getmCheckBoxList();

        checkBoxList.set(pos, !checkBoxList.get(pos));

        MainActivity activity = (MainActivity) getActivity();
        if (checkBoxList.get(pos)) {//checked--->add to sendfile-list
            mPathList.add(bxfile.getFilePath());
            activity.addFileToSendFileList(bxfile.getFilePath(),bxfile.getFileName());

        } else {//unchecked--->remove from sendfile-list
            activity.removeFileFromSendFileList(bxfile.getFilePath());
            mPathList.remove(bxfile.getFilePath());
        }
        adapter.notifyDataSetChanged();



    }


}