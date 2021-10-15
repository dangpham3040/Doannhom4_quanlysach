package com.example.doannhom4_quanlythuvien.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.adapter.book_Adapter;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.model.Book;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Management extends AppCompatActivity {
    private Spinner spinner;
    private ImageView goback;
    private TextView title;
    private ArrayList<Book> data = new ArrayList<>();
    private GridView gridView;
    private book_Adapter adapter;

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

        khoitao();
        adapter = new book_Adapter(getApplicationContext(), R.layout.items_library, data);
        gridView.setAdapter(adapter);
        gridView.setNumColumns(1);

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
        for (int i = 0; i < 10; i++) {
            Book book = new Book(i + "", "sach" + i, "tac gia" + i, 4, StaticConfig.Default_avatar, StaticConfig.Default_avatar);
            data.add(book);
        }
        Book book = new Book(11 + "", "tieng viet", "dang", 4, StaticConfig.Default_avatar, StaticConfig.Default_avatar);
        data.add(book);
    }

    private void setEvent() {
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}