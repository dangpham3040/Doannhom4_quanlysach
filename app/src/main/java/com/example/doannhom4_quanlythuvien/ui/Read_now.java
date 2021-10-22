package com.example.doannhom4_quanlythuvien.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.model.*;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Read_now extends AppCompatActivity {
    private TextView title;
    private ImageView goback;
    private String pdfurl;
    private PDFView pdfView;
    private int sotrang;
    private ProgressBar progressBar;
    private Book chitiet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_now);
        setControl();
        setEvnet();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sotrang();
    }

    private void setEvnet() {
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("page", pdfView.getCurrentPage() + "");
                sotrang();
                finish();
            }
        });
    }

    private void sotrang() {
        NumberPage numberPage = new NumberPage(chitiet.getId(), StaticConfig.currentuser, pdfView.getCurrentPage());
        StaticConfig.mPageNumber.child(StaticConfig.currentuser).setValue(numberPage);
    }

    private void setControl() {
        title = findViewById(R.id.title);
        goback = findViewById(R.id.goback);
        pdfView = findViewById(R.id.pdfView);
        khoitao();
        new loadpdffromUrl().execute(pdfurl);

    }

    private void khoitao() {
        sotrang = 0;
        chitiet = (Book) getIntent().getSerializableExtra("chitiet");
        title.setText(chitiet.getTitle());
        progressBar = findViewById(R.id.progressBar);
        pdfurl = chitiet.getLink();

        StaticConfig.mPageNumber.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.exists()) {
                        if (ds.child("book_id").getValue(String.class).equals(chitiet.getId())
                                && ds.child("user_id").getValue(String.class).equals(StaticConfig.currentuser)) {
                            sotrang = ds.child("numberpage").getValue(int.class);
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

    //tạo async task để load pdf từ url
    private class loadpdffromUrl extends AsyncTask<String, Void, InputStream> implements OnLoadCompleteListener, OnErrorListener {
        @Override
        protected InputStream doInBackground(String... strings) {

            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // if response is success. we are getting input stream from url and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                //method to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            //after the executing async task we load pdf in to pdfview.
            pdfView.fromStream(inputStream)
                    //gán số trang mặc định
                    .defaultPage(sotrang)
                    .onLoad(this)
                    .onError(this)
                    .load();
        }

        @Override
        public void onError(Throwable t) {
            t.getMessage();
        }
        @Override
        public void loadComplete(int nbPages) {
            progressBar.setVisibility(View.GONE);
            sotrang = nbPages;
        }


    }
}
