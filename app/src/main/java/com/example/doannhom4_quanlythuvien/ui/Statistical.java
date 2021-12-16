package com.example.doannhom4_quanlythuvien.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.model.Book;
import com.example.doannhom4_quanlythuvien.model.Comment;
import com.example.doannhom4_quanlythuvien.model.Thongke;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.slider.LabelFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Statistical extends AppCompatActivity {
    private TextView tieude;
    private ImageView goback;
    private float vitri = 0f;
    private BarChart bar;
    private BarData data;
    private BarChart chart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);
        setControl();
        setEvnet();
    }

    private void setEvnet() {
        khoitao();
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void khoitao() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < StaticConfig.ArrayThongke.size(); i++) {
            Thongke tk = StaticConfig.ArrayThongke.get(i);
            entries.add(new BarEntry(vitri, tk.getTongsosao()));
            BarDataSet dataSet = new BarDataSet(entries, tk.getTen());
            data = new BarData(dataSet);
            chart = new BarChart(getApplicationContext());
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            dataSet.setValueTextSize(16f);
            dataSet.setValueTextColor(Color.BLACK);
            chart = new BarChart(getApplicationContext());
            bar.setData(data);
            vitri ++;
        }
        chart.setData(data);
        chart.animateY(5000);

        bar.setTouchEnabled(true);
        bar.setDragEnabled(true);
        bar.setScaleEnabled(true);
        bar.setHighlightFullBarEnabled(false);
    }

    private void setControl() {
        tieude = findViewById(R.id.title);
        goback = findViewById(R.id.goback);
        tieude.setText("Statistical");
        bar = findViewById(R.id.bar);

    }
}