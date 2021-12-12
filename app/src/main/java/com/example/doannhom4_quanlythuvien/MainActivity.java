package com.example.doannhom4_quanlythuvien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.ui.*;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
//    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        progressBar.setBackgroundColor(Color.BLUE);
        setControl();
    }
    private void setControl() {
        //màn hình loading....
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StaticConfig.items=0;
                //kiểm tra coi đã login chưa
                if (StaticConfig.fAuth.getCurrentUser() != null) {
                    startActivity(new Intent(getApplicationContext(), Starup.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), Login.class));
                }
            }
        }, 2500);
        //startActivity(new Intent(getApplicationContext(), Statistical.class));
    }
}