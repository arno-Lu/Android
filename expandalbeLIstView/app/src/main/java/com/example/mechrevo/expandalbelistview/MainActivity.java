package com.example.mechrevo.expandalbelistview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ExpandableListView mainlistview = null;
    List<String> parent = null;
    Map<String,List<String>> map = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mainlistview = (ExpandableListView)findViewById(R.id.main_expandListView);
        initData();
        mainlistview.setAdapter(new MyAdapter());
    }


    public void initData() {
        parent = new ArrayList<String>();
        parent.add("parent1");
        parent.add("parent2");
        parent.add("parent3");

        map = new HashMap<String, List<String>>();

        List<String> list1 = new ArrayList<String>();
        list1.add("child1-1");
        list1.add("child1-2");
        list1.add("child1-3");
        map.put("parent1", list1);

        List<String> list2 = new ArrayList<String>();
        list2.add("child2-1");
        list2.add("child2-2");
        list2.add("child2-3");
        map.put("parent2", list2);

        List<String> list3 = new ArrayList<String>();
        list3.add("child3-1");
        list3.add("child3-2");
        list3.add("child3-3");
        map.put("parent3", list3);
    }

    class MyAdapter extends BaseExpandableListAdapter{

        //得到子iem需要关联的数据
        @Override
        public Object getChild(int groupPosition,int childPosition){
            String key = parent.get(groupPosition);
            return (map.get(key).get(childPosition));
        }

        //得到子item的ID
        @Override
        public long getChildId(int groupPosition,int childPosition){
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition
        , boolean isLastChild , View convertView, ViewGroup parent){
            String key = MainActivity.this.parent.get(groupPosition);
            String info = map.get(key).get(childPosition);
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater)MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_children,null);
            }
                TextView tv =(TextView)convertView.findViewById(R.id.child_textView);
                tv.setText(info);
                return tv;

        }

        @Override
        public int getChildrenCount(int groupPosition){
            String key = parent.get(groupPosition);
            int size =map.get(key).size();
            return size;
        }

        @Override
        public Object getGroup(int groupPosition){
            return parent.get(groupPosition);
        }

        @Override
        public int getGroupCount(){
            return parent.size();
        }

        @Override
        public long getGroupId(int groupPosition){
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition,boolean isExpanded,View convertView,ViewGroup parent){
            if(convertView==null){
                LayoutInflater inflater = (LayoutInflater)MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_parent,null);
            }
                TextView tv =(TextView)convertView.findViewById(R.id.parent_textView);
                tv.setText(MainActivity.this.parent.get(groupPosition));
                return tv;

        }

        @Override
        public boolean hasStableIds(){
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition,int childPosition){
            return  true;
        }
    }

}
