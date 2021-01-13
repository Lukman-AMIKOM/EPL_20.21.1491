package com.pam.sportsdbfootballscore.model;

import android.os.Parcel;

public class Match extends MyObject {
    
    private String event;
    private League league;
    private String season;
    private Team homeTeam;
    private Team awayTeam;
    private int homeScore;
    private int awayScore;
    private int matchWeek;
    private String eventDate;
    private String time;
    private String stadium;
    private int spectators;
    private String thumbnail;
    private String video;
    private String status;
    
    public Match(int id) {
        super(id);
    }
    
    public Match(int id, String event, League league, String season,
                 Team homeTeam, Team awayTeam, int homeScore, int awayScore, int matchWeek,
                 String eventDate, String time, String stadium, int spectators, String thumbnail,
                 String video, String status, boolean isFavorite) {
        super(id);
        this.event = event;
        this.league = league;
        this.season = season;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.matchWeek = matchWeek;
        this.eventDate = eventDate;
        this.time = time;
        this.stadium = stadium;
        this.spectators = spectators;
        this.thumbnail = thumbnail;
        this.video = video;
        this.status = status;
        setFavorite(isFavorite);
    }
    
    protected Match(Parcel in) {
        super(in);
        event = in.readString();
        league = in.readParcelable(League.class.getClassLoader());
        season = in.readString();
        homeTeam = in.readParcelable(Team.class.getClassLoader());
        awayTeam = in.readParcelable(Team.class.getClassLoader());
        homeScore = in.readInt();
        awayScore = in.readInt();
        matchWeek = in.readInt();
        eventDate = in.readString();
        time = in.readString();
        stadium = in.readString();
        spectators = in.readInt();
        thumbnail = in.readString();
        video = in.readString();
        status = in.readString();
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(event);
        dest.writeParcelable(league, flags);
        dest.writeString(season);
        dest.writeParcelable(homeTeam, flags);
        dest.writeParcelable(awayTeam, flags);
        dest.writeInt(homeScore);
        dest.writeInt(awayScore);
        dest.writeInt(matchWeek);
        dest.writeString(eventDate);
        dest.writeString(time);
        dest.writeString(stadium);
        dest.writeInt(spectators);
        dest.writeString(thumbnail);
        dest.writeString(video);
        dest.writeString(status);
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }
        
        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };
    
    public String getEvent() {
        return event;
    }
    
    public void setEvent(String event) {
        this.event = event;
    }
    
    public League getLeague() {
        return league;
    }
    
    public void setLeague(League league) {
        this.league = league;
    }
    
    public String getSeason() {
        return season;
    }
    
    public void setSeason(String season) {
        this.season = season;
    }
    
    public Team getHomeTeam() {
        return homeTeam;
    }
    
    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }
    
    public Team getAwayTeam() {
        return awayTeam;
    }
    
    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }
    
    public int getHomeScore() {
        return homeScore;
    }
    
    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }
    
    public int getAwayScore() {
        return awayScore;
    }
    
    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }
    
    public int getMatchWeek() {
        return matchWeek;
    }
    
    public void setMatchWeek(int matchWeek) {
        this.matchWeek = matchWeek;
    }
    
    public String getEventDate() {
        return eventDate;
    }
    
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public String getStadium() {
        return stadium;
    }
    
    public void setStadium(String stadium) {
        this.stadium = stadium;
    }
    
    public int getSpectators() {
        return spectators;
    }
    
    public void setSpectators(int spectators) {
        this.spectators = spectators;
    }
    
    public String getThumbnail() {
        return thumbnail;
    }
    
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    
    public String getVideo() {
        return video;
    }
    
    public void setVideo(String video) {
        this.video = video;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
