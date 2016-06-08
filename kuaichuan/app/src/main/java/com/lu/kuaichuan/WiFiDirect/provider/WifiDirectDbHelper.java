package com.lu.kuaichuan.wifidirect.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lu.kuaichuan.wifidirect.utils.Constants;
import com.lu.kuaichuan.wifidirect.utils.LogUtils;

/**
 * Created by admin on 2016/3/14.
 */
public class WifiDirectDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "WifiDirectDbHelper";
    public static final int VERSION_1 = 1;

    private Context mContext;

    public WifiDirectDbHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, VERSION_1);
        mContext = context;
    }

    private static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Constants.TABLE_NAME + " (" +
                Constants.InstanceColumns.ID + " INTEGER PRIMARY KEY," +
                Constants.InstanceColumns.NAME + " TEXT NOT NULL, " +
                Constants.InstanceColumns.PATH + " TEXT NOT NULL, " +
                Constants.InstanceColumns.LENGTH + " INTEGER NOT NULL, " +
                Constants.InstanceColumns.STATE + " INTEGER NOT NULL, " +
                Constants.InstanceColumns.TRANSFER_LENGTH + " INTEGER NOT NULL, " +
                Constants.InstanceColumns.TRANSFER_DIRECTION + " INTEGER NOT NULL, " +
                Constants.InstanceColumns.TRANSFER_MAC + " TEXT NOT NULL);");
        LogUtils.i(TAG, "table created");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
