package com.example.mymemo.Activity.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mymemo.Activity.model.WriteModel;

@Database(entities = {WriteModel.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class WriteDataBase extends RoomDatabase {
    private static WriteDataBase database;

    private static String DATABASE_NAME = "Tackbase";

    public synchronized static WriteDataBase getInstance(Context context)
    {
        if (database == null)
        {
            database = Room.databaseBuilder(context.getApplicationContext(), WriteDataBase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
    public abstract WriteDAO writeDao();
}
