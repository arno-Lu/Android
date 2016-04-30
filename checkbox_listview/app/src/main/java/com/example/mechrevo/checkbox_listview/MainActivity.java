package com.example.mechrevo.checkbox_listview;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    Button show;
    ListView lv;
    List<Person> persons = new ArrayList<Person>();
    Context mContext;
    MyListAdapter adapter;
    List<Integer> listItemID = new ArrayList<Integer>();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        show = (Button)findViewById(R.id.show);
        lv = (ListView)findViewById(R.id.lisView);

        initPersonData();
        adapter = new MyListAdapter(persons);
        lv.setAdapter(adapter);

        show.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                listItemID.clear();
                for(int i=0;i<adapter.mChecked.size();i++){
                    if(adapter.mChecked.get(i)){
                        listItemID.add(i);
                    }
                }

                if(listItemID.size()==0){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage("没有选中任何记录");
                    builder1.show();
                }else{
                    StringBuilder sb = new StringBuilder();

                    for(int i=0;i<listItemID.size();i++){
                        sb.append("ItemID="+listItemID.get(i)+" . ");
                    }
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                    builder2.setMessage(sb.toString());
                    builder2.show();
                }
            }
        });
    }

    /**
     * 模拟数据
     */
    private void initPersonData(){
        Person mPerson;
        for(int i=1;i<=12;i++){
            mPerson = new Person();
            mPerson.setName("Andy"+i);
            mPerson.setAddress("GuangZhou"+i);
            persons.add(mPerson);
        }
    }

    //自定义ListView适配器
    class MyListAdapter extends BaseAdapter{
        List<Boolean> mChecked;
        List<Person> listPerson;
        HashMap<Integer,View> map = new HashMap<Integer,View>();

        public MyListAdapter(List<Person> list){
            listPerson = new ArrayList<Person>();
            listPerson = list;

            mChecked = new ArrayList<Boolean>();
            for(int i=0;i<list.size();i++){
                mChecked.add(false);
            }
        }

        @Override
        public int getCount() {
            return listPerson.size();
        }

        @Override
        public Object getItem(int position) {
            return listPerson.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder = null;

            if (map.get(position) == null) {
                Log.e("MainActivity","position1 = "+position);

                LayoutInflater mInflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = mInflater.inflate(R.layout.item_layout, null);
                holder = new ViewHolder();
                holder.selected = (CheckBox)view.findViewById(R.id.list_select);
                holder.name = (TextView)view.findViewById(R.id.list_name);
                holder.address = (TextView)view.findViewById(R.id.list_address);
                final int p = position;
                map.put(position, view);
                holder.selected.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox)v;
                        mChecked.set(p, cb.isChecked());
                    }
                });
                view.setTag(holder);
            }else{
                Log.e("MainActivity","position2 = "+position);
                view = map.get(position);
                holder = (ViewHolder)view.getTag();
            }

            holder.selected.setChecked(mChecked.get(position));
            holder.name.setText(listPerson.get(position).getName());
            holder.address.setText(listPerson.get(position).getAddress());

            return view;
        }

    }

    static class ViewHolder{
        CheckBox selected;
        TextView name;
        TextView address;
    }
}