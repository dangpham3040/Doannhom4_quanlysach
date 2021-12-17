package com.example.doannhom4_quanlythuvien.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.adapter.*;
import com.example.doannhom4_quanlythuvien.model.*;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Random;

public class Statistical extends AppCompatActivity {
    private TextView tieude;
    private ImageView goback;
    private float vitri = 0f;
    private BarChart bar;
    private BarData data;
    private GridView gridView_thongke;
    private statistical_Adapter adapter;
    private ArrayList<Statisticals> datamau = new ArrayList<>();


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
            Statisticals tk = StaticConfig.ArrayThongke.get(i);
            entries.add(new BarEntry(vitri, tk.getTongsosao()));
            BarDataSet dataSet = new BarDataSet(entries, "Data");
            data = new BarData(dataSet);
            int [] mau = StaticConfig.arrayListColer;
            dataSet.setColors(mau);
            dataSet.setValueTextSize(10f);
            dataSet.setValueTextColor(Color.BLACK);
            vitri= vitri+2;
        }
        bar.setData(data);

        bar.getLegend().setEnabled(false);
        bar.animateY(4500);
        bar.setTouchEnabled(true);
        bar.setDragEnabled(true);
        bar.setScaleEnabled(true);
        bar.setHighlightFullBarEnabled(false);
        adapter.notifyDataSetChanged();
    }

    private void setControl() {
        tieude = findViewById(R.id.title);
        goback = findViewById(R.id.goback);
        tieude.setText("Statistical");
        bar = findViewById(R.id.bar);
        gridView_thongke = findViewById(R.id.gridView_thongke);
        adapter = new statistical_Adapter(getApplicationContext(), R.layout.item_statistical, StaticConfig.ArrayThongke);
        gridView_thongke.setAdapter(adapter);


    }


}