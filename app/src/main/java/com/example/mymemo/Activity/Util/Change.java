package com.example.mymemo.Activity.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Change {

    //View를 Bitmap 으로 변환 (View to Bitmap)
    public Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(
                view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    //Bitmap을 String으로 변환 (Bitmap to String)
    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }


    //String을 Bitmap으로 변환 (String to Bitmap)
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.URL_SAFE );
            //byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }


    //Uri를 비트맵으로 변환  (Uri to Bitmap)
    public Bitmap UriToBitmap(Context mActivity, Uri imageuri) {
        Bitmap bm = null;
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                bm = ImageDecoder.decodeBitmap(ImageDecoder.createSource(mActivity.getContentResolver(), imageuri));
            } else {
                bm = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), imageuri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }


    //Bitmap을 URi 로 변환 (Bitmap to Uri)
    public Uri BitMaptoUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
