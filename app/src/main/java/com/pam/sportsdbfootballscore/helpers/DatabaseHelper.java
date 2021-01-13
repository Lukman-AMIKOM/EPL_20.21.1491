package com.pam.sportsdbfootballscore.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pam.sportsdbfootballscore.model.League;
import com.pam.sportsdbfootballscore.model.Match;
import com.pam.sportsdbfootballscore.model.MyObject;
import com.pam.sportsdbfootballscore.model.Team;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "db_favorite";
    private static final int DATABASE_VERSION = 1;
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LEAGUE);
        db.execSQL(CREATE_TABLE_FAVORITE_MATCH);
        db.execSQL(CREATE_TABLE_FAVORITE_TEAM);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE_MATCH);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE_TEAM);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEAGUE);
            onCreate(db);
        }
    }
    
    public ArrayList<Match> getFavoriteMatchList() {
        ArrayList<Match> favoriteMatchList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] columns = {
                FID, EVENT, FD + LEAGUE_ID, NAME, SEASON, HOME_TEAM_ID, AWAY_TEAM_ID, HOME_SCORE, AWAY_SCORE,
                MATCHWEEK, EVENT_DATE, EVENT_TIME, STADIUM, SPECTATORS, THUMBNAIL, VIDEO, STATUS
        };
        String table = TABLE_FAVORITE_MATCH + " " + F
                + " INNER JOIN " + TABLE_LEAGUE + " " + L + " ON " + FD + LEAGUE_ID + "=" + LD + ID;
        
        Cursor c = db.query(table, columns, null, null, null, null, null);
        if (c != null && c.moveToNext()) {
            do {
                int id = c.getInt(c.getColumnIndex(ID));
                String event = c.getString(c.getColumnIndex(EVENT));
                int leagueId = c.getInt(c.getColumnIndex(LEAGUE_ID));
                String leagueName = c.getString(c.getColumnIndex(NAME));
                League league = new League(leagueId, leagueName);
                String season = c.getString(c.getColumnIndex(SEASON));
                int homeTeamId = c.getInt(c.getColumnIndex(HOME_TEAM_ID));
                int awayTeamId = c.getInt(c.getColumnIndex(AWAY_TEAM_ID));
                Team homeTeam = new Team(homeTeamId, null);
                Team awayTeam = new Team(awayTeamId, null);
                int homeScore = c.getInt(c.getColumnIndex(HOME_SCORE));
                int awayScore = c.getInt(c.getColumnIndex(AWAY_SCORE));
                int matchweek = c.getInt(c.getColumnIndex(MATCHWEEK));
                String eventDate = c.getString(c.getColumnIndex(EVENT_DATE));
                String time = c.getString(c.getColumnIndex(EVENT_TIME));
                String stadium = c.getString(c.getColumnIndex(STADIUM));
                int spectators = c.getInt(c.getColumnIndex(SPECTATORS));
                String thumbnail = c.getString(c.getColumnIndex(THUMBNAIL));
                String video = c.getString(c.getColumnIndex(VIDEO));
                String status = c.getString(c.getColumnIndex(STATUS));
                
                Match match = new Match(id, event, league, season, homeTeam, awayTeam, homeScore, awayScore,
                        matchweek, eventDate, time, stadium, spectators, thumbnail, video, status, true);
                favoriteMatchList.add(match);
            } while (c.moveToNext());
            
            c.close();
        }
    
        db.close();
        return favoriteMatchList;
    }
    
    public ArrayList<Team> getFavoriteTeamList() {
        ArrayList<Team> favoriteTeamList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] columns = {
                FID, FNAME, LOGO, COUNTRY, FORMED_YEAR, DESCRIPTION, NICKNAMES,
                STADIUM, STADIUM_LOCATION, STADIUM_DESCRIPTION, STADIUM_PREVIEW,
                STADIUM_CAPACITY,
                LEAGUE_ID, LD + NAME + " as " + LEAGUE_NAME,
                LEAGUE2_ID, L2D + NAME + " as " + LEAGUE2_NAME,
                LEAGUE3_ID, L3D + NAME + " as " + LEAGUE3_NAME,
                LEAGUE4_ID, L4D + NAME + " as " + LEAGUE4_NAME,
                LEAGUE5_ID, L5D + NAME + " as " + LEAGUE5_NAME,
                WEBSITE, FACEBOOK, TWITTER, INSTAGRAM, YOUTUBE, JERSEY
        };
        String table = TABLE_FAVORITE_TEAM + " " + F
                + " INNER JOIN " + TABLE_LEAGUE + " " + L + " ON " + FD + LEAGUE_ID + "=" + LD + ID
                + " INNER JOIN " + TABLE_LEAGUE + " " + L2 + " ON " + FD + LEAGUE2_ID + "=" + L2D + ID
                + " INNER JOIN " + TABLE_LEAGUE + " " + L3 + " ON " + FD + LEAGUE3_ID + "=" + L3D + ID
                + " INNER JOIN " + TABLE_LEAGUE + " " + L4 + " ON " + FD + LEAGUE4_ID + "=" + L4D + ID
                + " INNER JOIN " + TABLE_LEAGUE + " " + L5 + " ON " + FD + LEAGUE5_ID + "=" + L5D + ID;
        
        Cursor c = db.query(table, columns, null, null, null, null, null);
        
        String[] leagueIdColumns = {LEAGUE_ID, LEAGUE2_ID, LEAGUE3_ID, LEAGUE4_ID, LEAGUE5_ID};
        String[] leagueNameColumns = {LEAGUE_NAME, LEAGUE2_NAME, LEAGUE3_NAME, LEAGUE4_NAME, LEAGUE5_NAME};
        int[] leagueIds = new int[leagueIdColumns.length];
        String[] leagueNames = new String[leagueIdColumns.length];
        
        if (c != null && c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndex(ID));
                String name = c.getString(c.getColumnIndex(NAME));
                String logo = c.getString(c.getColumnIndex(LOGO));
                String country = c.getString(c.getColumnIndex(COUNTRY));
                int formedYear = c.getInt(c.getColumnIndex(FORMED_YEAR));
                String description = c.getString(c.getColumnIndex(DESCRIPTION));
                String nicknames = c.getString(c.getColumnIndex(NICKNAMES));
                String stadium = c.getString(c.getColumnIndex(STADIUM));
                String stadiumLocation = c.getString(c.getColumnIndex(STADIUM_LOCATION));
                String stadiumDescription = c.getString(c.getColumnIndex(STADIUM_DESCRIPTION));
                String stadiumPreview = c.getString(c.getColumnIndex(STADIUM_PREVIEW));
                int stadiumCapacity = c.getInt(c.getColumnIndex(STADIUM_CAPACITY));
                for (int i = 0; i < leagueIdColumns.length; i++) {
                    leagueIds[i] = c.getInt(c.getColumnIndex(leagueIdColumns[i]));
                    leagueNames[i] = c.getString(c.getColumnIndex(leagueNameColumns[i]));
                }
                ArrayList<League> leagues = getLeagues(leagueIds, leagueNames);
                String website = c.getString(c.getColumnIndex(WEBSITE));
                String facebook = c.getString(c.getColumnIndex(FACEBOOK));
                String twitter = c.getString(c.getColumnIndex(TWITTER));
                String instagram = c.getString(c.getColumnIndex(INSTAGRAM));
                String youtube = c.getString(c.getColumnIndex(YOUTUBE));
                String jersey = c.getString(c.getColumnIndex(JERSEY));
                
                Team team = new Team(id, name, logo, country, formedYear, description,
                        nicknames, stadium, stadiumLocation, stadiumDescription, stadiumPreview, stadiumCapacity,
                        leagues.get(0), leagues, website, facebook, twitter, instagram, youtube, jersey, true);
                favoriteTeamList.add(team);
            } while (c.moveToNext());
            
            c.close();
        }
        
        db.close();
        return favoriteTeamList;
    }
    
    public void addFavoriteMatch(Match match) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        addLeague(match.getLeague());
        
        values.put(ID, match.getId());
        values.put(EVENT, match.getEvent());
        values.put(LEAGUE_ID, match.getLeague().getId());
        values.put(SEASON, match.getSeason());
        values.put(HOME_TEAM_ID, match.getHomeTeam().getId());
        values.put(AWAY_TEAM_ID, match.getAwayTeam().getId());
        values.put(HOME_SCORE, match.getHomeScore());
        values.put(AWAY_SCORE, match.getAwayScore());
        values.put(MATCHWEEK, match.getMatchWeek());
        values.put(EVENT_DATE, match.getEventDate());
        values.put(EVENT_TIME, match.getTime());
        values.put(STADIUM, match.getStadium());
        values.put(SPECTATORS, match.getSpectators());
        values.put(THUMBNAIL, match.getThumbnail());
        values.put(VIDEO, match.getVideo());
        values.put(STATUS, match.getStatus());
        
        db.insert(TABLE_FAVORITE_MATCH, null, values);
        db.close();
    }
    
    public void addFavoriteTeam(Team team) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        String[] leagueColumns = {LEAGUE_ID, LEAGUE2_ID, LEAGUE3_ID, LEAGUE4_ID, LEAGUE5_ID};
        int[] leagueIds = new int[leagueColumns.length];
        
        for (int i = 0; i < leagueIds.length; i++) {
            try {
                leagueIds[i] = team.getLeagues().get(i).getId();
                addLeague(team.getLeagues().get(i));
            } catch (IndexOutOfBoundsException e) {
                leagueIds[i] = -1;
                addLeague(null);
            }
        }
        
        values.put(ID, team.getId());
        values.put(NAME, team.getName());
        values.put(LOGO, team.getLogo());
        values.put(COUNTRY, team.getCountry());
        values.put(FORMED_YEAR, team.getFormedYear());
        values.put(DESCRIPTION, team.getDescription());
        values.put(NICKNAMES, team.getNicknames());
        values.put(STADIUM, team.getStadium());
        values.put(STADIUM_LOCATION, team.getStadiumLocation());
        values.put(STADIUM_DESCRIPTION, team.getStadiumDescription());
        values.put(STADIUM_PREVIEW, team.getStadiumPreview());
        values.put(STADIUM_CAPACITY, team.getStadiumCapacity());
        for (int i = 0; i < leagueColumns.length; i++) {
            values.put(leagueColumns[i], leagueIds[i]);
        }
        values.put(WEBSITE, team.getWebsite());
        values.put(FACEBOOK, team.getFacebook());
        values.put(TWITTER, team.getTwitter());
        values.put(INSTAGRAM, team.getInstagram());
        values.put(YOUTUBE, team.getYoutube());
        values.put(JERSEY, team.getJersey());
        
        db.insert(TABLE_FAVORITE_TEAM, null, values);
        db.close();
    }
    
    public void deleteFavorite(MyObject data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String tableName = data instanceof Match ? TABLE_FAVORITE_MATCH : TABLE_FAVORITE_TEAM;
        
        db.delete(tableName, ID + "=?", new String[]{String.valueOf(data.getId())});
        db.close();
    }
    
    private void addLeague(League league) {
        SQLiteDatabase dbRead = this.getReadableDatabase();
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        
        int id;
        String name;
        
        if (league == null) {
            id = -1;
            name = "";
        } else {
            id = league.getId();
            name = league.getName();
        }
        
        Cursor c = dbRead.query(TABLE_LEAGUE, new String[]{ID}, ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (!c.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(ID, id);
            values.put(NAME, name);
            dbWrite.insert(TABLE_LEAGUE, null, values);
            
            c.close();
        }
    }
    
    private ArrayList<League> getLeagues(int[] leagueIds, String[] leagueNames) {
        ArrayList<League> leagues = new ArrayList<>();
        
        for (int i = 0; i < leagueIds.length; i++) {
            if (leagueIds[i] != -1) {
                leagues.add(new League(leagueIds[i], leagueNames[i]));
            }
        }
        
        return leagues;
    }
    
    private static final String TABLE_LEAGUE = "League";
    private static final String TABLE_FAVORITE_MATCH = "Table_Favorite_Match";
    private static final String TABLE_FAVORITE_TEAM = "Table_Favorite_Team";
    
    private static final String ID = "id";
    private static final String EVENT = "event";
    private static final String LEAGUE_ID = "league_id";
    private static final String NAME = "name";
    private static final String SEASON = "season";
    private static final String HOME_TEAM_ID = "home_team_id";
    private static final String AWAY_TEAM_ID = "away_team_id";
    private static final String HOME_SCORE = "home_score";
    private static final String AWAY_SCORE = "away_score";
    private static final String MATCHWEEK = "matchweek";
    private static final String EVENT_DATE = "event_date";
    private static final String EVENT_TIME = "event_time";
    private static final String STADIUM = "stadium";
    private static final String SPECTATORS = "spectators";
    private static final String THUMBNAIL = "thumbnail";
    private static final String VIDEO = "video";
    private static final String STATUS = "status";
    private static final String LOGO = "logo";
    private static final String COUNTRY = "country";
    private static final String FORMED_YEAR = "formed_year";
    private static final String DESCRIPTION = "description";
    private static final String NICKNAMES = "nicknames";
    private static final String STADIUM_LOCATION = "stadium_location";
    private static final String STADIUM_DESCRIPTION = "stadium_description";
    private static final String STADIUM_PREVIEW = "stadium_preview";
    private static final String STADIUM_CAPACITY = "stadium_capacity";
    private static final String LEAGUE2_ID = "league2_id";
    private static final String LEAGUE3_ID = "league3_id";
    private static final String LEAGUE4_ID = "league4_id";
    private static final String LEAGUE5_ID = "league5_id";
    private static final String WEBSITE = "website";
    private static final String FACEBOOK = "facebook";
    private static final String TWITTER = "twitter";
    private static final String INSTAGRAM = "instagram";
    private static final String YOUTUBE = "youtube";
    private static final String JERSEY = "jersey";
    
    private static final String D = ".";
    private static final String F = "f";
    private static final String FD = F + D;
    private static final String L = "l";
    private static final String L2 = "l2";
    private static final String L3 = "l3";
    private static final String L4 = "l4";
    private static final String L5 = "l5";
    private static final String LD = L + D;
    private static final String L2D = L2 + D;
    private static final String L3D = L3 + D;
    private static final String L4D = L4 + D;
    private static final String L5D = L5 + D;
    
    private static final String FID = FD + ID;
    private static final String FNAME = FD + NAME;
    private static final String LEAGUE_NAME = "league_name";
    private static final String LEAGUE2_NAME = "league2_name";
    private static final String LEAGUE3_NAME = "league3_name";
    private static final String LEAGUE4_NAME = "league4_name";
    private static final String LEAGUE5_NAME = "league5_name";
    
    private static final String CREATE_TABLE_LEAGUE =
            "CREATE TABLE " + TABLE_LEAGUE + " ("
                    + ID + " INTEGER UNIQUE PRIMARY KEY,"
                    + NAME + " TEXT)";
    
    private static final String CREATE_TABLE_FAVORITE_MATCH =
            "CREATE TABLE " + TABLE_FAVORITE_MATCH + " ("
                    + ID + " INTEGER NOT NULL UNIQUE PRIMARY KEY,"
                    + EVENT + " TEXT,"
                    + LEAGUE_ID + " INTEGER,"
                    + SEASON + " TEXT,"
                    + HOME_TEAM_ID + " INTEGER,"
                    + AWAY_TEAM_ID + " INTEGER,"
                    + HOME_SCORE + " INTEGER,"
                    + AWAY_SCORE + " INTEGER,"
                    + MATCHWEEK + " INTEGER,"
                    + EVENT_DATE + " TEXT,"
                    + EVENT_TIME + " TEXT,"
                    + STADIUM + " TEXT,"
                    + SPECTATORS + " INTEGER,"
                    + THUMBNAIL + " TEXT,"
                    + VIDEO + " TEXT,"
                    + STATUS + " TEXT,"
                    + "FOREIGN KEY (" + LEAGUE_ID + ") REFERENCES " + TABLE_LEAGUE + "(" + ID + "))";
    
    private static final String CREATE_TABLE_FAVORITE_TEAM =
            "CREATE TABLE " + TABLE_FAVORITE_TEAM + " ("
                    + ID + " INTEGER NOT NULL UNIQUE PRIMARY KEY,"
                    + NAME + " TEXT,"
                    + LOGO + " TEXT,"
                    + COUNTRY + " TEXT,"
                    + FORMED_YEAR + " INTEGER,"
                    + DESCRIPTION + " TEXT,"
                    + NICKNAMES + " TEXT,"
                    + STADIUM + " TEXT,"
                    + STADIUM_LOCATION + " TEXT,"
                    + STADIUM_DESCRIPTION + " TEXT,"
                    + STADIUM_PREVIEW + " TEXT,"
                    + STADIUM_CAPACITY + " INTEGER,"
                    + LEAGUE_ID + " INTEGER,"
                    + LEAGUE2_ID + " INTEGER,"
                    + LEAGUE3_ID + " INTEGER,"
                    + LEAGUE4_ID + " INTEGER,"
                    + LEAGUE5_ID + " INTEGER,"
                    + WEBSITE + " TEXT,"
                    + FACEBOOK + " TEXT,"
                    + TWITTER + " TEXT,"
                    + INSTAGRAM + " TEXT,"
                    + YOUTUBE + " TEXT,"
                    + JERSEY + " TEXT,"
                    + "FOREIGN KEY (" + LEAGUE_ID + ") REFERENCES " + TABLE_LEAGUE + "(" + ID + "),"
                    + "FOREIGN KEY (" + LEAGUE2_ID + ") REFERENCES " + TABLE_LEAGUE + "(" + ID + "),"
                    + "FOREIGN KEY (" + LEAGUE3_ID + ") REFERENCES " + TABLE_LEAGUE + "(" + ID + "),"
                    + "FOREIGN KEY (" + LEAGUE4_ID + ") REFERENCES " + TABLE_LEAGUE + "(" + ID + "),"
                    + "FOREIGN KEY (" + LEAGUE5_ID + ") REFERENCES " + TABLE_LEAGUE + "(" + ID + "))";
}
