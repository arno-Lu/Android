package com.ckt.io.wifidirect.Views;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ckt.io.wifidirect.R;
import com.ckt.io.wifidirect.utils.BitmapUtils;


/**
 * Created by admin on 2015/11/11.
 */
public class SpeedFloatWin {

    static boolean isFloatViewAdded = false;
    static boolean isFirstShow = true;
    static int x;
    static int y;
    static View view;

    public static void show(final Activity activity) {
        if (isFirstShow) { //read the stored position
            SharedPreferences mySharedPreferences = activity.getSharedPreferences(
                    "WifiDirect", Activity.MODE_PRIVATE);
            try {
                x = mySharedPreferences.getInt("x", 0);
                y = mySharedPreferences.getInt("y", 0);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                x = y = 0;
            }
            isFirstShow = false;
        }

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//        final WindowManager wm = (WindowManager) context.getApplicationContext()
//                .getSystemService(Context.WINDOW_SERVICE);
        //��ȡ����Activity��WindowManager
        final WindowManager wm = activity.getWindowManager();
        // set window type
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            /*
             * if params.type = WindowManager.LayoutParams.TYPE_PHONE; ��ô���ȼ��ή��һЩ,
             * ������֪ͨ�����ɼ�
             */
        params.format = PixelFormat.RGBA_8888; // ����ͼƬ��ʽ��Ч��Ϊ����͸��
        // ����Window flag
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            /*
             * �����flags���Ե�Ч����ͬ������ ����ɴ������������κ��¼�,ͬʱ��Ӱ�������¼���Ӧ��
             * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
             * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
             */
        // ������ĳ��ÿ�
        params.width = (int) BitmapUtils.dipTopx(activity, 80);
        params.height = (int) BitmapUtils.dipTopx(activity, 60);
        params.x = x;
        params.y = y;
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.float_win_layout, null);
        // �������Touch����
        v.setOnTouchListener(new View.OnTouchListener() {
            int lastX, lastY;
            int paramX, paramY;

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        paramX = params.x;
                        paramY = params.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        params.x = paramX + dx;
                        params.y = paramY + dy;
                        // ������λ��
                        wm.updateViewLayout(v, params);
                        x = params.x;
                        y = params.y;
                        break;
                    case MotionEvent.ACTION_UP:

                        break;
                }
                return false;
            }
        });

        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });

        //��ֹ��Ӷ�����
        if (!isFloatViewAdded) {
            wm.addView(v, params);
            view = v;
            isFloatViewAdded = true;
        }
    }

    public static void hide(Context context) {
        final WindowManager wm = (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        if (isFloatViewAdded && view != null) {
            wm.removeViewImmediate(view);
        }
        isFloatViewAdded = false;

        //������ڵ�λ��
        SharedPreferences mySharedPreferences = context.getSharedPreferences(
                "WifiDirect", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt("x", x);
        editor.putInt("y", y);
        editor.commit();
    }

    public static void updateSpeed(String sendSpeed, String receviceSpeed) {
        if (view == null) return;
        TextView txt_sendSpeed = (TextView) view.findViewById(R.id.txt_speed_send);
        txt_sendSpeed.setText(sendSpeed);
        TextView txt_receiveSpeed = (TextView) view.findViewById(R.id.txt_speed_recevice);
        txt_receiveSpeed.setText(receviceSpeed);
        //wm.updateViewLayout(v, params);
        view.invalidate();
    }
}
