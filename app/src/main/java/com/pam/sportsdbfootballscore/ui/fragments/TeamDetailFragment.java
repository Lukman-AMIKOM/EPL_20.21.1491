package com.pam.sportsdbfootballscore.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.pam.sportsdbfootballscore.R;
import com.pam.sportsdbfootballscore.adapters.TeamDetailPagerAdapter;
import com.pam.sportsdbfootballscore.helpers.FavoriteSetter;
import com.pam.sportsdbfootballscore.helpers.GlideLoader;
import com.pam.sportsdbfootballscore.model.Match;
import com.pam.sportsdbfootballscore.model.Team;
import com.pam.sportsdbfootballscore.ui.activities.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class TeamDetailFragment extends Fragment {
    
    public static final String ARG_TEAM = "arg_team";
    
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private MaterialToolbar toolbar;
    private FloatingActionButton btnFavorite;
    private ProgressBar pbStadium, pbTeamLogo;
    private ImageView imgStadium, imgTeamLogo;
    private TextView tvTeamName, tvStadium, tvEstablished;
    
    private ViewPager vpTeamDetail;
    private TabLayout tabLayout;
    private TeamDetailOverviewFragment overviewFragment;
    private TeamDetailMatchFragment matchFragment;
    private TeamDetailExtraFragment extraFragment;
    
    private Team team;
    private LinkedHashMap<String, ArrayList<Match>> matchMap;
    private final MainFragment mainFragment;
    
    private ArrayList<Fragment> pages;
    private ArrayList<String> tabTitles;
    
    private boolean isUpcomingReady, isLatestReady;
    
    public TeamDetailFragment(MainFragment mainFragment) {
        this.mainFragment = mainFragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (getArguments() != null) {
            team = getArguments().getParcelable(ARG_TEAM);
        }
        
        setRetainInstance(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_team_detail, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        init(view);
        setToolbar();
        if (pages == null) {
            initTabFragments();
        }
        setTabs();
    }
    
    private void init(View view) {
        appBarLayout = view.findViewById(R.id.app_bar_layout_team_detail);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout_team_detail);
        toolbar = view.findViewById(R.id.toolbar_team_detail);
        btnFavorite = view.findViewById(R.id.btn_favorite);
        pbStadium = view.findViewById(R.id.pb_stadium);
        pbTeamLogo = view.findViewById(R.id.pb_team_logo);
        imgStadium = view.findViewById(R.id.img_stadium);
        imgTeamLogo = view.findViewById(R.id.img_team_logo);
        tvTeamName = view.findViewById(R.id.tv_team_name);
        tvStadium = view.findViewById(R.id.tv_stadium);
        tvEstablished = view.findViewById(R.id.tv_establised);
        vpTeamDetail = view.findViewById(R.id.vp_team_detail);
        tabLayout = view.findViewById(R.id.tab_layout_team_detail);
    }
    
    private void initTabFragments() {
        overviewFragment = new TeamDetailOverviewFragment(team.getDescription(),
                team.getStadiumDescription(), team.getStadiumPreview());
        matchFragment = new TeamDetailMatchFragment(mainFragment);
        extraFragment = new TeamDetailExtraFragment(team);
    
        pages = new ArrayList<>();
        tabTitles = new ArrayList<>();
    
        pages.add(overviewFragment);
        pages.add(matchFragment);
        pages.add(extraFragment);
        tabTitles.add(getString(R.string.overview));
        tabTitles.add(getString(R.string.matches));
        tabTitles.add(getString(R.string.information));
    }
    
    private void setToolbar() {
        collapsingToolbarLayout.setTitle(team.getName());
        toolbar.setTitle("");
        
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> mainActivity.onBackPressed());
            if (mainActivity.getSupportActionBar() != null) {
                mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        
        GlideLoader.loadImage(mainActivity, imgStadium, pbStadium, team.getStadiumPreview(), R.drawable.stadium_placeholder);
        GlideLoader.loadImage(mainActivity, imgTeamLogo, pbTeamLogo, team.getLogo(), R.drawable.ic_menu_teams);
        FavoriteSetter.getInstance(mainActivity).setFavorite(btnFavorite, team.isFavorite());
        btnFavorite.setOnClickListener(v ->
                FavoriteSetter.getInstance(mainActivity).setFavoriteListener(btnFavorite, team, mainFragment));
    
        tvTeamName.setText(team.getName());
        tvStadium.setText(team.getStadium());
        tvEstablished.setText(getString(R.string.established, String.valueOf(team.getFormedYear())));
    }
    
    private void setTabs() {
        vpTeamDetail.setAdapter(new TeamDetailPagerAdapter(getChildFragmentManager(), pages, tabTitles));
        tabLayout.setupWithViewPager(vpTeamDetail);
    }
    
    public void setMatchMap(LinkedHashMap<String, ArrayList<Match>> matchMap) {
        this.matchMap = matchMap;
    }
    
    public void setUpcomingReady(boolean upcomingReady) {
        isUpcomingReady = upcomingReady;
        matchFragment.setMatchMap(matchMap);
    }
    
    public void setLatestReady(boolean latestReady) {
        isLatestReady = latestReady;
        matchFragment.setMatchMap(matchMap);
    }
    
    public void prepare() {
        if (isUpcomingReady && isLatestReady) {
            matchFragment.setMatchMap(matchMap);
        }
    }
}