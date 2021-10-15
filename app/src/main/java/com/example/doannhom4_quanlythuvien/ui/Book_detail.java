package com.example.doannhom4_quanlythuvien.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.model.Book;
import com.squareup.picasso.Picasso;

public class Book_detail extends AppCompatActivity {

    private TextView title;
    private ImageView goback;
    private TextView book_title, author, type;
    private ImageView cover;
    private RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        setControl();
        setEvnet();
    }

    private void setEvnet() {
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void setControl() {
        Book chitiet = (Book) getIntent().getSerializableExtra("chitiet");
        title = findViewById(R.id.title);
        title.setText(chitiet.getTitle());
        goback = findViewById(R.id.goback);
        book_title = findViewById(R.id.book_title);
        author = findViewById(R.id.author);
        type = findViewById(R.id.type);
        cover = findViewById(R.id.cover);
        ratingBar = findViewById(R.id.rating);

        //gan du lieu
        book_title.setText(chitiet.getTitle());
        author.setText(chitiet.getAuthor());
        ratingBar.setRating(chitiet.getRating());
        Picasso.get()
                .load(chitiet.getCoverPhotoURL())
                .fit()
//                                .transform(transformation)
                .into(cover);

    }
}