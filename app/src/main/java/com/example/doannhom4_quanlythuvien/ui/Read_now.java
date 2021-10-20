package com.example.doannhom4_quanlythuvien.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.model.Book;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

public class Read_now extends AppCompatActivity {
    private WebView view;
    private TextView title;
    private ImageView goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_now);
        setControl();
        setEvnet();
    }

    private void setEvnet() {
//        goback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

    private void setControl() {
        Book chitiet = (Book) getIntent().getSerializableExtra("chitiet");
        title = findViewById(R.id.title);
        // goback = findViewById(R.id.goback);
//        title.setText(chitiet.getTitle());
        view = findViewById(R.id.view);

        //pdfView.fromUri(Uri.parse("http://www.anweitong.com/upload/document/standard/national_standards/138793918364316200.pdf")).load();
        String link =
                "https://docs.google.com/document/d/1PnOsT41KBflmnxI1TRMV9mEldyGE7TpahIQN9tMhRLE/edit";

        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setAllowFileAccess(true);
        view.loadUrl(link);

    }

}