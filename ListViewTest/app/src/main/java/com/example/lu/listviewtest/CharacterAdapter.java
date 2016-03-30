package com.example.lu.listviewtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mechrevo on 2016/3/30.
 */
public class CharacterAdapter extends ArrayAdapter<Character> {

    private int resoerceid;

    public CharacterAdapter(Context context,int textViewResoueceId,List<Character> objects){
        super(context,textViewResoueceId,objects);
        resoerceid=textViewResoueceId;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){

        Character character = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resoerceid,null);
            viewHolder = new ViewHolder();
            viewHolder.characterImage = (ImageView) view.findViewById(R.id.character_image);
            viewHolder.characterName = (TextView) view.findViewById(R.id.character_name);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.characterImage.setImageResource(character.getImageId());
        viewHolder.characterName.setText(character.getName());
        return view;
    }

    class ViewHolder{

        ImageView characterImage;
        TextView characterName;
    }
}
