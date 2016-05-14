package com.lu.kuaichuan.util;

import android.app.Activity;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by mechrevo on 2016/5/14.
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
