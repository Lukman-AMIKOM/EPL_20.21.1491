package com.pam.sportsdbfootballscore.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Table implements Parcelable {
    
    private String name;
    private Team team;
    private int played;
    private int goalsFor;
    private int goalsAgainst;
    private int goalsDifference;
    private int win;
    private int draw;
    private int loss;
    private int points;
    
    public Table() {
    }
    
    public Table(Team team, int played, int goalsFor, int goalsAgainst,
                 int goalsDifference, int win, int draw, int loss, int points) {
        this.name = team.getName();
        this.team = team;
        this.played = played;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        this.goalsDifference = goalsDifference;
        this.win = win;
        this.draw = draw;
        this.loss = loss;
        this.points = points;
    }
    
    public Table(String name, Team team, int played, int goalsFor, int goalsAgainst,
                 int goalsDifference, int win, int draw, int loss, int points) {
        this.name = name;
        this.team = team;
        this.played = played;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        this.goalsDifference = goalsDifference;
        this.win = win;
        this.draw = draw;
        this.loss = loss;
        this.points = points;
    }
    
    protected Table(Parcel in) {
        name = in.readString();
        team = in.readParcelable(Team.class.getClassLoader());
        played = in.readInt();
        goalsFor = in.readInt();
        goalsAgainst = in.readInt();
        goalsDifference = in.readInt();
        win = in.readInt();
        draw = in.readInt();
        loss = in.readInt();
        points = in.readInt();
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(team, flags);
        dest.writeInt(played);
        dest.writeInt(goalsFor);
        dest.writeInt(goalsAgainst);
        dest.writeInt(goalsDifference);
        dest.writeInt(win);
        dest.writeInt(draw);
        dest.writeInt(loss);
        dest.writeInt(points);
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    public static final Creator<Table> CREATOR = new Creator<Table>() {
        @Override
        public Table createFromParcel(Parcel in) {
            return new Table(in);
        }
        
        @Override
        public Table[] newArray(int size) {
            return new Table[size];
        }
    };
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Team getTeam() {
        return team;
    }
    
    public void setTeam(Team team) {
        this.team = team;
    }
    
    public int getPlayed() {
        return played;
    }
    
    public void setPlayed(int played) {
        this.played = played;
    }
    
    public int getGoalsFor() {
        return goalsFor;
    }
    
    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }
    
    public int getGoalsAgainst() {
        return goalsAgainst;
    }
    
    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }
    
    public int getGoalsDifference() {
        return goalsDifference;
    }
    
    public void setGoalsDifference(int goalsDifference) {
        this.goalsDifference = goalsDifference;
    }
    
    public int getWin() {
        return win;
    }
    
    public void setWin(int win) {
        this.win = win;
    }
    
    public int getDraw() {
        return draw;
    }
    
    public void setDraw(int draw) {
        this.draw = draw;
    }
    
    public int getLoss() {
        return loss;
    }
    
    public void setLoss(int loss) {
        this.loss = loss;
    }
    
    public int getPoints() {
        return points;
    }
    
    public void setPoints(int points) {
        this.points = points;
    }
}
