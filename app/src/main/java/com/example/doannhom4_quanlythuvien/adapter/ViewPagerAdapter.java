package com.example.doannhom4_quanlythuvien.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.doannhom4_quanlythuvien.fragment.Contact_Fragment;
import com.example.doannhom4_quanlythuvien.fragment.Home_Fragment;
import com.example.doannhom4_quanlythuvien.fragment.Library_Fragment;
import com.example.doannhom4_quanlythuvien.fragment.Profile_Fragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Home_Fragment();
            case 1:
                return new Profile_Fragment();
            case 2:
                return new Library_Fragment();
            case 3:
                return new Contact_Fragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}