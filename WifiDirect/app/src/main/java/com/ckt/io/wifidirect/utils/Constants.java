package com.ckt.io.wifidirect.utils;

import android.net.Uri;

/**
 * Created by admin on 2016/3/14.
 */
public class Constants {
    public static final String AUTHORITY = "com.ckt.io.wifidirect";
    public final static String DATABASE_NAME = "wifi_direct.db";
    public final static String TABLE_NAME = "wifi_direct";

    public static class InstanceColumns{
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/wifi_direct");
        public static final String ID = "_id";
        public static final String NAME = "_name";
        public static final String PATH = "_path";
        public static final String LENGTH = "_length";
        public static final String TRANSFER_LENGTH = "_tlength";
        public static final String STATE = "_state";
        public static final String TRANSFER_DIRECTION = "_direction";
        public static final String TRANSFER_MAC = "_mac";
    }

    // transfer direction
    public final static int DIRECTION_OUT = 1;
    public final static int DIRECTION_IN = 2;

    // transfer state
    public static class State {
        public final static int STATE_IDEL = 1; //ֹͣ
        public final static int STATE_TRANSFERING = 2;
        public final static int STATE_PENDING = 3; //�ȴ�
        public final static int STATE_TRANSFER_DONE = 4;
    }
}
