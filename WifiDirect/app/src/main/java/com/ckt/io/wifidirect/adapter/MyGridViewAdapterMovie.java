package com.ckt.io.wifidirect.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ckt.io.wifidirect.R;

import java.util.ArrayList;

/**
 * Created by admin on 2016/3/8.
 */
public class MyGridViewAdapterMovie extends BaseAdapter {
    private ArrayList<String> mNameList = new ArrayList<>();
    private ArrayList<Drawable> mIconList = new ArrayList<>();
    private ArrayList<Boolean> mCheckBoxList = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;

    public MyGridViewAdapterMovie(Context context, ArrayList<String> nameList, ArrayList<Drawable> iconList, ArrayList<Boolean> checkBoxList) {
        mNameList = nameList;
        mIconList = iconList;
        mCheckBoxList = checkBoxList;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public ArrayList<Boolean> getmCheckBoxList() {
        return this.mCheckBoxList;
    }

    @Override
    public int getCount() {
        return mNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return mNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewTag viewTag;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.movie_grid_view_item, null);
            viewTag = new ItemViewTag((ImageView) convertView.findViewById(R.id.id_grid_view_icon_movie),
                    (TextView) convertView.findViewById(R.id.id_grid_view_name_movie), (CheckBox) convertView.findViewById(R.id.id_grid_view_checkbox_movie));
            convertView.setTag(viewTag);
        } else {
            viewTag = (ItemViewTag) convertView.getTag();
        }
        viewTag.mName.setText(mNameList.get(position));
        viewTag.mIcon.setImageDrawable(mIconList.get(position));
        viewTag.mCheckBox.setChecked(mCheckBoxList.get(position));
        viewTag.mCheckBox.setVisibility(mCheckBoxList.get(position) ? View.VISIBLE : View.GONE);
        return convertView;
    }

    public class ItemViewTag {
        public ImageView mIcon;
        public TextView mName;
        public CheckBox mCheckBox;

        public ItemViewTag(ImageView icon, TextView name, CheckBox checkBox) {
            mName = name;
            mIcon = icon;
            mCheckBox = checkBox;
        }
    }
}
