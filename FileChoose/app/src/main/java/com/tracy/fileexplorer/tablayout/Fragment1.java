package com.tracy.fileexplorer.tablayout;

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
    private TextView localefile_bottom_tv;
    private Button localefile_bottom_btn;
    private View local_bottom;
    private Button clear_bottom_btn;



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){  //判断Fragment是否为当前页面
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            onFileClick();
        }

    }


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
        localefile_bottom_btn = (Button) mainView.findViewById(R.id.localefile_bottom_btn);
        localefile_bottom_tv = (TextView) mainView.findViewById(R.id.localefile_bottom_tv);
        clear_bottom_btn =(Button)mainView.findViewById(R.id.clear_bottom_btn);
        local_bottom=mainView.findViewById(R.id.localefile_bottom);

        Bundle bundle = getArguments();
        if (bundle.getInt("music") == 1) {
            setData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        }

        onFileClick();
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);
        Button clear_btn =(Button)getActivity().findViewById(R.id.clear_bottom_btn);
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TFile> choosedFiles = bfm.getChoosedFiles();
                choosedFiles.clear();
            }
        });
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
        onFileClick();
    }

    //点击文件，触发ui更新
    //onResume，触发ui更新
    private void onFileClick() {
        int cnt = bfm.getFilesCnt();
        if(cnt==0){
            local_bottom.setVisibility(View.GONE);
        }else {
            local_bottom.setVisibility(View.VISIBLE);

            localefile_bottom_tv.setText(bfm.getFilesSizes());
            localefile_bottom_btn.setText(String.format(getString(R.string.bxfile_choosedCnt), cnt));
            localefile_bottom_btn.setEnabled(cnt>0);
        }

    }

}
