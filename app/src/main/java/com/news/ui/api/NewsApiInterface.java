package com.news.ui.api;

import com.news.ui.model.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiInterface {

    @GET("top-headlines")
    Call<News> getNews(

            @Query("country") String country,
            @Query("apiKey") String apiKey

    );

    @GET("v2/everything")
    Call<News> getNewsDown(@Query("q") String q,
                                   @Query("language") String country,
                                   @Query("apiKey") String apikey,
                                   @Query("page") int page,
                                   @Query("pageSize") int pageSize);
}
