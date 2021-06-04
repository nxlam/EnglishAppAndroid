package com.example.englishapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.englishapp.fragments.GrammarFragment;
import com.example.englishapp.fragments.SettingsFragment;
import com.example.englishapp.fragments.VocaFragment;

public class BottomNavAdapter extends FragmentStatePagerAdapter {
    int pageNum = 3;

    public BottomNavAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Vocabulary";
            case 1: return "Practice";
            default: return "Settings";
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new VocaFragment();
            case 1: return new GrammarFragment();
            default: return new SettingsFragment();
        }
    }

    @Override
    public int getCount() {
        return pageNum;
    }
}
