package com.example.mymemo.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymemo.Activity.Interface.MiniListDeleteCallback;
import com.example.mymemo.Activity.Util.Change;
import com.example.mymemo.Activity.adapter.MiniListAdapter;
import com.example.mymemo.Activity.database.WriteDataBase;
import com.example.mymemo.Activity.model.WriteModel;
import com.example.mymemo.R;

import java.util.ArrayList;
import java.util.Date;

public class WriteActivity extends AppCompatActivity implements MiniListDeleteCallback {
    ImageView back_Btn;
    EditText write_Title;
    EditText write_Context;
    TextView write_Count;
    ImageView write_Picture;
    ImageView write_Drawing;
    ImageView write_Picture_Close;
    TextView write_Submit;
    RecyclerView gallert_image;

    WriteModel data;
    WriteDataBase dataBase;

    Toast toast ;

    MiniListAdapter miniAdapter;
    ArrayList<String> uriList;

    SimpleDateFormat mFormat;
    Date mDate;

    Change change ;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        back_Btn = findViewById(R.id.back_Btn);
        write_Title = findViewById(R.id.write_Title);
        write_Context = findViewById(R.id.write_Context);
        write_Count = findViewById(R.id.write_Count);
        write_Picture = findViewById(R.id.write_Picture);
        write_Drawing = findViewById(R.id.write_Drawing);
        write_Submit = findViewById(R.id.write_Submit);
        gallert_image = findViewById(R.id.gallert_image);
        write_Picture_Close = findViewById(R.id.write_Picture_close);
        uriList = new ArrayList<>();
        data = new WriteModel();

         change = new Change();

        dataBase = WriteDataBase.getInstance(this);
        toast = new Toast(WriteActivity.this);

        miniAdapter = new MiniListAdapter( WriteActivity.this , uriList);
        gallert_image.setAdapter(miniAdapter);
        miniAdapter.setMiniListDeleteCallback(WriteActivity.this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WriteActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        gallert_image.setLayoutManager(linearLayoutManager);

            //뒤로가기
            back_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("@@@@@@@@@ 뒤로가기 @@@@@@@@@@@@@");
                }
            });

            //그림그리기
            write_Drawing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent drawingIntent = new Intent(WriteActivity.this,DrawingActivity.class);
                    drawingLauncher.launch(drawingIntent);
                }
            });


            //첨부된 이미지 지우기 버튼 리스너
            write_Picture_Close.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
             uriList.clear();
             gallert_image.setVisibility(View.GONE);
             write_Picture_Close.setVisibility(View.GONE);
             miniAdapter.notifyDataSetChanged();
             }
            });

            //신규 저장 리스너
            write_Submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = write_Title.getText().toString().trim();
                    String context = write_Context.getText().toString().trim();

                    Intent i = new Intent();
                    
                    i.putExtra("title", title);
                    i.putExtra("context", context);

                    setResult(WriteActivity.RESULT_OK, i);

                    if (!title.equals("")) {
                        if (!context.equals("")) {

                                if(uriList != null){
                                    if(!uriList.isEmpty()){
                                        String imgThum = String.valueOf(uriList.get(0));
                                        data.setUriList(uriList);
                                        data.setImgThum(imgThum);
                                    }else {
                                        data.setUriList(null);
                                        data.setImgThum(null);
                                    }
                                }
                                i.putStringArrayListExtra("uri",data.getUriList());
                                i.putExtra("imgThum", data.getImgThum());

                            mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            long mNow = System.currentTimeMillis();
                            mDate = new Date(mNow);
                                 data.setDate(mFormat.format(mDate));
                                 i.putExtra("date", data.getDate());

                                data.setTitle(title);
                                data.setContext(context);

                                dataBase.writeDao().insert(data);
                                finish();

                        } else {
                            toast.makeText(WriteActivity.this, "내용 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        toast.makeText(WriteActivity.this, "제목 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //에딧텍스트 글자수 실시간 표시
            write_Context.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String input = write_Context.getText().toString();
                    write_Count.setText(input.length() + " / 200");
                    if (input.length() > 200) {
                        toast.makeText(WriteActivity.this, "글자수를 넘었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        //사진불러오기
        write_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_PICK);
                pictureLauncher.launch(intent);
            }
        });
    }

    //데이터에 uri를 담아서 selet해주고 그걸 글쓰기 화면에 표시해줄껍니다.
    ActivityResultLauncher<Intent> pictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == MainActivity.RESULT_OK) {
                        Intent uriIntent = result.getData();
                        ClipData mClipData = uriIntent.getClipData();
                            for (int i = 0; i < mClipData.getItemCount(); i++) {
                                uriList.add(String.valueOf(mClipData.getItemAt(i).getUri()));
                            }
                    }
                    if (!uriList.isEmpty()) {
                        gallert_image.setVisibility(View.VISIBLE);
                        write_Picture_Close.setVisibility(View.VISIBLE);
                    } else {
                        gallert_image.setVisibility(View.GONE);
                        write_Picture_Close.setVisibility(View.GONE);
                    }
                    miniAdapter.notifyDataSetChanged();
                }
    });

    ActivityResultLauncher<Intent> drawingLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //글쓰기 액티비티가 종료될때 특정값을 반환받는다.
                    if (result.getResultCode() == MainActivity.RESULT_OK) {
                        Intent intent = result.getData();
                        String drawingImage = intent.getStringExtra("drawingImage");

                        uriList.add(drawingImage);
                    }
                    if (!uriList.isEmpty()) {
                        gallert_image.setVisibility(View.VISIBLE);
                        write_Picture_Close.setVisibility(View.VISIBLE);
                    } else {
                        gallert_image.setVisibility(View.GONE);
                        write_Picture_Close.setVisibility(View.GONE);
                    }
                    miniAdapter.notifyDataSetChanged();
                }
            });

    @Override
    public void deleteClick(int pos) {
        uriList.remove(pos);
        if(uriList.isEmpty()){
            gallert_image.setVisibility(View.GONE);
            write_Picture_Close.setVisibility(View.GONE);
        }
        miniAdapter.notifyDataSetChanged();
    }
}