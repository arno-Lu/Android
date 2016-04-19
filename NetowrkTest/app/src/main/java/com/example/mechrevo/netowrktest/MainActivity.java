package com.example.mechrevo.netowrktest;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int SHOW_RESPONSE = 0;
    private Button sendRequest;
    private TextView responseText;


    private Handler handler = new Handler(){

        public void handleMessage(Message msg){   //为什么不是@Override
            switch (msg.what){
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    responseText.setText(response);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendRequest = (Button)findViewById(R.id.send_request);
        responseText = (TextView)findViewById(R.id.response_text);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if(view.getId()==R.id.send_request){
            sendRequestWithHttpURLConnection();
        }
    }

    private void sendRequestWithHttpURLConnection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL("http://www.baidu.com/");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response2 = new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){  //通过line不断按行读取返回信息
                        response2.append(line);
                    }
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj = response2.toString();      //返回信息存入message
                    handler.sendMessage(message);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(connection!=null){
                        connection.disconnect();          //关闭连接
                    }
                }
            }
        }).start();
    }
}
