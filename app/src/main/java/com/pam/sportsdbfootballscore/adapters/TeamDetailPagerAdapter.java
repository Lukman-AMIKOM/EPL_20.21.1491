package com.pam.sportsdbfootballscore.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class TeamDetailPagerAdapter extends FragmentPagerAdapter {
    
    private final ArrayList<Fragment> pages;
    private final ArrayList<String> tabTitles;
    
    public TeamDetailPagerAdapter(@NonNull FragmentManager fm, ArrayList<Fragment> pages, ArrayList<String> tabTitles) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.pages = pages;
        this.tabTitles = tabTitles;
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
    
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}
