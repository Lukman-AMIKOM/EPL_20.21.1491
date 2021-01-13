package com.pam.sportsdbfootballscore.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pam.sportsdbfootballscore.R;
import com.pam.sportsdbfootballscore.helpers.GlideLoader;

public class TeamDetailOverviewFragment extends Fragment {
    
    private final String teamDescription, stadiumDescription, stadiumUrl;
    
    public TeamDetailOverviewFragment(String teamDescription, String stadiumDescription, String stadiumUrl) {
        this.teamDescription = teamDescription;
        this.stadiumDescription = stadiumDescription;
        this.stadiumUrl = stadiumUrl;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setRetainInstance(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_team_detail_overview, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    
        TextView tvDescription = view.findViewById(R.id.tv_team_description);
        TextView tvStadiumDescription = view.findViewById(R.id.tv_stadium_description);
        ProgressBar pbStadium = view.findViewById(R.id.pb_stadium);
        ImageView imgStadium = view.findViewById(R.id.img_stadium);
        
        tvDescription.setText(teamDescription);
        tvStadiumDescription.setText(stadiumDescription);
        GlideLoader.loadImage(getContext(), imgStadium, pbStadium, stadiumUrl, R.drawable.stadium_placeholder);
    }
}