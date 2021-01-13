package com.pam.sportsdbfootballscore.ui.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pam.sportsdbfootballscore.R;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    
    LayoutInflater inflater;
    List<String> spinnerItems;
    
    public CustomSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> spinnerItems) {
        super(context, resource, spinnerItems);
        this.spinnerItems = spinnerItems;
        inflater = LayoutInflater.from(context);
    }
    
    @SuppressLint({"ViewHolder", "InflateParams"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = inflater.inflate(R.layout.custom_spinner, null);
        TextView type = convertView.findViewById(R.id.spinner_item_text);
        type.setText(spinnerItems.get(position));
        return convertView;
    }
}
