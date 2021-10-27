package com.example.doannhom4_quanlythuvien.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.model.Book;
import com.example.doannhom4_quanlythuvien.model.Comment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class comment_Adapter extends ArrayAdapter {
    Context context;
    int resource;
    ArrayList<Comment> data;

    public comment_Adapter(@NonNull Context context, int resource, ArrayList<Comment> data) {
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
        ImageView Avatar = convertView.findViewById(R.id.avatar);
        TextView name = convertView.findViewById(R.id.username);
        TextView date = convertView.findViewById(R.id.date);
        RatingBar ratingBar = convertView.findViewById(R.id.rating);
        TextView text = convertView.findViewById(R.id.tv_Comment);

        Comment comment = data.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String dateStr = sdf.format(comment.getTimestamp());
        date.setText(dateStr);
        ratingBar.setRating(comment.getRating());
        text.setText(comment.getText());
        StaticConfig.mUser.child(comment.getUser_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.setText(snapshot.child("name").getValue(String.class));
                Picasso.get()
                        .load(snapshot.child("pic").getValue(String.class))
                        .fit()
                        .placeholder(R.drawable.no_image)
                        .into(Avatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });

        return convertView;
    }
}
