package com.example.mechrevo.uisms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mechrevo on 2016/4/6.
 */
public class MsgAdapter extends ArrayAdapter<Msg> {

    private int resourceId;

    public MsgAdapter(Context context, int textViewResourceId, List<Msg> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        Msg msg = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);    //通过id得到当前将会加载的view
            viewHolder = new ViewHolder();
            viewHolder.leftLayout = (LinearLayout)view.findViewById(R.id.left_layout);   //实例化
            viewHolder.rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            viewHolder.leftMsg = (TextView) view.findViewById(R.id.left_msg);
            viewHolder.rightMsg= (TextView) view.findViewById( R.id. right_msg);
            view.setTag(viewHolder);

        }else {
            view=convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        if(msg.getType()==Msg.TYPE_RECEIVED){    //相同，则为收到信息，将左边显示，右边隐藏
            viewHolder.leftLayout.setVisibility(view.VISIBLE);
            viewHolder.rightLayout.setVisibility(view.GONE);
            viewHolder.leftMsg .setText(msg.getContent());
        }else {
            viewHolder.rightLayout.setVisibility(view.VISIBLE);
            viewHolder.leftLayout.setVisibility(view.GONE);
            viewHolder.rightMsg .setText(msg.getContent());
        }
        return  view;
   }

    class ViewHolder{

        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
    }
}
