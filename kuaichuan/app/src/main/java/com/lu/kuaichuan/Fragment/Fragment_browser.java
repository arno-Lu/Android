package com.lu.kuaichuan.Fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lu.kuaichuan.File.FileManager;
import com.lu.kuaichuan.File.LocaleFileAdapter;
import com.lu.kuaichuan.File.SyncImageLoader;
import com.lu.kuaichuan.File.TFile;
import com.lu.kuaichuan.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lu.com.kuaichuan.R;

/**
 * Created by Lu on 2016/5/15.
 */
public class Fragment_browser extends Fragment implements AdapterView.OnItemClickListener{
    private String tag = "LocaleFileBrowser";
    private File curFile;//当前目录
    private String startPath;//初始path
    private TextView curDir;
    private ListView lv;
    private List<TFile> data;
    private LocaleFileAdapter adapter;
    private TextView emptyView;
    private FileManager bfm;

    private SyncImageLoader.OnImageLoadListener imageLoadListener;
    private SyncImageLoader syncImageLoader;
    private AbsListView.OnScrollListener onScrollListener;

    private int firstImageFileIndex;//第一个图片文件的index(滚动时只对于普通文件loadImage)



    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        bfm = FileManager.getInstance();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState) {

        View mainView = inflater.inflate(R.layout.localefile_browser,container,false);
        curDir = (TextView) mainView.findViewById(R.id.curDir);
        lv = (ListView) mainView.findViewById(R.id.listView);
        lv.setOnItemClickListener(this);
        emptyView = (TextView) mainView.findViewById(R.id.emptyView);
        initData();

        return mainView;
    }



    private void initData() {
        // TODO Auto-generated method stub
        Bundle bundle = getArguments();
        startPath = bundle.getString("startPath");
        if(!FileUtils.isDir(startPath)){
            startPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        syncImageLoader = new SyncImageLoader();
        imageLoadListener= new SyncImageLoader.OnImageLoadListener() {
            @Override
            public void onImageLoad(Integer t, Drawable drawable) {
                View view = lv.findViewWithTag(t);
                if (view != null) {
                    ImageView iv = (ImageView) view
                            .findViewById(R.id.fileType);
                    iv.setImageDrawable(drawable);
                }else{
                    Log.i(tag, "View not exists");
                }
            }

            @Override
            public void onError(Integer t) {
                View view = lv.findViewWithTag(t);
                if (view != null) {
                    ImageView iv = (ImageView) view
                            .findViewById(R.id.fileType);
                    iv.setImageResource(R.drawable.bxfile_file_unknow);
                }else{
                    Log.i(tag, " onError View not exists");
                }
            }
        };
        onScrollListener = new AbsListView.OnScrollListener() {
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
        setData(startPath);
    }

    //在文件夹区域，不用load
    public void loadImage() {
        int start = lv.getFirstVisiblePosition();
        int end = lv.getLastVisiblePosition();
        if(end<firstImageFileIndex){
            Log.i(tag, "loadImage return");
            return;
        }
        if(start<firstImageFileIndex)
            start = firstImageFileIndex;
        if (end >= data.size()) {
            end = data.size() - 1;
        }
//		Log.i(tag, "loadImage start:"+start+" , end:"+end);
        syncImageLoader.setLoadLimit(start, end);
        syncImageLoader.unlock();
    }

    private void setData(String dirPath){
        curDir.setText(dirPath);
        curFile = new File(dirPath);
        File[] childs = curFile.listFiles();
        if(null == childs || 0==childs.length){
            lv.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }else{
            lv.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            if(null!=data)
                data.clear();
            else
                data = new ArrayList<TFile>();
            for(File f:childs){
                TFile.Builder builder = new TFile.Builder(f.getAbsolutePath());
                TFile bxfile = builder.build();
                if(null != bxfile)
                    data.add(bxfile);
            }
            Collections.sort(data);
            initFirstFileIndex();
            if(null == adapter){
                syncImageLoader.restore();
                adapter = new LocaleFileAdapter(data,getActivity(),syncImageLoader,imageLoadListener);
                lv.setAdapter(adapter);
                lv.setOnScrollListener(onScrollListener);
                loadImage();
            }else{
                syncImageLoader.restore();
                loadImage();
                adapter.notifyDataSetChanged();
                lv.setSelection(0);
            }
        }
    }

    //找到第一个图片类型文件index
    private void initFirstFileIndex(){
        firstImageFileIndex = -1;
        for(int i=0;i<data.size();i++){
            TFile f = data.get(i);
            if(!f.isDir() && f.getMimeType().equals(TFile.MimeType.IMAGE)){
                firstImageFileIndex = i;
                return ;
            }
        }
    }



    //点击 目录 进入下一级；点击文件进行勾选操作
    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
        TFile bxfile = data.get(pos);
        if(bxfile.isDir()){
            setData(bxfile.getFilePath());
        }else{
                List<TFile> choosedFiles = bfm.getChoosedFiles();
                CheckBox fileCheckBox = (CheckBox) view.findViewById(R.id.fileCheckBox);
                if(choosedFiles.contains(bxfile)){
                    choosedFiles.remove(bxfile);
                    fileCheckBox.setChecked(false);
                }else{
                        choosedFiles.add(bxfile);
                        fileCheckBox.setChecked(true);
                }
            int cnt = bfm.getFilesCnt();
            sendBroadcast(cnt);

        }


    }
    public void sendBroadcast(int cnt){
        Intent intent = new Intent("send_cnt_change");
        intent.putExtra("cnt",cnt);
        getActivity().sendBroadcast(intent);
    }


    //退到初始目录，finish() , else显示上级目录
    /*@Override
    public void onBackPressed() {
        if(startPath.equals(curFile.getAbsolutePath())){
            super.onBackPressed();
        }else{
            setData(curFile.getParentFile().getAbsolutePath());
        }
    }*/
}
