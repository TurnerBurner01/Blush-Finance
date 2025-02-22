package com.example.blushfinance.fragments.News_Page;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("top-headlines")
    Call<NewsResponse> getTopHeadlines(
            @Query("category") String category,
            @Query("apiKey") String apiKey
    );
}
