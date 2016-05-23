package com.ckt.io.wifidirect.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

public class MovieUtils {
    public static ArrayList<Movie> getAllMovies(Context context) {
        ArrayList<Movie> movieList;
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Video.Media._ID,
                        MediaStore.Video.Media.TITLE,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media.DISPLAY_NAME
                },
                MediaStore.Video.Media.MIME_TYPE + "=? or " + MediaStore.Video.Media.MIME_TYPE + "=?",
                new String[]{"video/mp4", "video/avi"}, null);
        movieList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            Movie movie;
            do {
                movie = new Movie();
                movie.setTitle(cursor.getString(1));
                if (cursor.getString(2) != null) {
                    float size = cursor.getInt(2) / 1024f / 1024f;
                    movie.setSize((size + "").substring(0, 4) + "M");
                } else {
                    movie.setSize("未知");
                }
                if (cursor.getString(3) != null) {
                    movie.setFileUrl(cursor.getString(3));
                }
                movie.setDuration(cursor.getInt(4));
                movie.setFileName(cursor.getString(5));
                movieList.add(movie);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return movieList;
    }
}
