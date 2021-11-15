package com.example.doannhom4_quanlythuvien.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.doannhom4_quanlythuvien.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Gallery_Adapter extends ArrayAdapter {
    Context context;
    int resource;
    ArrayList<String> data;

    public Gallery_Adapter(@NonNull Context context, int resource, ArrayList<String> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        ImageView cover = convertView.findViewById(R.id.cover);

        String vitri = data.get(position);

        Picasso.get()
                .load(vitri)
                .fit()
                .placeholder(R.drawable.no_image)
                .into(cover);
        return convertView;
    }
}
