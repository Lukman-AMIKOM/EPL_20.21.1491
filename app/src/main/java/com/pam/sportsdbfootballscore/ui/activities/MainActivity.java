package com.pam.sportsdbfootballscore.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.pam.sportsdbfootballscore.R;
import com.pam.sportsdbfootballscore.interfaces.DataListener;
import com.pam.sportsdbfootballscore.ui.fragments.MainFragment;
import com.pam.sportsdbfootballscore.ui.fragments.SplashScreenFragment;

public class MainActivity extends AppCompatActivity implements DataListener {
    
    private static final String KEY_STARTED = "key_started";
    
    private FragmentManager fragmentManager;
    private MainFragment mainFragment;
    private boolean started = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (savedInstanceState != null) {
            started = savedInstanceState.getBoolean(KEY_STARTED);
        }
        
        hideToolbar();
        init();
    }
    
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(KEY_STARTED, started);
        super.onSaveInstanceState(outState);
    }
    
    private void init() {
        fragmentManager = getSupportFragmentManager();
        if (!started) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.main_container, new SplashScreenFragment(), SplashScreenFragment.class.getSimpleName())
                    .commit();
            
            mainFragment = new MainFragment(this, this);
        }
    }
    
    private void hideToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
    
    @Override
    public void onDataReady() {
        try {
            if (started) {
                fragmentManager
                        .beginTransaction()
                        .add(R.id.main_container, mainFragment, MainFragment.class.getSimpleName())
                        .commit();
            } else {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.main_container, mainFragment, MainFragment.class.getSimpleName())
                        .commit();
                started = true;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}