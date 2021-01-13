package com.pam.sportsdbfootballscore.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.transition.MaterialFadeThrough;
import com.pam.sportsdbfootballscore.R;
import com.pam.sportsdbfootballscore.adapters.MainPagerAdapter;
import com.pam.sportsdbfootballscore.interfaces.TabSelectedListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainContentFragment extends Fragment {
    
    private TabLayout tabLayout;
    private ViewPager vpList;
    
    private final ArrayList<ListFragment> pages;
    private final TabSelectedListener tabSelectedListener;
    private final String fragmentTag;
    
    public MainContentFragment(ArrayList<ListFragment> pages, TabSelectedListener tabSelectedListener, String fragmentTag) {
        this.pages = pages;
        this.tabSelectedListener = tabSelectedListener;
        this.fragmentTag = fragmentTag;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setEnterTransition(new MaterialFadeThrough().setDuration(200));
        setExitTransition(new MaterialFadeThrough().setDuration(200));
        
        setRetainInstance(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_content, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        init(view);
        setTabs();
    }
    
    private void init(View view) {
        tabLayout = view.findViewById(R.id.tab_layout_list);
        vpList = view.findViewById(R.id.vp_list);
    }
    
    private void setTabs() {
        vpList.setAdapter(new MainPagerAdapter(getChildFragmentManager(), pages));
        tabLayout.setupWithViewPager(vpList);
        
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
            ImageView imgTabIcon = view.findViewById(R.id.img_tab_icon);
            TextView tvTabTitle = view.findViewById(R.id.tv_tab_title);
            
            imgTabIcon.setImageDrawable(pages.get(i).getTitleIcon());
            tvTabTitle.setText(pages.get(i).getTitle());
            
            Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(view);
        }
        
        TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabSelectedListener.onTabSelected(MainContentFragment.this, getSelectedTabFragment());
            }
            
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        };
        
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
        
        if (getSelectedTabFragment() != null && getSelectedTabFragment().getListType().equals(ListFragment.TYPE_TEAMS)) {
            tabSelectedListener.onTabSelected(this, getSelectedTabFragment());
        }
    }
    
    private int getSelectedTabPosition() {
        return tabLayout.getSelectedTabPosition();
    }
    
    private ListFragment getSelectedTabFragment() {
        int tabIndex = getSelectedTabPosition();
        return tabIndex != -1 ? getSelectedTabFragment(tabIndex) : null;
    }
    
    private ListFragment getSelectedTabFragment(int tabIndex) {
        return pages.get(tabIndex);
    }
    
    public String getFragmentTag() {
        return fragmentTag;
    }
}