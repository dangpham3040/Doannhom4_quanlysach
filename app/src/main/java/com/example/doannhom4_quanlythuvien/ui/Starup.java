package com.example.doannhom4_quanlythuvien.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Starup extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private NavController navigation;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    int onStartCount = 0;
    int Tong = 0;
    int solan = 0;
    int soSao = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starup);
        setControl();
        setEvent();
        Tongsao();
//        onStartCount = 1;
//        if (savedInstanceState == null) // 1st time
//        {
//            this.overridePendingTransition(R.anim.anim_slide_in_left,
//                    R.anim.anim_slide_out_left);
//        } else // already created so reverse animation
//        {
//            onStartCount = 2;
//        }
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
        viewPager.setCurrentItem(StaticConfig.items);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setControl() {
        //set id của người dùng hiện tại
        StaticConfig.currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom);

        viewPager = findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        //test thong bao
//        thongbao();
    }


    public void onBackPressed() {
        new AlertDialog.Builder(Starup.this)
                .setTitle("Exit ")
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

    public void Tongsao() {
        StaticConfig.ArrayThongke.clear();
        StaticConfig.mBook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Book sach = ds.getValue(Book.class);
                    StaticConfig.mComment.child(sach.getId()).addValueEventListener(new ValueEventListener() {
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
                            StaticConfig.ArrayThongke.add(new Thongke(sach.getTitle(), soSao));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//    //test thong bao
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public void thongbao(){
//        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "Thong bao")
//                .setSmallIcon(R.drawable.ibrary)
//                .setContentTitle("Quản lý sách")
//                .setContentText("test thồng báo");
//        NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        manager.notify(0,notification.build());
//    }

//    @Override
//    protected void onStart() {
//        // TODO Auto-generated method stub
//        super.onStart();
//        if (onStartCount > 1) {
//            this.overridePendingTransition(R.anim.anim_slide_in_right,
//                    R.anim.anim_slide_out_right);
//
//        } else if (onStartCount == 1) {
//            onStartCount++;
//        }
//
//    }
}