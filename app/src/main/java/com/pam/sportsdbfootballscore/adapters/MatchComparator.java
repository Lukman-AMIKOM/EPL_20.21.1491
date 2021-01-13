package com.pam.sportsdbfootballscore.adapters;

import com.pam.sportsdbfootballscore.model.Match;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class MatchComparator implements Comparator<Match> {
    
    public static final String TIME_ASCENDING = "time_ascending";
    
    private final String type;
    
    public MatchComparator(String type) {
        this.type = type;
    }
    
    @Override
    public int compare(Match match1, Match match2) {
        if (match1.hashCode() == match2.hashCode()) {
            return 0;
        } else {
            if (TIME_ASCENDING.equals(type)) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                try {
                    Date time1 = sdf.parse(match1.getTime());
                    Date time2 = sdf.parse(match2.getTime());
    
                    if (time1 != null) {
                        if (time1.before(time2)) {
                            return -1;
                        } else if (time1.after(time2)) {
                            return 1;
                        }
                    }
                    return 0;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return 0;
    }
}
