package com.example.doannhom4_quanlythuvien.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.AutoText;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.doannhom4_quanlythuvien.MainActivity;
import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.adapter.ViewPagerAdapter;
import com.example.doannhom4_quanlythuvien.fragment.Contact_Fragment;
import com.example.doannhom4_quanlythuvien.fragment.*;
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
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

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
                        viewPager.setCurrentItem(0);
                        break;

                    case R.id.fragment_profile:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.fragment_library:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.fragment_contact:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.fragment_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.fragment_profile).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.fragment_library).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.fragment_contact).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//
//        switch (menuItem.getItemId()) {
//            case R.id.fragment_home:
//                viewPager.setCurrentItem(0);
//                break;
//
//            case R.id.fragment_profile:
//                viewPager.setCurrentItem(1);
//                break;
//            case R.id.fragment_contact:
//                viewPager.setCurrentItem(2);
//                break;
//            case R.id.fragment_library:
//                viewPager.setCurrentItem(3);
//                break;
//        }
//        return true;
//    }
    private void setControl() {
        //set id của người dùng hiện tại
        StaticConfig.currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom);

        viewPager = findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
    }
//
//    private void loadFragment(Fragment fragment) {
//        // load fragment
//        if (fragment != null) {
//            FrameLayout fl = (FrameLayout) findViewById(R.id.fragment);
//            fl.removeAllViews();
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment, fragment);
//            transaction.addToBackStack(null);
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            transaction.commit();
//        }
//    }

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