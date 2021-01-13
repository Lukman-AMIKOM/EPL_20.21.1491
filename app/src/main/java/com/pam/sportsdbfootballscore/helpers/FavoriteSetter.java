package com.pam.sportsdbfootballscore.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pam.sportsdbfootballscore.R;
import com.pam.sportsdbfootballscore.interfaces.FavoriteClickListener;
import com.pam.sportsdbfootballscore.model.Match;
import com.pam.sportsdbfootballscore.model.MyObject;
import com.pam.sportsdbfootballscore.model.Team;

public class FavoriteSetter {
    
    private static FavoriteSetter instance = null;
    
    private Drawable favoriteDefaultIcon;
    private Drawable favoriteSelectedIcon;
    
    public static FavoriteSetter getInstance(Context context) {
        if (instance == null) {
            instance = new FavoriteSetter();
        }
        
        instance.initFavoriteIcons(context);
        return instance;
    }
    
    private void initFavoriteIcons(Context context) {
        favoriteDefaultIcon = ContextCompat.getDrawable(context, R.drawable.ic_favorite_default);
        favoriteSelectedIcon = ContextCompat.getDrawable(context, R.drawable.ic_favorite_selected);
    }
    
    private void updateFavorite(ImageView imgFavorite, MyObject data, FavoriteClickListener favoriteClickListener) {
        data.setFavorite(!data.isFavorite());
        setFavorite(imgFavorite, data.isFavorite());
        favoriteClickListener.onFavoriteClicked(data);
    }
    
    public void setFavoriteListener(ImageView imgFavorite, MyObject data, FavoriteClickListener favoriteClickListener) {
        if (data.isFavorite()) {
            Context context = imgFavorite.getContext();
            String name = null;
            if (data instanceof Match) {
                name = ((Match) data).getEvent();
            } else if (data instanceof Team) {
                name = ((Team) data).getName();
            }
        
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        updateFavorite(imgFavorite, data, favoriteClickListener);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            };
        
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(Html.fromHtml(context.getString(R.string.remove_favorite_confirmation, name)))
                    .setPositiveButton(context.getString(R.string.yes), dialogClickListener)
                    .setNegativeButton(context.getString(R.string.no), dialogClickListener)
                    .show();
        } else {
            updateFavorite(imgFavorite, data, favoriteClickListener);
        }
    }
    
    public void setFavorite(ImageView imgFavorite, boolean isFavorite) {
        if (imgFavorite instanceof FloatingActionButton) {
            Context context = imgFavorite.getContext().getApplicationContext();
            if (isFavorite) {
                imgFavorite.setColorFilter(context.getResources().getColor(R.color.favorite_selected));
                imgFavorite.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.toolbar_color_night)));
            } else {
                imgFavorite.clearColorFilter();
                imgFavorite.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.list_background_color)));
            }
        }
        
        imgFavorite.setImageDrawable(isFavorite ? favoriteSelectedIcon : favoriteDefaultIcon);
    }
}
