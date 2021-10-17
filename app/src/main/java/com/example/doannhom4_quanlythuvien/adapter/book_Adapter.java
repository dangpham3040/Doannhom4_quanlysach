package com.example.doannhom4_quanlythuvien.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.model.*;
import com.squareup.picasso.Picasso;

import java.nio.file.DirectoryStream;
import java.util.ArrayList;
import java.util.Locale;

public class book_Adapter extends ArrayAdapter implements Filterable {
    Context context;
    int resource;
    ArrayList<Book> data;
    ArrayList<Book> dataResults;

    @Override
    public int getCount() {
        return data.size();
    }

    public book_Adapter(@NonNull Context context, int resource, ArrayList<Book> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.dataResults = data;
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
        return convertView;
    }

//    // Filter Class
//    public void filter(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//        dataResults.clear();
//        if (charText.length() == 0) {
//            dataResults.addAll(data);
//        } else {
//
//            for (Book wp : data) {
//                if (wp.getAuthor().toLowerCase(Locale.getDefault()).contains(charText)) {
//                    dataResults.add(wp);
//                    Log.d("search", dataResults.toString());
//                }
//                if (charText.isEmpty()) {
//                    dataResults.addAll(data);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public Filter getFilter() {
//
//        return filter;
//    }
//
//    Filter filter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            FilterResults filterResults = new FilterResults();
//            if (constraint == null || constraint.length() == 0) {
//                filterResults.count = data.size();
//                filterResults.values = data;
//            } else {
//                String seachChr = constraint.toString().toLowerCase();
//                ArrayList<Book> searchResults = new ArrayList<>();
//                for (Book d : data) {
//                    if (d.getTitle().contains(seachChr) || d.getAuthor().contains(seachChr)) {
//                        searchResults.add(d);
//                    }
//                    filterResults.count = searchResults.size();
//                    filterResults.values = searchResults;
//                }
//            }
//            return filterResults;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            dataResults = (ArrayList<Book>) results.values;
//            notifyDataSetChanged();
//        }
//    };
}
