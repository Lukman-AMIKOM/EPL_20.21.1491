package com.pam.sportsdbfootballscore.ui.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pam.sportsdbfootballscore.R;
import com.pam.sportsdbfootballscore.helpers.DatabaseHelper;
import com.pam.sportsdbfootballscore.helpers.Fetcher;
import com.pam.sportsdbfootballscore.helpers.PrefManager;
import com.pam.sportsdbfootballscore.interfaces.Api;
import com.pam.sportsdbfootballscore.interfaces.FavoriteClickListener;
import com.pam.sportsdbfootballscore.interfaces.DataListener;
import com.pam.sportsdbfootballscore.interfaces.MatchClickListener;
import com.pam.sportsdbfootballscore.interfaces.RetrofitResponseListener;
import com.pam.sportsdbfootballscore.interfaces.TabSelectedListener;
import com.pam.sportsdbfootballscore.interfaces.TeamClickListener;
import com.pam.sportsdbfootballscore.model.Match;
import com.pam.sportsdbfootballscore.model.MyObject;
import com.pam.sportsdbfootballscore.model.Table;
import com.pam.sportsdbfootballscore.model.Team;
import com.pam.sportsdbfootballscore.ui.activities.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

public class MainFragment extends Fragment implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        RetrofitResponseListener,
        FavoriteClickListener,
        MatchClickListener,
        TeamClickListener,
        TabSelectedListener {
    
    public static final String PREF_THEME = "pref_theme";
    public static final String KEY_NIGHT_MODE = "key_theme";
    
    public static final int LEAGUE_ID_EPL = 4328;
    private static final String CURRENT_SEASON = "2020-2021";
    public static final int REQUEST_RECENT_MATCHES = 100;
    public static final int REQUEST_UPCOMING_MATCHES = 200;
    public static final int REQUEST_UPDATE_TEAM = 105;
    public static final int REQUEST_TEAM_DETAILS = 300;
    public static final int REQUEST_TEAMS = 400;
    public static final int REQUEST_TEAM_LATEST_RESULTS = 500;
    public static final int REQUEST_TEAM_UPCOMING_MATCHES = 600;
    public static final int REQUEST_LEAGUE_TABLE = 700;
    
    private LinkedHashMap<String, ArrayList<Match>> recentMatchMap;
    private LinkedHashMap<Integer, Team> teamMap;
    private ArrayList<Match> favoriteMatchList;
    private ArrayList<Team> favoriteTeamList;
    private final DataListener dataListener;
    
    private String TAG_FRAGMENT_MATCHES;
    private String TAG_FRAGMENT_TEAMS;
    private String TAG_FRAGMENT_TABLE;
    
    private MainActivity mainActivity;
    private MaterialToolbar toolbar;
    private PrefManager prefManager;
    
    private MainContentFragment matchesFragment, teamsFragment, tableFragment;
    private ArrayList<ListFragment> matchesFragmentPages, teamsFragmentPages, tableFragmentPages;
    private ListFragment recentMatchFragment, upcomingMatchFragment, teamListFragment,
            favoriteMatchFragment, favoriteTeamFragment, leagueTableFragment;
    private ListFragment selectedListFragment;
    
    private LinkedHashMap<String, ArrayList<Match>> upcomingMatchMap;
    private LinkedHashMap<String, ArrayList<Match>> teamMatchMap;
    private ArrayList<Team> teamList;
    private ArrayList<Table> leagueTable;
    private String selectedContent;
    private Team clickedTeam;
    
    private int nightModeFlags;
    private int requestCount = 0;
    private int requestDone = 0;
    private boolean nightMode = false;
    
    public MainFragment(Context context, DataListener dataListener) {
        this.dataListener = dataListener;
        init(context);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initLists();
        initListFragments();
        
        TAG_FRAGMENT_MATCHES = getString(R.string.matches);
        TAG_FRAGMENT_TEAMS = getString(R.string.teams);
        TAG_FRAGMENT_TABLE = getString(R.string.table);
        selectedContent = TAG_FRAGMENT_MATCHES;
        
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        
        init(view);
        setToolbar();
        if (matchesFragment == null) {
            initMainContentFragments();
        }
        setSelectedContentFragment();
    }
    
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        
        if (mainActivity != null) {
            prefManager = new PrefManager(mainActivity, PREF_THEME);
            nightMode = prefManager.getDarkMode(KEY_NIGHT_MODE);
        }
        MenuItem itemNightMode = menu.findItem(R.id.menu_night_mode);
        itemNightMode.setTitle(nightMode ? getString(R.string.light_mode) : getString(R.string.dark_mode));
        
        super.onCreateOptionsMenu(menu, inflater);
    }
    
    public void init(Context context) {
        recentMatchMap = new LinkedHashMap<>();
        teamMap = new LinkedHashMap<>();
        
        DatabaseHelper db = new DatabaseHelper(context);
        favoriteTeamList = db.getFavoriteTeamList();
        favoriteMatchList = db.getFavoriteMatchList();
        
        Fetcher.getInstance().setFavoriteTeamList(favoriteTeamList);
        Fetcher.getInstance().setFavoriteMatchList(favoriteMatchList);
        
        getList();
    }
    
    private void init(View view) {
        mainActivity = (MainActivity) getActivity();
        toolbar = view.findViewById(R.id.main_toolbar);
        
        BottomNavigationView bottomMenu = view.findViewById(R.id.nav_bottom_main);
        bottomMenu.setOnNavigationItemSelectedListener(this);
    }
    
    private void initLists() {
        matchesFragmentPages = new ArrayList<>(1);
        teamsFragmentPages = new ArrayList<>(1);
        tableFragmentPages = new ArrayList<>(1);
        upcomingMatchMap = new LinkedHashMap<>(1);
        teamList = new ArrayList<>(1);
        leagueTable = new ArrayList<>(1);
    }
    
    private void initListFragments() {
        recentMatchFragment = initListFragment(getString(R.string.recent_matches), ListFragment.TYPE_RECENT_MATCHES);
        recentMatchFragment.setMatchMap(recentMatchMap);
        upcomingMatchFragment = initListFragment(getString(R.string.upcoming_matches), ListFragment.TYPE_UPCOMING_MATCHES);
        upcomingMatchFragment.setMatchMap(upcomingMatchMap);
        favoriteMatchFragment = initListFragment(getString(R.string.favorites), ListFragment.TYPE_FAVORITE_MATCHES);
        favoriteMatchFragment.setFavoriteMatchList(favoriteMatchList);
        
        teamListFragment = initListFragment(getString(R.string.teams), ListFragment.TYPE_TEAMS);
        teamListFragment.setTeamList(teamList);
        favoriteTeamFragment = initListFragment(getString(R.string.favorites), ListFragment.TYPE_FAVORITE_TEAMS);
        favoriteTeamFragment.setTeamList(favoriteTeamList);
        
        leagueTableFragment = initListFragment(getString(R.string.tab_table), ListFragment.TYPE_LEAGUE_TABLE);
        leagueTableFragment.setLeagueTable(leagueTable);
        
        matchesFragmentPages.add(recentMatchFragment);
        matchesFragmentPages.add(upcomingMatchFragment);
        matchesFragmentPages.add(favoriteMatchFragment);
        teamsFragmentPages.add(teamListFragment);
        teamsFragmentPages.add(favoriteTeamFragment);
        tableFragmentPages.add(leagueTableFragment);
    }
    
    private ListFragment initListFragment(String title, String listType) {
        return new ListFragment(this, title, listType);
    }
    
    private void initMainContentFragments() {
        matchesFragment = new MainContentFragment(matchesFragmentPages, this, getTag(TAG_FRAGMENT_MATCHES));
        teamsFragment = new MainContentFragment(teamsFragmentPages, this, getTag(TAG_FRAGMENT_TEAMS));
        tableFragment = new MainContentFragment(tableFragmentPages, this, getTag(TAG_FRAGMENT_TABLE));
    }
    
    private void getList() {
        Fetcher.getInstance().getMatches(Api.PAST_EVENTS, MainFragment.LEAGUE_ID_EPL, this, MainFragment.REQUEST_RECENT_MATCHES);
    }
    
    private void setToolbar() {
        toolbar.setTitle("");
        
        final int toolbarIcon;
        Drawable recentMatchesIcon, upcomingMatchesIcon, favoriteMatchesIcon,
                teamsIcon, favoriteTeamsIcon, tableIcon;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            toolbarIcon = R.drawable.ic_epl_logo_night;
            recentMatchesIcon = getIconDrawable(R.drawable.ic_tab_recent_matches_night);
            upcomingMatchesIcon = getIconDrawable(R.drawable.ic_tab_fixtures_night);
            teamsIcon = getIconDrawable(R.drawable.ic_tab_teams_night);
            favoriteMatchesIcon = getIconDrawable(R.drawable.ic_tab_favorite_matches_night);
            favoriteTeamsIcon = getIconDrawable(R.drawable.ic_tab_favorite_teams_night);
            tableIcon = getIconDrawable(R.drawable.ic_tab_table_night);
        } else {
            toolbarIcon = R.drawable.ic_epl_logo;
            recentMatchesIcon = getIconDrawable(R.drawable.ic_tab_recent_matches);
            upcomingMatchesIcon = getIconDrawable(R.drawable.ic_tab_fixtures);
            teamsIcon = getIconDrawable(R.drawable.ic_tab_teams);
            favoriteMatchesIcon = getIconDrawable(R.drawable.ic_tab_favorite_matches);
            favoriteTeamsIcon = getIconDrawable(R.drawable.ic_tab_favorite_teams);
            tableIcon = getIconDrawable(R.drawable.ic_tab_table);
        }
        
        recentMatchFragment.setTitleIcon(recentMatchesIcon);
        upcomingMatchFragment.setTitleIcon(upcomingMatchesIcon);
        teamListFragment.setTitleIcon(teamsIcon);
        favoriteMatchFragment.setTitleIcon(favoriteMatchesIcon);
        favoriteTeamFragment.setTitleIcon(favoriteTeamsIcon);
        leagueTableFragment.setTitleIcon(tableIcon);
        
        if (mainActivity != null) {
            mainActivity.setSupportActionBar(toolbar);
            if (mainActivity.getSupportActionBar() != null) {
                mainActivity.getSupportActionBar().setIcon(toolbarIcon);
            }
        }
    }
    
    private void setSelectedContentFragment() {
        FragmentManager fragmentManager = getChildFragmentManager();
        MainContentFragment mainContentFragment;
        
        if (selectedContent.equals(TAG_FRAGMENT_MATCHES)) {
            mainContentFragment = matchesFragment;
        } else if (selectedContent.equals(TAG_FRAGMENT_TEAMS)) {
            mainContentFragment = teamsFragment;
        } else {
            mainContentFragment = tableFragment;
            if (leagueTable.isEmpty()) {
                Fetcher.getInstance().getLeagueTable(LEAGUE_ID_EPL, CURRENT_SEASON, this, REQUEST_LEAGUE_TABLE);
            }
        }
        
        if (fragmentManager.findFragmentByTag(mainContentFragment.getFragmentTag()) == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.main_content, mainContentFragment, mainContentFragment.getFragmentTag())
                    .commit();
        }
    }
    
    private String getTag(String contentType) {
        return MainContentFragment.class.getSimpleName() + ":" + contentType;
    }
    
    private Drawable getIconDrawable(int resId) {
        return ContextCompat.getDrawable(Objects.requireNonNull(getContext()), resId);
    }
    
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.nav_matches) {
            selectedContent = TAG_FRAGMENT_MATCHES;
        } else if (id == R.id.nav_teams) {
            selectedContent = TAG_FRAGMENT_TEAMS;
        } else if (id == R.id.nav_table) {
            selectedContent = TAG_FRAGMENT_TABLE;
        }
        
        setSelectedContentFragment();
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_about) {
            AboutFragment aboutFragment = new AboutFragment();
            
            if (mainActivity != null) {
                FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_bottom, R.anim.fade_out, R.anim.fade_in, R.anim.exit_to_bottom)
                        .replace(R.id.main_container, aboutFragment, AboutFragment.class.getSimpleName())
                        .addToBackStack(null)
                        .commit();
            }
        } else if (item.getItemId() == R.id.menu_night_mode) {
            if (mainActivity != null) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (!nightMode) {
                        mainActivity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        mainActivity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                    nightMode = !nightMode;
                    prefManager.setDarkMode(KEY_NIGHT_MODE, nightMode);
                }, 400);
            }
        }
        
        return true;
    }
    
    @Override
    public void onFavoriteClicked(MyObject data) {
        int listTitle;
        
        if (data instanceof Match) {
            listTitle = R.string.favorite_matches;
            updateFavoriteList(favoriteMatchFragment, favoriteMatchList, data, listTitle);
        } else {
            listTitle = R.string.favorite_teams;
            updateFavoriteList(favoriteTeamFragment, favoriteTeamList, data, listTitle);
        }
    }
    
    private void updateFavoriteList(ListFragment favoriteFragment,
                                    ArrayList<? extends MyObject> favoriteList,
                                    MyObject data, int listTitle) {
        DatabaseHelper db = new DatabaseHelper(mainActivity);
        
        if (data.isFavorite()) {
            if (!favoriteList.contains(data)) {
                if (data instanceof Match) {
                    favoriteMatchList.add((Match) data);
                    db.addFavoriteMatch((Match) data);
                } else if (data instanceof Team) {
                    favoriteTeamList.add((Team) data);
                    favoriteTeamFragment.getTeamAdapter().add((Team) data);
                    db.addFavoriteTeam((Team) data);
                }
            }
            
            if (selectedListFragment != favoriteFragment) {
                try {
                    Objects.requireNonNull(favoriteFragment.getRecyclerView().getAdapter()).notifyDataSetChanged();
                } catch (Exception ignored) {
                }
            }
            
            showNotification(R.string.add_notification, listTitle);
        } else {
            int index = favoriteList.indexOf(data);
            favoriteList.remove(data);
            db.deleteFavorite(data);
            
            if (data instanceof Match) {
                try {
                    Objects.requireNonNull(favoriteFragment.getRecyclerView().getAdapter()).notifyItemRemoved(index);
                } catch (Exception ignored) {
                }
                
                if (selectedListFragment == favoriteMatchFragment) {
                    updateMatchMap(recentMatchFragment, recentMatchMap, (Match) data);
                    updateMatchMap(upcomingMatchFragment, upcomingMatchMap, (Match) data);
                }
            } else if (data instanceof Team) {
                if (selectedListFragment == favoriteTeamFragment) {
                    teamListFragment.getTeamAdapter().notifyDataSetChanged();
                }
                favoriteTeamFragment.getTeamAdapter().remove((Team) data);
            }
            
            showNotification(R.string.remove_notification, listTitle);
        }
    }
    
    private void updateMatchMap(ListFragment listFragment, LinkedHashMap<String, ArrayList<Match>> matchMap, Match match) {
        for (ArrayList<Match> matches : matchMap.values()) {
            int index = matches.indexOf(match);
            if (index != -1) {
                matches.get(index).setFavorite(match.isFavorite());
                try {
                    Objects.requireNonNull(listFragment.getRecyclerView().getAdapter()).notifyDataSetChanged();
                } catch (Exception ignored) {
                }
                break;
            }
        }
    }
    
    private void showNotification(int action, int listTitle) {
        Toast.makeText(mainActivity, getString(action, getString(listTitle)), Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onMatchClicked(Match match) {
        MatchDetailFragment matchDetailFragment = new MatchDetailFragment(this);
        
        Bundle args = new Bundle();
        args.putParcelable(MatchDetailFragment.ARG_MATCH, match);
        matchDetailFragment.setArguments(args);
        
        mainActivity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.main_container, matchDetailFragment, MatchDetailFragment.class.getSimpleName() + ":" + match.getId())
                .addToBackStack(null)
                .commit();
    }
    
    @Override
    public void onTeamClicked(Team team) {
        clickedTeam = team;
        TeamDetailFragment teamDetailFragment = new TeamDetailFragment(this);
        
        teamMatchMap = new LinkedHashMap<>();
        Fetcher.getInstance().getTeamLatestResults(team.getId(), this, REQUEST_TEAM_LATEST_RESULTS);
        Fetcher.getInstance().getTeamUpcomingMatches(team.getId(), this, REQUEST_TEAM_UPCOMING_MATCHES);
        
        Bundle args = new Bundle();
        args.putParcelable(TeamDetailFragment.ARG_TEAM, team);
        teamDetailFragment.setArguments(args);
        
        mainActivity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.main_container, teamDetailFragment, TeamDetailFragment.class.getSimpleName() + ":" + team.getId())
                .addToBackStack(null)
                .commit();
    }
    
    private TeamDetailFragment getTeamDetailFragment() {
        return (TeamDetailFragment) mainActivity.getSupportFragmentManager()
                .findFragmentByTag(TeamDetailFragment.class.getSimpleName() + ":" + clickedTeam.getId());
    }
    
    private void setTeamDetailMatchMap(int code) {
        TeamDetailFragment teamDetailFragment = getTeamDetailFragment();
        if (teamDetailFragment != null) {
            teamDetailFragment.setMatchMap(teamMatchMap);
            switch (code) {
                case REQUEST_TEAM_LATEST_RESULTS:
                    teamDetailFragment.setLatestReady(true);
                    break;
                case REQUEST_TEAM_UPCOMING_MATCHES:
                    teamDetailFragment.setUpcomingReady(true);
                    break;
            }
//            teamDetailFragment.prepare();
        }
    }
    
    @Override
    public void onTabSelected(MainContentFragment mainContentFragment, ListFragment selectedListFragment) {
        this.selectedListFragment = selectedListFragment;
        String listType = selectedListFragment.getListType();
        
        if (mainContentFragment == matchesFragment) {
            if (listType.equals(ListFragment.TYPE_UPCOMING_MATCHES)) {
                if ((upcomingMatchMap == null || upcomingMatchMap.isEmpty()) && !upcomingMatchFragment.isLoading()) {
                    upcomingMatchFragment.setLoading(true);
                    Fetcher.getInstance().getMatches(Api.NEXT_EVENTS, LEAGUE_ID_EPL, MainFragment.this, REQUEST_UPCOMING_MATCHES);
                }
            }
        } else if (mainContentFragment == teamsFragment) {
            if (listType.equals(ListFragment.TYPE_TEAMS)) {
                if (teamList.isEmpty()) {
                    teamListFragment.setLoading(true);
                    Fetcher.getInstance().getTeams(LEAGUE_ID_EPL, MainFragment.this, REQUEST_TEAMS);
                }
            }
        }
    }
    
    @Override
    public void onResponse(int responseCode, String response) {
        switch (responseCode) {
            case MainFragment.REQUEST_RECENT_MATCHES:
                Fetcher.getInstance().getMatchMap(response, recentMatchMap, teamMap,
                        MainFragment.REQUEST_RECENT_MATCHES, Fetcher.KEY_TYPE_DATE);
                Fetcher.getInstance().getTeamDetails(teamMap, this, 101);
                break;
            case 101:
                for (Match match : favoriteMatchList) {
                    Team homeTeam = match.getHomeTeam();
                    Team awayTeam = match.getAwayTeam();
                    
                    if (teamMap.containsKey(homeTeam.getId())) {
                        match.setHomeTeam(teamMap.get(homeTeam.getId()));
                    } else {
                        Fetcher.getInstance().updateTeam(homeTeam, this, REQUEST_UPDATE_TEAM);
                        requestCount++;
                    }
                    
                    if (teamMap.containsKey(awayTeam.getId())) {
                        match.setAwayTeam(teamMap.get(awayTeam.getId()));
                    } else {
                        Fetcher.getInstance().updateTeam(awayTeam, this, REQUEST_UPDATE_TEAM);
                        requestCount++;
                    }
                }
                
                start();
                break;
            case REQUEST_UPDATE_TEAM:
                requestDone++;
                start();
                break;
            case REQUEST_UPCOMING_MATCHES:
                upcomingMatchFragment.loadData();
                Fetcher.getInstance().getMatchMap(response, upcomingMatchMap, teamMap,
                        REQUEST_UPCOMING_MATCHES, Fetcher.KEY_TYPE_DATE);
                Fetcher.getInstance().getTeamDetails(teamMap, this, REQUEST_TEAM_DETAILS);
                break;
            case REQUEST_TEAM_DETAILS:
                upcomingMatchFragment.setAdapter();
                upcomingMatchFragment.setLoading(false);
                break;
            case REQUEST_TEAMS:
                teamListFragment.loadData();
                
                try {
                    JSONObject responseJsonObject = new JSONObject(response);
                    JSONArray teams = responseJsonObject.getJSONArray("teams");
                    
                    for (int i = 0; i < teams.length(); i++) {
                        JSONObject teamJsonObject = teams.getJSONObject(i);
                        int id = Integer.parseInt(teamJsonObject.getString("idTeam"));
                        
                        if (!teamMap.containsKey(id)) {
                            String name = teamJsonObject.getString("strTeam");
                            
                            Team team = new Team(id, name);
                            Fetcher.getInstance().updateTeam(teamJsonObject, team);
                            teamMap.put(id, team);
                        }
                    }
                    
                    for (Team team : teamMap.values()) {
                        if (team.getLeague().getId() == LEAGUE_ID_EPL) {
                            teamList.add(team);
                        }
                    }
                } catch (JSONException | NumberFormatException e) {
                    e.printStackTrace();
                }
                
                teamListFragment.setAdapter();
                teamListFragment.setLoading(false);
                break;
            case REQUEST_TEAM_LATEST_RESULTS:
                Fetcher.getInstance().getMatchMap(response, teamMatchMap, teamMap,
                        REQUEST_TEAM_LATEST_RESULTS, getString(R.string.team_latest_results));
                Fetcher.getInstance().getTeamDetails(teamMap, this, REQUEST_TEAM_LATEST_RESULTS + 1);
                break;
            case REQUEST_TEAM_UPCOMING_MATCHES:
                Fetcher.getInstance().getMatchMap(response, teamMatchMap, teamMap,
                        REQUEST_TEAM_UPCOMING_MATCHES, getString(R.string.team_upcoming_matches));
                Fetcher.getInstance().getTeamDetails(teamMap, this, REQUEST_TEAM_UPCOMING_MATCHES + 1);
                break;
            case REQUEST_TEAM_LATEST_RESULTS + 1:
                setTeamDetailMatchMap(REQUEST_TEAM_LATEST_RESULTS);
                break;
            case REQUEST_TEAM_UPCOMING_MATCHES + 1:
                setTeamDetailMatchMap(REQUEST_TEAM_UPCOMING_MATCHES);
                break;
            case REQUEST_LEAGUE_TABLE:
                try {
                    JSONObject responseJsonObject = new JSONObject(response);
                    JSONArray tables = responseJsonObject.getJSONArray("table");
                    
                    for (int i = 0; i < tables.length(); i++) {
                        JSONObject tablesJSONObject = tables.getJSONObject(i);
                        
                        Team team;
                        int teamId = Integer.parseInt(tablesJSONObject.getString("teamid"));
                        if (teamMap.containsKey(teamId)) {
                            team = teamMap.get(teamId);
                        } else {
                            String name = tablesJSONObject.getString("name");
                            team = new Team(teamId, name);
                            Fetcher.getInstance().updateTeam(team, this, 0);
                        }
                        
                        int played = Integer.parseInt(tablesJSONObject.getString("played"));
                        int goalsFor = Integer.parseInt(tablesJSONObject.getString("goalsfor"));
                        int goalsAgainst = Integer.parseInt(tablesJSONObject.getString("goalsagainst"));
                        int goalsDifference = Integer.parseInt(tablesJSONObject.getString("goalsdifference"));
                        int win = Integer.parseInt(tablesJSONObject.getString("win"));
                        int draw = Integer.parseInt(tablesJSONObject.getString("draw"));
                        int loss = Integer.parseInt(tablesJSONObject.getString("loss"));
                        int points = Integer.parseInt(tablesJSONObject.getString("total"));
                        
                        Table table = new Table(team, played, goalsFor, goalsAgainst, goalsDifference,
                                win, draw, loss, points);
                        leagueTable.add(table);
                    }
                    
                    leagueTableFragment.setAdapter();
                } catch (JSONException | NumberFormatException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    
    private void start() {
        if (requestCount == requestDone) {
            dataListener.onDataReady();
        }
    }
}