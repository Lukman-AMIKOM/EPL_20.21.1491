package com.pam.sportsdbfootballscore.ui.fragments;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.appbar.AppBarLayout;
import com.pam.sportsdbfootballscore.R;
import com.pam.sportsdbfootballscore.adapters.DateGroupAdapter;
import com.pam.sportsdbfootballscore.adapters.MatchAdapter;
import com.pam.sportsdbfootballscore.adapters.MatchComparator;
import com.pam.sportsdbfootballscore.adapters.TableAdapter;
import com.pam.sportsdbfootballscore.adapters.TeamAdapter;
import com.pam.sportsdbfootballscore.adapters.TeamComparator;
import com.pam.sportsdbfootballscore.model.Match;
import com.pam.sportsdbfootballscore.model.Table;
import com.pam.sportsdbfootballscore.model.Team;
import com.pam.sportsdbfootballscore.ui.custom.CustomSpinnerAdapter;
import com.pam.sportsdbfootballscore.ui.custom.MySpinner;
import com.pam.sportsdbfootballscore.ui.custom.ResponsiveGridLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class ListFragment extends Fragment {
    
    public static final String TYPE_RECENT_MATCHES = "type_recent_matches";
    public static final String TYPE_UPCOMING_MATCHES = "type_upcoming_matches";
    public static final String TYPE_TEAMS = "type_teams";
    public static final String TYPE_FAVORITE_MATCHES = "type_favorite_matches";
    public static final String TYPE_FAVORITE_TEAMS = "type_favorite_teams";
    public static final String TYPE_LEAGUE_TABLE = "type_league_table";
    
    private final String title;
    private final String listType;
    private final MainFragment mainFragment;
    
    private Drawable titleIcon;
    
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    
    private LinkedHashMap<String, ArrayList<Match>> matchMap;
    private ArrayList<Match> favoriteMatchList;
    private ArrayList<Team> teamList;
    private ArrayList<Table> leagueTable;
    
    private MySpinner spinnerSorter;
    private ImageView imgOrderingMode, imgViewMode;
    private TeamComparator teamComparator;
    private TeamAdapter teamAdapter;
    
    private String sorterName, sorterEstablishedYear;
    private String selectedSorter;
    private String selectedViewMode = TeamAdapter.MODE_LIST;
    private boolean isListAscending = true;
    
    private Drawable listIcon, gridIcon;
    private Drawable ascendingIcon, descendingIcon;
    
    private boolean loading;
    
    public ListFragment(MainFragment mainFragment, String title, String listType) {
        this.mainFragment = mainFragment;
        this.title = title;
        this.listType = listType;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setRetainInstance(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        init(view);
    }
    
    private void init(View view) {
        AppBarLayout appBarLayout = view.findViewById(R.id.app_bar_layout_list);
        if (!listType.contains("teams")) {
            appBarLayout.setVisibility(View.GONE);
        }
        progressBar = view.findViewById(R.id.pb_list_main);
        progressBar.setVisibility(View.VISIBLE);
        
        recyclerView = view.findViewById(R.id.rv_main);
        recyclerView.setHasFixedSize(true);
        
        if (listType.contains("teams")) {
            spinnerSorter = view.findViewById(R.id.spinner_sorter);
            imgOrderingMode = view.findViewById(R.id.img_ordering_mode);
            imgViewMode = view.findViewById(R.id.img_view_mode);
            
            initIcons();
            initSorter();
            initOrderingMode();
            initViewMode();
        }
        
        if (listType.equals(TYPE_RECENT_MATCHES) ||
                (listType.equals(TYPE_UPCOMING_MATCHES) && !matchMap.isEmpty()) ||
                listType.equals(TYPE_FAVORITE_MATCHES) ||
                (listType.equals(TYPE_TEAMS) && !teamList.isEmpty()) ||
                (listType.equals(TYPE_LEAGUE_TABLE) && !leagueTable.isEmpty()) ||
                listType.contains("favorite")) {
            setAdapter();
            progressBar.setVisibility(View.GONE);
        }
    }
    
    private void initIcons() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            listIcon = getIconDrawable(R.drawable.ic_mode_list_night);
            gridIcon = getIconDrawable(R.drawable.ic_mode_grid_night);
            
            ascendingIcon = getIconDrawable(R.drawable.ic_sort_ascending_night);
            descendingIcon = getIconDrawable(R.drawable.ic_sort_descending_night);
        } else {
            listIcon = getIconDrawable(R.drawable.ic_mode_list);
            gridIcon = getIconDrawable(R.drawable.ic_mode_grid);
            
            ascendingIcon = getIconDrawable(R.drawable.ic_sort_ascending);
            descendingIcon = getIconDrawable(R.drawable.ic_sort_descending);
        }
    }
    
    private void initSorter() {
        sorterName = getString(R.string.name);
        sorterEstablishedYear = getString(R.string.established_year);
        selectedSorter = sorterName;
        
        List<String> sorterList = Arrays.asList(getResources().getStringArray(R.array.teams_sorter));
        CustomSpinnerAdapter spinnerAdapter = new CustomSpinnerAdapter(Objects.requireNonNull(getContext()),
                android.R.layout.simple_spinner_item, sorterList);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item_row);
        
        spinnerSorter.setAdapter(spinnerAdapter);
        spinnerSorter.setBackground(ContextCompat.getDrawable(getContext(), android.R.color.transparent));
        spinnerSorter.setSelection(sorterList.indexOf(selectedSorter), false);
        spinnerSorter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spinnerSorter.getSelectedItem().toString().equals(selectedSorter)) {
                    selectedSorter = spinnerSorter.getSelectedItem().toString();
                    changeSorter();
                }
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    
    private void initOrderingMode() {
        imgOrderingMode.setImageDrawable(isListAscending ? ascendingIcon : descendingIcon);
        setComparator(new TeamComparator(getComparator()));
        
        imgOrderingMode.setOnClickListener(v -> {
            isListAscending = !isListAscending;
            setOrderingMode();
        });
    }
    
    private void initViewMode() {
        setViewMode();
        imgViewMode.setOnClickListener(v -> {
            selectedViewMode = selectedViewMode.equals(TeamAdapter.MODE_LIST) ? TeamAdapter.MODE_GRID : TeamAdapter.MODE_LIST;
            setViewMode();
            setAdapter();
        });
    }
    
    private void setOrderingMode() {
        imgOrderingMode.setImageDrawable(isListAscending ? ascendingIcon : descendingIcon);
        
        changeSorter();
    }
    
    private void setViewMode() {
        switch (selectedViewMode) {
            case TeamAdapter.MODE_LIST:
                imgViewMode.setImageDrawable(gridIcon);
                break;
            case TeamAdapter.MODE_GRID:
                imgViewMode.setImageDrawable(listIcon);
                break;
        }
    }
    
    private void changeSorter() {
        setComparator(new TeamComparator(getComparator()));
        setAdapter();
    }
    
    private void setComparator(TeamComparator comparator) {
        this.teamComparator = comparator;
    }
    
    private Drawable getIconDrawable(int resId) {
        return ContextCompat.getDrawable(Objects.requireNonNull(getContext()), resId);
    }
    
    private String getComparator() {
        if (selectedSorter.equals(sorterName)) {
            return isListAscending ? TeamComparator.NAME_ASCENDING : TeamComparator.NAME_DESCENDING;
        } else if (selectedSorter.equals(sorterEstablishedYear)) {
            return isListAscending ? TeamComparator.AGE_ASCENDING : TeamComparator.AGE_DESCENDING;
        }
        
        return TeamComparator.NAME_ASCENDING;
    }
    
    public void setAdapter() {
        switch (listType) {
            case TYPE_RECENT_MATCHES:
            case TYPE_UPCOMING_MATCHES:
                setMatchAdapter();
                break;
            case TYPE_FAVORITE_MATCHES:
                recyclerView.setPadding(0, 7, 0, 7);
                setMatchAdapter();
                break;
            case TYPE_TEAMS:
            case TYPE_FAVORITE_TEAMS:
                recyclerView.setPadding(0, 7, 0, 7);
                setTeamAdapter();
                break;
            case TYPE_LEAGUE_TABLE:
                setTableAdapter();
                break;
        }
    }
    
    private void setMatchAdapter() {
        recyclerView.setLayoutManager(getLayoutManager());
        
        if (listType.equals(TYPE_FAVORITE_MATCHES)) {
            recyclerView.setAdapter(new MatchAdapter(favoriteMatchList, mainFragment));
        } else {
            if (matchMap != null && !matchMap.isEmpty()) {
                if (listType.equals(TYPE_UPCOMING_MATCHES)) {
                    for (ArrayList<Match> matches : matchMap.values()) {
                        Collections.sort(matches, new MatchComparator(MatchComparator.TIME_ASCENDING));
                    }
                }
                recyclerView.setAdapter(new DateGroupAdapter(matchMap, mainFragment, DateGroupAdapter.LIST_FRAGMENT));
                progressBar.setVisibility(View.GONE);
            }
        }
    }
    
    private void setTeamAdapter() {
        teamAdapter = new TeamAdapter(selectedViewMode, mainFragment, teamComparator);
        teamAdapter.add(teamList);
        
        recyclerView.setLayoutManager(getLayoutManager());
        recyclerView.setAdapter(teamAdapter);
        progressBar.setVisibility(View.GONE);
    }
    
    private void setTableAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new TableAdapter(leagueTable, mainFragment));
        progressBar.setVisibility(View.GONE);
    }
    
    private RecyclerView.LayoutManager getLayoutManager() {
        if (selectedViewMode.equals(TeamAdapter.MODE_LIST)) {
            return new LinearLayoutManager(getContext());
        } else {
            return new ResponsiveGridLayoutManager(getContext(),
                    GridLayoutManager.VERTICAL, false, TypedValue.COMPLEX_UNIT_DIP, 148f);
        }
    }
    
    public String getTitle() {
        return title;
    }
    
    public Drawable getTitleIcon() {
        return titleIcon;
    }
    
    public void setTitleIcon(Drawable titleIcon) {
        this.titleIcon = titleIcon;
    }
    
    public void setMatchMap(LinkedHashMap<String, ArrayList<Match>> matchMap) {
        this.matchMap = matchMap;
    }
    
    public void setFavoriteMatchList(ArrayList<Match> favoriteMatchList) {
        this.favoriteMatchList = favoriteMatchList;
    }
    
    public void setTeamList(ArrayList<Team> teamList) {
        this.teamList = teamList;
    }
    
    public void setLeagueTable(ArrayList<Table> leagueTable) {
        this.leagueTable = leagueTable;
    }
    
    public String getListType() {
        return listType;
    }
    
    public boolean isLoading() {
        return loading;
    }
    
    public void setLoading(boolean loading) {
        this.loading = loading;
    }
    
    public void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        new Handler(Looper.getMainLooper()).postDelayed(() -> progressBar.setVisibility(View.GONE), 15000);
    }
    
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
    
    public TeamAdapter getTeamAdapter() {
        return teamAdapter;
    }
}