package com.lu.kuaichuan.wifidirect.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.lu.kuaichuan.wifidirect.R;
import com.lu.kuaichuan.wifidirect.p2p.WifiP2pHelper;
import com.lu.kuaichuan.wifidirect.utils.ApkUtils;
import com.lu.kuaichuan.wifidirect.utils.FileTypeUtils;
import com.lu.kuaichuan.wifidirect.utils.LogUtils;

/**
 * Created by Administrator on 2016/4/6.
 */
public abstract class MyBaseFragment extends Fragment {

    /**获取position位置对应的数据的路径--->需要子类来实现
     * @param position
     * @return
     */
    protected abstract String getPositonPath(int position);

    /**特殊处理apk， 如果已经安装了， 就打开， 否则安装
     * @param path
     * @return
     */
    protected boolean isApkInstalled(String path){
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo menuInfo1 = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int pos = menuInfo1.position;
        String path = getPositonPath(pos);
        menu.clear();
        int order = 0;
        menu.add(0, R.string.menu_open, order++, R.string.menu_open);
        if(FileTypeUtils.isApk(path) && isApkInstalled(path)) { // 已安装APK ---> 删除改为卸载
            menu.add(0, R.string.menu_uninstall, order ++, R.string.menu_uninstall);
        }else {
            menu.add(0, R.string.menu_delete, order ++, R.string.menu_delete);
        }
        menu.add(0, R.string.menu_property, order ++, R.string.menu_property);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(!getUserVisibleHint()) {//解决一个问题：在viewpager中，当有几个fragment都注册了上下文菜单时，单击一个，其实会有多个fragment响应，从而导致一些问题。解决方案：只处理当前显示的fragment
            return false;//返回false，viewpager会继续往下面的fragment发送这个菜单点击的消息
        }
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = menuInfo.position;
        String path = getPositonPath(pos);
        switch (item.getItemId()) {
            //这个是针对已安装apk的
            case R.string.menu_uninstall://uninstall apk
                LogUtils.i(WifiP2pHelper.TAG, "context menu click--->menu_uninstall");
                String packageName = ApkUtils.getApkPackageName(getContext(), path);
                Uri packageUri = Uri.parse("package:" + packageName);
                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
                startActivity(uninstallIntent);
                break;
            /////////////////////////////////////////////////////////////////////////////////////
            case R.string.menu_open:
                if(FileTypeUtils.isApk(path) && isApkInstalled(path)) { //已安装apk--->just open the app instead of install it
                    String packageName1 = ApkUtils.getApkPackageName(getContext(), path);
                    Intent it = getActivity().getPackageManager().getLaunchIntentForPackage(packageName1);
                    startActivity(it);
                }else {
                    FileTypeUtils.openFile(getContext(), path);
                }
                break;

            case R.string.menu_delete:

                break;
            case R.string.menu_property:

                break;
        }
        return super.onContextItemSelected(item);
    }
}
