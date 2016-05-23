package com.ckt.io.wifidirect.Views;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ckt.io.wifidirect.Activity.MainActivity;
import com.ckt.io.wifidirect.R;

import java.util.ArrayList;

public class SendFileListPopWin extends PopupWindow implements AdapterView.OnItemClickListener, View.OnClickListener {
    private MainActivity activity;
    ArrayList<String> list;
    private String title;
    private boolean isAdd;
    private ImageView img_ok;
    private ImageView img_cancel;
    private TextView txt_title;
    private ListView listView;
    private MyListViewAdapter adapter;
    private ArrayList<Boolean> isItemSeleced;

    private Handler handler = new Handler();
    private Runnable runnable_doubleClick;
    private boolean isAllowDoubleClick;
    IOnOkListener onOkListener;

    public SendFileListPopWin(boolean isAdd, String title, MainActivity activity, ArrayList<String> list, IOnOkListener onOkListener) {
        this.activity = activity;
        this.list = list;
        this.isAdd = isAdd;
        this.isItemSeleced = new ArrayList<Boolean>();
        this.onOkListener = onOkListener;
        for(int i=0; i<list.size(); i++) {
            this.isItemSeleced.add(false);
        }
        this.title = title;

        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        this.setWidth(lp.width);

        int h = activity.getWindowManager().getDefaultDisplay().getHeight()*2/3;
        this.setHeight(h);
        View view = activity.getLayoutInflater().inflate(R.layout.sendfilelist_popwin_layout, null);
        setContentView(view);

        this.setFocusable(true);
        this.setAnimationStyle(R.style.sendfilelist_popwin_animal_style);

        this.listView = (ListView) view.findViewById(R.id.listView);
        this.txt_title = (TextView) view.findViewById(R.id.txt_title);
        this.img_ok = (ImageView) view.findViewById(R.id.img_ok);
        this.img_cancel = (ImageView) view.findViewById(R.id.img_cancel);
        this.img_ok.setOnClickListener(this);
        this.img_cancel.setOnClickListener(this);
        this.txt_title.setOnClickListener(this);


        adapter = new MyListViewAdapter();
        this.listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(this);
        this.txt_title.setText(title);

        isAllowDoubleClick = false;
        runnable_doubleClick = new Runnable() {
            @Override
            public void run() {
                isAllowDoubleClick = false;
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        boolean isChecked = isItemSeleced.get(position);
        isItemSeleced.set(position, !isChecked);
        adapter.notifyDataSetChanged();
        int count = 0;
        for(int i=0; i<isItemSeleced.size(); i++) {
            if(isItemSeleced.get(i))    count++;
        }
        txt_title.setText(title+"--->"+count);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_ok:
                if(onOkListener != null) {
                    ArrayList<String> ret = new ArrayList<String>();
                    for(int i=0; i<list.size(); i++) {
                        if(isItemSeleced.get(i)) {
                            ret.add(list.get(i));
                        }
                    }
                    onOkListener.onOk(ret, isAdd);
                }
                this.dismiss();
                break;
            case R.id.img_cancel:
                this.dismiss();
                break;
            case R.id.txt_title:
                if(isAllowDoubleClick) { //double click
                    int count = 0;
                    for(int i=0; i<isItemSeleced.size(); i++) {
                        if(isItemSeleced.get(i)) {
                            count ++;
                        }
                        isItemSeleced.set(i, true);
                    }
                    txt_title.setText(title+"--->"+isItemSeleced.size());
                    if(count == isItemSeleced.size()) { //all choosed ---> all not choosed
                        for(int i=0; i<isItemSeleced.size(); i++) {
                            isItemSeleced.set(i, false);
                        }
                        txt_title.setText(title+"--->");
                    }

                    adapter.notifyDataSetChanged();
                    isAllowDoubleClick = false;
                }else {//first click and wait 300ms for next click to finshed a doubleclick
                    isAllowDoubleClick = true;
                    handler.postDelayed(runnable_doubleClick, 300);
                }
                break;
        }
    }


    class MyListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {
            TextView txt_info;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null) {
                convertView = activity.getLayoutInflater().inflate(R.layout.sendfilelist_popwin_listitem, null);
                holder = new ViewHolder();
                holder.txt_info = (TextView) convertView.findViewById(R.id.txt_info);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            Object obj = list.get(position);
            String info= (String) list.get(position);
            holder.txt_info.setText(info);
            if(isItemSeleced.get(position)) {
                convertView.setBackgroundResource(R.drawable.sendfilelist_popwin_item_selected_bg);
            }else {
                convertView.setBackgroundDrawable(null);
            }
            return convertView;
        }
    }

    public interface IOnOkListener {
        public void onOk(ArrayList<String> seleckedList, boolean isAdd);
    }
}
