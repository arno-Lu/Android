package com.lu.kuaichuan.wifidirect.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.lu.kuaichuan.wifidirect.R;
import com.lu.kuaichuan.wifidirect.utils.BitmapUtils;
import com.lu.kuaichuan.wifidirect.utils.FileResLoaderUtils;
import com.lu.kuaichuan.wifidirect.utils.FileTypeUtils;

import java.util.ArrayList;

/**
 * Created by admin on 2016/3/8.
 */
public class MyGridViewAdapter extends BaseAdapter {
    private ArrayList<String> mNameList = new ArrayList<>();
    private ArrayList<String> mPathList = new ArrayList<>();
    private ArrayList<Boolean> mCheckBoxList = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    private int imagetViewHeight;

    public MyGridViewAdapter(Context context, ArrayList<String> nameList, ArrayList<String> pathList, ArrayList<Boolean> checkBoxList, int imageViewHeigth) {
        mNameList = nameList;
        mPathList = pathList;
        mCheckBoxList = checkBoxList;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.imagetViewHeight = (int) BitmapUtils.dipTopx(context, imageViewHeigth);
//        this.imagetViewHeight = 100;
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
            convertView = mInflater.inflate(R.layout.grid_view_item, null);
            viewTag = new ItemViewTag((ImageView) convertView.findViewById(R.id.id_grid_view_icon_movie),
                    (TextView) convertView.findViewById(R.id.id_grid_view_name_movie), (CheckBox) convertView.findViewById(R.id.id_grid_view_checkbox_movie));
            convertView.setTag(viewTag);
            ViewGroup.LayoutParams lp = viewTag.mIcon.getLayoutParams();
            lp.width = lp.height = this.imagetViewHeight;
            viewTag.mIcon.setLayoutParams(lp);
        } else {
            viewTag = (ItemViewTag) convertView.getTag();
        }
        viewTag.mName.setText(mNameList.get(position));
        String path = mPathList.get(position);
        Object pic = FileResLoaderUtils.getPic(path);
        if(pic instanceof Drawable) {
            viewTag.mIcon.setImageDrawable((Drawable) pic);
        }else if(pic instanceof Integer) {
            viewTag.mIcon.setImageResource((Integer) pic);
        }else if(pic instanceof Bitmap) {
            viewTag.mIcon.setImageBitmap((Bitmap) pic);
        }else {
            viewTag.mIcon.setImageResource(FileTypeUtils.getDefaultFileIcon(path));
        }
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
