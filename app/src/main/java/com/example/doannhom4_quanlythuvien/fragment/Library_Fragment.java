package com.example.doannhom4_quanlythuvien.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.adapter.book_Adapter;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.model.Book;
import com.example.doannhom4_quanlythuvien.model.Library;
import com.example.doannhom4_quanlythuvien.ui.Book_detail;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Library_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Library_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private Spinner spinner;
    private ArrayList<Book> data = new ArrayList<>();
    private GridView gridView;
    private book_Adapter adapter;
    private String theloai;
    private EditText etsearch;
    private ArrayList<Book> result = new ArrayList<>();
    private ArrayList<Library> yeuthich = new ArrayList<>();
    private CheckBox checkBox;
    private Button btndel;
    private ProgressBar progressBar;


    private Book book;
    private Book temp;
    private Library library;

    public Library_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Library_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Library_Fragment newInstance(String param1, String param2) {
        Library_Fragment fragment = new Library_Fragment();
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
        view = inflater.inflate(R.layout.fragment_library, container, false);
        // Inflate the layout for this fragment
        setControl();
        setEnvet();
        return view;
    }

    private void setControl() {
        etsearch = view.findViewById(R.id.search);
        gridView = view.findViewById(R.id.book_gallery);
        spinner = view.findViewById(R.id.book_type);
        checkBox = view.findViewById(R.id.checkbox);
        btndel = view.findViewById(R.id.btndel);
        progressBar = view.findViewById(R.id.progressBar);
        khoitao();
        progressBar.setVisibility(View.INVISIBLE);
        StaticConfig.is_del = false;

        adapter = new book_Adapter(getContext(), R.layout.items_library, data);
        gridView.setAdapter(adapter);
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
        if (!arrayAdapter.isEmpty()) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void khoitao() {
        Query thuvien = StaticConfig.mLibrary.child(StaticConfig.currentuser).orderByChild("timestamp");
        thuvien.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //xoá list book
                yeuthich.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    library = ds.getValue(Library.class);
                    yeuthich.add(0, library);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }

        });
        StaticConfig.mBook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //xoá list book
                data.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    book = ds.getValue(Book.class);
                    for (int i = 0; i < yeuthich.size(); i++) {
                        if (book.getId().equals(yeuthich.get(i).getBook_id())
                                && yeuthich.get(i).getIs_heart() == true &&
                                yeuthich.get(i).getUser_id().equals(StaticConfig.currentuser)) {
                            data.add(book);
                        }
                    }
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
        //xoa sach khoi danh sach yeu thich
        btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("so luong check",StaticConfig.ArrayCheck.size()+"");
                for (int i = 0; i < StaticConfig.ArrayCheck.size(); i++) {
                    Book sach = StaticConfig.ArrayCheck.get(i);
                    StaticConfig.mLibrary.child(sach.getId()).child("is_heart").setValue(false);
                    result.remove(sach);
                }
                StaticConfig.ArrayCheck.clear();
                adapter.notifyDataSetChanged();
                btndel.setVisibility(View.INVISIBLE);
                StaticConfig.is_del = false;
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                StaticConfig.is_del = true;
                btndel.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                return false;
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
                if(theloai!=null)
                StaticConfig.mBook.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //xoá list book
                        result.removeAll(result);
                        String tempchr = etsearch.getText().toString().toLowerCase();
                        if(theloai!=null)
                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i).getTitle().toLowerCase().contains(tempchr) ||
                                    data.get(i).getAuthor().toLowerCase().contains(tempchr)
                            ) {
                                if (theloai.equals("All") || theloai.equals(data.get(i).getType()))
                                    result.add(data.get(i));
                            }
                        }
                        if (tempchr.isEmpty() && theloai.equals("All")) {
                            result = data;
                        }
                        adapter = new book_Adapter(getContext(), R.layout.items_library, result);
                        gridView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                        if (tempchr.isEmpty() && theloai.equals("All")) {
                            khoitao();
                        }
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
                result.removeAll(result);
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getType().equals(theloai) || theloai.equals("All")) {
                        result.add(data.get(i));
                    }
                }
                adapter = new book_Adapter(getContext(), R.layout.items_library, result);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
    }
}