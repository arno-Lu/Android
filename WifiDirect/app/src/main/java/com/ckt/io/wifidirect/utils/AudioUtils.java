package com.ckt.io.wifidirect.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AudioUtils {

    //读取音乐图片时的uri
    private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    public static ArrayList<Song> songList;
    public static Context context;
    public static ArrayList<Song> getAllSongs(Context context) {
        ArrayList<Song> songs;
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ALBUM_ID},
                MediaStore.Audio.Media.MIME_TYPE + "=? or " + MediaStore.Audio.Media.MIME_TYPE + "=?",
                new String[]{"audio/mpeg", "audio/x-ms-wma"}, null
        );
        songs = new ArrayList<>();
        if (cursor.moveToFirst()) {
            Song song;
            do {
                song = new Song();
                song.setId(cursor.getInt(0));
                song.setFileName(cursor.getString(1));
                song.setTitle(cursor.getString(2));
                song.setDuration(cursor.getInt(3));
                song.setSinger(cursor.getString(4));
                song.setAlbum(cursor.getString(5));
                if (cursor.getString(6) != null) {
                    float size = cursor.getInt(6) / 1024f / 1024f;
                    song.setSize((size + "").substring(0, 3) + "M");
                } else {
                    song.setSize("未知");
                }
                if (cursor.getString(7) != null) {
                    song.setFileUrl(cursor.getString(7));
                }
                song.setAlbum_id(cursor.getInt(8));
                songs.add(song);
            } while (cursor.moveToNext());
            cursor.close();
        }
        songList = songs;
        AudioUtils.context = context;
        return songs;
    }

    public static String getMusicName(String path) {
        if(path == null || songList == null) {
            return "";
        }
        Song s = null;
        for(int i=0; i<songList.size(); i++) {
            s = songList.get(i);
            if(path.equals(s.getFileUrl())) {
                break;
            }else {
                s = null;
            }
        }
        if(s != null) {
            return s.getTitle();
        }
        return "";
    }

    //获取指定音乐专辑-->音乐ID的音乐图片
    public static Bitmap getMusicBitpMap(String path) {
        Bitmap bm = null;
        if(songList==null || path==null) {
            return null;
        }
        Song s = null;
        for(int i=0; i<songList.size(); i++) {
            s = songList.get(i);
            if(path.equals(s.getFileUrl())) {
                break;
            }else {
                s = null;
            }
        }
        if(s == null) return null;
        int albumid = s.getAlbum_id();
        int songid = s.getId();
        try {
            if (albumid < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/" + songid + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver()
                        .openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            } else {

                Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
                ParcelFileDescriptor pfd = context.getContentResolver()
                        .openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            }
        } catch (FileNotFoundException ex) {
        }
        return bm;
    }

    public static void addNewFileToDB(Context context, String path) {
        MediaScannerConnection.scanFile(context,
                new String[]{path},
                null,
                null);
    }
}
