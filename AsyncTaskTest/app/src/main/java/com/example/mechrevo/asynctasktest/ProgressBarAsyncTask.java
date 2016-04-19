package com.example.mechrevo.asynctasktest;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by mechrevo on 2016/4/18.
 */
public class ProgressBarAsyncTask extends AsyncTask<Integer,Integer,String>{   //Integer为传入参数，Integer为进度，String为返回的result

    private TextView textView;
    private ProgressBar progressBar;

    public ProgressBarAsyncTask(TextView textView,ProgressBar progressBar){
        super();
        this.textView = textView;
        this.progressBar = progressBar;
    }

    @Override
    protected String doInBackground(Integer... params){    //子线程中运行，处理主要耗时操作
        NetOperator netOperator = new NetOperator();
        int i=0;
        for(i=1;i<=100;i++){
            netOperator.operator();
            publishProgress(i);
        }
        return i+params[0].intValue()+"";                //返回进度
    }

    @Override
    protected void onPostExecute(String result){
        textView.setText("异步操作执行"+result);
    }          //子线程操作结束调用

    @Override
    protected void onPreExecute(){
        textView.setText("开始执行异步线程");
    }         //开始前调用
    @Override
    protected void onProgressUpdate(Integer...values){         //在调用publishProgress之后调用，对Ui进行操作，对界面进行更新
        int value = values[0];
        progressBar.setProgress(value);
    }

}
