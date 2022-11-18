package com.example.mymemo.Activity.Interface;

public class MemoEventBus {
    public int pos ;
    public int id ;
    public String title;
    public String context;

    public MemoEventBus(int pos , int id , String title, String context){
        this.pos = pos;
        this.id = id;
        this.title = title;
        this.context = context;
    }


}
