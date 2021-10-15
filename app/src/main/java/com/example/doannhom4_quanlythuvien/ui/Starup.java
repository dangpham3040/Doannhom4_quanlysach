package com.example.doannhom4_quanlythuvien.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.AutoText;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.doannhom4_quanlythuvien.MainActivity;
import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.fragment.Contact_Fragment;
import com.example.doannhom4_quanlythuvien.fragment.Home_Fragment;
import com.example.doannhom4_quanlythuvien.fragment.Library_Fragment;
import com.example.doannhom4_quanlythuvien.fragment.Profile_Fragment;
import com.example.doannhom4_quanlythuvien.fragment.SettingsFragment;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.model.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class Starup extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private NavController navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starup);
        setControl();
        setEvent();
    }

    private void setEvent() {

        bottomNavigationView.setItemHorizontalTranslationEnabled(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.fragment_home:
                        fragment = new Home_Fragment();
                        loadFragment(fragment);
                        break;
                    case R.id.fragment_profile:
                        fragment = new Profile_Fragment();
                        loadFragment(fragment);
                        break;
                    case R.id.fragment_contact:
                        fragment = new Contact_Fragment();
                        loadFragment(fragment);
                        break;
                    case R.id.fragment_library:
                        fragment=new Library_Fragment();
                        loadFragment(fragment);
                        break;

                }
                return true;
            }
        });
    }


    private void setControl() {
        //set id của người dùng hiện tại
        StaticConfig.currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom);
        navigation = Navigation.findNavController(this, R.id.fragment);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        if (fragment != null) {
            FrameLayout fl = (FrameLayout) findViewById(R.id.fragment);
            fl.removeAllViews();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment, fragment);
            transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }
    }

    public void onBackPressed() {
        new AlertDialog.Builder(getApplicationContext())
                .setTitle("Thoát ")
                .setMessage("Are you sure you want to close  the app??")
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
    }
}