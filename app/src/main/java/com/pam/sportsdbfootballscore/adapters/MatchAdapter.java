package com.pam.sportsdbfootballscore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pam.sportsdbfootballscore.R;
import com.pam.sportsdbfootballscore.helpers.FavoriteSetter;
import com.pam.sportsdbfootballscore.interfaces.FavoriteClickListener;
import com.pam.sportsdbfootballscore.interfaces.MatchClickListener;
import com.pam.sportsdbfootballscore.helpers.GlideLoader;
import com.pam.sportsdbfootballscore.model.Match;
import com.pam.sportsdbfootballscore.model.Team;
import com.pam.sportsdbfootballscore.ui.fragments.MainFragment;

import java.util.ArrayList;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {
    
    private final ArrayList<Match> matches;
    private final FavoriteClickListener favoriteClickListener;
    private final MatchClickListener matchClickListener;
    
    public MatchAdapter(ArrayList<Match> matches, MainFragment mainFragment) {
        this.matches = matches;
        this.favoriteClickListener = mainFragment;
        this.matchClickListener = mainFragment;
    }
    
    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_match, parent, false);
        return new MatchViewHolder(view, favoriteClickListener);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match match = matches.get(position);
        holder.bind(match);
        holder.layoutItemMatchRow.setOnClickListener(v -> matchClickListener.onMatchClicked(match));
    }
    
    @Override
    public int getItemCount() {
        return matches.size();
    }
    
    public static class MatchViewHolder extends RecyclerView.ViewHolder {
        
        TextView tvHomeTeam, tvAwayTeam, tvHomeScore, tvAwayScore, tvTime;
        View border;
        ProgressBar pbHomeTeam, pbAwayTeam;
        ImageView imgHomeTeam, imgAwayTeam, imgFavorite;
        View layoutItemMatchRow, layoutImgFavorite;
        
        private Match match;
        private Team homeTeam;
        private Team awayTeam;
        
        private final FavoriteClickListener favoriteClickListener;
        
        public MatchViewHolder(@NonNull View itemView, FavoriteClickListener favoriteClickListener) {
            super(itemView);
            tvHomeTeam = itemView.findViewById(R.id.tv_home_team);
            tvAwayTeam = itemView.findViewById(R.id.tv_away_team);
            tvHomeScore = itemView.findViewById(R.id.tv_home_score);
            tvAwayScore = itemView.findViewById(R.id.tv_away_score);
            pbHomeTeam = itemView.findViewById(R.id.pb_home_team);
            pbAwayTeam = itemView.findViewById(R.id.pb_away_team);
            imgHomeTeam = itemView.findViewById(R.id.img_home_team);
            imgAwayTeam = itemView.findViewById(R.id.img_away_team);
            tvTime = itemView.findViewById(R.id.tv_time);
            border = itemView.findViewById(R.id.border);
            imgFavorite = itemView.findViewById(R.id.img_favorite);
            layoutItemMatchRow = itemView.findViewById(R.id.layout_item_match_row);
            layoutImgFavorite = itemView.findViewById(R.id.layout_img_favorite);
            this.favoriteClickListener = favoriteClickListener;
        }
        
        protected void bind(Match match) {
            homeTeam = match.getHomeTeam();
            awayTeam = match.getAwayTeam();
            this.match = match;
            
            setTeamNames();
            setTeamLogos();
            setScoreboard();
            setFavorite();
        }
        
        private void setTeamNames() {
            tvHomeTeam.setText(homeTeam.getName());
            tvAwayTeam.setText(awayTeam.getName());
        }
        
        private void setTeamLogos() {
            Context context = itemView.getContext();
            
            GlideLoader.loadImage(context, imgHomeTeam, pbHomeTeam, homeTeam.getLogo(), R.drawable.ic_tab_favorite_teams);
            GlideLoader.loadImage(context, imgAwayTeam, pbAwayTeam, awayTeam.getLogo(), R.drawable.ic_tab_favorite_teams);
        }
    
        private void setScoreboard() {
            if (match.getStatus().equalsIgnoreCase("Match Finished") ||
                    (match.getStatus().equalsIgnoreCase("Not Started") && match.getHomeScore() != -1)) {
                setScore(tvHomeScore, String.valueOf(match.getHomeScore()));
                setScore(tvAwayScore, String.valueOf(match.getAwayScore()));
                border.setVisibility(View.VISIBLE);
                hideView(tvTime);
            } else if (match.getStatus().equalsIgnoreCase("Match Postponed")) {
                setScore(tvTime, itemView.getResources().getString(R.string.postponed));
                tvTime.setTextSize(8f);
                hideScoreboard();
            } else {
                String time = match.getTime();
                setScore(tvTime, time.substring(0, time.lastIndexOf(":")));
                tvTime.setTextSize(12f);
                hideScoreboard();
            }
        }
    
        private void setScore(TextView textView, String score) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(score);
        }
        
        private void hideScoreboard() {
            hideView(border);
            hideView(tvHomeScore);
            hideView(tvAwayScore);
        }
        
        private void hideView(View view) {
            view.setVisibility(View.GONE);
        }
    
        private void setFavorite() {
            FavoriteSetter.getInstance(itemView.getContext()).setFavorite(imgFavorite, match.isFavorite());
            
            imgFavorite.setOnClickListener(v -> setFavoriteListener());
            layoutImgFavorite.setOnClickListener(v -> setFavoriteListener());
        }
        
        private void setFavoriteListener() {
            FavoriteSetter.getInstance(itemView.getContext()).setFavoriteListener(imgFavorite, match, favoriteClickListener);
        }
    }
}
