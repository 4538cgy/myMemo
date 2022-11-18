package com.example.mymemo.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.mymemo.Activity.Interface.CallbackClickListnener;
import com.example.mymemo.Activity.Interface.MemoEventBus;
import com.example.mymemo.Activity.adapter.WriteListAdapter;
import com.example.mymemo.Activity.database.WriteDataBase;
import com.example.mymemo.Activity.model.WriteModel;
import com.example.mymemo.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CallbackClickListnener {
    ImageView new_Write;
    ImageView list_Type;
    RecyclerView list_Recycler;

    List<WriteModel> dataList;
    WriteModel dataitem;
    WriteDataBase writeDataBase;

    WriteListAdapter wrAdapter;

    boolean listType = false;
    final int SUCCESS = 1; // 메모 리스트 성공시

    myHandler mHandler;

    LinearLayoutManager linearLayoutManager;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    ArrayList<String> uriList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        new_Write = findViewById(R.id.new_Write);
        list_Type = findViewById(R.id.list_Type);
        list_Recycler = findViewById(R.id.list_Recycler);
        mHandler = new myHandler();
        dataList = new ArrayList<>();
        uriList = new ArrayList<>();

        wrAdapter = new WriteListAdapter(MainActivity.this, dataList);
        list_Recycler.setAdapter(wrAdapter);
        wrAdapter.setCallbackClickListnener(MainActivity.this);

        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list_Recycler.setLayoutManager(linearLayoutManager);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        getList();

        list_Type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listType == false) {
                    staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
                    list_Recycler.setLayoutManager(staggeredGridLayoutManager);
                    listType = true;
                } else {
                    list_Recycler.setLayoutManager(linearLayoutManager);
                    listType = false;
                }
            }
        });

        //+ 버튼 // 글쓰기 액티비티로 이동
        new_Write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WriteActivity.class);
                launcher.launch(i);
            }
        });

        EventBus.getDefault().register(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MemoEventBus memoBus) {
        getList();
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //글쓰기 액티비티가 종료될때 특정값을 반환받는다.
                    if (result.getResultCode() == MainActivity.RESULT_OK) {
                        Intent intent = result.getData();

                        String title = intent.getStringExtra("title");
                        String context = intent.getStringExtra("context");
                        uriList = intent.getStringArrayListExtra("uri");
                        String imgThum = intent.getStringExtra("imgThum");
                        String date = intent.getStringExtra("date");

                        dataitem = new WriteModel();

                        dataitem.setTitle(title);
                        dataitem.setContext(context);
                        dataitem.setUriList(uriList);
                        dataitem.setImgThum(imgThum);
                        dataitem.setDate(date);

                        dataList.add(dataitem);

                        wrAdapter.notifyDataSetChanged();
                    }
                }
            });

    //상세보기 액티비티 이동
    @Override
    public void onClick(View v ,int pos , int id , String title , String context, ArrayList<String> uriList) {

        Intent i = new Intent(MainActivity.this, ViewActivity.class);

        i.putExtra("pos", pos);
        i.putExtra("ID" , id);
        i.putExtra("title", title);
        i.putExtra("context", context);
        i.putStringArrayListExtra("uri",uriList);
        startActivity(i);
    }

    public void getList(){
        new Thread() {
            @Override
            public void run() {
                List<WriteModel> dataList_Tmp = new ArrayList<>();
                writeDataBase = WriteDataBase.getInstance(MainActivity.this);
                dataList_Tmp = writeDataBase.writeDao().getAll();
                Message msg = mHandler.obtainMessage(SUCCESS, dataList_Tmp);
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    class myHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case SUCCESS:
                    dataList.clear();
                    if(msg.obj != null) {
                        dataList.addAll((List<WriteModel>) msg.obj);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wrAdapter.notifyDataSetChanged();
                        }
                    });
                    break;
            }
        }
    }
}

