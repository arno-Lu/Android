package com.lu.kuaichuan.wifidirect.fragment;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.lu.kuaichuan.wifidirect.activity.MainActivity;
import com.lu.kuaichuan.wifidirect.R;
import com.lu.kuaichuan.wifidirect.adapter.MyFragmentAdapter;
import com.lu.kuaichuan.wifidirect.Views.SendFileListPopWin;
import com.lu.kuaichuan.wifidirect.p2p.WifiP2pHelper;
import com.lu.kuaichuan.wifidirect.utils.BitmapUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ckt on 2/29/16.
 */
public class ContentFragment extends Fragment implements View.OnClickListener, MainActivity.OnSendFileListChangeListener {
    private ViewGroup mButtomFunViewGroup;
    private ViewGroup mButtomFun_fileNum;
    private ViewGroup mButtomFun_send;
    private ViewGroup mButtomFun_clear;
    private TextView txt_sendFileNum;
    private ImageView img_connect; //the right conner "send"
    private int img_connect_height;
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentArrayList;
    private ImageView image;
    private int viewPagerTitleIds[] = {R.id.id_connect_title,R.id.id_application_title, R.id.id_pic_title,R.id.id_office_title, R.id.id_media_title, R.id.id_file_title};
    private int currIndex;
    private int bmpW;
    private int offset;
    private View view;

    private FileExplorerFragment mFileFragment;

    private ConnectFragment connectFragment;
    private Fragment_pic fragment_pic;
    private Fragment_media_main fragment_media_main;
    private Fragment_office_main fragment_office_main;
    private DeviceChooseFragment mDeviceChooseFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_layout, container, false);
        txt_sendFileNum = (TextView) view.findViewById(R.id.txt_fileNum);
        mButtomFunViewGroup = (ViewGroup) view.findViewById(R.id.lin_send_fun);
        mButtomFun_fileNum = (ViewGroup) view.findViewById(R.id.lin_fun_fileNum);
        mButtomFun_send = (ViewGroup) view.findViewById(R.id.lin_fun_sendFile);
        mButtomFun_clear = (ViewGroup) view.findViewById(R.id.lin_fun_clear);
        mButtomFun_fileNum.setOnClickListener(this);
        mButtomFun_send.setOnClickListener(this);
        mButtomFun_clear.setOnClickListener(this);
        img_connect = (ImageView) view.findViewById(R.id.img_connect);
        img_connect_height = (int) BitmapUtils.dipTopx(getContext(), 55);
        img_connect.setOnClickListener(this);
        InitImage();
        InitViewPager();
        InitTextView();
        return view;
    }

    public void InitTextView() {
        for (int i = 0; i < viewPagerTitleIds.length; i++) {
            TextView textView = (TextView) view.findViewById(viewPagerTitleIds[i]);
            textView.setOnClickListener(new TextListener(i));
        }
    }

    public void InitImage() {
        image = (ImageView) view.findViewById(R.id.id_cursor);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = 0;
        ViewGroup.LayoutParams lp = image.getLayoutParams();
        lp.width = screenW / viewPagerTitleIds.length;
        bmpW = lp.width;
        image.setLayoutParams(lp);
        /*Matrix matrix = new Matrix();
        matrix.preTranslate(offset, 0);
        image.setImageMatrix(matrix);*/
    }

    public void InitViewPager() {
        mPager = (ViewPager) view.findViewById(R.id.id_view_pager);
        fragmentArrayList = new ArrayList<>();
        ApplicationFragment applicationFragment = new ApplicationFragment();
        MusicFragment musicFragment = new MusicFragment();
        MovieFragment movieFragment = new MovieFragment();
        mFileFragment = new FileExplorerFragment();

        mDeviceChooseFragment = new DeviceChooseFragment();
        connectFragment = new ConnectFragment();
        fragment_media_main = new Fragment_media_main();
        fragment_pic = new Fragment_pic();
        fragment_office_main = new Fragment_office_main();

        fragmentArrayList.add(connectFragment);
        fragmentArrayList.add(applicationFragment);
        fragmentArrayList.add(fragment_pic);
        fragmentArrayList.add(fragment_media_main);
        fragmentArrayList.add(fragment_office_main);
        fragmentArrayList.add(mFileFragment);

        MainActivity activity = (MainActivity) getActivity();
        mPager.setAdapter(new MyFragmentAdapter(activity.getSupportFragmentManager(), fragmentArrayList));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    public void toggleConnectImgBut(boolean isShow) {
        if(isShow) {
            if(img_connect.getVisibility() == View.GONE) { //show
                img_connect.setVisibility(View.VISIBLE);
                ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, img_connect.getWidth() / 2, img_connect.getHeight() / 2);
                scaleAnimation.setDuration(200);
                scaleAnimation.setInterpolator(new AccelerateInterpolator());
                img_connect.startAnimation(scaleAnimation);
            }
        }else {
            if(img_connect.getVisibility() == View.VISIBLE) { //hide
                ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0, 1, 0, img_connect.getWidth() / 2, img_connect.getHeight() / 2);
                scaleAnimation.setDuration(200);
                scaleAnimation.setInterpolator(new AccelerateInterpolator());
                img_connect.startAnimation(scaleAnimation);
                img_connect.setVisibility(View.GONE);
            }
        }
    }

    public void toggleButtomFun(boolean isShow) {
        final RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mButtomFunViewGroup.getLayoutParams();
        ValueAnimator animator;
        if (isShow) {//show
            if(img_connect.getTag() == null || !(Boolean)(img_connect.getTag())) {

            }else { //�Ѿ���ʾ��
                return;
            }
            img_connect.setTag(true);
            animator = ValueAnimator.ofInt(0, img_connect_height);
        } else {//hide
            if(img_connect.getTag() == null || (Boolean)(img_connect.getTag())) {

            }else { //�Ѿ�������
                return;
            }
            img_connect.setTag(false);
            animator = ValueAnimator.ofInt(img_connect_height, 0);
        }
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                lp.height = value;
                mButtomFunViewGroup.setLayoutParams(lp);
            }
        });
        animator.setDuration(200);
//        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
        mButtomFunViewGroup.setLayoutParams(lp);
    }

    @Override
    public void onClick(View v) {
        final MainActivity activity = (MainActivity) getActivity();
        switch (v.getId()) {
            case R.id.img_connect:
                activity.getDeviceConnectDialog().show();
                break;
            case R.id.lin_fun_fileNum: //click the fileNum view--->show a popwin to list all selected files to send
                new SendFileListPopWin(false, activity.getResources().getString(R.string.delete_sendfilelist_popwin_title), activity, activity.getSendFiles(), new SendFileListPopWin.IOnOkListener() {
                    @Override
                    public void onOk(ArrayList<String> seleckedList, boolean isAdd) {
                        activity.removeFileFromSendFileList(seleckedList);
                    }
                }).showAtLocation(this.image, Gravity.CENTER, 0, 0);
                break;
            case R.id.lin_fun_sendFile: //send the selected files
                WifiP2pHelper wifiP2pHelper = activity.getWifiP2pHelper();
                if (wifiP2pHelper.isConnected()) {
                    Log.d(WifiP2pHelper.TAG, "trying send files");
                    ArrayList<File> list = new ArrayList<>();
                    for (int i = 0; i < activity.getSendFiles().size(); i++) {
                        list.add(new File(activity.getSendFiles().get(i)));
                    }
                    wifiP2pHelper.sendFiles(list);
                    activity.clearSendFileList();
                } else {
                    activity.getDeviceConnectDialog().show();
                }


                break;
            case R.id.lin_fun_clear: //clear all sendFile list
                new AlertDialog.Builder(activity)
                        .setTitle(R.string.clear_sendfilelist_alert_title)
                        .setMessage(R.string.clear_sendfilelist_alert_content)
                        .setPositiveButton(R.string.clear_sendfilelist_alert_button_sure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.clearSendFileList();
                            }
                        })
                        .setNegativeButton(R.string.clear_sendfilelist_alert_button_cancel, null)
                        .show();


                break;
        }
    }

    //the others has changed the sendFile-List, do update views here
    //this listen was seted in MainActivity
    @Override
    public void onSendFileListChange(ArrayList<String> sendFiles, int num) {
        txt_sendFileNum.setText("" + num);
        if (num == 0) {
            toggleButtomFun(false);
        } else {
            toggleButtomFun(true);
        }
        for (int i = 0; i < fragmentArrayList.size(); i++) {
            Object obj = fragmentArrayList.get(i);
            if (obj instanceof MainActivity.OnSendFileListChangeListener) {
                ((MainActivity.OnSendFileListChangeListener) obj).onSendFileListChange(sendFiles, num);
            }
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private int one;

        public MyOnPageChangeListener() {
            one = offset * 2 + bmpW;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Animation animation = new TranslateAnimation(currIndex * one, position * one, 0, 0);
            currIndex = position;
            animation.setFillAfter(true);
            animation.setDuration(200);
            image.startAnimation(animation);
            int i = currIndex + 1;
            Log.d(WifiP2pHelper.TAG, "onPageSelected()" + position + "  " + one);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public class TextListener implements View.OnClickListener {
        private int index = 0;

        public TextListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mPager.setCurrentItem(index);
        }
    }

    public boolean isNowFileExplorerFragment() {
        return fragmentArrayList.get(mPager.getCurrentItem()) instanceof FileExplorerFragment;
    }

    public FileExplorerFragment getFileExplorerFragment() {
        return mFileFragment;
    }

}
