package com.example.doannhom4_quanlythuvien.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.databinding.ActivityStatisticalBinding;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.model.Book;
import com.example.doannhom4_quanlythuvien.model.Comment;
import com.example.doannhom4_quanlythuvien.model.Thongke;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Statistical extends AppCompatActivity {
    ActivityStatisticalBinding binding;
    private TextView tieude;
    private ImageView goback;
    private float vitri = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setControl();
        setEvnet();
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < StaticConfig.ArrayThongke.size(); i++) {
            Thongke tk = StaticConfig.ArrayThongke.get(i);
            entries.add(new BarEntry(vitri, tk.getTongsosao()));
            BarDataSet dataSet = new BarDataSet(entries, tk.getTen());
            vitri = vitri + 2;
            BarData data = new BarData(dataSet);
            BarChart chart = new BarChart(getApplicationContext());
            setContentView(chart);
            chart.setData(data);
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            chart.animateY(5000);
            binding.bar.setData(data);
        }
        binding.bar.setTouchEnabled(true);
        binding.bar.setDragEnabled(true);
        binding.bar.setScaleEnabled(true);
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