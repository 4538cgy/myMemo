package com.example.mymemo.Activity.model;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "TB_TackMemo")
public class WriteModel implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "write_Title")
    private String title;

    @ColumnInfo(name = "write_Context")
    private String context;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "dataType")
    private String dataType;

    @ColumnInfo(name = "uriList")
    private ArrayList<String> uriList;

    @ColumnInfo(name = "imageThum")
    private String imgThum;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContext() {
        return context;
    }
    public void setContext(String context) {
        this.context = context;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public ArrayList<String> getUriList() {
        return uriList;
    }

    public void setUriList(ArrayList<String> uriList) {
        this.uriList = uriList;
    }

    public String getImgThum() {
        return imgThum; }
    public void setImgThum(String imgThum) {
        this.imgThum = imgThum; }

    @Override
    public String toString() {
        return "WriteModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", context='" + context + '\'' +
                ", date='" + date + '\'' +
                ", dataType='" + dataType + '\'' +
                ", uriList=" + uriList +
                ", imgThum='" + imgThum + '\'' +
                '}';
    }
}