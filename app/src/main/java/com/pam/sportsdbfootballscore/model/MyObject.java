package com.pam.sportsdbfootballscore.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class MyObject implements Parcelable {
    
    private int id;
    private boolean isFavorite;
    
    public MyObject(int id) {
        this.id = id;
    }
    
    protected MyObject(Parcel in) {
        id = in.readInt();
        isFavorite = in.readByte() != 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    public static final Creator<MyObject> CREATOR = new Creator<MyObject>() {
        @Override
        public MyObject createFromParcel(Parcel in) {
            return new MyObject(in);
        }
        
        @Override
        public MyObject[] newArray(int size) {
            return new MyObject[size];
        }
    };
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyObject myObject = (MyObject) o;
        return id == myObject.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public boolean isFavorite() {
        return isFavorite;
    }
    
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
