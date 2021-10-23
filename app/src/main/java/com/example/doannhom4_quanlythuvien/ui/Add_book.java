package com.example.doannhom4_quanlythuvien.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.model.Book;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Add_book extends AppCompatActivity {

    private TextView title, describe;
    private ImageView goback;
    private EditText ettitle, etauthor;
    private ImageView cover;
    private Spinner spinner;
    private RatingBar ratingBar;
    private String ten, tacgia, loai, link = "", mieuta, cover_link;
    private float sosao;
    private Uri filePath;
    private Button btnsave;
    private Button choose_file;
    private String file_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        setControl();
        setEvent();
    }

    private void setEvent() {
        //Viết hoa
        ettitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !ettitle.getText().toString().isEmpty()) {
                    String name = ettitle.getText().toString();
                    //ký tự dầu hoa còn lại thường
                    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                    //thay thế các khoảng trống thành 1 khoảng trống
                    name = name.trim().replaceAll(" +", " ");
                    ettitle.setText(name);
                }

            }
        });
        etauthor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !etauthor.getText().toString().isEmpty()) {
                    String name = etauthor.getText().toString();
                    //ký tự dầu hoa còn lại thường
                    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                    //thay thế các khoảng trống thành 1 khoảng trống
                    name = name.trim().replaceAll(" +", " ");
                    etauthor.setText(name);
                }

            }
        });
        choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mieuta = describe.getText().toString();
                ten = ettitle.getText().toString();
                tacgia = etauthor.getText().toString();
                if (ten.isEmpty() || tacgia.isEmpty() || link.isEmpty()
                        || sosao == 0 || mieuta.isEmpty() || cover_link.isEmpty() || link.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "vui long nhap du thong tin!!", Toast.LENGTH_SHORT).show();
                } else {
                    String key = StaticConfig.mBook.push().getKey();
                    Book book = new Book(key, ten, tacgia, cover_link, link, loai, sosao);
                    StaticConfig.mBook.child(key).setValue(book);
                    if (StaticConfig.mBook.child(key) != null) {
                        finish();
                    }

                }
            }
        });

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sosao = ratingBar.getRating();
                //Toast.makeText(getApplicationContext(), sosao + "", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loai = spinner.getSelectedItem().toString();
                //Toast.makeText(getApplicationContext(), loai, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Management.class));
            }
        });
    }

    private void chooseFile() {
        file_type = "file";
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), StaticConfig.PICK_IMAGE_REQUEST);
    }

    private void chooseImage() {
        file_type = "image";
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), StaticConfig.PICK_IMAGE_REQUEST);
    }

    //gán dữ liệu vào filePath
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == StaticConfig.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                if (file_type.equals("image")) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    cover.setImageBitmap(bitmap);
                    uploadImage();
                }
                if (file_type.equals("file")) {
                    uploadFile();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = StaticConfig.storageReference.child("PDF/" + filePath.getLastPathSegment());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //lấy link đã up lên firebase
                                    Log.d("downloadUrl:", "" + uri);
                                    link = uri.toString();
                                    //StaticConfig.mUser.child(StaticConfig.currentuser).child("pic").setValue(link);
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Add_book.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

//Up hình lên firebase

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = StaticConfig.storageReference.child("Cover/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //lấy link đã up lên firebase
                                    Log.d("downloadUrl:", "" + uri);
                                    cover_link = uri.toString();
                                    //StaticConfig.mUser.child(StaticConfig.currentuser).child("pic").setValue(link);
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Add_book.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    private void setControl() {
        choose_file = findViewById(R.id.choose_file);
        describe = findViewById(R.id.book_describe);
        btnsave = findViewById(R.id.btnsave);
        title = findViewById(R.id.title);
        goback = findViewById(R.id.goback);
        spinner = findViewById(R.id.book_type);
        etauthor = findViewById(R.id.author);
        ettitle = findViewById(R.id.book_title);
        cover = findViewById(R.id.cover);
        ratingBar = findViewById(R.id.rating);

        //gan tieu de
        title.setText("Thêm sách");
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
        StaticConfig.mCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //ngoai tru all
                    if (!ds.child("name").getValue(String.class).equals("All")) {
                        arrayList.add(ds.child("name").getValue(String.class));
                    }
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
}