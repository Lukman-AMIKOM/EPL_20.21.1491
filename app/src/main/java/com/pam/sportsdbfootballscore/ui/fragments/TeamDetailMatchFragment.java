package com.pam.sportsdbfootballscore.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.pam.sportsdbfootballscore.R;
import com.pam.sportsdbfootballscore.adapters.DateGroupAdapter;
import com.pam.sportsdbfootballscore.model.Match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class TeamDetailMatchFragment extends Fragment {
    
    private ProgressBar pbMatches;
    private RecyclerView rvMatches;
    private LinkedHashMap<String, ArrayList<Match>> matchMap;
    
    private final MainFragment mainFragment;
    
    public TeamDetailMatchFragment(MainFragment mainFragment) {
        this.mainFragment = mainFragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setRetainInstance(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_team_detail_match, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        init(view);
        
        if (matchMap != null) {
            setMatchList();
        }
    }
    
    private void init(View view) {
        pbMatches = view.findViewById(R.id.pb_matches);
        pbMatches.setVisibility(View.VISIBLE);
        rvMatches = view.findViewById(R.id.rv_matches);
        rvMatches.setHasFixedSize(true);
    }
    
    private void setMatchList() {
        if (matchMap.size() > 1) {
            ArrayList<String> keys = new ArrayList<>(matchMap.keySet());
            Collections.sort(keys, (o1, o2) -> Integer.compare(0, o1.compareTo(o2)));
            
            LinkedHashMap<String, ArrayList<Match>> copy = new LinkedHashMap<>(matchMap);
            matchMap = new LinkedHashMap<>();
            for (String key : keys) {
                matchMap.put(key, copy.get(key));
            }
        }
        rvMatches.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMatches.setAdapter(new DateGroupAdapter(matchMap, mainFragment, DateGroupAdapter.TEAM_DETAIL_FRAGMENT));
        pbMatches.setVisibility(View.GONE);
    }
    
    public void setMatchMap(LinkedHashMap<String, ArrayList<Match>> matchMap) {
        this.matchMap = matchMap;
        setMatchList();
    }
}