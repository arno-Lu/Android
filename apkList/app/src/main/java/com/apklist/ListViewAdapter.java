package com.apklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mechrevo on 2016/5/7.
 */
public class ListViewAdapter extends BaseAdapter {
    Context context;
    private List<AppInfo> list;

    public ListViewAdapter(Context context) {
        this.context = context;
    }

    public List<AppInfo> getList() {
        return list;
    }

    public void setList(List<AppInfo> list) {
        this.list = list;
    }

    class viewHolder {
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
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.listview_item, null);
            holder = new viewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
            holder.sizeTextView = (TextView) convertView.findViewById(R.id.sizeTextView);
            holder.timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }


        AppInfo appInfo=list.get(position);
        holder.nameTextView.setText(appInfo.getAppName());
        holder.sizeTextView.setText(appInfo.getAppSize());
        holder.timeTextView.setText(appInfo.getAppTime());
        holder.iconImageView.setImageDrawable(appInfo.getAppIcon());
        return convertView;
    }


}
