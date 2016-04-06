package com.example.mechrevo.uisms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView msgListView;
    private EditText intputText;
    private Button send;
    private MsgAdapter adapter;
    private List<Msg> msgList = new ArrayList<Msg>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initMsgs();
        adapter = new MsgAdapter(MainActivity.this,R.layout.msg_item,msgList);
        intputText = (EditText) findViewById(R.id.intput_text);
        send = (Button)findViewById(R.id.send);
        msgListView = (ListView) findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = intputText.getText().toString();
                if(!"".equals(content)){  //不为空
                    Msg msg = new Msg(content,Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyDataSetChanged();  //适配器的内容改变时需要强制调用getView来刷新每个Item的内容,可以实现动态的刷新列表的功能
                    msgListView.setSelection(msgList.size());
                    intputText.setText("");   //清空内容
                }
            }
        });
;


    }

    private void initMsgs(){
        Msg msg1 = new Msg("123456",Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("987654",Msg.TYPE_SENT);
        msgList.add(msg2);
        Msg msg3 = new Msg("9631258",Msg.TYPE_RECEIVED);
        msgList.add(msg3);
    }
}
