package com.ckt.io.wifidirect.fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ckt.io.wifidirect.activity.MainActivity;
import com.ckt.io.wifidirect.R;
import com.ckt.io.wifidirect.File.FileManager;
import com.ckt.io.wifidirect.File.SyncImageLoader;
import com.ckt.io.wifidirect.File.TFile;
import com.ckt.io.wifidirect.File.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lu on 2016/5/24.
 */
public class Fragment_pic extends Fragment implements AdapterView.OnItemClickListener{
    private String tag = "LocaleFileGallery";
    private GridView gv;
    private MyGVAdapter adapter;
    private List<TFile> data;
    private TextView emptyView;
    private FileManager bfm;



    private SyncImageLoader syncImageLoader;
    private int gridSize;
    private AbsListView.LayoutParams gridItemParams;//主要根据不同分辨率设置item宽高

    ArrayList<Boolean> checkBoxList = new ArrayList<>();
    ArrayList<String> mPathList = new ArrayList<>();


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (1 == msg.what) {
                syncImageLoader = new SyncImageLoader();
                gridItemParams = new AbsListView.LayoutParams(gridSize, gridSize);
                adapter = new MyGVAdapter();
                gv.setAdapter(adapter);
                gv.setOnScrollListener(adapter.onScrollListener);
                gv.setOnItemClickListener(Fragment_pic.this);
            } else if (0 == msg.what) {
                gv.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText("该分类没有文件");
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View mainView = inflater.inflate(R.layout.localefile_gallery, container, false);
        bfm = FileManager.getInstance();
        gv = (GridView) mainView.findViewById(R.id.gridView);
        emptyView = (TextView) mainView.findViewById(R.id.emptyView);


        //计算一下在不同分辨率下gridItem应该站的宽度，在adapter里重置一下item宽高
        gridSize = (Utils.getScreenWidth(getActivity()) - getResources().getDimensionPixelSize(R.dimen.view_8dp) * 5) / 4;// 4列3个间隔，加上左右padding，共计5个
//		Log.i(tag, "gridSize:"+gridSize);
        return mainView;
    }



    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (null == data) {
            new Thread(){
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    data = bfm.getMediaFiles(getActivity(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    if(data != null) {
                        for(int i=0; i<data.size(); i++) {
                            checkBoxList.add(false);
                        }
                    }
                    if (null != data)
                        handler.sendEmptyMessage(1);
                    else
                        handler.sendEmptyMessage(0);
                }

            }.start();
        }else {
            //Fragment 的生命周期在ViewPager中有不同，回用date
            if(data != null) {
                for(int i=0; i<data.size(); i++) {
                    checkBoxList.add(false);
                }
            }
            syncImageLoader = new SyncImageLoader();
            gridItemParams = new AbsListView.LayoutParams(gridSize, gridSize);
            adapter = new MyGVAdapter();
            gv.setAdapter(adapter);
            gv.setOnScrollListener(adapter.onScrollListener);
            gv.setOnItemClickListener(Fragment_pic.this);
        }
    }



    class MyGVAdapter extends BaseAdapter {

        ArrayList<Boolean> checkBoxList = new ArrayList<>();
        public ArrayList<Boolean> getmCheckBoxList() {
            return this.checkBoxList;
        }


        AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        syncImageLoader.lock();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        loadImage();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        syncImageLoader.lock();
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        };

        public void loadImage() {
            int start = gv.getFirstVisiblePosition();
            int end = gv.getLastVisiblePosition();
            if (end >= getCount()) {
                end = getCount() - 1;
            }
            syncImageLoader.setLoadLimit(start, end);
            syncImageLoader.unlock();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if(null!=data)
                return data.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            for(int i=0;i<data.size();i++){
                checkBoxList.add(false);
            }
            if(null == convertView){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.gallery_item, null);
            }
            ImageView img = (ImageView) convertView.findViewById(R.id.img);
            img.setImageResource(R.drawable.bxfile_file_default_pic);
            View itemView = convertView.findViewById(R.id.itemView);
            CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.id_grid_view_checkbox_pic);
            //重置宽高
            itemView.setLayoutParams(gridItemParams);
            TFile bxfile = data.get(position);
            img.setTag(position);
            syncImageLoader.loadDiskImage(position, bxfile.getFilePath(), imageLoadListener);


            mCheckBox.setChecked(checkBoxList.get(position));
            mCheckBox.setVisibility(checkBoxList.get(position) ? View.VISIBLE : View.GONE);
            return convertView;
        }


        SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener() {
            @Override
            public void onImageLoad(Integer t, Drawable drawable) {
                View view = gv.findViewWithTag(t);
                if (view != null) {
                    ImageView iv = (ImageView) view
                            .findViewById(R.id.img);
                    iv.setImageDrawable(drawable);

                }else{
                    Log.i(tag, "View not exists");
                }
            }

            @Override
            public void onError(Integer t) {
                View view = gv.findViewWithTag(t);
                if (view != null) {
                    ImageView iv = (ImageView) view
                            .findViewById(R.id.img);
                    iv.setImageResource(R.drawable.bxfile_file_default_pic);

                }else{
                    Log.i(tag, " onError View not exists");
                }
            }


        };
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View convertView, int pos, long arg3) {

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