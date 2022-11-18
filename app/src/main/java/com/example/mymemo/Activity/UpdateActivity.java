package com.example.mymemo.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymemo.Activity.Interface.MemoEventBus;
import com.example.mymemo.Activity.Interface.MiniListDeleteCallback;
import com.example.mymemo.Activity.adapter.MiniListAdapter;
import com.example.mymemo.Activity.database.WriteDAO;
import com.example.mymemo.Activity.database.WriteDataBase;
import com.example.mymemo.Activity.model.WriteModel;
import com.example.mymemo.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpdateActivity extends AppCompatActivity implements MiniListDeleteCallback {
    ImageView back_Btn;
    EditText update_Title;
    EditText update_Context;
    TextView update_Count;
    ImageView update_Picture;
    ImageView update_Drawing;
    TextView update_Submit;
    ImageView update_Picture_close;
    RecyclerView gallert_image;

    Intent getIntent;
    int pos;
    int id;
    String title;
    String context;

    Toast toast ;

    WriteModel data;
    WriteDataBase dataBase;

    ArrayList<String> updataUriList;
    MiniListAdapter updataAdapter;

    SimpleDateFormat mFormat;
    Date mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        back_Btn = findViewById(R.id.back_Btn);
        update_Title = findViewById(R.id.update_Title);
        update_Context = findViewById(R.id.update_Context);
        update_Count = findViewById(R.id.update_Count);
        update_Picture = findViewById(R.id.update_Picture);
        update_Drawing = findViewById(R.id.update_Drawing);
        update_Submit = findViewById(R.id.update_Submit);
        gallert_image = findViewById(R.id.gallert_image);
        update_Picture_close = findViewById(R.id.update_Picture_close);
        dataBase = WriteDataBase.getInstance(UpdateActivity.this);
        updataUriList = new ArrayList<>();
        data = new WriteModel();

        getIntent = getIntent();
        pos = getIntent.getIntExtra("pos", 0);
        id = getIntent.getIntExtra("ID", 0);
        title = getIntent.getStringExtra("title");
        context = getIntent.getStringExtra("context");

        update_Title.setText(title);
        update_Context.setText(context);

        updataUriList = getIntent.getStringArrayListExtra("uri");

        if (updataUriList != null) {
            gallert_image.setVisibility(View.VISIBLE);
            update_Picture_close.setVisibility(View.VISIBLE);
        } else {
            gallert_image.setVisibility(View.GONE);
            update_Picture_close.setVisibility(View.GONE);
            updataUriList = new ArrayList<>();
        }

        updataAdapter = new MiniListAdapter(UpdateActivity.this, updataUriList);
        gallert_image.setAdapter(updataAdapter);
        updataAdapter.setMiniListDeleteCallback(UpdateActivity.this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UpdateActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        gallert_image.setLayoutManager(linearLayoutManager);

        EventBus.getDefault().post(new MemoEventBus(pos, id, title, context));

        //수정 이미지 불러오기 버튼
        update_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_PICK);
                pictureLauncher.launch(intent);
            }
        });

        update_Picture_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updataUriList.clear();
                gallert_image.setVisibility(View.GONE);
                update_Picture_close.setVisibility(View.GONE);
                updataAdapter.notifyDataSetChanged();
            }
        });


        update_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = update_Title.getText().toString().trim();
                String context = update_Context.getText().toString().trim();
                Intent i = new Intent();

                i.putExtra("title", title);
                i.putExtra("context", context);

                setResult(WriteActivity.RESULT_OK, i);

                if (!title.equals("")) {
                    if (!context.equals("")) {
                        if (updataUriList != null) {
                            if (!updataUriList.isEmpty()) {
                                String imgThum = String.valueOf(updataUriList.get(0));
                                data.setUriList(updataUriList);
                                data.setImgThum(imgThum);
                            } else {
                                data.setUriList(null);
                                data.setImgThum(null);
                            }
                        }

                        i.putStringArrayListExtra("uri", data.getUriList());
                        i.putExtra("imgThum", data.getImgThum());

                        mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        long mNow = System.currentTimeMillis();
                        mDate = new Date(mNow);
                        data.setDate(mFormat.format(mDate));
                        i.putExtra("date", data.getDate());
                        String date = data.getDate();

                        if(!updataUriList.isEmpty() || updataUriList != null){
                            String updateThum = String.valueOf(updataUriList.get(0));
                            dataBase.writeDao().update(id, title, context, date, updataUriList, updateThum);
                        }else {
                            dataBase.writeDao().update(id, title, context, date, null, null);
                        }
                        EventBus.getDefault().post(new MemoEventBus(pos, id, title, context));
                        finish();

                    } else {
                        toast.makeText(UpdateActivity.this, "내용 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    toast.makeText(UpdateActivity.this, "제목 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //그림그리기 버튼 리스너
        update_Drawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent drawingIntent = new Intent(UpdateActivity.this,DrawingActivity.class);
                UpdatedrawingLauncher.launch(drawingIntent);
            }
        });
    }
    //uri가 있을경우 이미지 불러오기
    ActivityResultLauncher<Intent> pictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == MainActivity.RESULT_OK) {
                        Intent uriIntent = result.getData();
                        ClipData mClipData = uriIntent.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            updataUriList.add(String.valueOf(mClipData.getItemAt(i).getUri()));
                        }
                    }
                    if (!updataUriList.isEmpty()) {
                        gallert_image.setVisibility(View.VISIBLE);
                        update_Picture_close.setVisibility(View.VISIBLE);
                    } else {
                        gallert_image.setVisibility(View.GONE);
                        update_Picture_close.setVisibility(View.GONE);
                    }
                    updataAdapter.notifyDataSetChanged();
                }
    });

    ActivityResultLauncher<Intent> UpdatedrawingLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //글쓰기 액티비티가 종료될때 특정값을 반환받는다.
                    if (result.getResultCode() == MainActivity.RESULT_OK) {
                        Intent intent = result.getData();
                        String drawingImage = intent.getStringExtra("drawingImage");
                        updataUriList.add(drawingImage);
                    }
                    if (!updataUriList.isEmpty()) {
                        gallert_image.setVisibility(View.VISIBLE);
                        update_Picture_close.setVisibility(View.VISIBLE);
                    } else {
                        gallert_image.setVisibility(View.GONE);
                        update_Picture_close.setVisibility(View.GONE);
                    }
                    updataAdapter.notifyDataSetChanged();
                }
            });

    //이미지 클릭삭제
    @Override
    public void deleteClick(int pos) {
        updataUriList.remove(pos);
        if(updataUriList.isEmpty()){
            gallert_image.setVisibility(View.GONE);
            update_Picture_close.setVisibility(View.GONE);
        }
        updataAdapter.notifyDataSetChanged();
    }
}