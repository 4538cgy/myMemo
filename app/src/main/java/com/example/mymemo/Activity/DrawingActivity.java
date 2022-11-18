package com.example.mymemo.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymemo.Activity.Util.Change;
import com.example.mymemo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DrawingActivity extends AppCompatActivity {
    ImageView drawing_pen , drawing_eraser ,color_Black, color_White,color_Red,color_Green,color_Blue;
    TextView TEXT_1,TEXT_2,TEXT_3 , drawing_Submit;
    ConstraintLayout drawing_Con;

    private MyCanvas myView;
    int colorKey = 0;
    String drawingImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        drawingMenu drawingmenu = new drawingMenu();

        drawing_pen = findViewById(R.id.drawing_pen);
        drawing_eraser = findViewById(R.id.drawing_eraser);

        TEXT_1 = findViewById(R.id.TEXT_1);
        TEXT_2 = findViewById(R.id.TEXT_2);
        TEXT_3 = findViewById(R.id.TEXT_3);

        color_Black = findViewById(R.id.color_Black);
        color_White = findViewById(R.id.color_White);
        color_Red = findViewById(R.id.color_Red);
        color_Green = findViewById(R.id.color_Green);
        color_Blue = findViewById(R.id.color_Blue);

        drawing_pen.setOnClickListener(drawingmenu);
        drawing_eraser.setOnClickListener(drawingmenu);

        TEXT_1.setOnClickListener(drawingmenu);
        TEXT_2.setOnClickListener(drawingmenu);
        TEXT_3.setOnClickListener(drawingmenu);

        color_Black.setOnClickListener(drawingmenu);
        color_White.setOnClickListener(drawingmenu);
        color_Red.setOnClickListener(drawingmenu);
        color_Green.setOnClickListener(drawingmenu);
        color_Blue.setOnClickListener(drawingmenu);

        drawing_Con = findViewById(R.id.drawing_Con);
        drawing_Submit = findViewById(R.id.drawing_Submit);

        myView = new MyCanvas(DrawingActivity.this);
        drawing_Con.addView(myView);

        drawing_pen.setSelected(true);
        TEXT_2.setSelected(true);
        color_Black.setSelected(true);


        //내가 그린 그림 첨부
        drawing_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ScreenShot(myView);
                setResult(WriteActivity.RESULT_OK, i);
                i.putExtra("drawingImage", drawingImage);
                finish();
            }
        });
    }

    class drawingMenu implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){

                //펜/지우개 지정
                case R.id.drawing_pen:
                    drawing_pen.setSelected(true);
                    drawing_eraser.setSelected(false);
                    if(myView.mPaint.getColor() == Color.WHITE) {
                        switch (colorKey) {
                            case 1:
                                myView.mPaint.setColor(Color.BLACK);
                                break;
                            case 3:
                                myView.mPaint.setColor(Color.YELLOW);
                                break;
                            case 2:
                                myView.mPaint.setColor(Color.RED);
                                break;
                            case 4:
                                myView.mPaint.setColor(Color.GREEN);
                                break;
                            case 5:
                                myView.mPaint.setColor(Color.BLUE);
                                break;
                        }
                    }else{
                        myView.mPaint.getColor();
                    }
                    break;
                case R.id.drawing_eraser:
                    drawing_eraser.setSelected(true);
                    drawing_pen.setSelected(false);
                    myView.mPaint.setColor(Color.WHITE);
                   break;

                   //펜/지우개 굵기 지정
                case R.id.TEXT_1:
                    TEXT_1.setSelected(true);
                    TEXT_2.setSelected(false);
                    TEXT_3.setSelected(false);
                    myView.mPaint.setStrokeWidth(5);
                    break;

                case R.id.TEXT_2:
                    TEXT_1.setSelected(false);
                    TEXT_2.setSelected(true);
                    TEXT_3.setSelected(false);
                    myView.mPaint.setStrokeWidth(15);
                    break;

                case R.id.TEXT_3:
                    TEXT_1.setSelected(false);
                    TEXT_2.setSelected(false);
                    TEXT_3.setSelected(true);
                    myView.mPaint.setStrokeWidth(25);
                    break;

                    //펜 색지정
                case R.id.color_Black:
                    color_Black.setSelected(true);
                    color_White.setSelected(false);
                    color_Red.setSelected(false);
                    color_Green.setSelected(false);
                    color_Blue.setSelected(false);
                    myView.mPaint.setColor(Color.BLACK);
                    colorKey = 1 ;
                    break;
                case R.id.color_White:
                    color_Black.setSelected(false);
                    color_White.setSelected(true);
                    color_Red.setSelected(false);
                    color_Green.setSelected(false);
                    color_Blue.setSelected(false);
                    myView.mPaint.setColor(Color.YELLOW);
                    colorKey = 2 ;
                    break;
                case R.id.color_Red:
                    color_Black.setSelected(false);
                    color_White.setSelected(false);
                    color_Red.setSelected(true);
                    color_Green.setSelected(false);
                    color_Blue.setSelected(false);
                    myView.mPaint.setColor(Color.RED);
                    colorKey = 3 ;
                    break;
                case R.id.color_Green:
                    color_Black.setSelected(false);
                    color_White.setSelected(false);
                    color_Red.setSelected(false);
                    color_Green.setSelected(true);
                    color_Blue.setSelected(false);
                    myView.mPaint.setColor(Color.GREEN);
                    colorKey = 4 ;
                    break;
                case R.id.color_Blue:
                    color_Black.setSelected(false);
                    color_White.setSelected(false);
                    color_Red.setSelected(false);
                    color_Green.setSelected(false);
                    color_Blue.setSelected(true);
                    myView.mPaint.setColor(Color.BLUE);
                    colorKey = 5 ;
                    break;
            }
        }
    }
    public class MyCanvas extends View {
        Context context;
        Bitmap mBitmap;
        Canvas mCanvas;
        Path mPath;
        Paint mPaint;
        public MyCanvas(Context context) {
            super(context);
            this.context = context;
            mPath = new Path();
            mPaint = new Paint();
            mPaint.setColor(Color.BLACK);
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(10);
            mPaint.setStyle(Paint.Style.STROKE);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(mBitmap, 0, 0, null); //지금까지 그려진 내용
            canvas.drawPath(mPath, mPaint); //현재 그리고 있는 내용
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int x = (int)event.getX();
            int y = (int)event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPath.reset();
                    mPath.moveTo(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mPath.lineTo(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    mPath.lineTo(x, y);
                    mCanvas.drawPath(mPath, mPaint); //mBitmap 에 기록
                    mPath.reset();
                    break;
            }
            this.invalidate();
            return true;
        }
    }

    //화면 캡쳐하기
    public File ScreenShot(View view){

        view.setDrawingCacheEnabled(true);  //화면에 뿌릴때 캐시를 사용하게 한다
        Bitmap screenBitmap = view.getDrawingCache();   //캐시를 비트맵으로 변환

        String filename = System.currentTimeMillis()+"_screenshot.png";
        File file = new File(Environment.getExternalStorageDirectory()+"/Pictures", filename);  //Pictures폴더 screenshot.png 파일
        drawingImage = file.getAbsolutePath();
        FileOutputStream os = null;
        try{
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os);   //비트맵을 PNG파일로 변환
            os.close();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

        view.setDrawingCacheEnabled(false);
        return file;
    }
}