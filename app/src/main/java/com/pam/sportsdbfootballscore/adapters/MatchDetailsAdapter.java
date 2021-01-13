package com.pam.sportsdbfootballscore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pam.sportsdbfootballscore.R;

import java.util.ArrayList;

public class MatchDetailsAdapter extends RecyclerView.Adapter<MatchDetailsAdapter.MatchDetailsViewHolder> {
    
    private final ArrayList<String> titles;
    private final ArrayList<String> contents;
    
    public MatchDetailsAdapter(ArrayList<String> titles, ArrayList<String> contents) {
        this.titles = titles;
        this.contents = contents;
    }
    
    @NonNull
    @Override
    public MatchDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_match_detail, parent, false);
        return new MatchDetailsViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MatchDetailsViewHolder holder, int position) {
        holder.tvDetailTitle.setText(titles.get(position));
        holder.tvDetailContent.setText(contents.get(position));
    }
    
    @Override
    public int getItemCount() {
        return titles.size();
    }
    
    public static class MatchDetailsViewHolder extends RecyclerView.ViewHolder {
        
        TextView tvDetailTitle, tvDetailContent;
        
        public MatchDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDetailTitle = itemView.findViewById(R.id.tv_detail_title);
            tvDetailContent = itemView.findViewById(R.id.tv_detail_content);
        }
    }
}
