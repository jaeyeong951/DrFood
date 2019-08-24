package com.example.drfood;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends Activity {

    ImageButton startButton;
    ImageButton Button;
    materialParser pharm;

    private final static int CAMERA_PERMISSIONS_GRANTED = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//
//        StrictMode.setThreadPolicy(policy);


        startButton = findViewById(R.id.barcode);   // Button Boilerplate
        Button = findViewById(R.id.chips_button);

        getCameraPermission();

        // 다음 Activity로 넘어가기 위한 onClickListener
        // 이렇게 한 이유는 Permission Check 가
        // 기본적으로 UI Thread가 아닌 다른 Thread 에서 동시에 실행되기 때문에
        // 첫 실행 때, 권한이 없어서 SurfaceView 에서 addCallback 처리를 제대로 못하는 상황이 생긴다.
        // 그래서 검은 화면이 나온다. 고로, 아예 Activity를 다르게 해줬다.
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goNextActivity = new Intent(getApplicationContext(), QRCodeScan.class);
                startActivityForResult(goNextActivity, 1001);
            }
        });

        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pharm = new materialParser();
                pharm.execute();
            }
        });
    }

    private boolean getCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            // 권한이 왜 필요한지 설명이 필요한가?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {
                Toast.makeText(this, "카메라 사용을 위해 확인버튼을 눌러주세요!", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA},
                        CAMERA_PERMISSIONS_GRANTED);
                return true;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            String result = data.getStringExtra("key");
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

    class materialParser extends AsyncTask<Void, Void, Void> {
        String rawMaterial;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //public final static String PHARM_URL = "http://openapi.hira.or.kr/openapi/service/pharmacyInfoService/getParmacyBasisList";
        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("http://apis.data.go.kr/B553748/CertImgListService/getCertImgListService?serviceKey=%2BwvPpNobnpO%2BxNDsB3NdwZqjZYg4C8JqEy7NhZxXof%2F2Owy9Vu2eYP1pZVtIw%2FcPEVTx8nKQ1ph%2F4ppRNxKBLA%3D%3D&prdlstNm=빈츠");

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                BufferedInputStream bis = new BufferedInputStream(url.openStream());
                xpp.setInput(bis, "utf-8");

                String tag = null;
                int event_type = xpp.getEventType();

                ArrayList<String> materialList = new ArrayList<>();

                while (event_type != XmlPullParser.END_DOCUMENT) {
                    if (event_type == XmlPullParser.START_TAG) {
                        tag = xpp.getName();
                    } else if (event_type == XmlPullParser.TEXT) {
                        /*
                         * 성분만 가져와 본다.
                         */
                        if (tag.equals("rawmtrl")) {
                            rawMaterial = xpp.getText();
//                            Log.e("원재료",rawMaterial);
                        }
                    } else if (event_type == XmlPullParser.END_TAG) {
                        tag = xpp.getName();
                        if (tag.equals("item")) {
                            Log.e("태그의 끝",rawMaterial);
                            materialList.add(rawMaterial);
                            for(int i = 0; i < materialList.size(); i++){
                                Log.e("원쟈료등", materialList.get(i));
                            }
                        }
                        //item별로 분리
                    }
                    event_type = xpp.next();
                }
                Iterator<String> list_it = materialList.iterator();
                while(list_it.hasNext()){
                    Log.e("뭔든","뭐든");
                    //Log.e("원재료들",list_it.next());
                    String a = list_it.next();
                    Log.e("원재료들",a);
                }
                for(int i = 0; i < materialList.size(); i++){
                    Log.e("원쟈료등", materialList.get(i));
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

//        private void printList(ArrayList<String> list) {
//            for (String entity : list) {
//                //System.out.println(entity);
//                Log.e("과자 성분", entity);
//                Log.e("뭐든", "뭐든");
//            }
//        }


    }
}

