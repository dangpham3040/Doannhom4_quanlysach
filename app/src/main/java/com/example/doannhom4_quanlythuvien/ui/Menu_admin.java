package com.example.doannhom4_quanlythuvien.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.databinding.ActivityMenuAdminBinding;

public class Menu_admin extends AppCompatActivity {
    ActivityMenuAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setEvnet();
    }

    private void setEvnet() {
        binding.thoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Starup.class));
            }
        });
        binding.quanly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Management.class));
            }
        });
        binding.thongke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Statistical.class));
            }
        });
    }


}