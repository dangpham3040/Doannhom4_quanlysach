package com.example.doannhom4_quanlythuvien.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.doannhom4_quanlythuvien.R;
import com.example.doannhom4_quanlythuvien.adapter.viewPager_Adapter;
import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.example.doannhom4_quanlythuvien.model.*;
import com.example.doannhom4_quanlythuvien.model.Statisticals;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class Starup extends AppCompatActivity {
    private static int i = 0;
    private BottomNavigationView bottomNavigationView;
    private NavController navigation;
    private ViewPager viewPager;
    private viewPager_Adapter viewPagerAdapter;
    int onStartCount = 0;
    static int Tong = 0;
    static int solan = 0;
    static int soSao = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starup);
        setControl();
        setEvent();
        Tongsao();
    }

    private void setEvent() {
        bottomNavigationView.setItemHorizontalTranslationEnabled(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                    case R.id.fragment_Setting:
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
                        StaticConfig.items = 0;
                        bottomNavigationView.getMenu().findItem(R.id.fragment_home).setChecked(true);
                        break;
                    case 1:
                        StaticConfig.items = 1;
                        bottomNavigationView.getMenu().findItem(R.id.fragment_profile).setChecked(true);
                        break;
                    case 2:
                        StaticConfig.items = 2;
                        bottomNavigationView.getMenu().findItem(R.id.fragment_library).setChecked(true);
                        break;
                    case 3:
                        StaticConfig.items = 3;
                        bottomNavigationView.getMenu().findItem(R.id.fragment_Setting).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(StaticConfig.items);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setControl() {
        //set id của người dùng hiện tại
        StaticConfig.currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom);

        viewPager = findViewById(R.id.view_pager);
        viewPagerAdapter = new viewPager_Adapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

    }


    public void onBackPressed() {
        new AlertDialog.Builder(Starup.this)
                .setTitle("Exit ")
                .setMessage("Are you sure you want to close  the app??")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void Tongsao() {
        StaticConfig.ArrayThongke.clear();
        StaticConfig.mBook.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                i = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Book sach = ds.getValue(Book.class);
                    StaticConfig.mComment.child(sach.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Tong = 0;
                            solan = 1;
                            soSao = 0;
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Comment cm = ds.getValue(Comment.class);
                                Tong += cm.getRating();
                                soSao = Tong / solan;
                                solan++;
                            }
                            if (Tong > 0) {
                                StaticConfig.ArrayThongke.add(new Statisticals(sach.getTitle(), soSao, i));
                            }
                            i++;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            throw error.toException();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
        for (int i = 0; i < 100; i++) {
            Random rnd = new Random();
            int mau = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            StaticConfig.arrayListColer[i] = mau;
        }

    }

}