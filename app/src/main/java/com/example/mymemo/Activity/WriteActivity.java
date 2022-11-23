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
    
    //1. 변수 네이밍은 카멜케이스로 적어주세요. 
    
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

    //2. 이런 띄워쓰기는 항상 리포맷코드를 돌려서 없애주시는게 좋습니다.
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
        
        //3. 데이터 바인딩이나 뷰바인딩을 활용해서 매번 반복되는 findViewById 를 없앨 수 있습니다.
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
        
        //4. 레이아웃 매니저는 xml 리사이클러뷰에 속성으로 설정해주세요. orientation 도 마찬가지 그러면 코드량을 더욱 줄일 수 있습니다.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WriteActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        gallert_image.setLayoutManager(linearLayoutManager);

            //뒤로가기
            back_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //5. sysout 사용보단 팀버 라이브러리나 Debug 용 Logger 를 사용해주세요. sysout 남겨두시면 나중에 앱 릴리즈해도 사용자한테 로그가 보입니다.
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
                    
                    //6. 키는 로우 스트링보단 전역적으로 선언하시고 key_feature와 같은 네이밍 룰을 가지고 계시면 편합니다.
                    i.putExtra("title", title);
                    i.putExtra("context", context);

                    setResult(WriteActivity.RESULT_OK, i);

                    //8. 자바에도 isNotEmpty 함수가 있지않던가요?
                    if (!title.equals("")) {
                        if (!context.equals("")) {

                                if(uriList != null){
                                    if(!uriList.isEmpty()){
                                        //7. uriList에 0을 고정적으로 접근하시면 앱이 터질 가능성이 있습니다.
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
                            //로우 스트링은 사용을 최대한 자제해주시고 strings.xml 에서 관리해주세요.
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
    
    //notifyDataSetChanged 를 사용하시기보단 range와 같은 바뀐부분만 변경되게하여 리소스 사용을 줄여주세요. ListAdapter와 diffUtil 의 조합을 사용하셔도 좋아요.
}
