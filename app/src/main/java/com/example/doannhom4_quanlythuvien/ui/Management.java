package com.example.doannhom4_quanlythuvien.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.adapter.book_Adapter;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.model.Book;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Management extends AppCompatActivity {
    private Spinner spinner;
    private ImageView goback;
    private TextView title;
    private ArrayList<Book> data = new ArrayList<>();
    private GridView gridView;
    private book_Adapter adapter;
    private String theloai;
    private EditText etsearch;
    private Button btnadd;
    private ArrayList<Book> result = new ArrayList<>();

    private Book book;
    private Book temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        setControl();
        setEvent();
    }

    private void setControl() {
        spinner = findViewById(R.id.book_type);
        title = findViewById(R.id.title);
        goback = findViewById(R.id.goback);
        gridView = findViewById(R.id.book_gallery);
        etsearch = findViewById(R.id.search);
        btnadd = findViewById(R.id.btnadd);


        khoitao();
        adapter = new book_Adapter(getApplicationContext(), R.layout.items_library, data);
        gridView.setAdapter(adapter);

        //gán tựa đề
        title.setText("Management");
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
        StaticConfig.mCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    arrayList.add(ds.child("name").getValue(String.class));
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
        spinner.setAdapter(arrayAdapter);
    }

    private void khoitao() {

        Query querybook = StaticConfig.mBook.orderByChild("timestamp");
        querybook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //xoá list book
                data.removeAll(data);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    book = ds.getValue(Book.class);
                    data.add(book);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });

    }

    private void setEvent() {
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Add_book.class));
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                StaticConfig.mBook.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //xoá list book
                        result.removeAll(result);
                        String tempchr = etsearch.getText().toString().toLowerCase();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            temp = ds.getValue(Book.class);
                            if (temp.getTitle().toLowerCase().contains(tempchr) || temp.getAuthor().toLowerCase().contains(tempchr) || temp.getType().toLowerCase().contains(tempchr)) {
                                result.add(temp);
                            }

                            if (tempchr.isEmpty()) {
                                khoitao();
                                break;
                            }
                        }
                        adapter = new book_Adapter(getApplicationContext(), R.layout.items_library, result);
                        gridView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        throw error.toException();
                    }
                });
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                theloai = spinner.getSelectedItem().toString();
                StaticConfig.mBook.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //xoá list book
                        result.removeAll(result);
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            temp = ds.getValue(Book.class);

                            if (temp.getType().contains(theloai)) {
                                result.add(temp);
                            } else if (theloai.equals("All")) {
                                result = data;
                            }
                        }
                        adapter = new book_Adapter(getApplicationContext(), R.layout.items_library, result);
                        gridView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        if (theloai.equals("All")) {
                            khoitao();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        throw error.toException();
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}