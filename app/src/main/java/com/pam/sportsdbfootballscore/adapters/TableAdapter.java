package com.pam.sportsdbfootballscore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pam.sportsdbfootballscore.R;
import com.pam.sportsdbfootballscore.helpers.GlideLoader;
import com.pam.sportsdbfootballscore.interfaces.TeamClickListener;
import com.pam.sportsdbfootballscore.model.Table;

import java.util.ArrayList;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {
    
    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_HEADER = 2;
    
    private final ArrayList<Table> leagueTables;
    private final TeamClickListener teamClickListener;
    
    public TableAdapter(ArrayList<Table> leagueTables, TeamClickListener teamClickListener) {
        this.leagueTables = leagueTables;
        this.teamClickListener = teamClickListener;
    }
    
    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int resource;
        if (viewType == VIEW_TYPE_NORMAL) {
            resource = R.layout.list_item_table_row;
        } else {
            resource = R.layout.list_item_table_header;
        }
    
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new TableViewHolder(view, viewType);
    }
    
    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_NORMAL) {
            Table table = leagueTables.get(position - 1);
            
            holder.tvPosition.setText(String.valueOf(position));
            GlideLoader.loadImage(holder.itemView.getContext(), holder.imgTeamLogo, holder.pbTeamLogo,
                    table.getTeam().getLogo(), R.drawable.ic_tab_teams);
            holder.tvTeamName.setText(table.getName());
            holder.tvPlayed.setText(String.valueOf(table.getPlayed()));
            holder.tvWon.setText(String.valueOf(table.getWin()));
            holder.tvDrawn.setText(String.valueOf(table.getDraw()));
            holder.tvLost.setText(String.valueOf(table.getLoss()));
            holder.tvGoalDiff.setText(String.valueOf(table.getGoalsDifference()));
            holder.tvPoints.setText(String.valueOf(table.getPoints()));
            holder.itemView.setOnClickListener(v -> teamClickListener.onTeamClicked(table.getTeam()));
        }
    }
    
    @Override
    public int getItemCount() {
        return leagueTables.size() + 1;
    }
    
    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_HEADER : VIEW_TYPE_NORMAL;
    }
    
    public static class TableViewHolder extends RecyclerView.ViewHolder {
        
        TextView tvPosition, tvTeamName, tvPlayed, tvWon,
                tvDrawn, tvLost, tvGoalDiff, tvPoints;
        ProgressBar pbTeamLogo;
        ImageView imgTeamLogo;
        
        public TableViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            tvPosition = itemView.findViewById(R.id.tv_position);
            tvTeamName = itemView.findViewById(R.id.tv_team_name);
            tvPlayed = itemView.findViewById(R.id.tv_played);
            tvWon = itemView.findViewById(R.id.tv_won);
            tvDrawn = itemView.findViewById(R.id.tv_drawn);
            tvLost = itemView.findViewById(R.id.tv_lost);
            tvGoalDiff = itemView.findViewById(R.id.tv_goal_diff);
            tvPoints = itemView.findViewById(R.id.tv_points);
            if (viewType == VIEW_TYPE_NORMAL) {
                pbTeamLogo = itemView.findViewById(R.id.pb_team_logo);
                imgTeamLogo = itemView.findViewById(R.id.img_team_logo);
            }
        }
    }
}
