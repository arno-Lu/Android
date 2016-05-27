package com.lu.kuaichuan.WiFiDirect;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import com.lu.kuaichuan.Activity.WiFiDirectActivity;
import com.lu.kuaichuan.File.TFile;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created by Lu on 2016/5/17.
 */
//文件信息和文件合并后在写入socket输出流,先确定文件类型，再写入相应文件（需要指定大小？）
    //http://www.cnblogs.com/x-man/archive/2012/05/09/2491516.html
public class FileTransferService extends IntentService {

    private static final int SOCKET_TIMEOUT = 5000;
    public static final String ACTION_SEND_FILE = "com.example.android.wifidirect.SEND_FILE";
    public static final String EXTRAS_FILE_PATH = "file_url";
    public static final String EXTRAS_GROUP_OWNER_ADDRESS = "go_host";
    public static final String EXTRAS_GROUP_OWNER_PORT = "go_port";
    public static final String FILE_NAME = "file_name";
    public FileTransferService(String name) {
        super(name);
    }

    public FileTransferService() {
        super("FileTransferService");
    }

    Socket  socket;
    FileInputStream FS ;
    ByteArrayInputStream INFO;

    /*
     * (non-Javadoc)
     * @see android.app.IntentService#onHandleIntent(android.content.Intent)
     */
    @Override
    protected void onHandleIntent(Intent intent) {


        if (intent.getAction().equals(ACTION_SEND_FILE)) {
            String fileUri = intent.getExtras().getString(EXTRAS_FILE_PATH);
            String host = intent.getExtras().getString(EXTRAS_GROUP_OWNER_ADDRESS);


            String name = intent.getExtras().getString(FILE_NAME);
            byte[] name_byte = name.getBytes();
            byte[] info = Arrays.copyOf(name_byte, 256);
            try {

                 INFO = new ByteArrayInputStream(info); //信息流

                File file = new File(fileUri);

                FS = new FileInputStream(file);  //文件名

                SequenceInputStream all = new SequenceInputStream(INFO, FS); //合并流


                socket = new Socket();
                int port = intent.getExtras().getInt(EXTRAS_GROUP_OWNER_PORT);

                Log.d(WiFiDirectActivity.TAG, "Opening client socket - ");
                socket.bind(null);
                socket.connect((new InetSocketAddress(host, port)), SOCKET_TIMEOUT);

                Log.d(WiFiDirectActivity.TAG, "Client socket - " + socket.isConnected());
                BufferedOutputStream stream = new BufferedOutputStream(socket.getOutputStream());


                byte[] buf = new byte[1024];
                int len = 0;
                while((len = all.read(buf))!=-1){
                    stream.write(buf,0,len);
                }

                stream.close();
                all.close();
                FS.close();
                INFO.close();
                Log.d(WiFiDirectActivity.TAG, "Client: Data written");
            } catch (IOException e) {
                Log.e(WiFiDirectActivity.TAG, e.getMessage());
            } finally {
                if (socket != null) {
                    if (socket.isConnected()) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            // Give up
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}