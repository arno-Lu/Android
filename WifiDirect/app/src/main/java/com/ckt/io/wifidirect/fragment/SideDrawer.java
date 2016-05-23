package com.ckt.io.wifidirect.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.ckt.io.wifidirect.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ckt on 2/28/16.
 */
public class SideDrawer extends Fragment {
    private ListView listView;
    private ArrayList<HashMap<String, Object>> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.side_drawer_layout, container, false);
        listView = (ListView) view.findViewById(R.id.id_list_view);
        list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("Image", R.drawable.mao);
            map.put("Text", "This is a Test " + i);
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.listview_item, new String[]{"Image", "Text"},
                new int[]{R.id.list_item_Image, R.id.list_item_Title});
        listView.setAdapter(adapter);
        return view;
    }
}
