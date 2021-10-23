package com.example.doannhom4_quanlythuvien.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.model.*;
import com.squareup.picasso.Picasso;

import java.nio.file.DirectoryStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

public class book_Adapter extends ArrayAdapter implements Filterable {
    Context context;
    int resource;
    ArrayList<Book> data;


    @Override
    public int getCount() {
        return data.size();
    }

    public book_Adapter(@NonNull Context context, int resource, ArrayList<Book> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;

    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return this.data.get(data.size() - 1 - position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        ImageView cover = convertView.findViewById(R.id.cover);
        TextView name = convertView.findViewById(R.id.title);
        TextView author = convertView.findViewById(R.id.author);
        RatingBar ratingBar = convertView.findViewById(R.id.rating);

        Book sach = data.get(position);
        Picasso.get()
                .load(sach.getCoverPhotoURL())
                .fit()
                .placeholder(R.drawable.no_image)
                .into(cover);
        name.setText(sach.getTitle());
        author.setText(sach.getAuthor());
        ratingBar.setRating(sach.getRating());
        CheckBox checkBox = convertView.findViewById(R.id.checkbox);
        //lay danh sach da chon
       if(checkBox!=null){
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (checkBox.isChecked()) {
                        StaticConfig.ArrayCheck.add(sach);
                    } else {
                        StaticConfig.ArrayCheck.remove(sach);
                    }
                }
            });
        }
        return convertView;
    }


}
