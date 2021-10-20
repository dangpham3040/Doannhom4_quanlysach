package com.example.doannhom4_quanlythuvien.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.model.*;
import com.example.doannhom4_quanlythuvien.adapter.*;
import com.example.doannhom4_quanlythuvien.ui.Book_detail;
import com.example.doannhom4_quanlythuvien.ui.Starup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private Spinner spinner;
    private String theloai = "";
    private GridView gridView;
    private ArrayList<Book> data = new ArrayList<>();
    private ArrayList<Book> result = new ArrayList<>();
    private book_Adapter adapter;
    private EditText etsearch;
    private Book book;
    private Book temp;
    private int solan = 0;


    public Home_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Home_Fragment newInstance(String param1, String param2) {
        Home_Fragment fragment = new Home_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        setControl();
        setEnvet();
        return view;
    }

    private void setControl() {
        //thêm Category
//        String[] mang = {"All","Arts and Music", "Biographies", "Business", "Comics"};
//        for (int i = 0; i < 5; i++) {
//            Category category= new Category(i,mang[i]);
//            StaticConfig.mCategory.child(i+"").setValue(category);
//        }
        //lấy thể loại sách từ firebase gắn cho spinner
        etsearch = view.findViewById(R.id.search);
        gridView = view.findViewById(R.id.book_gallery);

        adapter = new book_Adapter(getContext(), R.layout.item_book, data);
        gridView.setAdapter(adapter);
        khoitao();
        spinner = view.findViewById(R.id.book_type);
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrayList);
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
        StaticConfig.mBook.addValueEventListener(new ValueEventListener() {
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

    private void setEnvet() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = data.get(position);
                Intent intent = new Intent(getContext(), Book_detail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("chitiet", book);
                intent.putExtras(bundle);
                startActivity(intent);
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
                        adapter = new book_Adapter(getContext(), R.layout.item_book, result);
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
                        adapter = new book_Adapter(getContext(), R.layout.item_book, result);
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