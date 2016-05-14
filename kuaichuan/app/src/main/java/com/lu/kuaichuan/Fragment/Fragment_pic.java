package com.lu.kuaichuan.Fragment;

import android.app.Application;
import android.content.Intent;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lu.kuaichuan.File.FEApplication;
import com.lu.kuaichuan.File.FileManager;
import com.lu.kuaichuan.File.SyncImageLoader;
import com.lu.kuaichuan.File.TFile;
import com.lu.kuaichuan.util.Utils;

import java.util.List;

import lu.com.kuaichuan.R;

/**
 * Created by Lu on 2016/5/14.
 */
public class Fragment_pic extends Fragment implements AdapterView.OnItemClickListener {
    private String tag = "LocaleFileGallery";
    private GridView gv;
    private MyGVAdapter adapter;
    private List<TFile> data;
    private TextView emptyView;
    private FileManager bfm;


    private List<TFile> choosedFiles;
    private SyncImageLoader syncImageLoader;
    private int gridSize;
    private AbsListView.LayoutParams gridItemParams;//主要根据不同分辨率设置item宽高




    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (1 == msg.what) {
                syncImageLoader = new SyncImageLoader();
                choosedFiles = bfm.getChoosedFiles();
                gridItemParams = new AbsListView.LayoutParams(gridSize, gridSize);
                adapter = new MyGVAdapter();
                gv.setAdapter(adapter);
                gv.setOnScrollListener(adapter.onScrollListener);
                gv.setOnItemClickListener(Fragment_pic.this);
            } else if (0 == msg.what) {
                gv.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText(getString(R.string.curCatagoryNoFiles));
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
            FEApplication bxApp = (FEApplication) getActivity().getApplication();
            bxApp.execRunnable(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    data = bfm.getMediaFiles(getActivity(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    if (null != data)
                        handler.sendEmptyMessage(1);
                    else
                        handler.sendEmptyMessage(0);
                }

            });
        }else {
            //Fragment 的生命周期在ViewPager中有不同，回用date
            syncImageLoader = new SyncImageLoader();
            choosedFiles = bfm.getChoosedFiles();
            gridItemParams = new AbsListView.LayoutParams(gridSize, gridSize);
            adapter = new MyGVAdapter();
            gv.setAdapter(adapter);
            gv.setOnScrollListener(adapter.onScrollListener);
            gv.setOnItemClickListener(Fragment_pic.this);
        }
    }



    class MyGVAdapter extends BaseAdapter {

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
            if(null == convertView){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.gallery_item, null);
            }
            ImageView img = (ImageView) convertView.findViewById(R.id.img);
            img.setImageResource(R.drawable.bxfile_file_default_pic);
            View itemView = convertView.findViewById(R.id.itemView);
            //重置宽高
            itemView.setLayoutParams(gridItemParams);
            TFile bxfile = data.get(position);
            img.setTag(position);
            syncImageLoader.loadDiskImage(position, bxfile.getFilePath(), imageLoadListener);
            View checkView = convertView.findViewById(R.id.checkView);
            if(choosedFiles.contains(bxfile)){
                checkView.setVisibility(View.VISIBLE);
            }else{
                checkView.setVisibility(View.GONE);
            }
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
        View checkView = convertView.findViewById(R.id.checkView);
        TFile bxfile = data.get(pos);

        if (choosedFiles.contains(bxfile)) {
            choosedFiles.remove(bxfile);
            checkView.setVisibility(View.GONE);
        } else {

            choosedFiles.add(bxfile);
            checkView.setVisibility(View.VISIBLE);
        }
        int cnt = bfm.getFilesCnt();
        sendBroadcast(cnt);

    }

    public void sendBroadcast(int cnt){
        Intent intent = new Intent("send_cnt_change");
        intent.putExtra("cnt",cnt);
        getActivity().sendBroadcast(intent);
    }

}
