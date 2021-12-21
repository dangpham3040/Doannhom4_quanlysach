package com.example.doannhom4_quanlythuvien.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doannhom4_quanlythuvien.R;

public class Contact extends AppCompatActivity {
    private TextView title;
    private ImageView goback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        setControl();
        setEvnet();
    }

    private void setEvnet() {
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Starup.class));
            }
        });
    }

    private void setControl() {
        title = findViewById(R.id.title);
        goback = findViewById(R.id.goback);
        title.setText("Contact");
        title.setTextSize(25f);
    }
}