package com.pam.sportsdbfootballscore.helpers;

import com.pam.sportsdbfootballscore.interfaces.RetrofitResponseListener;
import com.pam.sportsdbfootballscore.model.League;
import com.pam.sportsdbfootballscore.model.Match;
import com.pam.sportsdbfootballscore.model.Team;
import com.pam.sportsdbfootballscore.ui.fragments.MainFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fetcher {
    
    public static final String KEY_TYPE_DATE = "event_date";
    
    private static Fetcher instance = null;
    
    private int count = 0;
    private int index = 0;
    
    private ArrayList<Team> favoriteTeamList;
    private ArrayList<Match> favoriteMatchList;
    
    public static Fetcher getInstance() {
        if (instance == null) {
            instance = new Fetcher();
        }
        
        return instance;
    }
    
    public void setFavoriteTeamList(ArrayList<Team> favoriteTeamList) {
        this.favoriteTeamList = favoriteTeamList;
    }
    
    public void setFavoriteMatchList(ArrayList<Match> favoriteMatchList) {
        this.favoriteMatchList = favoriteMatchList;
    }
    
    public void getMatches(String eventsType, int leagueId, RetrofitResponseListener listener,
                           int requestCode) {
        Call<String> call = Client.getInstance().getApi().getMatches(eventsType, leagueId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                if (response.body() != null) {
                    listener.onResponse(requestCode, response.body());
                }
            }
            
            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
            }
        });
    }
    
    public void getTeams(int leagueId, RetrofitResponseListener listener, int requestCode) {
        Call<String> call = Client.getInstance().getApi().getTeams(leagueId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                if (response.body() != null) {
                    listener.onResponse(requestCode, response.body());
                }
            }
            
            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
            }
        });
    }
    
    public void getTeamLatestResults(int teamId, RetrofitResponseListener listener, int requestCode) {
        Call<String> call = Client.getInstance().getApi().getTeamLatestResults(teamId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                if (response.body() != null) {
                    listener.onResponse(requestCode, response.body());
                }
            }
            
            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
            }
        });
    }
    
    public void getTeamUpcomingMatches(int teamId, RetrofitResponseListener listener, int requestCode) {
        Call<String> call = Client.getInstance().getApi().getTeamUpcomingMatches(teamId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                if (response.body() != null) {
                    listener.onResponse(requestCode, response.body());
                }
            }
            
            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
            }
        });
    }
    
    public void getLeagueTable(int leagueId, String season, RetrofitResponseListener listener, int requestCode) {
        Call<String> call = Client.getInstance().getApi().getLeagueTable(leagueId, season);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                if (response.body() != null) {
                    listener.onResponse(requestCode, response.body());
                }
            }
    
            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
            }
        });
    }
    
    public void getTeamDetails(LinkedHashMap<Integer, Team> teamMap, RetrofitResponseListener listener,
                               int requestCode) {
        count = teamMap.size();
        index = 0;
        
        for (Team team : teamMap.values()) {
            int idx = favoriteTeamList.indexOf(team);
            if (idx != -1) {
                teamMap.put(team.getId(), favoriteTeamList.get(idx));
            }
            
            League league = team.getLeague();
            if (league == null) {
                getTeamDetails(team, listener, requestCode);
            } else {
                index++;
                if (index == count) {
                    listener.onResponse(requestCode, null);
                }
            }
        }
    }
    
    private void getTeamDetails(Team team, RetrofitResponseListener listener, int requestCode) {
        Call<String> call = Client.getInstance().getApi().getTeamDetails(team.getId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                if (response.body() != null) {
                    try {
                        JSONObject responseJsonObject = new JSONObject(response.body());
                        JSONArray teams = responseJsonObject.getJSONArray("teams");
                        JSONObject teamJsonObject = teams.getJSONObject(0);
                        
                        updateTeam(teamJsonObject, team);
                        
                        if (requestCode == MainFragment.REQUEST_UPDATE_TEAM) {
                            team.setName(teamJsonObject.getString("strTeam"));
                            listener.onResponse(requestCode, null);
                        } else {
                            index++;
                            if (index == count) {
                                listener.onResponse(requestCode, null);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            
            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
            }
        });
    }
    
    public void getMatchMap(String response, LinkedHashMap<String, ArrayList<Match>> matchMap,
                            LinkedHashMap<Integer, Team> teamMap, int requestCode, String keyType) {
        try {
            JSONObject responseJsonObject = new JSONObject(response);
            String eventIdentifier;
            if (requestCode == MainFragment.REQUEST_TEAM_LATEST_RESULTS) {
                eventIdentifier = "results";
            } else {
                eventIdentifier = "events";
            }
            
            JSONArray events = responseJsonObject.getJSONArray(eventIdentifier);
            
            for (int i = 0; i < events.length(); i++) {
                JSONObject matchJsonObject = events.getJSONObject(i);
                
                int id = Integer.parseInt(matchJsonObject.getString("idEvent"));
                String event = matchJsonObject.getString("strEvent");
                int leagueId = Integer.parseInt(matchJsonObject.getString("idLeague"));
                String leagueName = matchJsonObject.getString("strLeague");
                League league = new League(leagueId, leagueName);
                String season = matchJsonObject.getString("strSeason");
                int homeScore = getNumber(matchJsonObject.getString("intHomeScore"));
                int awayScore = getNumber(matchJsonObject.getString("intAwayScore"));
                int matchWeek = getNumber(matchJsonObject.getString("intRound"));
                String eventDate = matchJsonObject.getString("dateEvent");
                String time = matchJsonObject.getString("strTime");
                String stadium = matchJsonObject.getString("strVenue");
                int spectators = getNumber(matchJsonObject.getString("intSpectators"));
                String thumbnail = matchJsonObject.getString("strThumb");
                String video = matchJsonObject.getString("strVideo");
                String status = matchJsonObject.getString("strStatus");
                
                int idHomeTeam = Integer.parseInt(matchJsonObject.getString("idHomeTeam"));
                String homeTeamName = matchJsonObject.getString("strHomeTeam");
                int idAwayTeam = Integer.parseInt(matchJsonObject.getString("idAwayTeam"));
                String awayTeamName = matchJsonObject.getString("strAwayTeam");
                
                Team homeTeam = getTeam(teamMap, idHomeTeam, homeTeamName);
                Team awayTeam = getTeam(teamMap, idAwayTeam, awayTeamName);
                
                Match match = new Match(id, event, league, season, homeTeam, awayTeam,
                        homeScore, awayScore, matchWeek, eventDate, time, stadium, spectators, thumbnail,
                        video, status, false);
                int index = favoriteMatchList.indexOf(match);
                if (index != -1) {
                    match.setFavorite(true);
                    favoriteMatchList.set(index, match);
                }
    
                String key = keyType.equals(KEY_TYPE_DATE) ? eventDate : keyType;
                
                if (matchMap.containsKey(key)) {
                    Objects.requireNonNull(matchMap.get(key)).add(match);
                } else {
                    ArrayList<Match> matches = new ArrayList<>();
                    matches.add(match);
                    matchMap.put(key, matches);
                }
            }
        } catch (JSONException | NullPointerException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
    
    public void updateTeam(Team team, RetrofitResponseListener listener, int requestCode) {
        getTeamDetails(team, listener, requestCode);
    }
    
    public void updateTeam(JSONObject teamJsonObject, Team team) {
        try {
            String logo = teamJsonObject.getString("strTeamBadge");
            String country = teamJsonObject.getString("strCountry");
            int formedYear = Integer.parseInt(teamJsonObject.getString("intFormedYear"));
            String description = teamJsonObject.getString("strDescriptionEN");
            String nicknames = teamJsonObject.getString("strKeywords");
            String stadium = teamJsonObject.getString("strStadium");
            String stadiumLocation = teamJsonObject.getString("strStadiumLocation");
            String stadiumDescription = teamJsonObject.getString("strStadiumDescription");
            String stadiumPreview = teamJsonObject.getString("strStadiumThumb");
            int stadiumCapacity;
            try {
                stadiumCapacity = Integer.parseInt(teamJsonObject.getString("intStadiumCapacity"));
            } catch (NumberFormatException e) {
                stadiumCapacity = 0;
            }
            
            ArrayList<League> leagues = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                String identifier = i == 1 ? "" : String.valueOf(i);
                int leagueId = getNumber(teamJsonObject.getString("idLeague" + identifier));
                
                if (leagueId != -1) {
                    String leagueName = teamJsonObject.getString("strLeague" + identifier);
                    League league = new League(leagueId, leagueName);
                    leagues.add(league);
                }
            }
            
            String website = teamJsonObject.getString("strWebsite");
            String facebook = teamJsonObject.getString("strFacebook");
            String twitter = teamJsonObject.getString("strTwitter");
            String instagram = teamJsonObject.getString("strInstagram");
            String youtube = teamJsonObject.getString("strYoutube");
            String jersey = teamJsonObject.getString("strTeamJersey");
            
            team.setLogo(logo);
            team.setCountry(country);
            team.setFormedYear(formedYear);
            team.setDescription(description);
            team.setNicknames(nicknames);
            team.setStadium(stadium);
            team.setStadiumLocation(stadiumLocation);
            team.setStadiumDescription(stadiumDescription);
            team.setStadiumPreview(stadiumPreview);
            team.setStadiumCapacity(stadiumCapacity);
            team.setLeague(leagues.get(0));
            team.setLeagues(leagues);
            team.setWebsite(website);
            team.setFacebook(facebook);
            team.setTwitter(twitter);
            team.setInstagram(instagram);
            team.setYoutube(youtube);
            team.setJersey(jersey);
            team.setFavorite(favoriteTeamList.contains(team));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    private int getNumber(String strNumber) {
        return strNumber == null || strNumber.isEmpty() ||
                strNumber.equalsIgnoreCase("null") ? -1 : Integer.parseInt(strNumber);
    }
    
    private Team getTeam(LinkedHashMap<Integer, Team> teamMap, int idTeam, String name) {
        if (teamMap.containsKey(idTeam)) {
            return teamMap.get(idTeam);
        } else {
            Team team = new Team(idTeam, name);
            teamMap.put(idTeam, team);
            return team;
        }
    }
}
