package com.example.lu.listviewtest;

/**
 * Created by mechrevo on 2016/3/30.
 */
public class Character {

    private String name;
    private int imageId;

    public Character(String name,int imageId){

        this.name=name;
        this.imageId=imageId;
    }

    public String getName(){
        return name;
    }

    public int getImageId(){
        return imageId;
    }
}
