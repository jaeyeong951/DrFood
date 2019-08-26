package com.example.drfood;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Product_Information extends Activity {
    String product_name;
    String product_company;
    String product_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_info);
        product_name = "감자칩";
        TextView pdName = findViewById(R.id.product_name);
        TextView pdCompany = findViewById(R.id.product_company);
        ImageView pdImage = findViewById(R.id.product_image);

        pdName.setText(product_name);
    }

}
