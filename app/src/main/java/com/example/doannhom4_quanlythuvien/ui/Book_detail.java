package com.example.doannhom4_quanlythuvien.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowId;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.adapter.*;
import com.example.doannhom4_quanlythuvien.fragment.Library_Fragment;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.model.Book;
import com.example.doannhom4_quanlythuvien.model.Comment;
import com.example.doannhom4_quanlythuvien.model.Library;
import com.example.readmoretextview.ReadMoreTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Book_detail extends AppCompatActivity {

    private TextView title;
    private ImageView goback;
    private TextView book_title, author, type, date, book_describe;
    private ImageView cover;
    private RatingBar ratingBar;
    private ImageView heart;
    private String book_id;
    private boolean isheart = false;
    private Button readnow;
    private ImageButton add_comment;
    private GridView gridView;
    private Book chitiet;
    private ArrayList<Comment> data = new ArrayList<>();
    private comment_Adapter adapter;
    private float sosao = 0;
    private String mathuvien = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        setControl();
        setEvnet();
        Log.e("itm",StaticConfig.items+"");
    }

    private void setEvnet() {
        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommnetDialog();
            }
        });
        readnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book chitiet = (Book) getIntent().getSerializableExtra("chitiet");
                Intent intent = new Intent(getApplicationContext(), Read_now.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("chitiet", chitiet);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isheart == true) {
                    Boolean is_heart = false;
                    Library library = new Library(mathuvien, book_id, StaticConfig.currentuser, is_heart);
                    StaticConfig.mLibrary.child(mathuvien).setValue(library);
                    heart.setImageResource(R.drawable.heart_off);
                }
                if (isheart == false) {
                    Boolean is_heart = true;
                    Library library = new Library(mathuvien, book_id, StaticConfig.currentuser, is_heart);
                    StaticConfig.mLibrary.child(mathuvien).setValue(library);
                    heart.setImageResource(R.drawable.heart_on);
                }

            }
        });
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getApplicationContext(),Starup.class));
            }
        });
    }

    private void addCommnetDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_comment_add);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        RatingBar ratingBar = dialog.findViewById(R.id.rating);
        EditText Comment = dialog.findViewById(R.id.edit_Comment);
        Button Submit = dialog.findViewById(R.id.Submit);
        ImageButton goback = dialog.findViewById(R.id.goback);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Comment.getText().toString().isEmpty()) {
                    StaticConfig.mComment.child(book_id).child(StaticConfig.currentuser).
                            setValue(new Comment(book_id, StaticConfig.currentuser, Comment.getText().toString(), ratingBar.getRating()));
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void setControl() {
        chitiet = (Book) getIntent().getSerializableExtra("chitiet");
        title = findViewById(R.id.title);
        title.setText(chitiet.getTitle());
        goback = findViewById(R.id.goback);
        book_title = findViewById(R.id.book_title);
        author = findViewById(R.id.author);
        type = findViewById(R.id.type);
        cover = findViewById(R.id.cover);
        ratingBar = findViewById(R.id.rating);
        heart = findViewById(R.id.heart);
        date = findViewById(R.id.date);
        book_describe = findViewById(R.id.book_describe);
        readnow = findViewById(R.id.readnow);
        add_comment = findViewById(R.id.add_comment);
        gridView = findViewById(R.id.list_comment);

        adapter = new comment_Adapter(getApplicationContext(), R.layout.item_comment, data);
        gridView.setAdapter(adapter);
        khoitao();

        //see more
        book_describe.setText(chitiet.getDescription());
        ReadMoreTextView readMoreTextView = new ReadMoreTextView();
        readMoreTextView.setTextView(book_describe);
        readMoreTextView.setMaximumLine(10);
        if (book_describe.length() > 1000) {
            readMoreTextView.setCollapseText("See Less");
            readMoreTextView.setExpandText("See More");
            readMoreTextView.setColorCode("#e74c3c");
            readMoreTextView.setReadMore();
        }

        //gan du lieu
        book_id = chitiet.getId();
        book_title.setText("Title: " + chitiet.getTitle());
        author.setText("Author: " + chitiet.getAuthor());

        Picasso.get()
                .load(chitiet.getCoverPhotoURL())
                .fit()
                .placeholder(R.drawable.no_image)
//                .transform()
                .into(cover);
        type.setText("Category: " + chitiet.getType());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String dateStr = sdf.format(chitiet.getTimestamp());
        date.setText("Date: " + dateStr);
        StaticConfig.mLibrary.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Library library = ds.getValue(Library.class);
                    if (library.getBook_id().equals(book_id)) {
                        if (library.getIs_heart() == true
                                && library.getUser_id().equals(StaticConfig.currentuser)) {
                            heart.setImageResource(R.drawable.heart_on);
                            isheart = true;
                        } else {
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

    private void khoitao() {
        mathuvien = StaticConfig.currentuser + chitiet.getId();
        Query xapsepbinhluan = StaticConfig.mComment.child(chitiet.getId()).orderByChild("timestamp");
        xapsepbinhluan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 1;
                float tongsao = 0;
                data.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Comment comment = ds.getValue(Comment.class);
                    data.add(0, comment);
                    tongsao += ds.child("rating").getValue(Float.class);
                    sosao = tongsao / i;
                    i++;
                }
                adapter.notifyDataSetChanged();
                ratingBar.setRating(sosao);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });

    }
}