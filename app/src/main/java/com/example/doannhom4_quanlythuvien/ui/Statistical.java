package com.example.doannhom4_quanlythuvien.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.databinding.ActivityStatisticalBinding;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

public class Statistical extends AppCompatActivity {
    ActivityStatisticalBinding binding;
    private TextView tieude;
    private ImageView goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ArrayList<BarEntry> data = new ArrayList<>();
        data.add(new BarEntry(0, 2));
        BarDataSet dataSet1 = new BarDataSet(data, "data1");
        data.add(new BarEntry(1, 5));
        BarDataSet dataSet2 = new BarDataSet(data, "data2");
        data.add(new BarEntry(2, 1));
        BarDataSet dataSet3 = new BarDataSet(data, "data3");


        BarData barData = new BarData(dataSet1);
        BarData barData2 = new BarData(dataSet2);
        BarData barData3 = new BarData(dataSet3);
        binding.bar.setData(barData);
        binding.bar.setData(barData2);
        binding.bar.setData(barData3);
        binding.bar.setTouchEnabled(true);
        binding.bar.setDragEnabled(true);
        binding.bar.setScaleEnabled(true);
        setControl();
        setEvnet();
    }

    private void setEvnet() {
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setControl() {
        tieude = findViewById(R.id.title);
        goback = findViewById(R.id.goback);
        tieude.setText("Statistical");
    }
}