package com.example.drfood;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class Product_infomation_compose extends AppCompatActivity {

    LinearLayout safe_bar;
    LinearLayout care_bar;
    LinearLayout harm_bar;
    LinearLayout none_bar;
    TextView safe_text;
    TextView care_text;
    TextView harm_text;

    int safe_num = 0;
    int care_num = 0;
    int harm_num = 0;
    int none_num = 0;

    String Additive_EWG[] = new String[100];
    String Additive_Name[] = new String[100];
    String No_Additive_Name[] = new String[100];


    private ListView listView;
    private ListView listView2;
    private ListViewAdapter adapter;
    private ListViewAdapter adapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_info_compose);

        safe_bar = findViewById(R.id.safe_bar_xml);
        care_bar = findViewById(R.id.care_bar_xml);
        harm_bar = findViewById(R.id.harm_bar_xml);
        none_bar = findViewById(R.id.none_bar_xml);
        safe_text = findViewById(R.id.product_safe_num);
        care_text = findViewById(R.id.product_care_num);
        harm_text = findViewById(R.id.product_harm_num);

        listView = findViewById(R.id.arrayList);

        Intent intent = getIntent();
        safe_num = intent.getExtras().getInt("안전num");
        care_num = intent.getExtras().getInt("주의num");
        harm_num = intent.getExtras().getInt("위험num");
        none_num = intent.getExtras().getInt("Nonenum");
        Additive_EWG = intent.getExtras().getStringArray("첨가물ewg");
        Additive_Name = intent.getExtras().getStringArray("첨가물이름");
        No_Additive_Name = intent.getExtras().getStringArray("no첨가물");

        LinearLayout.LayoutParams lay = (LinearLayout.LayoutParams) safe_bar.getLayoutParams();
        lay.weight = safe_num;
        LinearLayout.LayoutParams lay1 = (LinearLayout.LayoutParams) care_bar.getLayoutParams();
        lay1.weight = care_num;
        LinearLayout.LayoutParams lay2 = (LinearLayout.LayoutParams) harm_bar.getLayoutParams();
        lay2.weight = harm_num;
        LinearLayout.LayoutParams lay3 = (LinearLayout.LayoutParams) none_bar.getLayoutParams();
        lay3.weight = none_num;

        String safe_num_str = Integer.toString(safe_num);
        String care_num_str = Integer.toString(care_num);
        String harm_num_str = Integer.toString(harm_num);

        safe_text.setText(safe_num_str);
        care_text.setText(care_num_str);
        harm_text.setText(harm_num_str);

        for (int i = 0; i < 100; i++) {
            if (Additive_EWG[i] == null) {
                break;
            }
            Log.e("룰룰ㄹ라", Additive_EWG[i]);
            Log.e("룰라2", Additive_Name[i]);
            Log.e("룰라3", No_Additive_Name[i]);
        }


        adapter = new ListViewAdapter();
        listView = (ListView) findViewById(R.id.arrayList);

        adapter2 = new ListViewAdapter();
        listView2 = findViewById(R.id.arrayList2);

        //어뎁터 할당
        listView.setAdapter(adapter);
        listView2.setAdapter(adapter2);

        //adapter를 통한 값 전달
        for (int i = 0; i < Additive_Name.length; i++) {
            if(Additive_Name[i] == null)
                break;
            adapter.addVO(Additive_Name[i], Additive_EWG[i]);
        }


        for(int i = 0; i < No_Additive_Name.length; i++){
            if(No_Additive_Name[i] == null)
                break;
            if(No_Additive_Name[i].equals(""))
                continue;
            adapter2.addVO(No_Additive_Name[i], "");
        }
    }
}
