package com.example.mymemo.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.mymemo.Activity.Interface.MemoEventBus;
import com.example.mymemo.Activity.adapter.DetilViewPagerAdapter;
import com.example.mymemo.Activity.database.WriteDataBase;
import com.example.mymemo.Activity.model.WriteModel;
import com.example.mymemo.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {
    ImageView view_Back_Btn;
    ImageView view_Menu;
    TextView view_Title;
    TextView view_Context;

    ViewPager2 image_viewpager;

    Intent getIntent;
    int pos;
    int id;
    String title;
    String context;

    ArrayList<String> uriList;

    WriteDataBase dataBase = WriteDataBase.getInstance(ViewActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        view_Back_Btn = findViewById(R.id.view_Back_Btn);
        view_Menu = findViewById(R.id.view_Menu);
        view_Title = findViewById(R.id.view_Title);
        view_Context = findViewById(R.id.view_Context);
        image_viewpager = findViewById(R.id.image_viewpager);
        uriList = new ArrayList<>();

        getIntent = getIntent();
        pos = getIntent.getIntExtra("pos", 0);
        id = getIntent.getIntExtra("ID" , 0);
        title =  getIntent.getStringExtra("title");
        context = getIntent.getStringExtra("context");
        uriList = getIntent.getStringArrayListExtra("uri");

        view_Title.setText(title);
        view_Context.setText(context);

        if(uriList != null){
            image_viewpager.setAdapter(new DetilViewPagerAdapter(ViewActivity.this,uriList));
            image_viewpager.setVisibility(View.VISIBLE);
        }else {
            image_viewpager.setVisibility(View.GONE);
        }

        //메뉴버튼
        view_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(ViewActivity.this, v);

                MenuInflater inflater = popup.getMenuInflater();
                Menu menu = popup.getMenu();
                inflater.inflate(R.menu.option_menu, menu);
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch(item.getItemId()){
                            case R.id.view_Delete:

                                WriteModel dataitem = new WriteModel();
                                dataitem.setId(id);

                                dataBase.writeDao().delete(dataitem);
                                EventBus.getDefault().post(new MemoEventBus(pos , id , title , context));

                                finish();
                                break;

                            case R.id.view_Update:

                                Intent upDateIntent = new Intent(ViewActivity.this, UpdateActivity.class);

                                upDateIntent.putExtra("update","OK");
                                upDateIntent.putExtra("pos" , pos);
                                upDateIntent.putExtra("ID", id);
                                upDateIntent.putExtra("title" , title);
                                upDateIntent.putExtra("context" , context);
                                upDateIntent.putStringArrayListExtra("uri" , uriList);

                                startActivity(upDateIntent);
                                finish();
                                break;
                        }
                        return false;
                    }
                });
            }
        });
    }
}