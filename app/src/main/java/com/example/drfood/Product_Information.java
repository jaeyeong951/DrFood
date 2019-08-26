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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_info);
        TextView pdName = findViewById(R.id.product_name);
        pdImage = findViewById(R.id.product_image);

        Intent intent = getIntent();
        product_image = intent.getStringExtra("이미지");
        product_name = intent.getStringExtra("이름");
        Log.e("이미지 되는지 test", product_image);
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
