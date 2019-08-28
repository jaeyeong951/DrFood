package com.example.drfood;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Product_Information extends AppCompatActivity {
    String product_name;
    String product_image;
    ImageView pdImage;
    Bitmap bitmap;
    LinearLayout safe_bar;

    //Additive 관련 성분들도
    String Additive_EWG[] = new String[100];
    String Additive_Name[] = new String[100];
    int Additive_Num;

    String No_Additive_Name[] = new String[100];
    int No_Additive_Num;

    String rawMaterialSplitedArray[] = new String[100];
    String allergyListSplitedArray[] = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_info);
        TextView pdName = findViewById(R.id.product_name);
        pdImage = findViewById(R.id.product_image);
        safe_bar = findViewById(R.id.safe_bar_xml);
        LinearLayout.LayoutParams lay = (LinearLayout.LayoutParams) safe_bar.getLayoutParams();
        lay.weight = 2;

        Intent intent = getIntent();
        product_image = intent.getStringExtra("이미지");
        product_name = intent.getStringExtra("이름");

        //성분 관련들 Intent
        Additive_EWG = intent.getExtras().getStringArray("성분EWG");
        Additive_Name = intent.getExtras().getStringArray("성분Name");
        Additive_Num = intent.getIntExtra("성분Num",1);

        No_Additive_Name = intent.getExtras().getStringArray("No성분Name");
       // No_Additive_Num = intent.getIntExtra("No성분Num", 1);

        rawMaterialSplitedArray = intent.getExtras().getStringArray("성분");
        allergyListSplitedArray = intent.getExtras().getStringArray("알러지");

        Log.d("알러지" , allergyListSplitedArray[0]);

        for(int i = 0; i < 15; i++){
            Log.d("성분"+ i, rawMaterialSplitedArray[i]);

        }

        Log.d("성분EWG", Additive_EWG[0]);
        Log.d("성분Name", Additive_Name[0]);
        Log.d("성분Num", "" + Additive_Num);
        Log.d("No성분Name", "" +No_Additive_Name[0] + No_Additive_Name[1] + No_Additive_Name[2]);
        //Log.d("No성분Num", "" + No_Additive_Num);


        pdName.setText(product_name);

        Thread mThread = new Thread(){
            @Override
            public void run(){
                try{
                    URL url = new URL(product_image);

                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mThread.start();

        try{
            mThread.join();
            pdImage.setImageBitmap(bitmap);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
