package com.example.mymemo.Activity.database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mymemo.Activity.model.WriteModel;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface WriteDAO {

    @Insert(onConflict = REPLACE)
    void insert(WriteModel data);

    @Delete
    void delete(WriteModel write);

    @Delete
    void reset(List<WriteModel> write);

    @Query("UPDATE TB_TackMemo SET write_Title = :title ,date = :date , write_Context = :context , uriList = :uriList , imageThum = :imgThum WHERE ID = :sID")
    void update(int sID, String title, String context , String date ,ArrayList<String> uriList , String imgThum);

    @Query("SELECT * FROM TB_TackMemo")
    List<WriteModel> getAll();

    //@Query("UPDATE ")
   /* @Query("SELECT * FROM TB_TackMemo WHERE ID = :sID")
    List<WriteModel> getSelect(int sID);*/
}