package com.ckt.io.wifidirect.Activity;

/**
 * Created by Lu on 2016/5/22.
 */

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ckt.io.wifidirect.R;

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
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class FtpActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private FtpServer mFtpServer;
    private Button WiFiButton;
    private Button ApButton;
    private TextView ap_name;
    private TextView wifi_title;
    private TextView tv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean flag=false;
    private String ftpConfigDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ftpConfig/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ftp);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle(R.string.pc_connect);
        toolbar.setTitleTextColor(getResources().getColor(R.color.titleColor));

        WiFiButton = (Button) findViewById(R.id.wifi_connect_id);
        ApButton = (Button)findViewById(R.id.ap_connect_id);
        ap_name = (TextView)findViewById(R.id.ap_name);
        wifi_title = (TextView)findViewById(R.id.wifi_title);
        tv = (TextView) findViewById(R.id.tvText);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.id_swip_ftp);


        ApButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=!flag;
                setWifiApEnabled(flag);
                if(flag == false){
                    ap_name.setVisibility(View.GONE);
                    wifi_title.setText(" 确保手机与电脑处于同一WiFi下：");
                    String info = "ftp://" + getLocalIpAddress() + ":2221\n";
                    tv.setText(info);
                }
            }
        });

        WiFiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);

        String info = "ftp://" + getLocalIpAddress() + ":2221\n";
        tv.setText(info);

        File f = new File(ftpConfigDir);
        if (!f.exists())
            f.mkdir();
        copyResourceFile(R.raw.users, ftpConfigDir + "users.properties");
        Config1();
    }

    public void onRefresh(){

        String info = "ftp://" + getLocalIpAddress() + ":2221\n";
        tv.setText(info);

        swipeRefreshLayout.setRefreshing(false);
    }

    public boolean setWifiApEnabled(boolean enabled) {
        WifiManager wifiManager = null;
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (enabled) { // disable WiFi in any case
            //wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
            wifiManager.setWifiEnabled(false);
        }
        try {
            //热点的配置类
            WifiConfiguration apConfig = new WifiConfiguration();
            //配置热点的名称(可以在名字后面加点随机数什么的)
            apConfig.SSID = "用户名";
            //配置热点的密码
            //通过反射调用设置热点
            Method method = wifiManager.getClass().getMethod(
                    "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);

            ap_name.setText("用户名");
            ap_name.setVisibility(View.VISIBLE);
            wifi_title.setText(" 连接电脑至本机热点：");

            //返回热点打开状态
            return (Boolean) method.invoke(wifiManager, apConfig, enabled);
        } catch (Exception e) {
            return false;
        }
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
