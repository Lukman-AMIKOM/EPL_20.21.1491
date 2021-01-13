package com.pam.sportsdbfootballscore.helpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class GlideLoader implements RequestListener<Drawable> {
    
    private final ProgressBar progressBar;
    private final ImageView imageView;
    private final Drawable imgFailed;
    
    public GlideLoader(ProgressBar progressBar, ImageView imageView, Drawable imgFailed) {
        this.progressBar = progressBar;
        this.imageView = imageView;
        this.imgFailed = imgFailed;
    }
    
    @Override
    
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        imageView.setImageDrawable(imgFailed);
        progressBar.setVisibility(View.GONE);
        return false;
    }
    
    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
        progressBar.setVisibility(View.GONE);
        return false;
    }
    
    public static void loadImage(Context context, ImageView imgView, ProgressBar progressBar, String url, int placeholder) {
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(url)
                .placeholder(placeholder)
                .listener(new GlideLoader(progressBar, imgView, ContextCompat.getDrawable(context, placeholder)))
                .into(imgView);
    }
}
