package com.ckt.io.wifidirect.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.ckt.io.wifidirect.utils.Constants;
import com.ckt.io.wifidirect.utils.LogUtils;

/**
 * Created by admin on 2016/3/14.
 */
public class WifiDirectContentProvider extends ContentProvider {
    private final static String TAG = "WifiDirectContentProvider";
    private WifiDirectDbHelper mDbHelper;

    private static final int TRANSFER_INSTANCE = 0;
    private static final int TRANSFER_INSTANCE_ID = 1;
    private static final UriMatcher sURLMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURLMatcher.addURI(Constants.AUTHORITY, Constants.TABLE_NAME, TRANSFER_INSTANCE);
        sURLMatcher.addURI(Constants.AUTHORITY, Constants.TABLE_NAME + "/#", TRANSFER_INSTANCE_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new WifiDirectDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sURLMatcher.match(uri)) {
            case TRANSFER_INSTANCE:
                qb.setTables(Constants.TABLE_NAME);
                break;
            case TRANSFER_INSTANCE_ID:
                qb.setTables(Constants.TABLE_NAME);
                qb.appendWhere(Constants.InstanceColumns.ID + "=");
                qb.appendWhere(uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor ret = qb.query(database, strings, s, strings1, null, null, s1);
        if (ret == null) {
            LogUtils.e(TAG, "query error, uri: " + uri);
        } else {
            ret.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return ret;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long rowId;
        switch (sURLMatcher.match(uri)) {
            case TRANSFER_INSTANCE:
                rowId = database.insert(Constants.TABLE_NAME, null, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Cannot insert from URL: " + uri);
        }
        Uri result = ContentUris.withAppendedId(Constants.InstanceColumns.CONTENT_URI, rowId);
        getContext().getContentResolver().notifyChange(result, null);
        return result;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int count = 0;
        String primaryKey;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        switch (sURLMatcher.match(uri)) {
            case TRANSFER_INSTANCE:
                count = db.delete(Constants.TABLE_NAME, s, strings);
                break;
            case TRANSFER_INSTANCE_ID:
                primaryKey = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)) {
                    s = Constants.InstanceColumns.ID + " = " + primaryKey;
                } else {
                    s = Constants.InstanceColumns.ID + " = " + primaryKey + " AND (" + s + ")";
                }
                count = db.delete(Constants.TABLE_NAME, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Cannot delete from URL: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int count = 0;
        String id;
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        switch (sURLMatcher.match(uri)) {
            case TRANSFER_INSTANCE_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)) {
                    s = Constants.InstanceColumns.ID + " = " + id;
                } else {
                    s = Constants.InstanceColumns.ID + " = " + id + " AND (" + s + ")";
                }
                count = database.update(Constants.TABLE_NAME, contentValues, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Cannot update URL: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}

