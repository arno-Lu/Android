package com.tracy.fileexplorer;

import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;


/***
 * 本地多媒体文件 浏览器
 * 图片 音频 视频
 * 
 * @author zhanglei
 *
 */
public class LocaleMediaFileBrowser extends BaseActivity implements OnItemClickListener {
	
//	private String tag = "LocaleMediaFileBrowser";
	private ListView lv;
	private List<TFile> data;
	private LocaleFileAdapter adapter;
	private TextView emptyView;
	private FileManager bfm;
	private TextView localefile_bottom_tv;
	private Button localefile_bottom_btn;
	private Button clear_bottom_btn;
	private View local_bottom;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(1 == msg.what){
				lv.setVisibility(View.VISIBLE);
				emptyView.setVisibility(View.GONE);
				adapter = new LocaleFileAdapter(data,LocaleMediaFileBrowser.this,null,null);
				lv.setAdapter(adapter);
			}else if(0 == msg.what){
				lv.setVisibility(View.GONE);
				emptyView.setVisibility(View.VISIBLE);
				emptyView.setText(getString(R.string.curCatagoryNoFiles));
			}
			super.handleMessage(msg);
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.localefile_browser);
		bfm = FileManager.getInstance();
		initViews();
		initData();
		onFileClick();
	}

	private void initData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		setTitle(intent.getStringExtra("title"));

		setData(intent.getData(),intent.getExtras().getString("select"));

	}
	
	private void setData(final Uri uri, final String select){
		FEApplication app = (FEApplication) getApplication();
		app.execRunnable(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				data= bfm.getMediaFiles(LocaleMediaFileBrowser.this,uri,select);
				if(null != data){
					Collections.sort(data);
					handler.sendEmptyMessage(1);
				}
				else
					handler.sendEmptyMessage(0);
			}

		});
	}


	private void initViews() {
		// TODO Auto-generated method stub
;
		lv = (ListView) findViewById(R.id.listView);
		lv.setOnItemClickListener(this);
		emptyView = (TextView) findViewById(R.id.emptyView);
		final List<TFile> choosedFiles = bfm.getChoosedFiles();

		clear_bottom_btn =(Button)findViewById(R.id.clear_bottom_btn);
		localefile_bottom_btn = (Button) findViewById(R.id.localefile_bottom_btn);
		localefile_bottom_tv = (TextView) findViewById(R.id.localefile_bottom_tv);
		local_bottom=findViewById(R.id.localefile_bottom);


	}



	//点击文件进行勾选操作
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
		CheckBox fileCheckBox = (CheckBox) view.findViewById(R.id.fileCheckBox);
		TFile bxfile = data.get(pos);
		final List<TFile> choosedFiles = bfm.getChoosedFiles()
	;
		if (choosedFiles.contains(bxfile)) {
			choosedFiles.remove(bxfile);
			fileCheckBox.setChecked(false);
		} else {

			choosedFiles.add(bxfile);
			fileCheckBox.setChecked(true);
		}
		onFileClick();
	}
	
	//点击文件，触发ui更新
	//onResume，触发ui更新
	private void onFileClick() {

		int cnt = bfm.getFilesCnt();
		if(cnt==0){
			local_bottom.setVisibility(View.GONE);
		}else {
			local_bottom.setVisibility(View.VISIBLE);

			localefile_bottom_tv.setText(bfm.getFilesSizes());
			localefile_bottom_btn.setText(String.format(getString(R.string.bxfile_choosedCnt), cnt));
			localefile_bottom_btn.setEnabled(cnt>0);
		}

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(null!=data){
			data.clear();
		}
		data = null;
		adapter = null;
		handler = null;
	}

}
