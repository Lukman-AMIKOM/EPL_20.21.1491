package com.pam.sportsdbfootballscore.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pam.sportsdbfootballscore.ui.fragments.ListFragment;

import java.util.ArrayList;

public class MainPagerAdapter extends FragmentPagerAdapter {
    
    private final ArrayList<ListFragment> pages;
    
    public MainPagerAdapter(@NonNull FragmentManager fm, ArrayList<ListFragment> pages) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.pages = pages;
    }
    
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }
    
    @Override
    public int getCount() {
        return pages.size();
    }
}
