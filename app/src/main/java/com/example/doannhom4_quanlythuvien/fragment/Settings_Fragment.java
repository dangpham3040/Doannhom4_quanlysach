package com.example.doannhom4_quanlythuvien.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.example.doannhom4_quanlythuvien.MainActivity;

import android.content.SharedPreferences;


import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.helpers.Language;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.ui.Contact;
import com.example.doannhom4_quanlythuvien.ui.Edit_profile;
import com.example.doannhom4_quanlythuvien.ui.Starup;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class Settings_Fragment extends PreferenceFragmentCompat {


    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals("logout")) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to sign out of the app??")
                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            StaticConfig.fAuth.signOut();
                            startActivity(new Intent(getContext(), MainActivity.class));
                        }
                    })
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;
        }
        if (key.equals("EditProfile")) {
            startActivity(new Intent(getContext(), Edit_profile.class));
            return true;
        }
        if (key.equals("Changepass")) {
            updatePassDialog();
            return true;
        }
        if (key.equals("Contact")) {
            startActivity(new Intent(getContext(), Contact.class));
            return true;
        }
        if (key.equals("language")) {
            String Ngonngu = "";
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = settings.edit();
            if (preference.getTitle().equals("English")) {
                Ngonngu = "vi";
            } else {
                Ngonngu = "";
            }
            setLocale(getActivity(), Ngonngu);
            editor.putString("ngonngu", Ngonngu);
            editor.apply();
            startActivity(new Intent(getContext(), Starup.class));
            return true;
        }
        if (key.equals("about")) {
            Toast.makeText(getContext(), preference.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

    private void updatePassDialog() {
        final Dialog dialog = new Dialog(getContext());
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
                        Toast.makeText(getContext(), "mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(getContext(), "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                        StaticConfig.fAuth.signOut();
                                        startActivity(new Intent(getContext(), MainActivity.class));
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Thất bại
                        Toast.makeText(getContext(), "Vui Lòng kiểm tra mật khẩu!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}