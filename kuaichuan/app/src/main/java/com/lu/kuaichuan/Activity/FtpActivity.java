package com.lu.kuaichuan.Activity;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.ssl.SslConfigurationFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import lu.com.kuaichuan.R;

public class FtpActivity extends AppCompatActivity {

    private FtpServer mFtpServer;
    private String ftpConfigDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ftpConfig/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ftp);

        TextView tv = (TextView) findViewById(R.id.tvText);
        String info = "ftp://" + getLocalIpAddress() + ":2221\n";
        tv.setText(info);

        File f = new File(ftpConfigDir);
        if (!f.exists())
            f.mkdir();
        copyResourceFile(R.raw.users, ftpConfigDir + "users.properties");
        Config1();
    }



    public String getLocalIpAddress() {
        String strIP = null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        strIP = inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("msg", ex.toString());
        }
        return strIP;
    }

    private void copyResourceFile(int rid, String targetFile) {
        InputStream fin = ((Context) this).getResources().openRawResource(rid);
        FileOutputStream fos = null;
        int length;
        try {
            fos = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024];
            while ((length = fin.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void Config1() {
//		Now, let's configure the port on which the default listener waits for connections.

        FtpServerFactory serverFactory = new FtpServerFactory();

        ListenerFactory factory = new ListenerFactory();

        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();

        String[] str = {"mkdir", ftpConfigDir};
        try {
            Process ps = Runtime.getRuntime().exec(str);
            try {
                ps.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String filename = ftpConfigDir + "users.properties";//"/sdcard/users.properties";
        File files = new File(filename);

        userManagerFactory.setFile(files);
        serverFactory.setUserManager(userManagerFactory.createUserManager());
        // set the port of the listener
        factory.setPort(2221);

        // replace the default listener
        serverFactory.addListener("default", factory.createListener());

        // start the server
        FtpServer server = serverFactory.createServer();
        this.mFtpServer = server;
        try {
            server.start();
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }

    void Config2() {
//		Now, let's make it possible for a client to use FTPS (FTP over SSL) for the default listener.


        FtpServerFactory serverFactory = new FtpServerFactory();

        ListenerFactory factory = new ListenerFactory();

        // set the port of the listener
        factory.setPort(2221);

        // define SSL configuration
        SslConfigurationFactory ssl = new SslConfigurationFactory();
        ssl.setKeystoreFile(new File(ftpConfigDir + "ftpserver.jks"));
        ssl.setKeystorePassword("password");

        // set the SSL configuration for the listener
        factory.setSslConfiguration(ssl.createSslConfiguration());
        factory.setImplicitSsl(true);

        // replace the default listener
        serverFactory.addListener("default", factory.createListener());

        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        userManagerFactory.setFile(new File(ftpConfigDir + "users.properties"));

        serverFactory.setUserManager(userManagerFactory.createUserManager());

        // start the server
        FtpServer server = serverFactory.createServer();
        this.mFtpServer = server;
        try {
            server.start();
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mFtpServer) {
            mFtpServer.stop();
            mFtpServer = null;
        }
    }
}
