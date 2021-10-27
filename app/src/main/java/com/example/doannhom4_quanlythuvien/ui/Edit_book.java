package com.example.doannhom4_quanlythuvien.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doannhom4_quanlythuvien.MainActivity;
import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.model.Book;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Edit_book extends AppCompatActivity {
    private TextView title;
    private ImageView goback;
    private EditText book_title, author;
    private Spinner spinner;
    private ImageView cover;
    private Button btnsave, btnremove, btnchoose_file;
    private String book_id;
    private String coverPhotoURL;
    private String file_type;
    private Uri filePath;
    private String link;
    private int solan = 0;
    private boolean is_title = false, is_book_id = false, is_author = false, is_type = false,
            is_coverPhotoURL = false, is_link = false;
    private Book chitiet;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);
        setControl();
        setEvnet();
    }

    private void setEvnet() {
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        btnchoose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (solan == 0) {
                    spinner.setSelection(arrayAdapter.getPosition(chitiet.getType()), true); //Add this line before setting listener
                }
                solan++;
                kiemtra();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Viết hoa
        book_title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !book_title.getText().toString().isEmpty()) {
                    String name = book_title.getText().toString();
                    //ký tự dầu hoa còn lại thường
                    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                    //thay thế các khoảng trống thành 1 khoảng trống
                    name = name.trim().replaceAll(" +", " ");
                    book_title.setText(name);
                }

            }
        });
        author.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !author.getText().toString().isEmpty()) {
                    String name = author.getText().toString();
                    //ký tự dầu hoa còn lại thường
                    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                    //author thế các khoảng trống thành 1 khoảng trống
                    name = name.trim().replaceAll(" +", " ");
                    author.setText(name);
                }

            }
        });
        book_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (book_title.getText().toString().isEmpty()) {
                    book_title.setError("Không đc để  trống");
                    is_title = false;
                }
                if (book_title.getText().toString().equals(chitiet.getTitle())) {
                    is_title = false;
                } else if (!book_title.getText().toString().isEmpty() && !book_title.getText().toString().equals(chitiet.getTitle())) {
                    is_title = true;
                }
                kiemtra();

            }
        });
        author.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (author.getText().toString().isEmpty()) {
                    author.setError("Không đc để  trống");
                    is_author = false;
                }
                if (author.getText().toString().equals(chitiet.getTitle())) {
                    is_author = false;
                } else if (!author.getText().toString().isEmpty() && !author.getText().toString().equals(chitiet.getAuthor())) {
                    is_author = true;
                }
                kiemtra();
            }
        });
        btnremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Edit_book.this)
                        .setTitle("Delete!!")
                        .setMessage("Are you sure you want to Delete??")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                StaticConfig.mBook.child(book_id).removeValue();
                                StaticConfig.mComment.child(book_id).removeValue();
                                startActivity(new Intent(getApplicationContext(), Management.class));
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Edit_book.this)
                        .setTitle("Update!!")
                        .setMessage("Are you sure you want to Update??")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                gán dữ liệu vào firebase
                                Book book = new Book(book_id, book_title.getText().toString(), author.getText().toString(),
                                        coverPhotoURL, link, spinner.getSelectedItem().toString());
                                StaticConfig.mBook.child(book_id).setValue(book)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        startActivity(new Intent(getApplicationContext(), Management.class));
                                    }

                                });

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
            public void onClick(View v) {
                if (btnsave.isEnabled()) {
                    new AlertDialog.Builder(Edit_book.this)
                            .setTitle("Go back!!")
                            .setMessage("Are you sure you want to Go back??")
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    finish();
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                } else {
                    finish();
                }

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
                                    is_link = true;
                                    kiemtra();
                                    //StaticConfig.mUser.child(StaticConfig.currentuser).child("pic").setValue(link);
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Edit_book.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                    coverPhotoURL = uri.toString();
                                    is_coverPhotoURL = true;
                                    kiemtra();
                                    //StaticConfig.mUser.child(StaticConfig.currentuser).child("pic").setValue(link);
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Edit_book.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        chitiet = (Book) getIntent().getSerializableExtra("chitiet");
        title = findViewById(R.id.title);
        title.setText(chitiet.getTitle());
        goback = findViewById(R.id.goback);
        book_title = findViewById(R.id.book_title);
        author = findViewById(R.id.author);
        spinner = findViewById(R.id.book_type);
        cover = findViewById(R.id.cover);
        btnremove = findViewById(R.id.btnremove);
        btnsave = findViewById(R.id.btnsave);
        btnchoose_file = findViewById(R.id.choose_file);


        link = chitiet.getLink();
        coverPhotoURL = chitiet.getCoverPhotoURL();
        book_id = chitiet.getId();
        book_title.setText(chitiet.getTitle());
        author.setText(chitiet.getAuthor());

        Picasso.get()
                .load(chitiet.getCoverPhotoURL())
                .fit()
                .placeholder(R.drawable.no_image)
//                                .transform(transformation)
                .into(cover);
        //then laoi

        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
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
        // spinner.setSelection(arrayAdapter.getPosition(chitiet.getType()));
        spinner.setAdapter(arrayAdapter);

    }

    private void kiemtra() {
        if (is_title == true || is_author == true || !spinner.getSelectedItem().toString().equals(chitiet.getType())
                || is_coverPhotoURL == true || is_link == true ) {
            btnsave.setEnabled(true);
        } else {
            btnsave.setEnabled(false);
        }
    }
}