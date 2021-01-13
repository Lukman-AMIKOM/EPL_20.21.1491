package com.pam.sportsdbfootballscore.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Team extends MyObject implements Parcelable {
    
    private String name;
    private String logo;
    private String country;
    private int formedYear;
    private String description;
    private String nicknames;
    private String stadium;
    private String stadiumLocation;
    private String stadiumDescription;
    private String stadiumPreview;
    private int stadiumCapacity;
    private League league;
    private ArrayList<League> leagues;
    private String website;
    private String facebook;
    private String twitter;
    private String instagram;
    private String youtube;
    private String jersey;
    
    public Team() {
        super(-1);
    }
    
    public Team(int id, String name) {
        super(id);
        this.name = name;
    }
    
    public Team(int id, String name, String logo, String country, int formedYear, String description,
                String nicknames, String stadium, String stadiumLocation, String stadiumDescription,
                String stadiumPreview, int stadiumCapacity, League league, ArrayList<League> leagues,
                String website, String facebook, String twitter, String instagram, String youtube,
                String jersey, boolean isFavorite) {
        super(id);
        this.name = name;
        this.logo = logo;
        this.country = country;
        this.formedYear = formedYear;
        this.description = description;
        this.nicknames = nicknames;
        this.stadium = stadium;
        this.stadiumLocation = stadiumLocation;
        this.stadiumDescription = stadiumDescription;
        this.stadiumPreview = stadiumPreview;
        this.stadiumCapacity = stadiumCapacity;
        this.league = league;
        this.leagues = leagues;
        this.website = website;
        this.facebook = facebook;
        this.twitter = twitter;
        this.instagram = instagram;
        this.youtube = youtube;
        this.jersey = jersey;
        setFavorite(isFavorite);
    }
    
    protected Team(Parcel in) {
        super(in);
        name = in.readString();
        logo = in.readString();
        country = in.readString();
        formedYear = in.readInt();
        description = in.readString();
        nicknames = in.readString();
        stadium = in.readString();
        stadiumLocation = in.readString();
        stadiumDescription = in.readString();
        stadiumPreview = in.readString();
        stadiumCapacity = in.readInt();
        league = in.readParcelable(League.class.getClassLoader());
        leagues = in.createTypedArrayList(League.CREATOR);
        website = in.readString();
        facebook = in.readString();
        twitter = in.readString();
        instagram = in.readString();
        youtube = in.readString();
        jersey = in.readString();
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(name);
        dest.writeString(logo);
        dest.writeString(country);
        dest.writeInt(formedYear);
        dest.writeString(description);
        dest.writeString(nicknames);
        dest.writeString(stadium);
        dest.writeString(stadiumLocation);
        dest.writeString(stadiumDescription);
        dest.writeString(stadiumPreview);
        dest.writeInt(stadiumCapacity);
        dest.writeParcelable(league, flags);
        dest.writeTypedList(leagues);
        dest.writeString(website);
        dest.writeString(facebook);
        dest.writeString(twitter);
        dest.writeString(instagram);
        dest.writeString(youtube);
        dest.writeString(jersey);
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }
        
        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getLogo() {
        return logo;
    }
    
    public void setLogo(String logo) {
        this.logo = logo;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public int getFormedYear() {
        return formedYear;
    }
    
    public void setFormedYear(int formedYear) {
        this.formedYear = formedYear;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getNicknames() {
        return nicknames;
    }
    
    public void setNicknames(String nicknames) {
        this.nicknames = nicknames;
    }
    
    public String getStadium() {
        return stadium;
    }
    
    public void setStadium(String stadium) {
        this.stadium = stadium;
    }
    
    public String getStadiumLocation() {
        return stadiumLocation;
    }
    
    public void setStadiumLocation(String stadiumLocation) {
        this.stadiumLocation = stadiumLocation;
    }
    
    public String getStadiumDescription() {
        return stadiumDescription;
    }
    
    public void setStadiumDescription(String stadiumDescription) {
        this.stadiumDescription = stadiumDescription;
    }
    
    public String getStadiumPreview() {
        return stadiumPreview;
    }
    
    public void setStadiumPreview(String stadiumPreview) {
        this.stadiumPreview = stadiumPreview;
    }
    
    public int getStadiumCapacity() {
        return stadiumCapacity;
    }
    
    public void setStadiumCapacity(int stadiumCapacity) {
        this.stadiumCapacity = stadiumCapacity;
    }
    
    public League getLeague() {
        return league;
    }
    
    public void setLeague(League league) {
        this.league = league;
    }
    
    public ArrayList<League> getLeagues() {
        return leagues;
    }
    
    public void setLeagues(ArrayList<League> leagues) {
        this.leagues = leagues;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public String getFacebook() {
        return facebook;
    }
    
    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }
    
    public String getTwitter() {
        return twitter;
    }
    
    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
    
    public String getInstagram() {
        return instagram;
    }
    
    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }
    
    public String getYoutube() {
        return youtube;
    }
    
    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }
    
    public String getJersey() {
        return jersey;
    }
    
    public void setJersey(String jersey) {
        this.jersey = jersey;
    }
}
