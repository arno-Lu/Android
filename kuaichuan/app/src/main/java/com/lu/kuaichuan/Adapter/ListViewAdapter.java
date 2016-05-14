package com.lu.kuaichuan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.lu.kuaichuan.File.FileManager;
import com.lu.kuaichuan.File.TFile;

import java.util.List;

import lu.com.kuaichuan.R;

/**
 * Created by Lu on 2016/5/14.
 */
public class ListViewAdapter extends BaseAdapter {
    Context context;
    private List<TFile> list;

    public ListViewAdapter(Context context) {
        this.context = context;
    }

    public List<TFile> getList() {
        return list;
    }

    public void setList(List<TFile> list) {
        this.list = list;
    }

    private FileManager bfm;
    private List<TFile> choosedFiles;

    class viewHolder {
        CheckBox checkBox;
        TextView nameTextView;
        TextView sizeTextView;
        TextView timeTextView;
        ImageView iconImageView;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        viewHolder holder = null;
        bfm = FileManager.getInstance();
        List<TFile> choosedFiles = bfm.getChoosedFiles();
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.listview_item, null);
            View fileView = convertView.findViewById(R.id.fileLl);
            fileView.setVisibility(View.VISIBLE);
            holder = new viewHolder();
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.fileCheckBox);
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
            holder.sizeTextView = (TextView) convertView.findViewById(R.id.sizeTextView);
            holder.timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }


        TFile appInfo=list.get(position);
        holder.checkBox.setChecked(choosedFiles.contains(appInfo));
        holder.nameTextView.setText(appInfo.getFileName());
        holder.sizeTextView.setText(appInfo.getFileSizeStr());
        holder.timeTextView.setText(appInfo.getLastModifyTimeStr());
        holder.iconImageView.setImageDrawable(appInfo.getImage());
        return convertView;
    }


}