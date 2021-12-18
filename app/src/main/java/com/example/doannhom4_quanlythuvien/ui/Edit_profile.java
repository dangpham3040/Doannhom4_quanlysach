package com.example.doannhom4_quanlythuvien.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doannhom4_quanlythuvien.MainActivity;
import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.helpers.*;
import com.example.doannhom4_quanlythuvien.model.Book;
import com.example.doannhom4_quanlythuvien.model.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Edit_profile extends AppCompatActivity {
    private TextView changepass;
    private EditText etname, etphone, etemail;
    private RadioButton rbmale, rbfemale;
    private RadioGroup radioGroup;
    private ImageView goback;
    private TextView title;
    private CircleImageView avatar;
    private String url_avatar;
    private String linkanh;
    private Uri filePath;
    private Button save;
    private String name, phone, gioitinh;
    //kiem tra co doi chua
    private Boolean isname = false, isphone = false, islink = false, issex = false;
    private String kname, kphone, klink, kgioitinh;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setControl();
        setEvnet();
    }

    private void setEvnet() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String choose_sex = "";
                if (rbmale.isChecked()) {
                    choose_sex = "Nam";
                }
                if (rbfemale.isChecked()) {
                    choose_sex = "Nữ";
                }
                if (!gioitinh.equals(choose_sex)) {
                    issex = true;
                    kiemtra();
                } else {
                    issex = false;
                    kiemtra();
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Edit_profile.this)
                        .setTitle("Update!!")
                        .setMessage("Are you sure you want to Update??")
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                gán dữ liệu vào firebase
                                StaticConfig.mUser.child(StaticConfig.currentuser).child("name").setValue(etname.getText().toString());
                                StaticConfig.mUser.child(StaticConfig.currentuser).child("phone").setValue(etphone.getText().toString());
                                StaticConfig.mUser.child(StaticConfig.currentuser).child("pic").setValue(url_avatar);
                                if (rbmale.isChecked()) {
                                    StaticConfig.mUser.child(StaticConfig.currentuser).child("sex").setValue("Nam");
                                }
                                if (rbfemale.isChecked()) {
                                    StaticConfig.mUser.child(StaticConfig.currentuser).child("sex").setValue("Nữ");
                                }
                                startActivity(new Intent(getApplicationContext(), Starup.class));
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        etname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                name = etname.getText().toString();
                if (name.isEmpty()) {
                    etname.setError("Tên không được để trống!!");
                    isname = false;
                }
                if (name.equals(kname)) {
                    isname = false;
                    kiemtra();

                } else if (!name.isEmpty() && !name.equals(kname)) {
                    isname = true;
                    kiemtra();
                }
            }
        });
        etphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                phone = etphone.getText().toString();
                if (phone.isEmpty()) {
                    etphone.setError("cần số điện thoại!!");
                    isphone = false;
                }
                if (phone.equals(kphone)) {
                    Log.d("phone", kphone);
                    isphone = false;
                    kiemtra();
                }
                if (phone.length() < 10 || phone.length() > 11) {
                    etphone.setError("số điện thoại tối thiểu 10 số!!");
                    isphone = false;
                    kiemtra();
                } else if (!phone.isEmpty() && !phone.equals(kphone) && phone.length() > 9 && phone.length() < 12) {
                    isphone = true;
                    kiemtra();
                }
            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                new AlertDialog.Builder(Edit_profile.this)
                        .setTitle("Select Option")
                        .setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if (options[item].equals("Take Photo")) {
                                    dialog.dismiss();
                                    chooseImage();
                                    islink = true;
                                    kiemtra();
                                } else if (options[item].equals("Choose From Gallery")) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), Gallery.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("chitiet", linkanh);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                } else if (options[item].equals("Cancel")) {
                                    dialog.dismiss();
                                }
                            }
                        })
                        .show();

            }
        });
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), Starup.class));
            }
        });
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassDialog();
            }
        });
    }

    private void updatePassDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_password);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText current_password = dialog.findViewById(R.id.edit_current_password);
        EditText new_password = dialog.findViewById(R.id.edit_new_password);
        Button change = dialog.findViewById(R.id.Submit);
        ImageButton goback = dialog.findViewById(R.id.goback);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpass = current_password.getText().toString();
                String newpass = new_password.getText().toString();
                if (!oldpass.isEmpty() && !newpass.isEmpty()) {
                    if (newpass.length() < 6) {
                        Toast.makeText(getApplicationContext(), "mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                    } else {
                        Updatepass(oldpass, newpass);
                    }
                }
            }
        });

        dialog.show();
    }

    private void Updatepass(String oldpass, String newpass) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //kiểm tra pass trước khi đổi
        AuthCredential auth = EmailAuthProvider.getCredential(user.getEmail(), oldpass);
        user.reauthenticate(auth)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Thành công
                        user.updatePassword(newpass)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                        StaticConfig.fAuth.signOut();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Thất bại
                        Toast.makeText(getApplicationContext(), "Vui Lòng kiểm tra mật khẩu!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void kiemtra() {
        if (!etname.getText().toString().isEmpty() && !etphone.getText().toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(etemail.getText().toString()).matches()) {
            if (isname == true || isphone == true || islink == true || issex == true) {
                save.setEnabled(true);

            } else {
                save.setEnabled(false);
            }
        } else {
            save.setEnabled(false);
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
    }

    //gán dữ liệu vào filePath
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                avatar.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//Up hình lên firebase

    private void uploadImage() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = StaticConfig.storageReference.child("Avatar/" + StaticConfig.currentuser + "/" + UUID.randomUUID().toString());
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
                                    url_avatar = uri.toString();
                                    //StaticConfig.mUser.child(StaticConfig.currentuser).child("pic").setValue(link);
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Edit_profile.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        avatar = findViewById(R.id.avatar);
        changepass = findViewById(R.id.changepass);
        etname = findViewById(R.id.etname);
        etemail = findViewById(R.id.etemail);
        etphone = findViewById(R.id.etphone);
        rbmale = findViewById(R.id.male);
        rbfemale = findViewById(R.id.female);
        goback = findViewById(R.id.goback);
        save = findViewById(R.id.save);
        radioGroup = findViewById(R.id.radioGroup);
        title = findViewById(R.id.title);
        title.setText("Chỉnh sửa thông tin");
        //load dữ liệu cảu user hiện tại
        Query profile = StaticConfig.mUser.orderByChild("id").equalTo(StaticConfig.currentuser);
        profile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.exists()) {
                        url_avatar = ds.child("pic").getValue(String.class);
                        linkanh=url_avatar;
                        Picasso.get()
                                .load(url_avatar)
                                .fit()
//                                .transform(transformation)
                                .into(avatar);
                        kname = ds.child("name").getValue(String.class);
                        kphone = ds.child("phone").getValue(String.class);
                        kgioitinh = ds.child("sex").getValue(String.class);
                        etemail.setText(ds.child("email").getValue(String.class));
                        klink = url_avatar;
                        etname.setText(kname);
                        etphone.setText(kphone);
                        gioitinh = kgioitinh;
                        if (gioitinh.equals("Nam")) {
                            rbmale.setChecked(true);
                        }
                        if (gioitinh.equals("Nữ")) {
                            rbfemale.setChecked(true);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "fail to load", Toast.LENGTH_SHORT).show();
            }
        });
    }
}