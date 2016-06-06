package com.lu.kuaichuan.wifidirect.File;

import android.app.Activity;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Lu on 2016/5/24.
 */
public class Utils {
    /**
     * @param cxt
     * @return 屏幕宽
     */
    public static int getScreenWidth(Activity cxt) {
        WindowManager m = cxt.getWindowManager();
        Display d = m.getDefaultDisplay();
        return d.getWidth();
    }
}
