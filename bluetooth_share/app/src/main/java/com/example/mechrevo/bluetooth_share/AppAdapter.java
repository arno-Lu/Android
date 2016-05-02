package com.example.mechrevo.bluetooth_share;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by mechrevo on 2016/5/2.
 */
public class AppAdapter extends BaseAdapter{

    private Context context =null;
    private LayoutInflater inflater =null;
    private ArrayList<PackageInfo> infoList = null;

    @SuppressWarnings("static-access")
    public AppAdapter(Context context,ArrayList<PackageInfo> infoList){
        this.context =context;
        this.infoList = infoList;
        // 取得xml里定义的view
        this.inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount(){return this.infoList.size();}

    public Object getItem(int position){
        return this.infoList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        //防止内存泄漏 ？
        if(convertView==null){
            convertView = this.inflater.inflate(R.layout.app_item,null);
        }

        ImageView img = (ImageView)convertView.findViewById(R.id.img_app_icon);
        TextView txtView = (TextView)convertView.findViewById(R.id.txt_app_name);

        img.setImageDrawable(this.infoList.get(position).applicationInfo.loadIcon(context.getPackageManager()));
        txtView.setText(context.getResources().getString(R.string.app_item_name) //软件：
        + this.infoList.get(position).applicationInfo.loadLabel(context.getPackageManager()));

        return convertView;
    }
}
