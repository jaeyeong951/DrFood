package com.example.drfood;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends Activity {

    ImageButton startButton;


    private DatabaseReference mDatabase;
    private String Snack_Name;
    private boolean Exgist_Result;
    private final static int CAMERA_PERMISSIONS_GRANTED = 100;
    private String UserUid;
    private String UserEmail;
    private String UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //데이터 베이스 주소 가져오기
        mDatabase = FirebaseDatabase.getInstance().getReference();

        startButton = findViewById(R.id.barcode);   // Button Boilerplate

        Intent Go_Login_Page = new Intent(MainActivity.this,LoginPageAct.class);

        startActivityForResult(Go_Login_Page, 3000);
        //3000은 로그인



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
                startActivityForResult(goNextActivity,1001);
            }
        });
    }

    private boolean getCameraPermission() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
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

            final String result = data.getStringExtra("key");
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();

            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if(dataSnapshot.getKey().equals(result)){

                        //데이터베이스 안에 존재하면 true 없으면 false
                        Exgist_Result = true;

                        Snack_Name = dataSnapshot.getValue().toString();



                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mDatabase.child("food").child("snack").child("바코드").addChildEventListener(childEventListener);


        }
        else if(requestCode == 3000){
            UserUid = data.getStringExtra("UserUid");
            UserName = data.getStringExtra("UserName");
            UserEmail = data.getStringExtra("UserEmail");
            Log.d("UserUid_Main", UserUid);
            Log.d("UserName_Main",UserName);
            Log.d("UserEmail_Main", UserEmail);

        }
    }

}

