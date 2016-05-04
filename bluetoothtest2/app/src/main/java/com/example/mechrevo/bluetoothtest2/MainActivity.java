package com.example.mechrevo.bluetoothtest2;

import android.content.ComponentName;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.SimpleAdapter.ViewBinder;

public class MainActivity extends Activity implements OnClickListener {
    public static final String TAG = "AppListActivity";
    private ListView listView;
    private List<Map<String, Object>> list;

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "created");
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.app_list);
        listView = (ListView) this.findViewById(R.id.listView1);
        list = new ArrayList<Map<String, Object>>();
        List<PackageInfo> appListInfo = this.getPackageManager()
                .getInstalledPackages(0);
        for (PackageInfo p : appListInfo) {
            if (p.applicationInfo.sourceDir.startsWith("/system/app/")) {
                continue;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            Drawable icon = null;
            String appName = "";
            try {
                appName = this.getPackageManager()
                        .getApplicationLabel(p.applicationInfo).toString();
                icon = this.getPackageManager().getApplicationIcon(
                        p.applicationInfo.packageName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.put("name", appName);
            map.put("package", p.applicationInfo.packageName);
            map.put("sourceDir", p.applicationInfo.sourceDir);
            map.put("icon", icon);
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list,
                R.layout.app_list_item, new String[] { "name", "icon" },
                new int[] { R.id.tv_name, R.id.iv_icon });
        adapter.setViewBinder(new ViewBinder() {
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // 判断是否为我们要处理的对象
                if (view instanceof ImageView && data instanceof Drawable) {
                    ImageView iv = (ImageView) view;
                    iv.setImageDrawable((Drawable) data);
                    return true;
                } else
                    return false;
            }
        });
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                if (list.get(position).get("sourceDir") != null) {
                    File f = new File(list.get(position).get("sourceDir")
                            .toString());
                    // 调用android分享窗口
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("*/*");
                    intent.setClassName("com.android.bluetooth"
                            , "com.android.bluetooth.opp.BluetoothOppLauncherActivity");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
                    startActivity(intent);
                }
                return false;
            }

        });
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        Log.v(TAG, "destroy");
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }

}