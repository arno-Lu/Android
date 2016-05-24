package com.ckt.io.wifidirect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ckt.io.wifidirect.R;
import com.ckt.io.wifidirect.File.FileManager;
import com.ckt.io.wifidirect.File.SyncImageLoader;
import com.ckt.io.wifidirect.File.TFile;
import com.ckt.io.wifidirect.File.TFile.MimeType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lu on 2016/5/24.
 */
public class LocalFileAdapter extends BaseAdapter {

    private FileManager bfm;
    private List<TFile> data;
    private Context cxt;
    int w;
    private SyncImageLoader syncImageLoader;
    private SyncImageLoader.OnImageLoadListener imageLoadListener;
    private ArrayList<Boolean> checkBoxList = new ArrayList<>();
    ;
    public ArrayList<Boolean> getmCheckBoxList() {
        return this.checkBoxList;
    }

    public LocalFileAdapter(List<TFile> data, Context cxt , SyncImageLoader syncImageLoader , SyncImageLoader.OnImageLoadListener imageLoadListener) {
        super();
        this.data = data;
        this.cxt = cxt;
        this.syncImageLoader = syncImageLoader;
        this.imageLoadListener = imageLoadListener;
        bfm = FileManager.getInstance();
        w = cxt.getResources().getDimensionPixelSize(R.dimen.view_36dp);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(null!=data)
            return data.size();
        return 0;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    //目录：显示目录view;文件：显示文件view及勾选状况
    @Override
    public View getView(int pos, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        for(int i=0;i<data.size();i++){
            checkBoxList.add(false);
        }
        if(null == view){
            view = LayoutInflater.from(cxt).inflate(R.layout.locale_file_item, null);
        }
        View dirView = view.findViewById(R.id.dirRl);
        TextView dirName = (TextView) view.findViewById(R.id.dirName);

        View fileView = view.findViewById(R.id.fileLl);

        view.setTag(pos);
        TFile bxFile = data.get(pos);
        if(bxFile.isDir()){
            dirView.setVisibility(View.VISIBLE);
            dirName.setText(bxFile.getFileName());
            fileView.setVisibility(View.GONE);
        }else{
            dirView.setVisibility(View.GONE);
            fileView.setVisibility(View.VISIBLE);

            CheckBox fileCheckBox = (CheckBox) view.findViewById(R.id.list_item_checkbox);
            ImageView fileType = (ImageView) view.findViewById(R.id.fileType);
            TextView fileName = (TextView) view.findViewById(R.id.fileName);
            TextView fileSize = (TextView) view.findViewById(R.id.fileSize);
            TextView fileModifyDate = (TextView) view.findViewById(R.id.fileModifyDate);
            fileName.setText(bxFile.getFileName());
            fileSize.setText(bxFile.getFileSizeStr());
            fileModifyDate.setText(bxFile.getLastModifyTimeStr());
            if(bxFile.getMimeType().equals(MimeType.IMAGE)){
                fileType.setImageResource(R.drawable.bxfile_file_default_pic);
                if(null!=syncImageLoader && null!=imageLoadListener)
                    syncImageLoader.loadDiskImage(pos, bxFile.getFilePath(), imageLoadListener);
            }else{
                fileType.setImageResource(bfm.getMimeDrawable(bxFile.getMimeType()));
            }
            if(checkBoxList.get(pos)) { //checked
                fileCheckBox.setVisibility(View.VISIBLE);
                fileCheckBox.setChecked(true);
            }else {//unchecked
                fileCheckBox.setVisibility(View.GONE);
                fileCheckBox.setChecked(false);
            }//是否勾选chechBox
        }
        return view;
    }

}
