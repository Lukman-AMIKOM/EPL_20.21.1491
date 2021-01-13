package com.pam.sportsdbfootballscore.interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    String BASE_URL = "https://www.thesportsdb.com/api/v1/json/1/";
    String PAST_EVENTS = "eventspastleague";
    String NEXT_EVENTS = "eventsnextleague";
    
    @GET("{events_type}.php")
    Call<String> getMatches(@Path("events_type") String eventsType, @Query("id") int leagueId);
    
    @GET("lookup_all_teams.php")
    Call<String> getTeams(@Query("id") int leagueId);
    
    @GET("lookupteam.php")
    Call<String> getTeamDetails(@Query("id") int teamId);
    
    @GET("eventsnext.php")
    Call<String> getTeamUpcomingMatches(@Query("id") int teamId);
    
    @GET("eventslast.php")
    Call<String> getTeamLatestResults(@Query("id") int teamId);
    
    @GET("lookuptable.php")
    Call<String> getLeagueTable(@Query("l") int leagueId, @Query("s") String season);
}
