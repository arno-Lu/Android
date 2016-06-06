package com.lu.kuaichuan.wifidirect.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by admin on 2016/3/7.
 */
public class ToastUtils {
    private static Toast mToast;
    public static void toast(Context context, int msg_id) {
        if(mToast == null) {
            mToast = Toast.makeText(context, msg_id, Toast.LENGTH_SHORT);
        }else {
            mToast.setText(context.getResources().getString(msg_id));
        }
        mToast.show();
    }

    public static void toast(Context context, String msg) {
        if(mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        }else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
