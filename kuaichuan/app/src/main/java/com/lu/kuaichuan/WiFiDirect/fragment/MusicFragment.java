package com.lu.kuaichuan.wifidirect.fragment;


import android.Manifest;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lu.kuaichuan.wifidirect.activity.MainActivity;
import com.lu.kuaichuan.wifidirect.R;
import com.lu.kuaichuan.wifidirect.adapter.MyListViewAdapter;
import com.lu.kuaichuan.wifidirect.p2p.WifiP2pHelper;
import com.lu.kuaichuan.wifidirect.utils.AudioUtils;
import com.lu.kuaichuan.wifidirect.utils.FileResLoaderUtils;
import com.lu.kuaichuan.wifidirect.utils.LogUtils;
import com.lu.kuaichuan.wifidirect.utils.SdcardUtils;
import com.lu.kuaichuan.wifidirect.utils.Song;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by ckt on 2/29/16.
 */
public class MusicFragment extends MyBaseFragment implements View.OnClickListener,
        AdapterView.OnItemLongClickListener,
        AdapterView.OnItemClickListener, MainActivity.OnSendFileListChangeListener,
        FileResLoaderUtils.OnLoadFinishedListener {
    private ArrayList<Song> songList = new ArrayList<>();
    private ArrayList<String> nameList = new ArrayList<>();
    private ArrayList<Object> iconList = new ArrayList<>();
    private ArrayList<Boolean> checkBoxList = new ArrayList<>();
    private ListView listView;
    private TextView refresh;
    MyListViewAdapter adapter;

    private FileResLoaderUtils drawableLoaderUtils;

    //������ԭlistview��λ��
    private int listViewState_pos = 0;
    private int listViewState_top = 0;

    public static final int LOAD_DATA_FINISHED = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_DATA_FINISHED:
                    listView.setAdapter(adapter);
                    listView.setSelectionFromTop(listViewState_pos, listViewState_top);
                    if (drawableLoaderUtils != null) {
                        for (int i = 0; i < nameList.size(); i++) {

//                            drawableLoaderUtils.load(getContext(), songList.get(i).getFileUrl());
                        }
                    }
                    ((BaseAdapter)(listView.getAdapter())).notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(WifiP2pHelper.TAG, "MusicFragment-->onCreateView()");
        View view = inflater.inflate(R.layout.music_layout, container, false);
        listView = (ListView) view.findViewById(R.id.id_adapter_list_view);
        refresh = (TextView) view.findViewById(R.id.id_music_refresh);

        final MainActivity activity = (MainActivity) getActivity();
        activity.requestPermission(MainActivity.REQUEST_CODE_READ_EXTERNAL + this.hashCode()%200,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                new Runnable() {
                    @Override
                    public void run() {
                        if (adapter == null) {
                            drawableLoaderUtils = FileResLoaderUtils.getInstance(MusicFragment.this);
                            loadData();
                        }else {
                            handler.sendEmptyMessage(LOAD_DATA_FINISHED);
                        }
                        refresh.setOnClickListener(MusicFragment.this);
                        listView.setOnItemClickListener(MusicFragment.this);
                        listView.setOnItemLongClickListener(MusicFragment.this);
                        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState) {

                                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) { //stop
                                    listView.setTag(false);
                                    ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                                } else { //scrolling
                                    listView.setTag(true);
                                }
                            }

                            @Override
                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
                        });
                    }
                },
                null);
        registerForContextMenu(listView);
        return view;
    }

    public void loadData() {
        new Thread() {
            @Override
            public void run() {
                ArrayList<String> fileList = new ArrayList<String>();
                songList = AudioUtils.getAllSongs(getContext());
                for (Song song : songList) {
                    nameList.add(song.getTitle());
                    iconList.add(R.drawable.music_icon);
                    checkBoxList.add(false);
                    fileList.add(song.getFileUrl());
                }
                adapter = new MyListViewAdapter(getContext(), fileList, nameList);
                handler.sendEmptyMessage(LOAD_DATA_FINISHED);
            }
        }.start();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(WifiP2pHelper.TAG, "MusicFragment-->onPause");
        listViewState_pos = listView.getFirstVisiblePosition();
        View child = listView.getChildAt(0);
        listViewState_top = child != null ? child.getTop() : 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_music_refresh:
                MediaScannerConnection.scanFile(getActivity(), new String[]{SdcardUtils.getInnerSDcardFile(getContext()).getPath()},
                        null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        LogUtils.i(WifiP2pHelper.TAG, "scan file finished");
                    }
                });
                if (songList != null) {
                    songList.clear();
                    nameList.clear();
                    iconList.clear();
                }
                songList = AudioUtils.getAllSongs(getActivity());
                for (Song song : songList) {
                    nameList.add(song.getTitle());
                    iconList.add(new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.icon)));
                }
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        checkBoxList = adapter.getmCheckBoxList();
        checkBoxList.set(position, !checkBoxList.get(position)); //���øõ�Ϊ�෴״̬
        MainActivity activity = (MainActivity) getActivity();
        if (checkBoxList.get(position)) {//checked--->add to sendfile-list
            activity.addFileToSendFileList(songList.get(position).getFileUrl(), songList.get(position).getFileName());
        } else {//unchecked--->remove from sendfile-list
            activity.removeFileFromSendFileList(songList.get(position).getFileUrl());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Object obj = adapter.getItem(position);
        if(obj!=null) {
            LogUtils.i(WifiP2pHelper.TAG, "   uri="+Uri.fromFile(new File((String) obj)));
            Intent it = new Intent(Intent.ACTION_VIEW);
//            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            it.setDataAndType(Uri.fromFile(new File((String) obj)), "audio/*");
            try {
//                startActivity(it);
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    @Override
    public void onSendFileListChange(ArrayList<String> sendFiles, int num) {
        if (adapter != null) {
            ArrayList<Boolean> checkList = adapter.getmCheckBoxList();
            String data = sendFiles.toString();
            Log.d(WifiP2pHelper.TAG, data);
            for (int i = 0; i < checkList.size(); i++) {
                String temp = songList.get(i).getFileUrl();
                if (data.contains(temp)) {
                    checkList.set(i, true);
                } else {
                    checkList.set(i, false);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadOneFinished(String path, Object obj, boolean isAllFinished) {
        int index = -1;
        for(int i=0; i<songList.size(); i++) {
            if(path.equals(songList.get(i).getFileUrl())) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            iconList.set(index, obj);
        }

        if (listView.getTag() == null || !(boolean) listView.getTag()) { //gridviewû�л���
            if(index==songList.size()/2 || isAllFinished) {
                ((BaseAdapter) (listView.getAdapter())).notifyDataSetChanged();
            }
        }
    }

    @Override
    protected String getPositonPath(int position) {
        return songList.get(position).getFileUrl();
    }
}
