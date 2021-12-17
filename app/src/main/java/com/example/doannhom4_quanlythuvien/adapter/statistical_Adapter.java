package com.example.doannhom4_quanlythuvien.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.model.*;
import com.example.doannhom4_quanlythuvien.ui.Statistical;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class statistical_Adapter extends ArrayAdapter {
    Context context;
    int resource;
    ArrayList<Statisticals> data;

    public statistical_Adapter(@NonNull Context context, int resource, ArrayList<Statisticals> data) {
        super(context, resource, data);
        this.data = data;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        TextView ten = convertView.findViewById(R.id.ten);
        ImageView mau = convertView.findViewById(R.id.mau);
        Statisticals s = data.get(position);
        ten.setText(s.getTen());
        int [] maudoi = StaticConfig.arrayListColer;

        mau.setBackgroundColor(maudoi[position]);
        return convertView;
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
