package com.example.doannhom4_quanlythuvien.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doannhom4_quanlythuvien.MainActivity;
import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.adapter.book_Adapter;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.model.Book;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class Management extends AppCompatActivity {
    private Spinner spinner;
    private ImageView goback, icon_lich;
    private TextView title, date;
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
        date = findViewById(R.id.tvDate);
        icon_lich = findViewById(R.id.icon_lich);
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
                    data.add(0, book);
                    adapter.notifyDataSetChanged();
                    Log.d("bookid", book.getTimestamp() + "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
        //lay ngay  hien tai
        final Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        calendar.set(nam, thang, ngay);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date.setText(simpleDateFormat.format(calendar.getTime()));

    }
    private void chonNgay() {
        final Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                date.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }
    private void setEvent() {
        icon_lich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonNgay();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = data.get(position);
                Intent intent = new Intent(getApplicationContext(), Edit_book.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("chitiet", book);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Add_book.class));
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Starup.class));
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
                        theloai = spinner.getSelectedItem().toString();
                        String tempchr = etsearch.getText().toString().toLowerCase();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            temp = ds.getValue(Book.class);
                            if (temp.getTitle().toLowerCase().contains(tempchr) ||
                                    temp.getAuthor().toLowerCase().contains(tempchr)
                            ) {
                                if (theloai.equals("All")) {
                                    result.add(temp);
                                }
                                if (theloai.equals(temp.getType())) {
                                    result.add(temp);
                                }
                            }

                            if (tempchr.isEmpty() && theloai.equals("All")) {
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
                String tempchr = etsearch.getText().toString().toLowerCase();
                StaticConfig.mBook.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //xoá list book
                        result.removeAll(result);
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            temp = ds.getValue(Book.class);
                            if (temp.getType().contains(theloai) && tempchr.isEmpty()) {
                                result.add(temp);
                            }
                            if (theloai.equals(temp.getType()) || theloai.equals("All")) {
                                if (temp.getTitle().toLowerCase().contains(tempchr) ||
                                        temp.getAuthor().toLowerCase().contains(tempchr)
                                ) {
                                    if (!tempchr.isEmpty())
                                        result.add(temp);
                                }
                            }
                            if (theloai.equals("All") && tempchr.isEmpty()) {
                                result = data;
                            }
                        }
                        adapter = new book_Adapter(getApplicationContext(), R.layout.items_library, result);
                        gridView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        if (theloai.equals("All") && tempchr.isEmpty()) {
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