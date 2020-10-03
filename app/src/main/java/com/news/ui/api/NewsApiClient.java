package com.news.ui.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsApiClient {

    public static final String BASE_URL = "https://newsapi.org/v2/";
    private static NewsApiInterface retrofitService;

    public static NewsApiInterface getService(){
        if (retrofitService == null) {
            retrofitService = builderRetrofit();
        }
        return retrofitService;
    }

    public static NewsApiInterface builderRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsApiInterface.class);
    }
}
