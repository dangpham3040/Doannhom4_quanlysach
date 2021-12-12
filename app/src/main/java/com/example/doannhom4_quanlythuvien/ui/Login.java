package com.example.doannhom4_quanlythuvien.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.royrodriguez.transitionbutton.TransitionButton;

public class Login extends AppCompatActivity {
    private TextView link_register, forgotpass;
    private ProgressBar progressBar;
    private EditText etEmail, etPass;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private TransitionButton btnLogin;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private CheckBox cbSaveLogin;
    //    int RC_SIGN_IN = 73;
    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;
    ImageView btnGoogleLoginPage;
    private boolean saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setControl();
        setEvent();
    }

    private void setEvent() {
        cbSaveLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cbSaveLogin.isChecked()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etEmail.getWindowToken(), 0);

                    if (cbSaveLogin.isChecked()) {
                        loginPrefsEditor.putBoolean("saveLogin", true);
                        loginPrefsEditor.putString("username", etEmail.getText().toString());
                        loginPrefsEditor.putString("password", etPass.getText().toString());
                        loginPrefsEditor.commit();
                    } else {
                        loginPrefsEditor.clear();
                        loginPrefsEditor.commit();
                    }
                }
            }

        });
//        // Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = etEmail.getText().toString();
                if (!Email.isEmpty()) {
                    StaticConfig.fAuth.sendPasswordResetEmail(Email);
                    Toast.makeText(Login.this, "kiểm tra hộp thư Email của bạn !!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Login.this, "cần Email để reset password!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        etPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (kiemtra()) {
                    btnLogin.setEnabled(true);
                }

            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (kiemtra()) {
                    btnLogin.setEnabled(true);
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                String Email = etEmail.getText().toString();
                String Pass = etPass.getText().toString();
                StaticConfig.fAuth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginPrefsEditor.putBoolean("saveLogin", true);
                            loginPrefsEditor.putString("username", etEmail.getText().toString());
                            loginPrefsEditor.putString("password", etPass.getText().toString());
                            loginPrefsEditor.commit();
                            animation();
                        } else {
                            Toast.makeText(Login.this, "sai email hoac pass!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

        });
        link_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), Register.class));
            }
        });
    }

    private void animation() {
        btnLogin.startAnimation();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isSuccessful = true;

                // Choose a stop animation if your call was succesful or not
                if (isSuccessful) {
                    btnLogin.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, new TransitionButton.OnAnimationStopEndListener() {
                        @Override
                        public void onAnimationStopEnd() {
                            Intent intent = new Intent(getBaseContext(), Starup.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                        }
                    });
                } else {
                    btnLogin.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                }
            }
        }, 2000);
    }


    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Something error", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean kiemtra() {
        if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError("email is require!!");
            return false;
        } else if (etPass.getText().toString().isEmpty()) {
            etPass.setError("Pass is require!!");
            return false;
        } else if (etPass.getText().toString().length() < 6) {
            etPass.setError("email have 6 characters ");
            return false;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            etEmail.setError("Enter Valid Email Address");
            return false;
        } else {
            return true;
        }
    }

    private void setControl() {
        link_register = findViewById(R.id.link_register);
        forgotpass = findViewById(R.id.forgot);
        btnLogin = findViewById(R.id.btnlogin);
        etEmail = findViewById(R.id.lemail);
        etPass = findViewById(R.id.lpass);
        progressBar = findViewById(R.id.progressBar);
        btnGoogleLoginPage = findViewById(R.id.btnGoogleLoginPage);
        cbSaveLogin = findViewById(R.id.cbSaveLogin);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            etEmail.setText(loginPreferences.getString("username", ""));
            etPass.setText(loginPreferences.getString("password", ""));
            if (kiemtra()) {
                btnLogin.setEnabled(true);
                cbSaveLogin.setChecked(true);
            }
        }
    }
}