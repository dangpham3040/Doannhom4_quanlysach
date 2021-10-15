package com.example.doannhom4_quanlythuvien.fragment;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.adapter.book_Adapter;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.model.Book;
import com.example.doannhom4_quanlythuvien.ui.Book_detail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
        etsearch=view.findViewById(R.id.search);
        gridView = view.findViewById(R.id.book_gallery);
        spinner = view.findViewById(R.id.book_type);
        khoitao();
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
    }

    private void khoitao() {
        for (int i = 0; i < 10; i++) {
            Book book = new Book(i + "", "sach" + i, "tac gia" + i, StaticConfig.Default_avatar, StaticConfig.Default_avatar, "Biographies", 4);
            data.add(book);
        }
        Book book = new Book(11 + "", "tieng viet", "dang", StaticConfig.Default_avatar, StaticConfig.Default_avatar, "Business", 3);
        data.add(book);
    }


    private void setEnvet() {
        etsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ArrayList<Book> result = new ArrayList<>();
                String tempchr = etsearch.getText().toString();


                for (int i = 0; i < data.size(); i++) {
                    Book temp = data.get(i);
                    if (temp.getTitle().contains(tempchr) || temp.getAuthor().contains(tempchr) || temp.getType().contains(tempchr)){
                        result.add(temp);
                        Log.d("so sach", result.size() + "");
                    }

                    if (tempchr.isEmpty()) {
                        result = data;
                        break;
                    }
                }
                adapter = new book_Adapter(getContext(), R.layout.item_book, result);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                theloai = spinner.getSelectedItem().toString();
                ArrayList<Book> result = new ArrayList<>();

                for (int i = 0; i < data.size(); i++) {
                    Book temp = data.get(i);
                    if (temp.getType().equals(theloai)) {
                        result.add(temp);
                        Log.d("so sach", result.size() + "");
                    }
                    if (theloai.equals("All")) {
                        result = data;
                    }
                }
                if(result.size()==0){
                    Toast.makeText(getContext(), "khong co", Toast.LENGTH_SHORT).show();
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