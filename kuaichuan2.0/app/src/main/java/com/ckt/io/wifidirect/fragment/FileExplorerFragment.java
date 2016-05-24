package com.ckt.io.wifidirect.fragment;

import java.io.File;
import java.util.ArrayList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ckt.io.wifidirect.activity.MainActivity;
import com.ckt.io.wifidirect.activity.MainActivity.OnSendFileListChangeListener;
import com.ckt.io.wifidirect.R;
import com.ckt.io.wifidirect.adapter.MyListViewAdapter;
import com.ckt.io.wifidirect.p2p.WifiP2pHelper;
import com.ckt.io.wifidirect.utils.FileResLoaderUtils;
import com.ckt.io.wifidirect.utils.FileTypeUtils;
import com.ckt.io.wifidirect.utils.LogUtils;
import com.ckt.io.wifidirect.utils.SdcardUtils;


@SuppressLint("ValidFragment")
public class FileExplorerFragment extends MyBaseFragment implements
		OnItemClickListener, OnSendFileListChangeListener,
		FileResLoaderUtils.OnLoadFinishedListener{

	private ArrayList<State> stateList = new ArrayList<State>();
	private State nowState;

	private ViewGroup lin_no_file;
	private ListView listView;
	private TextView txt_dir_Path; //显示当前的文件夹路径的
	private File mDir;

	private File externalSDFile;
	private File innerSdFile;

	private FileResLoaderUtils drawableLoaderUtils;

	private Handler handler = new Handler();

	private ArrayList<String> sort(ArrayList<String> list) {
		ArrayList<String> ret = new ArrayList<String>();
		while (list.size() > 0) {
			String s = list.get(0);
			for (int j = 1; j < list.size(); j++) {
				String tmp = list.get(j);
				if (s.compareTo(tmp) > 0) {
					s = tmp;
				}
			}
			list.remove(s);
			ret.add(s);
		}
		return ret;
	}

	private void updateView(State state) {
		mDir = state.dir;
		MyListViewAdapter adapter = (MyListViewAdapter) listView.getAdapter();
		if(state.isAutoUpdateChildFiles) {//更新mdir下的childs(如果不更新,需要自己手动设置filelist 和 checklist)--->目前只有家目录是自定义的childs,不会在这里自动更新childs
			state.list = new ArrayList<String >();
			if (mDir.canRead()) {
				ArrayList<String> tempFolderList = new ArrayList<String>(); // 暂时保存文件夹
				ArrayList<String> tempFileList = new ArrayList<String>(); // 用来暂时保存文件
				File fs[] = mDir.listFiles();
				for (File temp : fs) {
					if (temp.getName().startsWith(".")) {
						continue;
					}
					if (temp.isFile()) {
						tempFileList.add(temp.getPath());
					} else {
						tempFolderList.add(temp.getPath());
					}
				}
				state.list.addAll(sort(tempFolderList));
				state.list.addAll(sort(tempFileList));
			}
			state.checkList = new ArrayList<>();
			for(int i=0; i<state.list.size(); i++) {
				state.checkList.add(false);
			}
		}
		//根据文件发送列表来更新checklist
		MainActivity activity = (MainActivity) getActivity();
		updateCheckList(activity.getSendFiles(), state.list, state.checkList);
		adapter.setData(state.list, state.titleList, state.checkList);//reset the data and notifidatasetchanged
		listView.setSelectionFromTop(state.pos, state.top);
//		listView.setSelectionFromTop(60, -74);
		LogUtils.i(WifiP2pHelper.TAG, "updateViews pos="+state.pos+" top="+state.top);
		if (state.list.size() != 0) {
			lin_no_file.setVisibility(View.GONE);
		} else {
			lin_no_file.setVisibility(View.VISIBLE);
		}
		//更新顶部显示的当前文件夹路径
		ArrayList<String> dirs = new ArrayList<>();
		File f = state.dir;
		Log.d(WifiP2pHelper.TAG, "f="+f);
		while (f!=null) {
			if(externalSDFile!=null) {
				if(f.getPath().equals(externalSDFile.getPath())) {
					dirs.add(getResources().getString(R.string.external_sdcard));
					break;
				}
			}
			if(innerSdFile != null) {
				if(f.getPath().equals(innerSdFile.getPath())) {
					dirs.add(getResources().getString(R.string.inner_sdcard));
					break;
				}
			}
			File root = new File("/");
			if(f.getPath().equals(root.getPath())) {
				dirs.add(getResources().getString(R.string.root_dir));
				break;
			}
			else {
				dirs.add(f.getName());
				f = f.getParentFile();
			}
		}
		StringBuffer buf = new StringBuffer();
		for(int i=dirs.size()-1; i>=0; i--) {
			if(i==0) {
				buf.append(dirs.get(i));
			}else {
				buf.append(dirs.get(i) + " > ");
			}
		}
		txt_dir_Path.setText(buf.toString());
		//加载listview Item图片-->只有像apk, 图片等文件会加载
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				loadListViewItemDrawalbe();
			}
		}, 500);//延迟500ms后开始加载可见的listItem的图片(延迟500ms,是为了等待listview重新加载完成, 不然获取到第一个可视位置不对)
		nowState = state;
		LogUtils.i(WifiP2pHelper.TAG, "FileExplorerFragment updateView() --> state="+nowState.toString());
	}

	public boolean back() {
		Log.d(WifiP2pHelper.TAG, "FileExplorerFragment-->back-->stateList.size() =" + stateList.size());
		if (stateList.size() == 0) {
			return false;
		} else {
			State state = stateList.get(stateList.size() - 1);
			stateList.remove(stateList.size() - 1);
			updateView(state);
			return true;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		boolean isFirstCrateView = false;
		if(mDir == null) { //首次加载
			externalSDFile = SdcardUtils.getExternalSDcardFile(getActivity());
			innerSdFile = SdcardUtils.getInnerSDcardFile(getActivity());
			mDir = externalSDFile;
			if(mDir == null) {
				mDir = innerSdFile;
			}
			mDir = new File(getResources().getString(R.string.home_dir));
			final ArrayList<String> list = new ArrayList<>();
			final ArrayList<Boolean> checkList= new ArrayList<>();
			final ArrayList<String> titleList = new ArrayList<>();
			//依次向家目录下添加:  ①内置sdcard ②外置sdcard ③接收文件夹
			if(innerSdFile != null) {
				list.add(innerSdFile.getPath());
				checkList.add(false);
				titleList.add(getResources().getString(R.string.inner_sdcard));
			}
			if(externalSDFile != null) {
				list.add(externalSDFile.getPath());
				checkList.add(false);
				titleList.add(getResources().getString(R.string.external_sdcard));
			}
			MainActivity activity = (MainActivity) getActivity();
			final File receiveFileSaveDir = activity.getReceivedFileDirPath();
			activity.requestPermission(receiveFileSaveDir.hashCode() % 200 + MainActivity.REQUEST_CODE_WRITE_EXTERNAL,
					Manifest.permission.WRITE_EXTERNAL_STORAGE,
					new Runnable() {
						@Override
						public void run() {
							LogUtils.i(WifiP2pHelper.TAG, "gain the permission WRITE_EXTERNAL_STORAGE");
							receiveFileSaveDir.mkdir();
							list.add(receiveFileSaveDir.getPath());
							checkList.add(false);
							titleList.add(getResources().getString(R.string.received_file_dir));
						}
					}, null);
			LogUtils.i(WifiP2pHelper.TAG, "if the show first, I was wrong---------------------------------------------");
			nowState = new State(mDir, 0, 0, list, titleList, checkList, false);
		}

		View view = inflater.inflate(R.layout.fragment_file_explorer,
				container, false);
		lin_no_file = (ViewGroup) view.findViewById(R.id.lin_no_file);
		listView = (ListView) view.findViewById(R.id.listview);
		txt_dir_Path = (TextView) view.findViewById(R.id.txt_dir_path);
		listView.setAdapter(new MyListViewAdapter(getContext(), null));
		drawableLoaderUtils = FileResLoaderUtils.getInstance(this); //获取图片加载器实例对象
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {//stoped
					LogUtils.i(WifiP2pHelper.TAG, "listview stop");
					listView.setTag(false);
					((BaseAdapter) (listView.getAdapter())).notifyDataSetChanged();
					//加载需要加载图片的一个文件
					loadListViewItemDrawalbe();
				} else {
					LogUtils.i(WifiP2pHelper.TAG, "listview start scrolling");
					listView.setTag(true);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});
		updateView(nowState);
		registerForContextMenu(listView);
		return view;
	}

	private void loadListViewItemDrawalbe() {
		MyListViewAdapter adapter = (MyListViewAdapter) listView.getAdapter();
		int start = listView.getFirstVisiblePosition();
		int end = listView.getLastVisiblePosition();
		if(end >= adapter.getList().size()) {
			end = adapter.getList().size()-1;
		}
		LogUtils.i(WifiP2pHelper.TAG, "loadListViewItemDrawalbe: start="+start+" End="+end);
		if(end>=start) {
			for(int i=start; i<=end; i++) {
				String path = adapter.getList().get(i);
				if(FileTypeUtils.isNeedToLoadDrawable(path)) {
					drawableLoaderUtils.load(getContext(), path);
				}
			}
		}
	}

	private void updateCheckList(ArrayList<String> sendFiles, ArrayList<String> fileList, ArrayList<Boolean> checkList) {
		String data = sendFiles.toString();
		for(int i=0; i<fileList.size(); i++) {
			String temp = fileList.get(i);
			File f = new File(temp);
			if(f.isFile() && data.contains(temp)) {
				checkList.set(i, true);
			}else {
				checkList.set(i, false);
			}
		}
	}

	@Override
	public void onSendFileListChange(ArrayList<String> sendFiles, int num) {
		if(listView != null && listView.getAdapter() != null) {
			MyListViewAdapter adapter = (MyListViewAdapter) listView.getAdapter();
			ArrayList<Boolean> checkList = adapter.getmCheckBoxList();
			ArrayList<String> fileList = adapter.getList();
			updateCheckList(sendFiles, fileList, checkList);
			adapter.notifyDataSetInvalidated();
		}
	}

	@Override
	protected String getPositonPath(int position) {
		return nowState.list.get(position);
	}

	/**
	 * 保存浏览一个目录时的状态
	 */
	class State {
		public State(File dir, int pos, int top, ArrayList<String> list, ArrayList<String> titleList, ArrayList<Boolean> checkList) {
			this(dir, pos, top, list, titleList, checkList, true);
		}
		public State(File dir, int pos, int top, ArrayList<String> list, ArrayList<String> titleList, ArrayList<Boolean> checkList, boolean isAutoUpdateChildFiles) {
			this.pos = pos;
			this.dir = dir;
			this.top = top;
			this.checkList = checkList;
			this.list = list;
			this.isAutoUpdateChildFiles = isAutoUpdateChildFiles;
			this.titleList = titleList;
		}

		@Override
		public String toString() {
			return "dir="+dir+" pos="+pos+" top="+top+" isAutoUpdateChildFiles="+isAutoUpdateChildFiles;
		}

		private int pos;
		private File dir;
		private int top; // listview中第一个view的top
		private ArrayList<String> list;
		private ArrayList<Boolean> checkList;
		private ArrayList<String> titleList;
		private boolean isAutoUpdateChildFiles;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Log.d(WifiP2pHelper.TAG, "FileExplore-->onItemClick()");
		MyListViewAdapter adapter = (MyListViewAdapter) listView.getAdapter();
		String path = adapter.getList().get(position);
		File f = new File(path);
		if (f.isDirectory()) {
			final View v = listView.getChildAt(0);
			int top = (v == null) ? 0 : v.getTop();
			nowState.pos = listView.getFirstVisiblePosition();
			nowState.top = top;
//			State state = new State(mDir, listView.getFirstVisiblePosition(), top, adapter.getList(), adapter.getmCheckBoxList());
			stateList.add(nowState); //save old state
			updateView(new State(f, 0, 0, null, null, null)); //update new state
		}else { //click a file ---> add to sendFile-list
			MainActivity activity = (MainActivity) getActivity();
			adapter.toggleChecked(position);
			adapter.notifyDataSetInvalidated();
			if(adapter.isChecked(position)) {//checked-->add to sendfile-list
				String name = FileResLoaderUtils.getFileName(path);
				activity.addFileToSendFileList(f.getPath(), name);
			}else { //unchecked--->remove from sendfile-list
				activity.removeFileFromSendFileList(f.getPath());
			}
		}
		int len = f.getName().getBytes().length;
		Integer x = 500;
		Log.d(WifiP2pHelper.TAG, "fileName len(byte) = "+len + "  test:"+x.byteValue());
	}

	//加载完一个文件的图片后,的回调
	@Override
	public void onLoadOneFinished(String path, Object obj, boolean isAllFinished) {
		LogUtils.i(WifiP2pHelper.TAG, "LOAD_ONE_DRAWABLE_FINISHED");
		Object tag = listView.getTag();
		if(tag==null || !((Boolean)(tag))) {
			((BaseAdapter)(listView.getAdapter())).notifyDataSetChanged();
		}
	}
}
