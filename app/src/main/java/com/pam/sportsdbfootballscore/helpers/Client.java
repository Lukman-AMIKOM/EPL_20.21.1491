package com.pam.sportsdbfootballscore.helpers;

import com.pam.sportsdbfootballscore.interfaces.Api;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Client {
    
    private static Client instance = null;
    private final Api api;
    
    private Client() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create()).build();
        api = retrofit.create(Api.class);
    }
    
    public static synchronized Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
    
        return instance;
    }
    
    public Api getApi() {
        return api;
    }
}
