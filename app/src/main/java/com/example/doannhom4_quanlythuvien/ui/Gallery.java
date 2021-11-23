package com.example.doannhom4_quanlythuvien.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.adapter.Gallery_Adapter;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

public class Gallery extends AppCompatActivity {
    private TextView title;
    private ImageView goback;
    private GridView gridView;
    private ArrayList<String> data = new ArrayList<>();
    private Gallery_Adapter adapter;
    private ProgressBar progressBar;
    private String linkanh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        setControl();
        setEvnet();
    }

    private void setEvnet() {
        adapter = new Gallery_Adapter(getApplication(), R.layout.item_gallery, data);
        gridView.setAdapter(adapter);
        khoitao();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String link = data.get(i);
                new AlertDialog.Builder(Gallery.this)
                        .setTitle("Update Avatar!!")
                        .setMessage("Are you sure you want to Update Avatar??")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                StaticConfig.mUser.child(StaticConfig.currentuser).child("pic").setValue(link);
                                startActivity(new Intent(getApplicationContext(), Edit_profile.class));
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void khoitao() {
        StorageReference listimg = StaticConfig.storageReference.child("Avatar/" + StaticConfig.currentuser);
        listimg.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        data.clear();
                        for (StorageReference file : listResult.getItems()) {
                            file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    if (!uri.toString().equals(linkanh)) {
                                        data.add(uri.toString());
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void setControl() {
        goback = findViewById(R.id.goback);
        title = findViewById(R.id.title);
        gridView = findViewById(R.id.gridView);
        progressBar = findViewById(R.id.progressBar);
        title.setText("Gallery");
        linkanh = getIntent().getStringExtra("chitiet");
    }
}