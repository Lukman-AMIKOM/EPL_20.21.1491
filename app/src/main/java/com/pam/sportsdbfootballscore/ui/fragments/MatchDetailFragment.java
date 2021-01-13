package com.pam.sportsdbfootballscore.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.pam.sportsdbfootballscore.R;
import com.pam.sportsdbfootballscore.adapters.MatchDetailsAdapter;
import com.pam.sportsdbfootballscore.helpers.FavoriteSetter;
import com.pam.sportsdbfootballscore.interfaces.FavoriteClickListener;
import com.pam.sportsdbfootballscore.interfaces.TeamClickListener;
import com.pam.sportsdbfootballscore.model.Match;
import com.pam.sportsdbfootballscore.model.Team;
import com.pam.sportsdbfootballscore.ui.activities.MainActivity;
import com.pam.sportsdbfootballscore.helpers.GlideLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MatchDetailFragment extends Fragment {
    
    public static final String ARG_MATCH = "arg_match";
    
    private Match match;
    private final FavoriteClickListener favoriteClickListener;
    private final TeamClickListener teamClickListener;
    
    private AppBarLayout appBarLayout;
    private MaterialToolbar toolbar;
    private TextView tvMatchTitle;
    private ImageView imgFavorite;
    
    private ProgressBar pbStadium, pbHomeTeam, pbAwayTeam, pbMatchThumbnail;
    private ImageView imgStadium, imgMatchThumbnail, imgHomeTeam, imgAwayTeam;
    private TextView tvMatchDate, tvStadium, tvHomeTeam, tvAwayTeam,
            tvHomeScore, tvAwayScore, tvLeague;
    private RecyclerView rvMatchDetails;
    
    private ArrayList<String> titles, contents;
    
    private boolean isExpanded = true;
    
    public MatchDetailFragment(MainFragment mainFragment) {
        favoriteClickListener = mainFragment;
        teamClickListener = mainFragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (getArguments() != null) {
            match = getArguments().getParcelable(ARG_MATCH);
        }
        
        setRetainInstance(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_match_detail, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        init(view);
        setToolbar();
        setMainContent();
        
        appBarLayout.setExpanded(isExpanded);
    }
    
    private void init(View view) {
        // Toolbar
        appBarLayout = view.findViewById(R.id.app_bar_layout_match_detail);
        toolbar = view.findViewById(R.id.toolbar_match_detail);
        tvMatchTitle = view.findViewById(R.id.tv_detail_title);
        imgFavorite = view.findViewById(R.id.img_favorite);
        
        // Main Content
        // Date & Venue Panel
        pbStadium = view.findViewById(R.id.pb_stadium);
        imgStadium = view.findViewById(R.id.img_stadium);
        tvMatchDate = view.findViewById(R.id.tv_match_date);
        tvStadium = view.findViewById(R.id.tv_stadium);
        // Score Panel
        pbHomeTeam = view.findViewById(R.id.pb_home_team);
        pbAwayTeam = view.findViewById(R.id.pb_away_team);
        imgHomeTeam = view.findViewById(R.id.img_home_team);
        imgAwayTeam = view.findViewById(R.id.img_away_team);
        tvHomeTeam = view.findViewById(R.id.tv_home_team);
        tvAwayTeam = view.findViewById(R.id.tv_away_team);
        tvHomeScore = view.findViewById(R.id.tv_home_score);
        tvAwayScore = view.findViewById(R.id.tv_away_score);
        tvLeague = view.findViewById(R.id.tv_league);
        rvMatchDetails = view.findViewById(R.id.rv_match_details);
        pbMatchThumbnail = view.findViewById(R.id.pb_thumbnail);
        imgMatchThumbnail = view.findViewById(R.id.img_match_thumbnail);
    }
    
    private void setToolbar() {
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            int expanded = Math.abs(verticalOffset);
            int height = appBarLayout.getHeight();
            isExpanded = expanded < height;
        });
        
        tvMatchTitle.setText(match.getEvent());
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> mainActivity.onBackPressed());
            if (mainActivity.getSupportActionBar() != null) {
                mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        
        setFavorite();
    }
    
    private void setMainContent() {
        Team homeTeam = match.getHomeTeam();
        Team awayTeam = match.getAwayTeam();
        Context context = getContext();
        
        // Date & Venue Panel
        GlideLoader.loadImage(context, imgStadium, pbStadium, homeTeam.getStadiumPreview(), R.drawable.stadium_placeholder);
        tvMatchDate.setText(getFormattedMatchDate(match.getEventDate()));
        tvStadium.setText(match.getStadium());
    
        // Score Panel
        GlideLoader.loadImage(context, imgHomeTeam, pbHomeTeam, homeTeam.getLogo(), R.drawable.ic_tab_favorite_teams_night);
        GlideLoader.loadImage(context, imgAwayTeam, pbAwayTeam, awayTeam.getLogo(), R.drawable.ic_tab_favorite_teams_night);
        imgHomeTeam.setOnClickListener(v -> teamClickListener.onTeamClicked(homeTeam));
        imgAwayTeam.setOnClickListener(v -> teamClickListener.onTeamClicked(awayTeam));
        
        tvHomeTeam.setText(homeTeam.getName());
        tvAwayTeam.setText(awayTeam.getName());
        tvHomeScore.setText(getScore(match.getHomeScore()));
        tvAwayScore.setText(getScore(match.getAwayScore()));
        
        // Details
        tvLeague.setText(match.getLeague().getName());
        if (match.getThumbnail() != null &&
                !match.getThumbnail().isEmpty() &&
                !match.getThumbnail().equalsIgnoreCase("null")) {
            imgMatchThumbnail.setVisibility(View.VISIBLE);
            GlideLoader.loadImage(context, imgMatchThumbnail, pbMatchThumbnail, match.getThumbnail(), R.drawable.bg_main_tab);
        }
        setDetails();
    }
    
    private void setFavorite() {
        FavoriteSetter.getInstance(getContext()).setFavorite(imgFavorite, match.isFavorite());
        
        imgFavorite.setOnClickListener(v ->
                FavoriteSetter.getInstance(getContext()).setFavoriteListener(imgFavorite, match, favoriteClickListener));
    }
    
    private void setDetails() {
        rvMatchDetails.setHasFixedSize(true);
        rvMatchDetails.setLayoutManager(new LinearLayoutManager(getContext()));
    
        titles = new ArrayList<>();
        contents = new ArrayList<>();
    
        addDetail(getString(R.string.season_title), match.getSeason());
        addDetail(getString(R.string.matchweek_title), String.valueOf(match.getMatchWeek()));
        addDetail(getString(R.string.time_title), match.getTime().substring(0, match.getTime().lastIndexOf(":")));
        addDetail(getString(R.string.spectators_title), match.getSpectators() == -1 ? "—" : String.valueOf(match.getSpectators()));
        addDetail(getString(R.string.status_title), match.getStatus());
        
        rvMatchDetails.setAdapter(new MatchDetailsAdapter(titles, contents));
    }
    
    private String getFormattedMatchDate(String matchDate) {
        SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
    
        try {
            Date date = sdfSource.parse(matchDate);
            if (date != null) {
                return sdf.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    private String getScore(int score) {
        return score == -1 ? null : String.valueOf(score);
    }
    
    private void addDetail(String title, String content) {
        titles.add(title);
        contents.add(content);
    }
}