package com.pam.sportsdbfootballscore.interfaces;

import com.pam.sportsdbfootballscore.ui.fragments.ListFragment;
import com.pam.sportsdbfootballscore.ui.fragments.MainContentFragment;

public interface TabSelectedListener {
    void onTabSelected(MainContentFragment mainContentFragment, ListFragment selectedFragment);
}
