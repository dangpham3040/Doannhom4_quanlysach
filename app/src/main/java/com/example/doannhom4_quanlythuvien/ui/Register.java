package com.example.doannhom4_quanlythuvien.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.model.Book;
import com.example.doannhom4_quanlythuvien.model.User;
import com.example.doannhom4_quanlythuvien.helpers.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private TextView link_login;
    private RadioButton rbmale, rbfemale;
    private EditText hoTen, Email, passWord, cPassword, Phonenumber;
    private User user;
    private String gioitinh = "Nam";
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setControl();
        setEvent();

    }

    private void validcheck() {
        if (kiemtra() == true) {
            btnRegister.setEnabled(true);
        }
    }

    private void setEvent() {
        hoTen.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !hoTen.getText().toString().isEmpty()) {
                    String name = hoTen.getText().toString();
                    //ký tự dầu hoa còn lại thường
                    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                    //thay thế các khoảng trống thành 1 khoảng trống
                    name = name.trim().replaceAll(" +", " ");
                    hoTen.setText(name);
                }

            }
        });
        hoTen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                valid(hoTen.getText().toString(), hoTen);
                validcheck();
            }
        });
        Phonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                valid(Phonenumber.getText().toString(), Phonenumber);
                if (Phonenumber.getText().toString().length() <= 11 && Phonenumber.getText().toString().length() > 9) {

                } else {
                    btnRegister.setEnabled(false);
                    Phonenumber.setError("Số điênj thoài cần ít nhất 10 số !!");
                }
                validcheck();
            }
        });
        Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Email.getText().toString().isEmpty()) {
                    btnRegister.setEnabled(false);
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches()) {
                    btnRegister.setEnabled(false);
                    Email.setError("Enter Valid Email Address");
                }
                validcheck();
            }

        });
        rbmale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbmale.setChecked(true);
                validcheck();
            }
        });
        rbfemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbfemale.setChecked(true);
                validcheck();
            }
        });
        passWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (passWord.getText().toString().isEmpty()) {
                    btnRegister.setEnabled(false);
                    passWord.setError("mật khẩu cần thiết!!");
                    validcheck();
                }
            }
        });
        cPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cPassword.getText().toString().isEmpty()) {
                    btnRegister.setEnabled(false);
                    cPassword.setError("Cần Nhập lại mặt khẩu!!");
                }
                if (!passWord.getText().toString().equals(cPassword.getText().toString())) {
                    btnRegister.setEnabled(false);
                    cPassword.setError("cần phỉa giống với mật khẩu");
                }
                validcheck();
            }
        });
        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), Login.class));
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString();
                String pass = passWord.getText().toString();
                register(email, pass);
            }
        });

    }

    private Boolean kiemtra() {
        if (hoTen.getText().toString().isEmpty() || Phonenumber.getText().toString().isEmpty() || Email.getText().toString().isEmpty() ||
                passWord.getText().toString().isEmpty() || cPassword.getText().toString().isEmpty()) {
            btnRegister.setEnabled(false);
            return false;
        }
        if (Phonenumber.getText().toString().length() > 11 || Phonenumber.getText().toString().length() < 10) {
            btnRegister.setEnabled(false);
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches()) {
            btnRegister.setEnabled(false);
            return false;
        }
        if (!cPassword.getText().toString().equals(passWord.getText().toString())) {
            return false;
        } else {
            return true;
        }
    }

    private void register(String Email, String Pass) {
        //kiểm tra email đã tồn tại chưa
        StaticConfig.fAuth.fetchSignInMethodsForEmail(Email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                        if (isNewUser) {
                            //Đăng ký
                            StaticConfig.fAuth.createUserWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isComplete()) {
                                        UpdateUI();
                                        Toast.makeText(getApplicationContext(), "dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "email da ton tai", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    private void UpdateUI() {
        if (rbfemale.isChecked()) {
            gioitinh = "Nữ";
        }
        if (rbmale.isChecked()) {
            gioitinh = "Nam";
        }
        //Lấy id user làm key

        user = new User(hoTen.getText().toString(), Phonenumber.getText().toString(),
                Email.getText().toString(), gioitinh, StaticConfig.Default_avatar);
        StaticConfig.mUser.child(StaticConfig.currentuser).setValue(user);
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    private void valid(String toString, EditText text) {
        if (toString.isEmpty()) {
            text.setError("không được để trống !!");
            btnRegister.setEnabled(false);
        }
        Pattern p = Pattern.compile("[^A-Za-z0-9 ]");
        Matcher m = p.matcher(toString);
        // boolean b = m.matches();
        boolean b = m.find();
        if (b == true) {
            text.setError("Không được chứa ký tự đặt biệt!!");
            btnRegister.setEnabled(false);
        }
    }

    private void setControl() {
        hoTen = findViewById(R.id.etHoten);
        Email = findViewById(R.id.etEmail);
        passWord = findViewById(R.id.pass);
        cPassword = findViewById(R.id.cpass);
        Phonenumber = findViewById(R.id.etphone);

        link_login = findViewById(R.id.link_login);
        rbmale = findViewById(R.id.nam);
        rbfemale = findViewById(R.id.nu);
        btnRegister = findViewById(R.id.btnRegister);
    }
}