package com.example.doannhom4_quanlythuvien.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {
    private TextView link_register, forgotpass;
    private Button btnLogin;
    private ProgressBar progressBar;
    private EditText etEmail, etPass;
    int RC_SIGN_IN = 73;
    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;
    ImageView btnGoogleLoginPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setControl();
        setEvent();
    }

    private void setEvent() {
//        // Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogleLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//                startActivityForResult(signInIntent, RC_SIGN_IN);
                Toast.makeText(getApplicationContext(), "Todo", Toast.LENGTH_SHORT).show();
            }
        });
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
                            progressBar.setVisibility(View.VISIBLE);
                            startActivity(new Intent(getApplication(), Starup.class));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            User user1 = new User();
                            user1.setId(user.getUid());
                            user1.setname(user.getDisplayName());
                            user1.setPic(user.getPhotoUrl().toString());
//                            database.getReference().child("Users").child(user.getUid()).setValue(user1);
                            StaticConfig.mUser.child(user.getUid()).setValue(user1);
                            Intent intent = new Intent(getApplicationContext(), Starup.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "Sign in With Google", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
//                            Snackbar.make(binding.getRoot(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });
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
    }
}