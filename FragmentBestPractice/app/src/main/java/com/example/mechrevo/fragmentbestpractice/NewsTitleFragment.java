package com.example.mechrevo.fragmentbestpractice;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mechrevo on 2016/4/8.
 */
public class NewsTitleFragment extends Fragment implements AdapterView.OnItemClickListener{

    private ListView newsTitleListView;
    private List<News> newsList;
    private  NewsAdapter adapter;
    private boolean isTwoPane;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        newsList = getNews();
        adapter = new NewsAdapter(context,R.layout.news_item,newsList);
    }

    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.news_title_frag,container,false);                //加载了布局
        newsTitleListView = (ListView) view.findViewById(R.id.news_title_list_view);
        newsTitleListView.setAdapter(adapter);
        newsTitleListView.setOnItemClickListener(this);
        return  view;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);
        if(getActivity().findViewById(R.id.news_content)!=null){
            isTwoPane = true;
        }else {
            isTwoPane = false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent ,View view , int position,long id){
        News news = newsList.get(position);
        if(isTwoPane){
            NewsContentFragment newsContentFragment=(NewsContentFragment)getFragmentManager().findFragmentById(R.id.news_content_frag);
            newsContentFragment.refresh(news.getTitle(),news.getContent());
        }else {
            NewsContentActivity.actionStart(getActivity(),news.getTitle(),news.getContent());
        }
    }

    private List<News> getNews(){
        List<News> newsList = new ArrayList<News>();
        News news1 = new News();
        news1.setTitle("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        news1.setContent("1222222222222222222222222222222246135555555555555555555555555555555555555555555555555555555555555555555555555222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");
        newsList.add(news1);
        News news2 = new News();
        news2.setTitle("111111458355555566666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666");
        news2.setContent("4555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555");
        newsList.add(news2);
        return newsList;
    }
}
