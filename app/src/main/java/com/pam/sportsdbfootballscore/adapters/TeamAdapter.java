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
import androidx.recyclerview.widget.SortedList;

import com.pam.sportsdbfootballscore.R;
import com.pam.sportsdbfootballscore.helpers.FavoriteSetter;
import com.pam.sportsdbfootballscore.helpers.GlideLoader;
import com.pam.sportsdbfootballscore.interfaces.FavoriteClickListener;
import com.pam.sportsdbfootballscore.interfaces.TeamClickListener;
import com.pam.sportsdbfootballscore.model.Team;
import com.pam.sportsdbfootballscore.ui.fragments.MainFragment;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {
    
    public static final String MODE_LIST = "mode_list";
    public static final String MODE_GRID = "mode_grid";
    
    private final String viewMode;
    private final FavoriteClickListener favoriteClickListener;
    private final TeamClickListener teamClickListener;
    private final TeamComparator comparator;
    
    public TeamAdapter(String viewMode, MainFragment mainFragment, TeamComparator comparator) {
        this.viewMode = viewMode;
        this.favoriteClickListener = mainFragment;
        this.teamClickListener = mainFragment;
        this.comparator = comparator;
    }
    
    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int resource;
        if (viewMode.equals(MODE_LIST)) {
            resource = R.layout.list_item_team;
        } else {
            resource = R.layout.grid_item_team;
        }
        
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new TeamViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        Team team = teams.get(position);
        Context context = holder.itemView.getContext();
    
        holder.tvTeamName.setText(team.getName());
        holder.tvEstablished.setText(holder.itemView.getResources().getString(R.string.established, String.valueOf(team.getFormedYear())));
        GlideLoader.loadImage(context, holder.imgTeamLogo,
                holder.pbTeamLogo, team.getLogo(), R.drawable.ic_tab_favorite_teams);
        FavoriteSetter.getInstance(context).setFavorite(holder.imgFavorite, team.isFavorite());
        holder.imgFavorite.setOnClickListener(v ->
                FavoriteSetter.getInstance(context).setFavoriteListener(holder.imgFavorite, team, favoriteClickListener));
        holder.container.setOnClickListener(v -> teamClickListener.onTeamClicked(team));
    }
    
    @Override
    public int getItemCount() {
        return teams.size();
    }
    
    public static class TeamViewHolder extends RecyclerView.ViewHolder {
        
        View container;
        ProgressBar pbTeamLogo;
        ImageView imgTeamLogo, imgFavorite;
        TextView tvTeamName, tvEstablished;
        
        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.layout_item_team);
            pbTeamLogo = itemView.findViewById(R.id.pb_list_team);
            imgTeamLogo = itemView.findViewById(R.id.img_team_logo);
            tvTeamName = itemView.findViewById(R.id.tv_team_name);
            tvEstablished = itemView.findViewById(R.id.tv_establised);
            imgFavorite = itemView.findViewById(R.id.img_favorite);
        }
    }
    
    private final SortedList<Team> teams = new SortedList<>(Team.class, new SortedList.Callback<Team>() {
        @Override
        public int compare(Team movie1, Team movie2) {
            return comparator.compare(movie1, movie2);
        }
        
        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }
        
        @Override
        public boolean areContentsTheSame(Team oldItem, Team newItem) {
            return oldItem.equals(newItem);
        }
        
        @Override
        public boolean areItemsTheSame(Team item1, Team item2) {
            return item1.hashCode() == item2.hashCode();
        }
        
        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }
        
        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }
        
        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }
    });
    
    public void add(List<Team> movies) {
        teams.addAll(movies);
    }
    
    public void add(Team team) {
        teams.add(team);
    }
    
    public void remove(Team team) {
        teams.remove(team);
    }
}
