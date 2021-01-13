package com.pam.sportsdbfootballscore.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.pam.sportsdbfootballscore.R;
import com.pam.sportsdbfootballscore.adapters.MatchDetailsAdapter;
import com.pam.sportsdbfootballscore.helpers.GlideLoader;
import com.pam.sportsdbfootballscore.model.League;
import com.pam.sportsdbfootballscore.model.Team;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class TeamDetailExtraFragment extends Fragment {
    
    private static final String SOCIAL_TYPE_FACEBOOK = "facebook";
    private static final String SOCIAL_TYPE_TWITTER = "twitter";
    private static final String SOCIAL_TYPE_INSTAGRAM = "instagram";
    private static final String SOCIAL_TYPE_YOUTUBE = "youtube";
    private static final String SOCIAL_TYPE_WEBSITE = "homepage";
    
    private ImageView imgFacebook, imgTwitter, imgInstagram, imgYoutube, imgWebsite, imgJersey;
    private RecyclerView rvExtra;
    private ProgressBar pbExtra, pbJersey;
    
    private final Team team;
    
    private ArrayList<String> titles, contents;
    
    public TeamDetailExtraFragment(Team team) {
        this.team = team;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setRetainInstance(true);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_team_detail_extra, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        init(view);
        setSocialMedia();
        setExtra();
        GlideLoader.loadImage(getContext(), imgJersey, pbJersey, team.getJersey(), R.drawable.ic_person);
    }
    
    private void init(View view) {
        imgFacebook = view.findViewById(R.id.img_facebook_icon);
        imgTwitter = view.findViewById(R.id.img_twitter_icon);
        imgInstagram = view.findViewById(R.id.img_instagram_icon);
        imgYoutube = view.findViewById(R.id.img_youtube_icon);
        imgWebsite = view.findViewById(R.id.img_website_icon);
        imgJersey = view.findViewById(R.id.img_jersey);
        pbExtra = view.findViewById(R.id.pb_extra);
        pbJersey = view.findViewById(R.id.pb_jersey);
        rvExtra = view.findViewById(R.id.rv_extra);
        rvExtra.setHasFixedSize(true);
    }
    
    private void setSocialMedia() {
        System.out.println(team.getFacebook() == null);
        setImageIcon(getSocialMediaId(team.getFacebook()), imgFacebook, SOCIAL_TYPE_FACEBOOK);
        setImageIcon(getSocialMediaId(team.getTwitter()), imgTwitter, SOCIAL_TYPE_TWITTER);
        setImageIcon(getSocialMediaId(team.getInstagram()), imgInstagram, SOCIAL_TYPE_INSTAGRAM);
        setImageIcon(team.getYoutube(), imgYoutube, SOCIAL_TYPE_YOUTUBE);
        setImageIcon(team.getWebsite(), imgWebsite, SOCIAL_TYPE_WEBSITE);
    }
    
    private void setExtra() {
        pbExtra.setVisibility(View.VISIBLE);
        
        titles = new ArrayList<>();
        contents = new ArrayList<>();
        
        ArrayList<League> leagues = team.getLeagues();
        StringBuilder strLeagues = new StringBuilder();
        if (leagues != null) {
            for (League league : leagues) {
                strLeagues.append(league.getName()).append("\n");
            }
        }
        
        addDetail(getString(R.string.nicknames_title), isContentEmpty(team.getNicknames()) ? "—" : team.getNicknames());
        addDetail(getString(R.string.stadium_title), getString(R.string.stadium_content,
                team.getStadium(), getFormattedStadiumCapacity(team.getStadiumCapacity())));
        addDetail(getString(R.string.location_title), team.getStadiumLocation());
        addDetail(getString(R.string.country_title), team.getCountry());
        addDetail(getString(R.string.competitions_title), strLeagues.toString().trim());
        
        rvExtra.setLayoutManager(new LinearLayoutManager(getContext()));
        rvExtra.setAdapter(new MatchDetailsAdapter(titles, contents));
        pbExtra.setVisibility(View.GONE);
    }
    
    private String getSocialMediaId(String url) {
        return url.substring(url.lastIndexOf("/"));
    }
    
    private void setImageIcon(String id, ImageView imageView, String type) {
        Intent intent = null;
        if (id != null || !id.isEmpty() || !id.equalsIgnoreCase("null")) {
            imageView.setVisibility(View.VISIBLE);
            
            switch (type) {
                case SOCIAL_TYPE_FACEBOOK:
                    intent = getFacebookIntent(Objects.requireNonNull(getContext()).getApplicationContext(), id);
                    break;
                case SOCIAL_TYPE_TWITTER:
                    intent = getTwitterIntent(Objects.requireNonNull(getContext()).getApplicationContext(), id);
                    break;
                case SOCIAL_TYPE_INSTAGRAM:
                    intent = getInstagramIntent(Objects.requireNonNull(getContext()).getApplicationContext(), id);
                    break;
                case SOCIAL_TYPE_YOUTUBE:
                case SOCIAL_TYPE_WEBSITE:
                    String url = !id.contains("http://") ? "http://" + id : id;
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    break;
            }
            
            Intent finalIntent = intent;
            imageView.setOnClickListener(v -> startActivity(finalIntent));
        } else {
            imageView.setVisibility(View.GONE);
        }
    }
    
    private Intent getFacebookIntent(Context context, String facebookId) {
        String url = "https://www.facebook.com/" + facebookId;
        String uri;
        PackageManager packageManager = context.getPackageManager();
        
        try {
            long versionCode;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).getLongVersionCode();
            } else {
                //noinspection deprecation
                versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            }
            
            if (versionCode >= 3002850) {
                uri = "fb://facewebmodal/f?href=" + url;
            } else {
                uri = "fb://page/" + facebookId;
            }
        } catch (PackageManager.NameNotFoundException e) {
            uri = url;
        }
        
        return new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
    }
    
    private Intent getTwitterIntent(Context context, String twitterId) {
        String uri;
        try {
            context.getPackageManager().getPackageInfo("com.twitter.android", 0);
            uri = "twitter://user?screen_name=" + twitterId;
        } catch (PackageManager.NameNotFoundException e) {
            uri = "https://twitter.com/" + twitterId;
        }
        
        return new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
    }
    
    private Intent getInstagramIntent(Context context, String instagramId) {
        String uri;
        try {
            context.getPackageManager().getPackageInfo("com.instagram.android", 0);
            uri = "http://instagram.com/_u/" + instagramId;
        } catch (PackageManager.NameNotFoundException e) {
            uri = "http://instagram.com/" + instagramId;
        }
        
        return new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
    }
    
    private boolean isContentEmpty(String content) {
        return content == null || content.isEmpty() || content.equalsIgnoreCase("null");
    }
    
    private void addDetail(String title, String content) {
        titles.add(title);
        contents.add(content);
    }
    
    private String getFormattedStadiumCapacity(int capacity) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
        
        return formatter.format(capacity);
    }
}