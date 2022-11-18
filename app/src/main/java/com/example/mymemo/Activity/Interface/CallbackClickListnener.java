package com.example.mymemo.Activity.Interface;
import android.view.View;
import java.util.ArrayList;

public interface CallbackClickListnener {
    void onClick(View v , int pos , int id , String title , String context, ArrayList<String> uriList);
}
