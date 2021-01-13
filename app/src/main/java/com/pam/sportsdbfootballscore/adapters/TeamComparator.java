package com.pam.sportsdbfootballscore.adapters;

import com.pam.sportsdbfootballscore.model.Team;

import java.util.Comparator;

public class TeamComparator implements Comparator<Team> {
    
    public static final String NAME_ASCENDING = "name_a_z";
    public static final String NAME_DESCENDING = "name_z_a";
    public static final String AGE_ASCENDING = "age_low_high";
    public static final String AGE_DESCENDING = "age_high_low";
    
    private final String type;
    
    public TeamComparator(String type) {
        this.type = type;
    }
    
    @Override
    public int compare(Team team1, Team team2) {
        if (team1.hashCode() == team2.hashCode()) {
            return 0;
        } else {
            int comp;
            int year1;
            int year2;
            
            switch (type) {
                case NAME_ASCENDING:
                    comp = compareName(team1.getName(), team2.getName());
                    return Integer.compare(comp, 0);
                case NAME_DESCENDING:
                    comp = compareName(team1.getName(), team2.getName());
                    return Integer.compare(0, comp);
                case AGE_ASCENDING:
                    year1 = team1.getFormedYear();
                    year2 = team2.getFormedYear();
                    
                    comp = Integer.compare(year1, year2);
                    if (comp == 0) {
                        comp = compareName(team1.getName(), team2.getName());
                        return Integer.compare(comp, 0);
                    } else {
                        return comp;
                    }
                case AGE_DESCENDING:
                    year1 = team1.getFormedYear();
                    year2 = team2.getFormedYear();
                    
                    comp = Integer.compare(year2, year1);
                    if (comp == 0) {
                        comp = compareName(team1.getName(), team2.getName());
                        return Integer.compare(comp, 0);
                    } else {
                        return comp;
                    }
            }
        }
        
        return 0;
    }
    
    private int compareName(String name1, String name2) {
        return name1.compareTo(name2);
    }
}
