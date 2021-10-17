package com.example.doannhom4_quanlythuvien.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.model.Book;
import com.example.doannhom4_quanlythuvien.model.Library;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Book_detail extends AppCompatActivity {

    private TextView title;
    private ImageView goback;
    private TextView book_title, author, type;
    private ImageView cover;
    private RatingBar ratingBar;
    private ImageView heart;
    private String book_id;
    private boolean isheart = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        setControl();
        setEvnet();
    }

    private void setEvnet() {
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //heart.setImageResource(R.drawable.heart_on);
                if (isheart == true) {
                   Boolean is_heart = false;
                    Library library = new Library(StaticConfig.currentuser, book_id, is_heart);
                    StaticConfig.mLibrary.child(book_id).setValue(library);
                    heart.setImageResource(R.drawable.heart_off);
                }
                if (isheart == false) {
                    Boolean is_heart = true;
                    Library library = new Library(StaticConfig.currentuser, book_id, is_heart);
                    StaticConfig.mLibrary.child(book_id).setValue(library);
                    heart.setImageResource(R.drawable.heart_on);
                }

            }
        });
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
        heart = findViewById(R.id.heart);

        //gan du lieu
        book_id = chitiet.getId();
        book_title.setText(chitiet.getTitle());
        author.setText(chitiet.getAuthor());
        ratingBar.setRating(chitiet.getRating());
        Picasso.get()
                .load(chitiet.getCoverPhotoURL())
                .fit()
                .placeholder(R.drawable.no_image)
//                                .transform(transformation)
                .into(cover);
        type.setText(chitiet.getType());
        StaticConfig.mLibrary = StaticConfig.Database.getReference("Library/" + book_id);
        StaticConfig.mLibrary.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if(ds.child("user_id").getValue(String.class).equals(StaticConfig.currentuser)){
                        if (ds.child("is_heart").getValue(Boolean.class).equals(true)) {
                            heart.setImageResource(R.drawable.heart_on);
                            isheart = true;
                        }
                        if (ds.child("is_heart").getValue(Boolean.class).equals(false)) {
                            heart.setImageResource(R.drawable.heart_off);
                            isheart = false;
                        }
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });


    }
}