package com.lu.kuaichuan.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lu.com.kuaichuan.R;

/**
 * Created by Lu on 2016/5/16.
 */
public class Fragment_media_main extends Fragment {

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_media_main, container, false);

        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);

        View music = getActivity().findViewById(R.id.media_music);
        View video = getActivity().findViewById(R.id.media_video);

        music.setOnClickListener(new MediaClickListener());
        video.setOnClickListener(new MediaClickListener());
    }

    class MediaClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.media_music:
                    //如果是全局事务，则不能重复提交
                    FragmentManager fragmentManager_music = getFragmentManager();
                    FragmentTransaction transaction_music = fragmentManager_music.beginTransaction();

                    Bundle bundle_music = new Bundle();
                    bundle_music.putString("Type", "Music");
                    Fragment_media_make fragment_music_make = new Fragment_media_make();
                    fragment_music_make.setArguments(bundle_music);

                    transaction_music.replace(R.id.media_main_id, fragment_music_make);
                    transaction_music.addToBackStack(null);
                    transaction_music.commit();

                    break;

                case R.id.media_video:

                    FragmentManager fragmentManager_video = getFragmentManager();
                    FragmentTransaction transaction_video = fragmentManager_video.beginTransaction();

                    Bundle bundle_video = new Bundle();
                    bundle_video.putString("Type", "Video");
                    Fragment_media_make fragment_video_make = new Fragment_media_make();
                    fragment_video_make.setArguments(bundle_video);

                    transaction_video.replace(R.id.media_main_id, fragment_video_make);
                    transaction_video.addToBackStack(null);
                    transaction_video.commit();

                    break;

                default:
                    break;
            }
        }
    }
}