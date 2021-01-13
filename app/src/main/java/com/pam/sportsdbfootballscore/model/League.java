package com.pam.sportsdbfootballscore.model;

import android.os.Parcel;
import android.os.Parcelable;

public class League implements Parcelable {
    
    private int id;
    private String name;
    
    public League(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    protected League(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    public static final Creator<League> CREATOR = new Creator<League>() {
        @Override
        public League createFromParcel(Parcel in) {
            return new League(in);
        }
        
        @Override
        public League[] newArray(int size) {
            return new League[size];
        }
    };
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
