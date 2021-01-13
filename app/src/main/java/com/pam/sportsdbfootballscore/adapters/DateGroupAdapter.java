package com.pam.sportsdbfootballscore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pam.sportsdbfootballscore.R;
import com.pam.sportsdbfootballscore.model.Match;
import com.pam.sportsdbfootballscore.ui.fragments.MainFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

public class DateGroupAdapter extends RecyclerView.Adapter<DateGroupAdapter.DateGroupViewHolder> {
    
    public static final String LIST_FRAGMENT = "source_list_fragment";
    public static final String TEAM_DETAIL_FRAGMENT = "source_team_detail_fragment";
    
    private final LinkedHashMap<String, ArrayList<Match>> matchMap;
    private final MainFragment mainFragment;
    private final String sourceFragment;
    
    public DateGroupAdapter(LinkedHashMap<String, ArrayList<Match>> matchMap, MainFragment mainFragment, String sourceFragment) {
        this.matchMap = matchMap;
        this.mainFragment = mainFragment;
        this.sourceFragment = sourceFragment;
    }
    
    @NonNull
    @Override
    public DateGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_match_date, parent, false);
        return new DateGroupViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull DateGroupViewHolder holder, int position) {
        String dateTitle = (String) matchMap.keySet().toArray()[position];
        
        switch (sourceFragment) {
            case LIST_FRAGMENT:
                holder.tvDateTitle.setText(getFormattedDate(dateTitle));
                break;
            case TEAM_DETAIL_FRAGMENT:
                holder.tvDateTitle.setText(dateTitle);
                holder.tvDateTitle.setCompoundDrawables(null, null, null, null);
                break;
        }
    
        holder.rvMatches.setHasFixedSize(true);
        holder.rvMatches.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.rvMatches.setAdapter(new MatchAdapter(matchMap.get(dateTitle), mainFragment));
    }
    
    @Override
    public int getItemCount() {
        return matchMap.size();
    }
    
    private String getFormattedDate(String strDate) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());
        String result = null;
        
        try {
            Date date = sdf1.parse(strDate);
            if (date != null) {
                result = sdf2.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static class DateGroupViewHolder extends RecyclerView.ViewHolder {
        
        TextView tvDateTitle;
        RecyclerView rvMatches;
        
        public DateGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDateTitle = itemView.findViewById(R.id.tv_date_title);
            rvMatches = itemView.findViewById(R.id.rv_matches);
        }
    }
}
