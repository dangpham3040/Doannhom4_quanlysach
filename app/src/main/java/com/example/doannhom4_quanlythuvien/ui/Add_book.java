package com.example.doannhom4_quanlythuvien.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
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

import com.example.doannhom4_quanlythuvien.Notification.NotificationApi;
import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.model.Book;
import com.example.doannhom4_quanlythuvien.model.Notification;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_book extends AppCompatActivity {

    private TextView title, describe;
    private ImageView goback;
    private EditText ettitle, etauthor;
    private ImageView cover;
    private Spinner spinner;
    private String ten, tacgia, loai, link = "", mieuta, cover_link;
    private Uri filePath;
    private Button btnsave;
    private Button choose_file;
    final NotificationApi Api = NotificationApi.retrofit.create(NotificationApi.class);


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
                    String toString = ettitle.getText().toString();
                    //ký tự dầu hoa còn lại thường
                    toString = toString.substring(0, 1).toUpperCase() + toString.substring(1).toLowerCase();
                    //thay thế các khoảng trống thành 1 khoảng trống
                    toString = toString.trim().replaceAll(" +", " ");
                    ettitle.setText(toString);
                }

            }
        });
        etauthor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !etauthor.getText().toString().isEmpty()) {
                    String toString = etauthor.getText().toString();
                    //ký tự dầu hoa còn lại thường
                    toString = toString.substring(0, 1).toUpperCase() + toString.substring(1).toLowerCase();
                    //thay thế các khoảng trống thành 1 khoảng trống
                    toString = toString.trim().replaceAll(" +", " ");
                    etauthor.setText(toString);
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
                        || mieuta.isEmpty() || cover_link.isEmpty() || link.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "vui long nhap du thong tin!!", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(Add_book.this)
                            .setTitle("Add Book!!")
                            .setMessage("Are you sure you want to add this book??")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
//                                gán dữ liệu vào firebase
                                    String key = StaticConfig.mBook.push().getKey();
                                    Book book = new Book(key, ten, tacgia, cover_link, link, loai, mieuta);
                                    StaticConfig.mBook.child(key).setValue(book);
                                    if (StaticConfig.mBook.child(key) != null) {
                                        startActivity(new Intent(getApplicationContext(), Management.class));
                                        Starup.Tongsao();
                                        themThongbao(ten);
                                    }

                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();

            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loai = spinner.getSelectedItem().toString();
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

    private void themThongbao(String ten) {
        Notification notification = new Notification("Sách mới",ten+" vừa được thêm!!");
        Call<List<Notification>> call = Api.addNotificaion(notification);
        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {

            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
            }
        });
    }


    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), 0);
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    //gán dữ liệu vào filePath
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                if (requestCode == 1) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    cover.setImageBitmap(bitmap);
                    uploadImage();
                }
                if (requestCode == 0) {
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
            StorageReference ref = StaticConfig.storageReference.child("PDF/" + UUID.randomUUID().toString());
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
                                    choose_file.setBackgroundColor(Color.parseColor("#2260b6"));
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


        //gan tieu de
        title.setText(R.string.addbook);
        title.setTextSize(25f);
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