package com.example.portfoliomanagement.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.portfoliomanagement.ContactsFragment;

public class AllInOneActivityPageAdapter extends FragmentStatePagerAdapter {
    int tabCount;
    public AllInOneActivityPageAdapter(@NonNull FragmentManager fm,  int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
               return new ContactsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
