package com.example.drfood;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    LinearLayout care_bar;
    LinearLayout harm_bar;
    TextView safe_text;
    TextView care_text;
    TextView harm_text;
    TextView allergy_text;
    int allergy_num;

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
        care_bar = findViewById(R.id.care_bar_xml);
        harm_bar = findViewById(R.id.harm_bar_xml);
        safe_text = findViewById(R.id.product_safe_num);
        care_text = findViewById(R.id.product_care_num);
        harm_text = findViewById(R.id.product_harm_num);
        allergy_text = findViewById(R.id.product_atopy_num);

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

        allergy_num = intent.getExtras().getInt("알러지개수");

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


        make_bar(Additive_EWG);
    }

    void make_bar(String[] additivie_EWG)
    {
        Log.d("성분EWG21431", additivie_EWG[0]);
        int safe_num = 0;
        int care_num = 0;
        int harm_num = 0;

        for(int i = 0; i<additivie_EWG.length; i++)
        {
            if(additivie_EWG[i] == null){
                break;
            }
            Log.e("출력test", additivie_EWG[i]);
            if(additivie_EWG[i].equals("1")  || additivie_EWG[i].equals("2"))
            {
                safe_num = safe_num + 1;
            }
            else if(additivie_EWG[i].equals("3") || additivie_EWG[i].equals("4") || additivie_EWG[i].equals("5") || additivie_EWG[i].equals("6"))
            {
                care_num = care_num + 1;
            }
            else if(additivie_EWG[i].equals("7") || additivie_EWG[i].equals("8") || additivie_EWG[i].equals("9") || additivie_EWG[i].equals("10"))
            {
                harm_num = harm_num + 1;
            }

        }
            LinearLayout.LayoutParams lay = (LinearLayout.LayoutParams) safe_bar.getLayoutParams();
            lay.weight = safe_num;
            LinearLayout.LayoutParams lay1 = (LinearLayout.LayoutParams) care_bar.getLayoutParams();
            lay1.weight = care_num;
            LinearLayout.LayoutParams lay2 = (LinearLayout.LayoutParams) harm_bar.getLayoutParams();
            lay2.weight = harm_num;

            String safe_num_str = Integer.toString(safe_num);
            String care_num_str = Integer.toString(care_num);
            String harm_num_str = Integer.toString(harm_num);

            safe_text.setText(safe_num_str);
            care_text.setText(care_num_str);
            harm_text.setText(harm_num_str);

            String allergy_text_num = Integer.toString(allergy_num);

            allergy_text.setText(allergy_text_num);


    }

}